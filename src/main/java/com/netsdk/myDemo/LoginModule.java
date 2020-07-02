package com.netsdk.myDemo;

import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class LoginModule {
    public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
    private static boolean bInit   = false;
    // 设备信息
    public static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
    // 登陆句柄
    public static NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);

    public static boolean init(NetSDKLib.fDisConnect disConnect, NetSDKLib.fHaveReConnect haveReConnect) {
        bInit = netsdk.CLIENT_Init(disConnect, null);
        if (!bInit) {
            System.out.println("Initialize SDK failed");
            return false;
        }
        return bInit;
    }

    public static boolean login(String m_strIp, int m_nPort, String m_strUser, String m_strPassword) {
        //IntByReference nError = new IntByReference(0);
        //入参
        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam=new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstInParam.nPort=m_nPort;
        pstInParam.szIP=m_strIp.getBytes();
        pstInParam.szPassword=m_strPassword.getBytes();
        pstInParam.szUserName=m_strUser.getBytes();
        //出参
        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam=new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstOutParam.stuDeviceInfo=m_stDeviceInfo;
        //m_hLoginHandle = netsdk.CLIENT_LoginEx2(m_strIp, m_nPort, m_strUser, m_strPassword, 0, null, m_stDeviceInfo, nError);
        m_hLoginHandle=netsdk.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
        if(m_hLoginHandle.longValue() == 0) {
//            System.err.printf("LoginModule Device[%s] Port[%d]Failed. %s\n", m_strIp, m_nPort, ToolKits.getErrorCodePrint());
        } else {
            System.out.println("LoginModule Success [ " + m_strIp + " ]");
        }

        return m_hLoginHandle.longValue() == 0? false:true;
    }

    public static void stopRealLoadPicture(NetSDKLib.LLong m_hAttachHandle) {
        if(m_hAttachHandle.longValue() != 0) {
            netsdk.CLIENT_StopLoadPic(m_hAttachHandle);
            m_hAttachHandle.setValue(0);
        }
    }

    /**
     * \if ENGLISH_LANG
     * Logout Device
     * \else
     * 登出设备
     * \endif
     */
    public static boolean logout() {
        if(m_hLoginHandle.longValue() == 0) {
            return false;
        }

        boolean bRet = netsdk.CLIENT_Logout(m_hLoginHandle);
        if(bRet) {
            m_hLoginHandle.setValue(0);
        }

        return bRet;
    }

    /**
     * \if ENGLISH_LANG
     * CleanUp
     * \else
     * 清除环境
     * \endif
     */
    public static void cleanup() {
        if(bInit) {
            netsdk.CLIENT_Cleanup();
        }
    }


}
