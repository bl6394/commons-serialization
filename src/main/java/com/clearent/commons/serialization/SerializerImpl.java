package com.clearent.commons.serialization;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializerImpl<T> implements Serializer<T> {
    
    private static final ObjectMapper MAPPER;
    private static final String ERROR_MESSAGE = "Invalid MediaType.";
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerImpl.class);

    @Override
    public String serialize(T object, MediaType type) {
        String response = "";
        try {
            switch (type) {
                case APPLICATION_JSON :
                    response = MAPPER.writeValueAsString(object);
                    break;
                case APPLICATION_XML :
                    response = serializeXML(object);
                    break;
                case INVALID :
                default :
                    LOGGER.error(ERROR_MESSAGE);
                    throw new IllegalArgumentException(ERROR_MESSAGE);
            }
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException(e);
        }
        return response;
    }
    
    private String serializeXML(T object) throws JAXBException {
        String response;
        JAXBContext jc = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = getMarshaller(jc);
        final StringWriter w = new StringWriter();
        marshaller.marshal(object, w);
        response = w.toString();
        return response;
    }

    public Marshaller getMarshaller(JAXBContext jc) throws JAXBException {
        return jc.createMarshaller();
    }
    
    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
    }

}

