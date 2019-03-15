package com.clearent.commons.serialization;

import java.lang.reflect.InvocationTargetException;


public class DeserializerImpl<T> implements Deserializer<T> {

    @Override
    public T deserialize(String content, MediaType type, Class<T> clazz) {
        DeserializationStrategy<T> deserializationStrategy = getDeserializationStrategy(type, clazz);
        return deserializationStrategy.deserialize(content);
    }
    
    DeserializationStrategy<T> getDeserializationStrategy(MediaType type, Class<T> clazz){
        switch (type) {
            case APPLICATION_JSON :
                return new JsonDeserializer<T>(clazz);
            case APPLICATION_XML :
                return new XmlDeserializer<T>(clazz);
            case INVALID :
            default :
                throw new IllegalStateException("Unsupported Media Type");
        }
    }
    
    @SuppressWarnings("unchecked")
    DeserializationStrategy<T> getDeserializationStrategy(Class<?> deserializationClazz, Class<T> clazz){
        try {
            return (DeserializationStrategy<T>)deserializationClazz.getConstructor(Class.class).newInstance(clazz);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new IllegalStateException("unable to get instance of deserializer of type " + deserializationClazz, e);
        }
    }

}
