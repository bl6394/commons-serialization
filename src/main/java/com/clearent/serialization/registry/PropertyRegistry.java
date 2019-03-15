package com.clearent.serialization.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRegistry {
    
    private final Map<String, RegistryEntry> registry = new ConcurrentHashMap<>();
    
    public PropertyRegistry() {
    }

    public PropertyRegistry(Map<String, Class<?>> defaultRegistry) {
        for( Map.Entry<String,Class<?>> entry : defaultRegistry.entrySet()){
            registry.put(entry.getKey(), new RegistryEntry(entry.getKey(), entry.getValue()) );
        }
    }

    Map<String, RegistryEntry> getRegistry() {
        return registry;
    }
    
    public String findClassFQCNNameByPropertyName(String propertyName){
        return find(propertyName).getClassFullyQualifiedClassName();
    }
    
    public String findClassSimpleNameByPropertyName(String propertyName){
        return find(propertyName).getSimpleName();
    }
    
    public Class<?> findClassByPropertyName(String propertyName) {
        return find(propertyName).getClazz();
    }
    
    
    public void registerPropertyName(String propertyName, String fQCN){
        registry.put(propertyName, new RegistryEntry(propertyName, fQCN));
    }
    
    public void registerPropertyName(String propertyName, Class<?> clazz){
        registry.put(propertyName, new RegistryEntry(propertyName, clazz));
    }

    public boolean contains(String propertyName) {
        return registry.containsKey(propertyName);
    }

    public Class<?>[] getRegisteredClasses() {
        List<Class<?>> clazzes = new ArrayList<>();
        for(RegistryEntry entry : registry.values()){
            clazzes.add(entry.getClazz());
        }
        return clazzes.toArray(new Class<?>[0]);
    }

    private RegistryEntry find(String propertyName){
        if (registry.containsKey(propertyName)){
            return registry.get(propertyName);
        } else {
            throw new IllegalArgumentException(propertyName + " has not been registered");
        }
    }

    public String findPropertyNameByClass(Class<?> clazz) {
        for(RegistryEntry entry : registry.values()){
            if(clazz.equals(entry.getClazz())){
                return entry.getPropertyName();
            }
        }
        throw new IllegalArgumentException(clazz + " is not registered");
        
    }

    public boolean isEmpty() {
        return registry.isEmpty();
    }
}
