package com.netsdk.myDemo.CCD;

import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class Test {
    // 设备断线通知回调
    private static DisConnect disConnect = new DisConnect();
    // 网络连接恢复
    private static HaveReConnect haveReConnect = HaveReConnect.getInstance();
    // 订阅句柄
    public static NetSDKLib.LLong m_hAttachHandle = new NetSDKLib.LLong(0);
    // 人脸图
    private static BufferedImage personBufferedImage = null;

    // 用于人脸检测
    private static int groupId = 0;

    private static boolean isAttach = false;

    /**
     * AnalyzerDataCB对象是抓取图像后的回调，需要一直存活
     * */
    public static void main(String[] args) {
        Test test = new Test();
        test.login();
        while(true){
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if("aaa".equals(input)){
                m_hAttachHandle = LoadPicture.realLoadPicture(0, AnalyzerDataCB.getInstance());
            }else if("bbb".equals(input)){
                LoadPicture.stopRealLoadPic(m_hAttachHandle);
            }
        }
    }

    //登录
    public boolean login(){
        InitAndLogin.init(disConnect, haveReConnect);

        boolean result = InitAndLogin.login("192.168.1.10",
                Integer.parseInt("37777"), "admin",
                new String("admin888"));
        if(result){
            System.out.println("登陆成功");
        }else{
            System.out.println("登陆失败");
        }

        return result;
    }

    //登出
    public void logout(){
        InitAndLogin.stopRealLoadPicture(m_hAttachHandle);
        InitAndLogin.logout();
        InitAndLogin.cleanup();
    }

    private static class AnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack{
        private AnalyzerDataCB() {}

        private static class AnalyzerDataCBHolder {
            private static final AnalyzerDataCB instance = new AnalyzerDataCB();
        }

        public static AnalyzerDataCB getInstance() {
            return AnalyzerDataCB.AnalyzerDataCBHolder.instance;
        }

        public int invoke(NetSDKLib.LLong lAnalyzerHandle, int dwAlarmType,
                          Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize,
                          Pointer dwUser, int nSequence, Pointer reserved)
        {
            if (lAnalyzerHandle.longValue() == 0 || pAlarmInfo == null) {
                return -1;
            }

            switch(dwAlarmType)
            {
                case NetSDKLib.EVENT_IVS_FACEDETECT:   ///< 人脸检测
                {
                    NetSDKLib.DEV_EVENT_FACEDETECT_INFO msg = new NetSDKLib.DEV_EVENT_FACEDETECT_INFO();

                    ToolKits.GetPointerData(pAlarmInfo, msg);

                    // 保存图片，获取图片缓存
                    try {
                        saveFaceDetectPic(pBuffer, dwBufSize, msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 释放内存
                    msg = null;
                    System.gc();

                    break;
                }
                default:
                    break;
            }

            return 0;
        }

        /**
         * 保存人脸检测事件图片
         * @param pBuffer 抓拍图片信息
         * @param dwBufSize 抓拍图片大小
         * @param faceDetectInfo 人脸检测事件信息
         *
         */
        public void saveFaceDetectPic(Pointer pBuffer, int dwBufSize,
                                      NetSDKLib.DEV_EVENT_FACEDETECT_INFO faceDetectInfo) throws Exception {
            String path = "设备编号";
            String bucketName = "orgcodegkey";    //医疗机构编码

            if (pBuffer == null || dwBufSize <= 0) {
                return;
            }

            // 小图的 stuObject.nRelativeID 来匹配大图的 stuObject.nObjectID，来判断是不是 一起的图片
            if(groupId != faceDetectInfo.stuObject.nRelativeID) {   ///->保存全景图
                groupId = faceDetectInfo.stuObject.nObjectID;
            } else if(groupId == faceDetectInfo.stuObject.nRelativeID){   ///->保存人脸图
                if(faceDetectInfo.stuObject.stPicInfo != null) {
                    String strPersonPicPathName = path + "/" + faceDetectInfo.UTC.toStringTitle() + "_Person.jpg";
                    byte[] bufferPerson = pBuffer.getByteArray(0, dwBufSize);
                    ByteArrayInputStream byteArrInputPerson = new ByteArrayInputStream(bufferPerson);

                    try {
                        FileUpload.uploadPicture(bucketName,strPersonPicPathName,byteArrInputPerson,dwBufSize,"application/octet-stream");
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
