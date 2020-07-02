package com.netsdk.myDemo;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class DisConnect implements NetSDKLib.fDisConnect{
    public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        // 断线提示
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    frame.setTitle(Res.string().getFaceRecognition() + " : " + Res.string().getDisConnectReconnecting());
//                }
//            });
    }
}
