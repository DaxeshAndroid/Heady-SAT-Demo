package com.headydemo.Interfaces;

import android.content.Context;

/**
 * Created by dakshesh.khatri on 27-09-2018.
 */

public interface API_Presenter {

     interface GetWebserviceResponse {
        interface APIResponseListener {
            void onSuccess(String mObject);
            void onFailure(Throwable t);
        }
        void getResponseObject(Context mycontext,APIResponseListener onFinishedListener);
    }

}
