package com.netsdk.test;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class TestUpload {
    public static void main(String[] args) throws Exception{
        FileUpload fileUpload = new FileUpload();
        fileUpload.uploadFiles("bucket001","pic001","D:\\三医项目文档\\摄像头\\General_NetSDK_ChnEng_JAVA_Win64_IS_V3.052.0000001.0.R.200407\\FaceDetection\\sea.jpg");
    }
}
