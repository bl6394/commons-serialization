package com.clearent.commons.serialization;

public enum MediaType {

    APPLICATION_JSON("application/json"), APPLICATION_XML("application/xml"), INVALID("text/plain");

    private String value;

    MediaType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static final MediaType valueOfMediaType(String mT) {
        for (MediaType mediaType : MediaType.values()) {
            if (mediaType.value.equalsIgnoreCase(mT)) {
                return mediaType;
            }
        }
        return MediaType.INVALID;
    }

    public static final String getMediaTypeFromContentType(String contentType) {
        String returnValue = "";
        if (contentType != null) {
            String[] strings = contentType.split("[;,]");
            for (String value : strings) {
                if (value.toLowerCase().matches("application/\\w+")) {
                    returnValue = value.toLowerCase();
                }
            }
        }
        return returnValue;
    }

    // TODO possible bug. Do we want to return XML for Invalid type?
    public String getFileExtension() {
        return (APPLICATION_JSON.value.equals(value)) ? "json" : "xml";
    }

}
