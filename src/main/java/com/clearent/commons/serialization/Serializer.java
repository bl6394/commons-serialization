package com.clearent.commons.serialization;


public interface Serializer <T> {

    String serialize(T target, MediaType type);
}
