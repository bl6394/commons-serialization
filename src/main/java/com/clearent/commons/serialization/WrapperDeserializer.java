package com.clearent.commons.serialization;


public interface WrapperDeserializer<T> {
    
    T deserialize(String content, MediaType type );

}
