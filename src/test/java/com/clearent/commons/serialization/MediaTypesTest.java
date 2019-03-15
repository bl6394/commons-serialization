package com.clearent.commons.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MediaTypesTest {

    @Test
    public void validMediaType() {
        assertEquals(MediaType.APPLICATION_JSON,MediaType.valueOfMediaType("application/json"));
    }
    
    @Test
    public void caseInsensitiveValidMediaType() {
        assertEquals(MediaType.APPLICATION_JSON,MediaType.valueOfMediaType("ApPlicAtion/jsON"));
    }
    
    @Test
    public void inValidMediaType() {
        assertEquals(MediaType.INVALID,MediaType.valueOfMediaType("application/noExist"));
        assertEquals(MediaType.INVALID,MediaType.valueOfMediaType("text/plain"));
    }

    @Test
    public void makeSureTheCodeWorksWhenChromeDevelopersDecideToSuffixTheAcceptHeaderForWebP_comma() throws Exception{
        assertEquals("application/json",MediaType.getMediaTypeFromContentType("application/json,image/webp"));
    }

    @Test
    public void makeSureTheCodeWorksWhenChromeDevelopersDecideToSuffixTheAcceptHeaderForWebP_semicolon() throws Exception{
        assertEquals("application/json",MediaType.getMediaTypeFromContentType("application/json;image/webp"));
    }

    @Test
    public void value() {
        assertEquals("application/json",MediaType.APPLICATION_JSON.toString());
    }
    
    @Test
    public void getContentTypeBlank(){
        assertEquals("", MediaType.getMediaTypeFromContentType(""));
    }

    @Test
    public void getContentTypeNull(){
        assertEquals("", MediaType.getMediaTypeFromContentType(null));
    }

    @Test
    public void testJsonWithCharset() throws Exception {
        assertEquals("Did not handle charset from type", MediaType.APPLICATION_JSON, MediaType.valueOfMediaType(MediaType.getMediaTypeFromContentType("application/json; charset=ISO-8859-1")));
    }
}
