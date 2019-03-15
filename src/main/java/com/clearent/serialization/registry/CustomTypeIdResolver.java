package com.clearent.serialization.registry;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CustomTypeIdResolver extends TypeIdResolverBase {

    private JavaType mBaseType;

    CustomTypeIdResolver() {
    }
    
    CustomTypeIdResolver(PropertyRegistry registry) {
    }

    @Override
    public void init(JavaType baseType) {
        mBaseType = baseType;
    }

    @Override
    public String idFromValue(Object value) {
        return idFromValueAndType(value, value.getClass());
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> clazz) {
        return PropertyRegistryFactory.getInstance().findPropertyNameByClass(clazz);
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, mBaseType.getRawClass());
    }

    //Spring 4.3 upgrade CustomTypeIdResolver.java:[39,5] method does not override or implement a method from a supertype
    //@Override
    public JavaType typeFromId(String propertyName) {
        Class<?> clazz = PropertyRegistryFactory.getInstance().findClassByPropertyName(propertyName);
        return TypeFactory.defaultInstance().constructSpecializedType(mBaseType, clazz);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String propertyName) {
        Class<?> clazz = PropertyRegistryFactory.getInstance().findClassByPropertyName(propertyName);
        return TypeFactory.defaultInstance().constructSpecializedType(mBaseType, clazz);
    }

    @Override
    public Id getMechanism() {
        return Id.CUSTOM;
    }

}
