package com.clearent.serialization.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RegistryEntryTest {

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalConstruction() {
        new RegistryEntry(null, Object.class);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testClassNotFound() {
        new RegistryEntry("notOnClasspath", "com.clearent.ClassDoesNotExist");
    }
    
    @Test
    public void construction (){
        RegistryEntry registryEntry = new RegistryEntry("test-bean-object", "com.clearent.commons.serialization.TestBeanObject");
        assertNotNull(registryEntry);
        assertEquals("test-bean-object", registryEntry.getPropertyName());
        assertEquals("com.clearent.commons.serialization.TestBeanObject", registryEntry.getClassFullyQualifiedClassName());
        assertEquals("TestBeanObject", registryEntry.getSimpleName());
    }
    

}
