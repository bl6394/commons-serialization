package com.clearent.commons.serialization;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MaskingSerializer<T> extends JsonSerializer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaskingSerializer.class);

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            MaskingSerializer<T>.AnnotationHelper annotationHelper = new AnnotationHelper(field.getName());
            try {
                evaluateAnnotations(field, annotationHelper);
                if (isSupportedValueType(field, value)) {
                    if (!annotationHelper.ignore) {
                        if (annotationHelper.mask) {
                            writeFieldMasked(value, jgen, field, annotationHelper);
                        } else {
                            if (field.get(value) != null) {
                                if (field.get(value) instanceof LocalDateTime) {
                                    jgen.writeObjectField(annotationHelper.jsonPropertyName, field.get(value));
                                } else if (field.get(value) instanceof Date) {
                                    Date date = (Date) field.get(value);
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    jgen.writeStringField(annotationHelper.jsonPropertyName, dateFormat.format(date) );
                                } else {
                                    // I'm not happy about serializing numbers as Strings, but Jill told me not to change the API. And I agree with her.
                                    jgen.writeStringField(annotationHelper.jsonPropertyName, field.get(value).toString());
                                }
                            }
                        }
                    }
                } else {
                    if (!annotationHelper.ignore && field.get(value) != null && annotationHelper.jsonPropertyName != "$jacocoData") {
                        jgen.writeObjectField(annotationHelper.jsonPropertyName, field.get(value));
                    }
                }

            } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                LOGGER.error("failed to deserialize with masking", e);
                throw new IllegalStateException("failed to deserialize");
            }
        }
        jgen.writeEndObject();
    }

    private boolean isSupportedValueType(Field field, T value) throws IllegalAccessException {
        return field.get(value) instanceof String ||
                field.get(value) instanceof Number ||
                field.get(value) instanceof LocalDateTime ||
                field.get(value) instanceof Date;

    }

    private void writeFieldMasked(T value, JsonGenerator jgen, Field field, MaskingSerializer<T>.AnnotationHelper annotationHelper)
            throws NoSuchMethodException, IOException, JsonGenerationException, IllegalAccessException, InvocationTargetException {
        if (annotationHelper.maskMethodName.equalsIgnoreCase("")) {
            jgen.writeStringField(annotationHelper.jsonPropertyName, "...");
        } else {
            final Method maskingMethod = value.getClass().getMethod(annotationHelper.maskMethodName, new Class[0]);
            maskingMethod.setAccessible(true);
            jgen.writeStringField(annotationHelper.jsonPropertyName, maskingMethod.invoke(value, new Object[0]).toString());
        }
    }

    private void evaluateAnnotations(Field field, MaskingSerializer<T>.AnnotationHelper helper) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().equals(XmlTransient.class)) {
                helper.ignore = true;
            }
            if (annotation.annotationType().equals(JsonIgnore.class)) {
                helper.ignore = true;
            }
            if (annotation.annotationType().equals(MaskProperty.class)) {
                MaskProperty maskProperty = (MaskProperty) annotation;
                helper.mask = maskProperty.enabled();
                helper.maskMethodName = maskProperty.maskMethodName();
            }
            if (annotation.annotationType().equals(JsonProperty.class)) {
                JsonProperty jsonProperty = (JsonProperty) annotation;
                if (!"".equals(jsonProperty.value())) {
                    helper.jsonPropertyName = jsonProperty.value();
                }
            }
        }
    }

    private class AnnotationHelper {
        boolean ignore = false;
        boolean mask = false;
        String maskMethodName = "";
        String jsonPropertyName = "";

        public AnnotationHelper(String name) {
            this.jsonPropertyName = name;
        }
    }

}
