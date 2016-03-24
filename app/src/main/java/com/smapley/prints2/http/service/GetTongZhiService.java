package com.smapley.prints2.http.service;


import com.smapley.prints2.http.callback.SimpleCallback;
import com.smapley.prints2.http.params.GetTongZhiParams;

import org.xutils.x;

/**
 * Created by smapley on 15/12/18.
 */
public abstract class GetTongZhiService {

    public void load(GetTongZhiParams params) {

        x.http().post(params,new SimpleCallback() {
            @Override
            public void Success(final String data) {
                Succ(data);
            }
        });
    }


    public abstract void Succ(String data);
}