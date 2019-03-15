package com.clearent.commons.serialization;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateTimeAdapter  extends XmlAdapter<String, LocalDateTime>{

    private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeAdapter.class);

    public LocalDateTime unmarshal(String xmlGregorianCalendar) {
        try {
            LocalDateTime result = LocalDateTime.parse(xmlGregorianCalendar, DateTimeFormatter.ISO_DATE_TIME);
            return result;
        } catch (DateTimeParseException ex) {
            logger.error("Could not parse date" + xmlGregorianCalendar, ex);
            return null;
        }
    }

    public String marshal(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
