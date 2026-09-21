package com.dfs.agentapp.util;

import com.dfs.agentapp.dto.card.LovEntry;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LovDeserializer extends JsonDeserializer<List<LovEntry>> {

    @Override
    public List<LovEntry> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);
        List<LovEntry> entries = new ArrayList<>();

        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String code = fieldNames.next();
            String descr = node.get(code).asText();
            entries.add(new LovEntry(code, descr));
        }

        return entries;
    }
}
