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
     * 把本地文件上传到GCS
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

    /*
     * 1, 创建授权服务对象
     *    向 Google Cloud Storage 发出请求, 您必须创建服务对象。
     *    然后，您可以通过调用存储服务对象上的方法来进行 API 调用。
     * 
     * 2, 存储的对象在 google-cloud 中称为 “blob”，并被组织到称为 “buckets” 的容器中。
     *    Blob 是 BlobInfo 的子类，在 BlobInfo 之上增加了一层服务相关的功能。
     *    同样，Bucket 在 BucketInfo 之上增加了一层服务相关的功能
     */
    public void getService() {

        // 创建服务对象
        Storage storage = StorageOptions.getDefaultInstance().getService();
        // 存储数据
        // 在此代码段中，我们将创建一个新存储桶并将一个 blob 上传到该存储桶。
    }

    /**
     * 使用字节流做成文件到GCS
     * 
     * @throws IOException
     */
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
     * 验证返回json格式
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
