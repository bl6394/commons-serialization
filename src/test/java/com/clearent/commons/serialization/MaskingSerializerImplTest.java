package com.clearent.commons.serialization;

import static com.clearent.commons.serialization.TestHelper.getResource;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class MaskingSerializerImplTest  {

    @Test
    public void serializeJson() {
        String resource = getResource("/json/testBeanObject.json");
        TestBeanObject object = new TestBeanObject();
        Serializer<TestBeanObject> serializer = new MaskingSerializerImpl<TestBeanObject>();
        assertEquals(resource,serializer.serialize(object, MediaType.APPLICATION_JSON));
    }
    
    @Test
    public void serializeXml() {
        String resource = getResource("/xml/testBeanObject.xml");
        TestBeanObject object = new TestBeanObject();
        Serializer<TestBeanObject> serializer = new MaskingSerializerImpl<>();
        assertEquals(resource,serializer.serialize(object, MediaType.APPLICATION_XML));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void serializeInvalid() {
        TestBeanObject object = new TestBeanObject();
        Serializer<TestBeanObject> serializer = new MaskingSerializerImpl<TestBeanObject>();
        serializer.serialize(object, MediaType.INVALID);
    }

    @Test
    public void testApiKey() throws Exception {
        String resource = getResource("/json/testBeanObjectWithApiKey.json");
        TestBeanObject object = new TestBeanObject();
        object.setApiKey("1234567890");
        assertEquals(resource, object.toString());
    }
    
    @Test
    public void testLocalDateTime() throws Exception {
        String resource = getResource("/json/testBeanObjectWithApiKey.json");
        TestBeanObject object = new TestBeanObject();
        object.setApiKey("1234567890");
        assertEquals(resource, object.toString());
    }
    
    @Test
    public void testJavaUtilDate() throws Exception {
        String resource = getResource("/json/testBeanObjectWithDate.json");
        TestBeanObject object = new TestBeanObject();
        object.setOldStyleDate(new Date(523456789999L));
        assertEquals(resource, object.toString());
    }
    
    @Test
    public void testInnerComplexObjectWhenMasking() throws Exception {
        String resource = getResource("/json/testComplexBeanObjectWithInnerBean.json");
        TestBeanObject outerSelf = new TestBeanObject();
        outerSelf.setApiKey("1234567890");
        TestBeanObject innerSelf = new TestBeanObject();
        innerSelf.setApiKey("9876543210");
        outerSelf.setMyInnerSelf(innerSelf);
        assertEquals(resource, outerSelf.toString());
    }
    
    
}
