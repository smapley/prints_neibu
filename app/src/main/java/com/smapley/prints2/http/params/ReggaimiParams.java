package com.smapley.prints2.http.params;


import com.smapley.prints2.util.MyData;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

/**
 * Created by smapley on 16/1/25.
 */
public class ReggaimiParams extends RequestParams {

    public ReggaimiParams(String user1, String ming,String oldmi,String newmi1,String newmi2,String murl) {
        super(MyData.getURL_reggaimi());
        addBodyParameter("user1", user1);
        addBodyParameter("ming", ming);
        addBodyParameter("oldmi", oldmi);
        addBodyParameter("newmi1", newmi1);
        addBodyParameter("newmi2", newmi2);
        addBodyParameter("murl", murl);
        try {
            LogUtil.d(toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
