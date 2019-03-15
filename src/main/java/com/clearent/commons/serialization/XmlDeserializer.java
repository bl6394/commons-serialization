package com.clearent.commons.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class XmlDeserializer <T> implements DeserializationStrategy<T> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerImpl.class);
    protected final Class<T> clazz;
    
    public XmlDeserializer(Class<T> clazz){
        this.clazz = clazz;
    }
    
    @Override
    public T deserialize(String content) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            final Schema schema = createSchema(context);
            try{
                return _deserialize(content, clazz, schema, context);
            } catch (SAXException | JAXBException e){
                final String message = e.getCause().getMessage();
                LOGGER.error("FIDDLESTICKS_BAD_CLIENT_DATA", message);
                return _deserialize(content, clazz, null, context);
            } 
        } catch ( SAXException | JAXBException je) {
            final String message = je.getCause().getMessage();
            LOGGER.error("failed xml deserialization", message);
            String errorMessage = sanitizeMessage(message);
            throw new IllegalStateException("failed xml deserialization." + errorMessage);
        } catch (IOException e) {
            LOGGER.error("unable to generate schema");
            throw new IllegalStateException("unable to generate schema");
        }
    }
    
    protected T _deserialize(String content, Class<T> clazz, Schema schema, JAXBContext context) throws JAXBException, IOException, SAXException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schema);
        final T value = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(content.getBytes())), clazz).getValue();
        return value;
    }

    protected Schema createSchema(JAXBContext context) throws IOException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final MySchemaOutputResolver outputResolver = new MySchemaOutputResolver();
        context.generateSchema(outputResolver);
        final Schema newSchema = schemaFactory.newSchema(new StreamSource(new ByteArrayInputStream(outputResolver.getSchema().getBytes())));
        return newSchema;
    }

    protected String sanitizeMessage(String message) {
        String retValue = "";
        if (message == null) {
            return retValue;
        }
        if (message.contains("Content is not allowed in prolog")) {
            return "  Malformed XML";
        }
        if (message.contains("Invalid content was found starting with element '")) {
            String tailMessage = message.split("Invalid content was found starting with element '")[1];
            String invalidContent = tailMessage.split("'")[0];
            return "  unrecognized element [" + invalidContent + "]";
        }
        return retValue;
    }
    
    protected class MySchemaOutputResolver extends SchemaOutputResolver {

        private StringWriter stringWriter = new StringWriter();

        @Override
        public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
            StreamResult result = new StreamResult(stringWriter);
            result.setSystemId(suggestedFileName);
            return result;
        }

        private String getSchema() {
            return stringWriter.toString();
        }

    }

}
