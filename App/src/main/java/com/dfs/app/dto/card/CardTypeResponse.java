package com.dfs.app.dto.card;

import com.dfs.app.util.LovDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class CardTypeResponse {
    private String type;
    @JsonDeserialize(using = LovDeserializer.class)
    private List<LovEntry> lov;

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LovEntry> getLov() {
        return lov;
    }

    public void setLov(List<LovEntry> lov) {
        this.lov = lov;
    }
}
