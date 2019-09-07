package com.nsergey.quickdownloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class DownloadFileHttpClient {

    // disable annoying logging
    static {
        Set<String> artifactoryLoggers = new HashSet<>(Arrays.asList("org.apache.http", "groovyx.net.http"));
        for (String log : artifactoryLoggers) {
            ch.qos.logback.classic.Logger artLogger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                    log);
            artLogger.setLevel(ch.qos.logback.classic.Level.INFO);
            artLogger.setAdditive(false);
        }
    }

    public static void main(String[] args) throws Exception {
        DownloadFileHttpClient client = new DownloadFileHttpClient();
        client.download("http://effortlessenglishclub.com/podcast/HiddenCurriculumVIP-VidAudio.zip",
                        "U:\\Изучение языков\\Эй Джей Хог\\VIP-ProgramAuto\\HiddenCurriculumVIP-VidAudio.zip");
    }

    public long download(String url, String saveToFile) {
        long bytes = 0;
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Request Url: " + request.getURI());
            System.out.println("Response Code: " + responseCode);

            try (InputStream is = entity.getContent()) {
                bytes = Files.copy(is, Paths.get(saveToFile), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | UnsupportedOperationException e) {
                e.printStackTrace();
            }

            System.out.println("File Download Completed!!!");
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
