package com.clearent.commons.serialization;

import static com.clearent.commons.serialization.TestHelper.getResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class ArrayDeserializerImplTest {

    @Test
    public void deserializeJsonArray() {
        List<TestBeanObject> list = deserializeTestBeanObjectArrayAsJson();
        assertNotNull(list);
        assertEquals("432112341111", list.get(0).getCard());
    }

    @Test
    public void serializeDeserializeXmlArray() {
        List<TestBeanObject> list = deserializeTestBeanObjectArrayAsJson();;
        TestBeanObjectWrapper beanObjectWrapper = new TestBeanObjectWrapper();
        beanObjectWrapper.setList(list);
        String serializedXml = beanObjectWrapper.serialize(MediaType.APPLICATION_XML);
        ArrayDeserializerImpl<TestBeanObject, TestBeanObjectWrapper> arrayDeserializerImpl = new ArrayDeserializerImpl<TestBeanObject, TestBeanObjectWrapper>();
        List<TestBeanObject> xmlList = arrayDeserializerImpl.deserialize(serializedXml, MediaType.APPLICATION_XML, TestBeanObject.class, TestBeanObjectWrapper.class);
        assertNotNull(xmlList);
        assertEquals("432112341111", list.get(0).getCard());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void serializeInvalid(){
        List<TestBeanObject> list = deserializeTestBeanObjectArrayAsJson();;
        TestBeanObjectWrapper beanObjectWrapper = new TestBeanObjectWrapper();
        beanObjectWrapper.setList(list);
        beanObjectWrapper.serialize(MediaType.INVALID);
    }
    
    
    private List<TestBeanObject> deserializeTestBeanObjectArrayAsJson() {
        String resource = getResource("/json/arrayOfBeanObjects.json");
        ArrayDeserializerImpl<TestBeanObject, TestBeanObjectWrapper> arrayDeserializerImpl = new ArrayDeserializerImpl<TestBeanObject, TestBeanObjectWrapper>();
        List<TestBeanObject> list = arrayDeserializerImpl.deserialize(resource, MediaType.APPLICATION_JSON, TestBeanObject.class, TestBeanObjectWrapper.class);
        return list;
    }
    
}
