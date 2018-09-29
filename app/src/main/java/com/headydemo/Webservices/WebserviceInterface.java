package com.headydemo.Webservices;


import com.headydemo.Models.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
public interface WebserviceInterface {

    String GETDATA = "json";
    @GET(GETDATA)
    Call<String> WSgetData();

}
