# gcp-gcs
将本地csv文件上传到gcs
* 需要一个有Cloud Storage > Storage Admin角色的服务账号。并将 GOOGLE_APPLICATION_CREDENTIALS 环境变量设置为该密钥的位置。
* 或者使用ADC (Application Default Credentials) 应用默认凭据。
```
# 将用户凭据提供给 ADC
  gcloud auth application-default login
```