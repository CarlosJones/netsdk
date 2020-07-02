package com.netsdk.myDemo.CCD;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * Created by CarloJones on 2020/7/1.
 * 服务器的网络连接断线后，自动调用本类的invoke()接口
 */
public class DisConnect implements NetSDKLib.fDisConnect{
    public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        // 断线提示
    }
}
