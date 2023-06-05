package com.avg.kreditantrag.internal.helper;

import com.avg.kreditantrag.KreditantragApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that contains methods to serialize and deserialize JSON.
 */
public class JsonMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);

    /**
     * Serializes an object.
     *
     * @param objToSerialize object to serialize
     * @return a serialized JSON string
     * @throws JsonProcessingException if serialization fails
     */
    public static String serialize(Object objToSerialize) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String serializedObject = mapper.writeValueAsString(objToSerialize);
        LOGGER.debug("JsonMapper: serialize: objectToSerialize={}", objToSerialize);
        return serializedObject;
    }

    /**
     * Deserializes JSON string.
     *
     * @param json a json string to deserialize
     * @param valueType class type to deserialize JSON to
     * @return a deserialized JSON
     * @throws JsonProcessingException if deserialization fails
     */
    public static<T> T deserialize(String json, Class<T> valueType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.debug("JsonMapper: deserialize: valueType={}", valueType.getSimpleName());
        return mapper.readValue(json, valueType);
    }
}
