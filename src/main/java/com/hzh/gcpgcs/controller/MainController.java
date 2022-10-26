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
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;

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

        // StorageOptions.newBuilder() -> ServiceOptions.Builder.setProjectId ->
        // StorageOptions.Builder.build() -> ServiceOptions.getService() -> 返回当前服务的服务对象。
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    /*
     * 1, 创建授权服务对象
     * 向 Google Cloud Storage 发出请求, 您必须创建服务对象。
     * 然后，您可以通过调用存储服务对象上的方法来进行 API 调用。
     * 
     * 2, 存储的对象在 google-cloud 中称为 “blob”，并被组织到称为 “buckets” 的容器中。
     * Blob 是 BlobInfo 的子类，在 BlobInfo 之上增加了一层服务相关的功能。
     * 同样，Bucket 在 BucketInfo 之上增加了一层服务相关的功能
     */
    @GetMapping(value = "/getService")
    public void getService() {

        // 创建服务对象
        // StorageOptions.getDefaultInstance -> 返回一个默认 StorageOptions 实例.
        // ServiceOptions.getService() -> 返回当前服务的服务对象。
        Storage storage = StorageOptions.getDefaultInstance().getService();
        // 存储数据
        // 在此代码段中，我们将创建一个新存储桶并将一个 blob 上传到该存储桶。
        // 创建一个 bucket
        // BucketInfo.of -> 为提供的 bucket 名称创建一个 BucketInfo 对象.
        // storage.create -> 创建一个新的 bucket.
        String bucketName = "hzh-gcs-bucket002";
        Bucket bucket = storage.create(BucketInfo.of(bucketName));

        // 将 Blob 上传到新创建的存储桶
        // BlobId: GCS 对象标识符。
        // BlobId 对象包括包含存储桶的名称，blob 的名称，可能还有 blob 的世代。
        // 如果 #getGeneration() 为 null，则标识符指的是最新 blob 的世代。
        // BlobId.of -> 创建一个 blob 标识符。世代设置为空。
        BlobId blobId = BlobId.of(bucketName, "hzh/hello.txt");
        // BlobInfo: 有关 GCS 中对象的信息。
        // BlobInfo 对象包括 BlobId 实例和属性集, 比如blob的访问控制配置，用户提供的元数据，CRC32C 校验和，等。
        // BlobInfo的实例用于在 GCS 中创建新对象或更新现有对象的属性。
        // 为了处理现有的存储对象，API 包括 Blob 类，它扩展了 BlobInfo 并声明了对对象执行操作的方法。
        // BlobInfo 和 Blob 实例都不保留对象内容，只保留对象属性。
        // BlobInfo.newBuilder -> 返回一个 BlobInfo 构建器，其中使用提供的值设置 blob 标识。
        // BlobInfo.Builder: BlobInfo 的生成器。
        // BlobInfo.Builder.setContentType -> 设置 blob 的数据内容类型。
        // BlobInfo.Builder.build -> 创建一个 BlobInfo 对象。
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        // storage.create -> 创建一个新的 blob。直接上传用于上要传内容。
        // 对于大型内容，建议使用#writer，因为它使用可恢复上传。
        // 计算内容的 MD5 和 CRC32C 哈希值并用于验证传输的数据。
        // 接受一个可选的 userProject BlobGetOption 选项，该选项定义项目 ID 以分配运营成本。
        // 如果未明确设置，则从 blob 名称中检测内容类型。
        Blob blob = storage.create(blobInfo, "a simple blob".getBytes(UTF_8));
    }

    /*
     * 从 GCS 上读取数据
     */
    @GetMapping(value = "/retrievingData")
    public String retrievingData() {
        String bucketName = "hzh-gcs-bucket002";
        String blobName = "hzh/hello.txt";
        Storage storage = StorageOptions.getDefaultInstance().getService();

        BlobId blobId = BlobId.of(bucketName, blobName);
        // Storage.readAllBytes -> 从 blob 中读取所有字节。
        byte[] content = storage.readAllBytes(blobId);
        String contentString = new String(content, UTF_8);
        return contentString;
    }

    /*
     * 下载对象从 GCS.
     */
    @GetMapping(value = "/downloadObject")
    public static void downloadObject() {
        // The ID of your GCP project
        String projectId = "my-project-29437-364300";

        // The ID of your GCS bucket
        String bucketName = "hzh-gcs-bucket002";

        // The ID of your GCS object
        String objectName = "hzh/hello.txt";

        // The path to which the file should be downloaded
        String destFilePath = "./file/file.txt";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        // Storage.get -> 则返回请求的 blob 。如果未找到则返回 null。
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        // Blob.downloadTo -> 将此 blob 下载到给定的文件路径。
        blob.downloadTo(Paths.get(destFilePath));

        System.out.println(
                "Downloaded object "
                        + objectName
                        + " from bucket name "
                        + bucketName
                        + " to "
                        + destFilePath);
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
