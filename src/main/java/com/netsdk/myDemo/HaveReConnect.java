package com.netsdk.myDemo;

import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class HaveReConnect implements NetSDKLib.fHaveReConnect{
    public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);

        // 重连提示
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    frame.setTitle(Res.string().getFaceRecognition() + " : " + Res.string().getOnline());
//                }
//            });
    }
}
