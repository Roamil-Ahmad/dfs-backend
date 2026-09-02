package com.dfs.backoffice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves uploaded KYC documents over HTTP.
 *
 * <p>TBL_DOCUMENT.DOCUMENT_PATH stores a relative path such as
 * {@code document/308/L1_20260820204539_TB.png}. The portal appends that to the backoffice base
 * URL and renders it as an image, so the path has to resolve here, on this service, exactly as it
 * is stored. Nothing mapped it before, which is why every document came back 404.</p>
 *
 * <p>The directory is a bind mount shared with app and agentapp. Those two services write the
 * uploads; this one only reads them. Without the mount the files would sit in the writing
 * container's own layer, invisible here and destroyed by the next redeploy.</p>
 *
 * <p>The mapping is read-only by nature - a resource handler never writes - and is deliberately
 * narrow: it exposes {@code /document/**} and nothing else.</p>
 */
@Configuration
public class DocumentResourceConfig implements WebMvcConfigurer {

    /**
     * Filesystem root holding the uploads, matching {@code file.upload.path} in the services that
     * write them. Trailing separator matters: Spring resolves the handler against it as a prefix.
     */
    @Value("${document.serve.path:/opt/tomcat/webapps/document/}")
    private String documentPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = documentPath.endsWith("/") ? documentPath : documentPath + "/";
        registry.addResourceHandler("/document/**")
                .addResourceLocations("file:" + location);
    }
}
