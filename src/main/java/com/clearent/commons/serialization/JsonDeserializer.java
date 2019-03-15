package com.clearent.commons.serialization;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class JsonDeserializer<T> implements DeserializationStrategy<T> {

    private static final ObjectMapper MAPPER;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDeserializer.class);

    private final Class<T> clazz;

    public JsonDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(String content) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (UnrecognizedPropertyException upe) {
            final String errorMessage = "failed json deserialization.  unrecognized property [" + upe.getPropertyName() + "]";
            LOGGER.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        } catch (JsonProcessingException jpe) {
            LOGGER.error("failed json deserialization.  " + sanitizeMessage(jpe));
            throw new IllegalStateException("failed json deserialization. " + sanitizeMessage(jpe));
        } catch (IOException ioe) {
            LOGGER.error("failed json deserialization. " + ioe.getMessage());
            throw new IllegalStateException("failed json deserialization. " + ioe.getMessage());
        } catch (Exception e) {
            LOGGER.error("failed json deserialization. unexpected exception" + e.getMessage());
            throw new IllegalStateException("failed json deserialization. " + e.getMessage());
        }
    }

    private String sanitizeMessage(JsonProcessingException jpe){
        String unsafeMessage = jpe.getMessage();
        String[] split = unsafeMessage.split("\\[Source:");
        return split[0] + "[Source: {SOURCE_OBFUSCATED_FOR_PCI_COMPLIANCE})";
    }

    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
    }

}
