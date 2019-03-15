package com.clearent.commons.serialization;


public interface Deserializer<T> {
    
    T deserialize(String content, MediaType type, Class<T> clazz);

}
