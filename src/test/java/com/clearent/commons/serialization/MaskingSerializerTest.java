package com.clearent.commons.serialization;

import static com.clearent.commons.serialization.TestHelper.getResource;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MaskingSerializerTest {

    @Test
    public void serialization() throws JsonProcessingException {
        String resource = getResource("/json/testBeanObjectWithCardInfo.json");
        TestBeanObject request = createTestBeanObject();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(TestBeanObject.class, new MaskingSerializer<TestBeanObject>());
        mapper.findAndRegisterModules();
        mapper.registerModule(module);
        assertEquals(resource, mapper.writeValueAsString(request));
    }
    
    @Test
    public void serializionComplexJson() throws JsonProcessingException{
        String resource = getResource("/json/testBeanObject.json");
        TestBeanObject beanObject = new TestBeanObject();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(TestBeanObject.class, new MaskingSerializer<TestBeanObject>());
        mapper.findAndRegisterModules();
        mapper.registerModule(module);
        assertEquals(resource, mapper.writeValueAsString(beanObject));
    }
    
    private TestBeanObject createTestBeanObject() {
        TestBeanObject TestBeanObject = new TestBeanObject();
        TestBeanObject.setCard("4111111111111111");
        TestBeanObject.setType("SALE");
        TestBeanObject.setAmount("25.00");
        TestBeanObject.setExpDate("1219");
        return TestBeanObject;
    }

}
