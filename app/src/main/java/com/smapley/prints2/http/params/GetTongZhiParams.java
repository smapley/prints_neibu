package com.smapley.prints2.http.params;


import com.smapley.prints2.util.MyData;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

/**
 * Created by smapley on 16/1/25.
 */
public class GetTongZhiParams extends RequestParams {

    public GetTongZhiParams(String user1) {
        super(MyData.getURL_getTongzhi());
        addBodyParameter("user1", user1);
        try {
            LogUtil.d(toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
