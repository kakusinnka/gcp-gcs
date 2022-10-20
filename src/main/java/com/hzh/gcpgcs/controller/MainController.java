package com.hzh.gcpgcs.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.hzh.gcpgcs.dto.StudentDto;
import com.hzh.gcpgcs.dto.UserDto;

@RestController
public class MainController {

    @Autowired
    UserDto userDto;

    @Autowired
    StudentDto studentDto;

    /**
     * 从文件上传对象
     * 
     * @throws IOException
     */
    @GetMapping(value = "/UploadObject")
    public void uploadObject() throws IOException {
        // The ID of your GCP project
        String projectId = "my-project-29437-364300";

        // The ID of your GCS bucket
        String bucketName = "hzh-gcs-bucket001";

        // The ID of your GCS object
        String objectName = "hzh/hzh1.txt";

        // The path to your file to upload
        String filePath = "file\\hzh1.txt";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    @GetMapping(value = "/uploadObjectFromMemory")
    public static void uploadObjectFromMemory() throws IOException {
        // The ID of your GCP project
        String projectId = "my-project-29437-364300";

        // The ID of your GCS bucket
        String bucketName = "hzh-gcs-bucket001";

        // The ID of your GCS object
        String objectName = "hzh/hzh2.txt";

        // The string of contents you wish to upload
        String contents = "Hello world!";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] content = contents.getBytes(StandardCharsets.UTF_8);
        storage.createFrom(blobInfo, new ByteArrayInputStream(content));

        System.out.println(
                "Object "
                        + objectName
                        + " uploaded to bucket "
                        + bucketName
                        + " with contents "
                        + contents);
    }

    /**
     * 从文件上传对象
     * 
     * @throws IOException
     */
    @GetMapping(value = "/user")
    public UserDto user() {

        studentDto.setClasses("sanban");
        studentDto.setStudentId("202020");

        userDto.setId("123456");
        userDto.setName("laoliu");
        userDto.setStudentDto(studentDto);

        return userDto;
    }

}
