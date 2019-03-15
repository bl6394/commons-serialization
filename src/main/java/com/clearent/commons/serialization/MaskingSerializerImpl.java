package com.clearent.commons.serialization;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MaskingSerializerImpl<T> implements Serializer<T> {

    private static final ObjectMapper MAPPER;
    private static final String ERROR_MESSAGE = "Invalid MediaType.";
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerImpl.class);

    private final Deserializer<T> deserializer = new DeserializerImpl<T>();
    private final Serializer<T> serializer = new SerializerImpl<T>();

    @Override
    public String serialize(T object, MediaType type) {
        try {
            switch (type) {
                case APPLICATION_JSON :
                    return createJson(object);
                case APPLICATION_XML :
                    return createXml(createJson(object), object);
                case INVALID :
                default :
                    LOGGER.error(ERROR_MESSAGE);
                    throw new IllegalArgumentException(ERROR_MESSAGE);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private String createJson(T object) throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        final Class<? extends T> clazz = (Class<? extends T>) object.getClass();
        module.addSerializer(clazz, new MaskingSerializer<T>());
        MAPPER.registerModule(module);
        return MAPPER.writeValueAsString(object);
    }

    @SuppressWarnings("unchecked")
    private String createXml(String serializedMaskedObject, T object) {
        T deserializedWithMasking = deserializer.deserialize(serializedMaskedObject, MediaType.APPLICATION_JSON, (Class<T>) object.getClass());
        return serializer.serialize(deserializedWithMasking, MediaType.APPLICATION_XML);
    }
    
    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}
