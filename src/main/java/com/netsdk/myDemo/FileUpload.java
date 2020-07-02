package com.netsdk.test;

import io.minio.MinioClient;
import io.minio.errors.MinioException;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class FileUpload {
    public void uploadFiles(String bucketName,String fileName,String filePath) throws Exception{
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
//            MinioClient minioClient = new MinioClient("http://10.21.1.100:9000", "minioadmin", "minioadmin");
            MinioClient minioClient = new MinioClient("http://120.195.40.209:5035", "minioadmin", "minioadmin");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }

            // 使用putObject上传一个文件到存储桶中。
//            minioClient.putObject("asiatrip","asiaphotos.zip", "/home/user/Photos/asiaphotos.zip");
            minioClient.putObject(bucketName,fileName, filePath);
            System.out.println("/home/user/Photos/asiaphotos.zip is successfully uploaded as asiaphotos.zip to `asiatrip` bucket.");
        } catch(MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }


}
