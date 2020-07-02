package com.netsdk.myDemo;

import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class Test {
    // 设备断线通知回调
    private static DisConnect disConnect = new DisConnect();
    // 网络连接恢复
    private static HaveReConnect haveReConnect = new HaveReConnect();
    // 订阅句柄
    public static NetSDKLib.LLong m_hAttachHandle = new NetSDKLib.LLong(0);
    // 人脸图
    private static BufferedImage personBufferedImage = null;

    // 用于人脸检测
    private static int groupId = 0;

    private static boolean isAttach = false;

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
        LoginModule.init(disConnect, haveReConnect);

        boolean result = LoginModule.login("192.168.1.10",
                Integer.parseInt("37777"), "admin",
                new String("admin888"));

        System.out.println(result);
        return result;
    }

    //登出
    public void logout(){
        LoginModule.stopRealLoadPicture(m_hAttachHandle);
        LoginModule.logout();
        LoginModule.cleanup();
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
         */
        public void saveFaceDetectPic(Pointer pBuffer, int dwBufSize,
                                      NetSDKLib.DEV_EVENT_FACEDETECT_INFO faceDetectInfo) throws Exception {
            File path = new File("./FaceDetection");
            if (!path.exists()) {
                path.mkdir();
            }

            if (pBuffer == null || dwBufSize <= 0) {
                return;
            }

            // 小图的 stuObject.nRelativeID 来匹配大图的 stuObject.nObjectID，来判断是不是 一起的图片
            if(groupId != faceDetectInfo.stuObject.nRelativeID) {   ///->保存全景图
                personBufferedImage = null;
                groupId = faceDetectInfo.stuObject.nObjectID;
            } else if(groupId == faceDetectInfo.stuObject.nRelativeID){   ///->保存人脸图
                if(faceDetectInfo.stuObject.stPicInfo != null) {
                    String strPersonPicPathName = path + "/" + faceDetectInfo.UTC.toStringTitle() + "_FaceDetection_Person.jpg";
                    byte[] bufferPerson = pBuffer.getByteArray(0, dwBufSize);
                    ByteArrayInputStream byteArrInputPerson = new ByteArrayInputStream(bufferPerson);

                    try {
                        personBufferedImage = ImageIO.read(byteArrInputPerson);
                        if(personBufferedImage != null) {
                            File personFile = new File(strPersonPicPathName);
                            if(personFile != null) {
                                ImageIO.write(personBufferedImage, "jpg", personFile);
                            }
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
