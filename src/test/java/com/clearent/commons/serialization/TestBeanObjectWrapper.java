package com.clearent.commons.serialization;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testBeans")
@JsonInclude(Include.NON_NULL)
public class TestBeanObjectWrapper implements Wrapper<TestBeanObject>, WrapperDeserializer<TestBeanObjectWrapper>{
    
    @XmlTransient
    private final Deserializer<TestBeanObjectWrapper> deserializer = new DeserializerImpl<TestBeanObjectWrapper>();
    @XmlTransient
    private final Serializer<TestBeanObjectWrapper> serializer = new SerializerImpl<TestBeanObjectWrapper>(); 
    
    List<TestBeanObject> list = new ArrayList<TestBeanObject>();

    public List<TestBeanObject> getList() {
        return list;
    }

    public void setList(List<TestBeanObject> list) {
        this.list = list;
    }

    @Override
    public List<TestBeanObject> getWrappedObject() {
        return getList();
    }

    @Override
    public TestBeanObjectWrapper deserialize(String content, MediaType type) {
        return deserializer.deserialize(content, type, TestBeanObjectWrapper.class);
    }
    
    public String serialize(MediaType type) {
        return serializer.serialize(this, type);
    }

}
