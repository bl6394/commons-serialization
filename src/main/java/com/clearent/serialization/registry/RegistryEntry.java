package com.clearent.serialization.registry;

class RegistryEntry {

    private final String propertyName;
    private final String fullyQualifiedClassName;
    private final String simpleName;
    private final Class<?> clazz;

    RegistryEntry(String propertyName, String fullyQualifiedClassName) {
        validateAguments(propertyName, fullyQualifiedClassName);
        this.clazz = getClazzFromFQCN(fullyQualifiedClassName);
        this.simpleName = this.clazz.getSimpleName();
        this.propertyName = propertyName;
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }

    private Class<?> getClazzFromFQCN(String fullyQualifiedClassName) {
        try {
            return Class.forName(fullyQualifiedClassName, false, this.getClass().getClassLoader()); 
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(fullyQualifiedClassName + "was not found by classloader");
        }
    }

    private void validateAguments(String propertyName, String fullyQualifiedClassName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName can't be null");
        }
        if (fullyQualifiedClassName == null) {
            throw new IllegalArgumentException("fullyQualifiedClassName can't be null");
        }
    }
    
    public RegistryEntry(String propertyName, Class<?> clazz) {
        validateAguments(propertyName, clazz.getName());
        this.clazz = clazz;
        this.simpleName = this.clazz.getSimpleName();
        this.propertyName = propertyName;
        this.fullyQualifiedClassName = clazz.getName();
    }

    String getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }
    
    Class<?> getClazz() {
        return clazz;
    }

    String getSimpleName() {
        return simpleName;
    }

    String getClassFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

    String getPropertyName() {
        return propertyName;
    }

}
