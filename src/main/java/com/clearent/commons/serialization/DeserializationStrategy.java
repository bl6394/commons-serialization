package com.clearent.commons.serialization;

public interface DeserializationStrategy <T> {
    
    T deserialize(String content);

}
