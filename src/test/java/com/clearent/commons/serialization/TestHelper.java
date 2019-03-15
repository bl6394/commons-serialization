package com.clearent.commons.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class TestHelper {

    public static final String getResource(String name) {
        InputStream resourceAsStream = null;
        Scanner s = null;
        try {
            resourceAsStream = TestHelper.class.getResourceAsStream(name);
            if (resourceAsStream != null) {
                s = new Scanner(resourceAsStream, "UTF-8").useDelimiter("\\A");
                return (s.hasNext()) ? s.next() : "";
            } else {
                throw new IllegalArgumentException("Resource [" + name + "] not found");
            }
        } finally {
            closeResource(resourceAsStream);
            closeScanner(s);
        }
    }

    private static void closeResource(InputStream resourceAsStream) {
        if (resourceAsStream != null) {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeScanner(Scanner s) {
        if (s != null) {
            s.close();
        }
    }

}
