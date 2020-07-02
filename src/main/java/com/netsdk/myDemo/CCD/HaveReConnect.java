package com.netsdk.myDemo.CCD;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * Created by CarloJones on 2020/7/1.
 * 服务器的网络重新连接后，自动调用本类的invoke()接口
 * 此时，服务依然保持登陆和订阅状态，摄像头将会继续工作
 */
public class HaveReConnect implements NetSDKLib.fHaveReConnect{

    private HaveReConnect() {}

    private static class HaveReConnectHolder {
        private static final HaveReConnect instance = new HaveReConnect();
    }

    public static HaveReConnect getInstance() {
        return HaveReConnect.HaveReConnectHolder.instance;
    }

    public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
        // 重连提示
    }
}
