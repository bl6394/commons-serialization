package com.clearent.commons.serialization;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class FragmentSerializerImpl<T> extends SerializerImpl<T> implements Serializer<T> {

    // Marshalls XML, minus the declaration/prologue <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    @Override
    public Marshaller getMarshaller(JAXBContext jc) throws JAXBException {
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        return marshaller;
    }

}

