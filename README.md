# gcp-gcs
* https://github.com/googleapis/java-storage
* https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/overview

##  将本地 csv 文件上传到 GCS
* 需要一个有Cloud Storage > Storage Admin角色的服务账号。并将 GOOGLE_APPLICATION_CREDENTIALS 环境变量设置为该密钥的位置。
* 或者使用ADC (Application Default Credentials) 应用默认凭据。
```
# 将用户凭据提供给 ADC
  gcloud auth application-default login
```