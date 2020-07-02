package com.netsdk.myDemo;

import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;

/**
 * Created by CarloJones on 2020/7/1.
 */
public class LoadPicture {
    public static NetSDKLib.LLong realLoadPicture(int channel, NetSDKLib.fAnalyzerDataCallBack callback) {
        int bNeedPicture = 1; // 是否需要图片

        NetSDKLib.LLong m_hAttachHandle =  LoginModule.netsdk.CLIENT_RealLoadPictureEx(LoginModule.m_hLoginHandle, channel,
                NetSDKLib.EVENT_IVS_ALL, bNeedPicture, callback, null, null);
        if(m_hAttachHandle.longValue() == 0) {
//            System.err.println("CLIENT_RealLoadPictureEx Failed, Error:" + ToolKits.getErrorCodePrint());
        } else {
            System.out.println("通道[" + channel + "]订阅成功！");
        }

        return m_hAttachHandle;
    }

    /**
     * 停止上传智能分析数据－图片
     */
    public static void stopRealLoadPic(NetSDKLib.LLong m_hAttachHandle) {
        if (0 != m_hAttachHandle.longValue()) {
            LoginModule.netsdk.CLIENT_StopLoadPic(m_hAttachHandle);
            m_hAttachHandle.setValue(0);
        }
    }
}
