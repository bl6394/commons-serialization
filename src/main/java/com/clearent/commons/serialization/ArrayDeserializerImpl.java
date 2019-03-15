package com.clearent.commons.serialization;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ArrayDeserializerImpl<T, U extends WrapperDeserializer<U> & Wrapper<T>> implements ArrayDeserializer<T, U> {

    private static final ObjectMapper MAPPER;
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayDeserializerImpl.class);

    @Override
    public List<T> deserialize(String content, MediaType type, Class<T> clazz, Class<U> wrapperClazz) {
        switch (type) {
            case APPLICATION_JSON :
                return deserializeJson(content, clazz);
            case APPLICATION_XML :
                return deserializeXml(content, type, wrapperClazz);
            case INVALID :
            default :
                throw new IllegalStateException("Unsupported Media Type");
        }
    }


    private List<T> deserializeXml(String content, MediaType type, Class<U> wrapperClazz) {
        try {
            U objectWrapper = wrapperClazz.newInstance().deserialize(content, type);
            return objectWrapper.getWrappedObject();
        } catch ( InstantiationException | IllegalAccessException je) {
            LOGGER.error("failed deserialization", je);
            throw new IllegalStateException("failed deserialization for body [" + content + "]");
        }
    }


    private List<T> deserializeJson(String content, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException ioe) {
            LOGGER.error("failed deserialization", ioe);
            throw new IllegalStateException("failed deserialization for body [" + content + "]");
        }
    }
    
    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
    }

}
