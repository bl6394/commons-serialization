package com.clearent.commons.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaskProperty {
    
    public boolean enabled() default true;
    
    public String maskMethodName() default "";

}
