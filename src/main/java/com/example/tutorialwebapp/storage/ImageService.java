package com.example.tutorialwebapp.storage;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final Storage storage;
    private final String bucketName;

    @Autowired
    public ImageService(Storage storage, String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, file.getInputStream().readAllBytes());
        return filename;
    }


}
