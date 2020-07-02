package com.netsdk.myDemo.file;

public class TestUpload {
    public static void main(String[] args) throws Exception{
        FileUpload fileUpload = new FileUpload();
        fileUpload.uploadFiles("bucket001","/pic/pic001.jpg","/home/df/workspace/General_NetSDK_ChnEng_JAVA_Linux64_IS_V3.052.0000001.0.R.200407/FaceDetection/Time_20200702_104420_FaceDetection_Person.jpg");
    }
}
