package com.clearent.commons.serialization;

import java.util.List;

public interface ArrayDeserializer<T, U extends  WrapperDeserializer<U> & Wrapper<T>> {
    
    List<T> deserialize(String content, MediaType type, Class<T> clazz, Class<U> wrapperClazz);

}
