package com.clearent.commons.serialization;

import static com.clearent.commons.serialization.TestHelper.getResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class SerializerTest {

    @Test
    public void xmlSerialization() throws Exception {
        String expectedTestBeanObject = getResource("/xml/testBeanObjectWithCardInfo.xml");
        TestBeanObject TestBeanObject = createTestBeanObject();
        Serializer<TestBeanObject> serializer = new SerializerImpl<TestBeanObject>();
        assertNotNull(serializer);
        assertEquals(expectedTestBeanObject, serializer.serialize(TestBeanObject, MediaType.APPLICATION_XML));
    }

    @Test
    public void jsonSerialization() throws Exception {
        String expectedTestBeanObject = getResource("/json/testBeanObjectWithCardInfoUnMasked.json");
        TestBeanObject TestBeanObject = createTestBeanObject();
        Serializer<TestBeanObject> serializer = new SerializerImpl<TestBeanObject>();
        assertNotNull(serializer);
        assertEquals(expectedTestBeanObject, serializer.serialize(TestBeanObject, MediaType.APPLICATION_JSON));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void invalidSerialization() throws Exception {
        TestBeanObject TestBeanObject = createTestBeanObject();
        Serializer<TestBeanObject> serializer = new SerializerImpl<TestBeanObject>();
        assertNotNull(serializer);
        serializer.serialize(TestBeanObject, MediaType.INVALID);
    }

    private TestBeanObject createTestBeanObject() {
        TestBeanObject TestBeanObject = new TestBeanObject();
        TestBeanObject.setType("SALE");
        TestBeanObject.setCard("4111111111111111");
        TestBeanObject.setExpDate("1219");
        TestBeanObject.setAmount("25.00");
        return TestBeanObject;
    }
}
