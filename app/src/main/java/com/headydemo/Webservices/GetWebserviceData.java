package com.headydemo.Webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.headydemo.Interfaces.API_Presenter;
import com.headydemo.Interfaces.ProgressbarInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dakshesh.khatri on 27-09-2018.
 */

public class GetWebserviceData implements API_Presenter.GetWebserviceResponse,ProgressbarInterface {

    public  ProgressDialog progress;
    public Context mContext;
    @Override
    public void getResponseObject(Context mycontext,final APIResponseListener onFinishedListener) {
        mContext=mycontext;

        WebserviceInterface client=RetrofitClient.getClient().create(WebserviceInterface.class);

        final Call<String> DetailsCall = client.WSgetData();
        showProgress();
        DetailsCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideProgress();
                Log.e("Response",response.body());
                onFinishedListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideProgress();
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void showProgress() {
//        progress= new ProgressDialog(mContext);
//        progress.setMessage("loading");
//        progress.show();

    }

    @Override
    public void hideProgress() {
//        progress.hide();
    }
}
