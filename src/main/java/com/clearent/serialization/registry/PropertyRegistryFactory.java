package com.clearent.serialization.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRegistryFactory {
    
    private static final Map<String, Class<?>> DEFAULT_REGISTRY = new ConcurrentHashMap<>();
    
    public static final PropertyRegistry getInstance(){
        return new PropertyRegistry(DEFAULT_REGISTRY);
    }
    
    public static final void setDefaultRegistry(Map<String, Class<?>> defaultRegistry){
       DEFAULT_REGISTRY.putAll(defaultRegistry);
    }

}
