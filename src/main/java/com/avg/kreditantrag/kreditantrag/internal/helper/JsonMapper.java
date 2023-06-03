package com.avg.kreditantrag.kreditantrag.internal.helper;

import com.avg.kreditantrag.kreditantrag.KreditantragApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    public static String serialize(Object objToSerialize) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String serializedObject = mapper.writeValueAsString(objToSerialize);
        LOGGER.debug("JsonMapper: serialize: objectToSerialize={}\nSerialized JSON: {}", objToSerialize, serializedObject);
        return serializedObject;
    }
    public static<T> T deserialize(String json, Class<T> valueType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.debug("JsonMapper: deserialize: jsonString={}, valueType={}", json, valueType.getSimpleName());
        return mapper.readValue(json, valueType);
    }
}
