package com.example.tutorialwebapp.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${google.cloud.project.id}")
    private String projectId;

    @Value("${google.cloud.storage.bucket.name}")
    private String bucketName;

    @Bean
    public Storage storage() throws IOException {
        URL credentialsUrl = new URL("https://storage.googleapis.com/catinder_bucket/catinder-f029cf8f7ba5.json");
        InputStream credentialsStream = credentialsUrl.openStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        StorageOptions options = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build();
        return options.getService();
    }

    @Bean
    public String bucketName() {
        return bucketName;
    }

}
