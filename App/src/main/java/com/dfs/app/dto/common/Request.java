package com.dfs.app.dto.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Request {
    private String channel;
    private String segment;
    private String imieNo;
    private String latitude;
    /**
     * Longitude of the device.
     *
     * <p>The field was spelled "logitude" for a long time and shipped app builds still send that.
     * The alias keeps those working: Jackson accepts either spelling on the way in, and the
     * corrected name is what goes out. Do not remove the alias until the old builds are gone.</p>
     */
    @JsonAlias("logitude")
    private String longitude;
    private String language;
    private Object payload;

}
