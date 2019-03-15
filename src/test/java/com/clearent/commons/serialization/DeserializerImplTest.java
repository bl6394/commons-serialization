package com.clearent.commons.serialization;

import static com.clearent.commons.serialization.TestHelper.getResource;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DeserializerImplTest {
    
    @Rule 
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void deserializationJson() {
        String message = getResource("/json/testBeanObjectWithCardInfoUnMasked.json");
        TestBeanObject beanObject = TestBeanObject.deserialize(message, MediaType.APPLICATION_JSON);
        assertNotNull(beanObject);
        assertEquals(LocalDateTime.of(2016, 1, 1, 1, 1), beanObject.getMyDateTime());
    }
    
    @Test
    public void deserializationXml() {
        String message = getResource("/xml/testBeanObjectWithCardInfo.xml");
        TestBeanObject beanObject = TestBeanObject.deserialize(message, MediaType.APPLICATION_XML);
        assertNotNull(beanObject);
        assertEquals(LocalDateTime.of(2016, 1, 1, 1, 1), beanObject.getMyDateTime());
    }
    
    @Test
    public void deserializationJsonFailUnknownProperty() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("failed json deserialization.  unrecognized property [weirdField]");
        String message = getResource("/json/testBeanObjectWithCardInfoUnMaskedAndUnknownField.json");
        TestBeanObject.deserialize(message, MediaType.APPLICATION_JSON);
    }
    
    @Test
    public void deserializationXmlFailUnknownProperty() {
//        Enhanced Verification Turned off Pending Check to make sure we aren't breaking a client
//        expectedEx.expect(IllegalStateException.class);
//        expectedEx.expectMessage("failed xml deserialization.  unrecognized element [weirdField]");
        // TODO - replace expectedEx - once we disallow unverified XML.
        String message = getResource("/xml/testBeanObjectWithCardInfoAndUnknownField.xml");
        final TestBeanObject bean = TestBeanObject.deserialize(message, MediaType.APPLICATION_XML);
        assertNotNull(bean);
    }
    
    @Test
    public void deserializationXmlFailGarbage() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("failed xml deserialization.  Malformed XML");
        String message = getResource("/xml/garbage.xml");
        TestBeanObject.deserialize(message, MediaType.APPLICATION_XML);
    }

    @Test
    public void deserializationJsonFailGarbage() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("failed json deserialization. Unrecognized token 'Will': was expecting ('true', 'false' or 'null')");
        String message = getResource("/json/garbage.json");
        TestBeanObject.deserialize(message, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deserializationJsonIncomplete() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage(containsString("SOURCE_OBFUSCATED_FOR_PCI_COMPLIANCE"));
        String message = getResource("/json/incomplete.json");
        TestBeanObject.deserialize(message, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deserializationJsonIncompleteMissingQuote() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage(containsString("SOURCE_OBFUSCATED_FOR_PCI_COMPLIANCE"));
        String message = getResource("/json/incompleteMissingQuote.json");
        TestBeanObject.deserialize(message, MediaType.APPLICATION_JSON);
    }

    @Test
    public void simpleFactoryTest(){
        String message = getResource("/json/testBeanObjectWithCardInfoUnMasked.json");
        DeserializerImpl<TestBeanObject> deserializerImpl = new DeserializerImpl<TestBeanObject>();
        DeserializationStrategy<TestBeanObject> deserializationStrategy = deserializerImpl.getDeserializationStrategy(JsonDeserializer.class, TestBeanObject.class);
        TestBeanObject beanObject = deserializationStrategy.deserialize(message);
        assertNotNull(beanObject);
    }

}
