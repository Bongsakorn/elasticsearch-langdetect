package org.xbib.elasticsearch.index.mapper.langdetect;

import org.elasticsearch.common.io.Streams;
import org.junit.Test;
import org.xbib.elasticsearch.NodeTestUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SimpleHttpTest extends NodeTestUtils {

    @Test
    public void httpPost() throws IOException {
        startCluster();
        try {
            String httpAddress = findHttpAddress(client());
            if (httpAddress == null) {
                throw new IllegalArgumentException("no HTTP address found");
            }
            URL base = new URL(httpAddress);
            URL url = new URL(base, "_langdetect");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            Streams.copy(new StringReader("{\"text\":\"Hallo, wie geht es Ihnen?\"}"),
                    new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            StringWriter response = new StringWriter();
            Streams.copy(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), response);
            assertEquals("{\"languages\":[{\"language\":\"de\",\"probability\":0.9999958626688854}]}",
                    response.toString());
        } finally {
            stopCluster();
        }
    }

    @Test
    public void httpPostShortProfile() throws IOException {
        startCluster();
        try {
            String httpAddress = findHttpAddress(client());
            if (httpAddress == null) {
                throw new IllegalArgumentException("no HTTP address found");
            }
            URL base = new URL(httpAddress);
            URL url = new URL(base, "_langdetect/short-text");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            Streams.copy(new StringReader("{\"text\":\"Das ist ein Text\"}"),
                    new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            StringWriter response = new StringWriter();
            Streams.copy(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), response);
            assertEquals("{\"profile\":\"short-text\",\"languages\":[{\"language\":\"de\",\"probability\":0.999996853902916}]}",
                    response.toString());
        } finally {
            stopCluster();
        }
    }

    @Test
    public void httpPostShortProfileInBody() throws IOException {
        startCluster();
        try {
            String httpAddress = findHttpAddress(client());
            if (httpAddress == null) {
                throw new IllegalArgumentException("no HTTP address found");
            }
            URL base = new URL(httpAddress);
            URL url = new URL(base, "_langdetect");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            Streams.copy(new StringReader("{\"text\":\"Das ist ein Text\",\"profile\":\"short-text\"}"),
                    new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            StringWriter response = new StringWriter();
            Streams.copy(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), response);
            assertEquals("{\"profile\":\"short-text\",\"languages\":[{\"language\":\"de\",\"probability\":0.999996853902916}]}",
                    response.toString());
        } finally {
            stopCluster();
        }
    }
}
