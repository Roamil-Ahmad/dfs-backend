package com.wallet.transaction.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.transaction.dto.LocationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for determining the Afghan province and district that a given
 * latitude/longitude coordinate falls within.
 *
 * <p>Uses GADM 4.1 GeoJSON boundary files hosted on Tomcat server:
 * <ul>
 *   <li>/opt/tomcat/document/gadm41_AFG_2.json – district-level boundaries (Level 2)</li>
 *   <li>/opt/tomcat/document/gadm41_AFG_1.json – province-level boundaries (Level 1)</li>
 * </ul>
 *
 * <p>Both files are parsed once at startup and cached in memory.
 * Point-in-polygon detection uses the ray-casting (even-odd) algorithm.
 */
@Service
public class LocationService {

    @Value("${location.gadm.directory}")
    private String productionDir;

    @Value("${location.district.filename}")
    private String districtJsonFilename;

    @Value("${location.province.filename}")
    private String provinceJsonFilename;

    private final ObjectMapper objectMapper;

    private List<JsonNode> districtFeatures;
    private List<JsonNode> provinceFeatures;

    public LocationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**districtJsonFilename);
        provinceFeatures = loadFeatures(provinceJsonFilenamefeature lists at application startup.
     */
    @PostConstruct
    public void init() throws IOException {
        districtFeatures = loadFeatures(districtJsonFilename);
        provinceFeatures = loadFeatures(provinceJsonFilename);
    }

    /**
     * Returns the province and district that contain the given coordinates.
     *
     * <p>Strategy:
     * <ol>
     *   <li>Try district-level (AFG_2) first – returns both province + district.</li>
     *   <li>If no district matched, fall back to province-level (AFG_1) – returns province only.</li>
     *   <li>If still no match, both fields are {@code null} (point outside Afghanistan).</li>
     * </ol>
     *
     * @param latitude  WGS84 latitude  (−90 to +90)
     * @param longitude WGS84 longitude (−180 to +180)
     * @return {@link LocationResponse} with province/district names, or null fields if not found
     */
    public LocationResponse findLocation(double latitude, double longitude) {
        // Guard: reject coordinates outside valid WGS84 ranges
        if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException(
                    String.format("Coordinates out of range: latitude=%.6f, longitude=%.6f. " +
                                    "Valid range: latitude [-90, 90], longitude [-180, 180].",
                            latitude, longitude));
        }

        // 1. District lookup
        for (JsonNode feature : districtFeatures) {
            if (pointInFeature(feature, longitude, latitude)) {
                JsonNode props = feature.get("properties");
                return new LocationResponse(
                        props.get("NAME_1").asText(),
                        props.get("NAME_2").asText()
                );
            }
        }

        // 2. Province fallback
        for (JsonNode feature : provinceFeatures) {
            if (pointInFeature(feature, longitude, latitude)) {
                JsonNode props = feature.get("properties");
                return new LocationResponse(
                        props.get("NAME_1").asText(),
                        null
                );
            }
        }

        // 3. Outside Afghanistan
        return new LocationResponse(null, null);
    }

    // ─────────────────────────── private helpers ───────────────────────────

    /**
     * Loads GeoJSON features from the production file system.
     *
     * @param filename filename in the configured location directory (e.g., gadm41_AFG_2.json)
     * @return list of GeoJSON feature nodes
     * @throws IOException if the file cannot be read
     */
    private List<JsonNode> loadFeatures(String filename) throws IOException {
        // Prefer the bundled classpath resource (src/main/resources); fall back to the
        // configured filesystem directory (e.g. /opt/tomcat/document) for legacy deployments.
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            String productionPath = productionDir + "/" + filename;
            is = Files.newInputStream(Paths.get(productionPath));
        }
        try (InputStream in = is) {
            JsonNode root = objectMapper.readTree(in);
            List<JsonNode> features = new ArrayList<>();
            JsonNode featuresNode = root.get("features");
            if (featuresNode != null && featuresNode.isArray()) {
                for (JsonNode feature : featuresNode) {
                    features.add(feature);
                }
            }
            return features;
        }
    }

    /**
     * Checks whether the given (lng, lat) point lies inside the geometry of a GeoJSON Feature.
     * Supports both "Polygon" and "MultiPolygon" geometry types.
     */
    private boolean pointInFeature(JsonNode feature, double lng, double lat) {
        JsonNode geometry = feature.get("geometry");
        if (geometry == null || geometry.isNull()) return false;

        String type = geometry.get("type").asText();
        JsonNode coordinates = geometry.get("coordinates");
        if (coordinates == null || coordinates.isNull()) return false;

        switch (type) {
            case "MultiPolygon":
                // coordinates: [ polygon, polygon, ... ]
                // Each polygon: [ ring, ring, ... ]
                for (JsonNode polygon : coordinates) {
                    if (isPointInPolygon(polygon, lng, lat)) return true;
                }
                return false;

            case "Polygon":
                // coordinates: [ ring, ring, ... ]
                return isPointInPolygon(coordinates, lng, lat);

            default:
                return false;
        }
    }

    /**
     * Tests whether the point lies inside a GeoJSON polygon (array of rings).
     * The point must be inside the outer ring AND outside every hole ring.
     *
     * <p>GeoJSON polygon convention:
     * <ul>
     *   <li>Index 0 – outer (exterior) ring</li>
     *   <li>Indices 1+ – hole (interior) rings; a point inside a hole is outside the polygon</li>
     * </ul>
     *
     * @param polygon GeoJSON polygon coordinates node: [ outerRing, ...holes ]
     * @param lng     longitude of the test point
     * @param lat     latitude of the test point
     */
    private boolean isPointInPolygon(JsonNode polygon, double lng, double lat) {
        JsonNode outerRing = polygon.get(0);
        if (outerRing == null || !outerRing.isArray()) return false;

        // Must be inside the exterior ring
        if (!rayCast(outerRing, lng, lat)) return false;

        // Must not be inside any hole ring
        for (int h = 1; h < polygon.size(); h++) {
            JsonNode holeRing = polygon.get(h);
            if (holeRing != null && holeRing.isArray() && rayCast(holeRing, lng, lat)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Classic ray-casting (even-odd) point-in-polygon test.
     *
     * @param ring closed linear ring as a JSON array of [lng, lat] pairs
     * @param lng  longitude of the test point
     * @param lat  latitude of the test point
     * @return {@code true} if the point is inside the ring
     */
    private boolean rayCast(JsonNode ring, double lng, double lat) {
        int n = ring.size();
        boolean inside = false;
        int j = n - 1;

        for (int i = 0; i < n; i++) {
            JsonNode pi = ring.get(i);
            JsonNode pj = ring.get(j);

            double xi = pi.get(0).asDouble();  // longitude of vertex i
            double yi = pi.get(1).asDouble();  // latitude  of vertex i
            double xj = pj.get(0).asDouble();  // longitude of vertex j
            double yj = pj.get(1).asDouble();  // latitude  of vertex j

            // Check if the horizontal ray from (lng, lat) crosses edge (i→j)
            boolean crossesLatBand = (yi > lat) != (yj > lat);
            boolean rayIntersects  = lng < (xj - xi) * (lat - yi) / (yj - yi) + xi;

            if (crossesLatBand && rayIntersects) {
                inside = !inside;
            }
            j = i;
        }
        return inside;
    }
}
