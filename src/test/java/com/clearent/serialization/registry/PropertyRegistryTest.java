package com.clearent.serialization.registry;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.clearent.commons.serialization.TestBeanObject;

public class PropertyRegistryTest {

    @Test
    public void construction() {
        assertNotNull(new PropertyRegistry());
    }
    
    @Test
    public void loadOne(){
        Class<?>[] expectedClassArray = {TestBeanObject.class};
        PropertyRegistry propertyRegistry = new PropertyRegistry();
        propertyRegistry.registerPropertyName("test-bean-object", "com.clearent.commons.serialization.TestBeanObject");
        assertTrue(propertyRegistry.contains("test-bean-object"));
        assertTrue(Arrays.deepEquals(expectedClassArray, propertyRegistry.getRegisteredClasses()) );
    }

}
