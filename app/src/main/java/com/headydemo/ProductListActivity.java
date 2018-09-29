package com.headydemo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headydemo.Adapters.Category_Product_Adapter;
import com.headydemo.Adapters.Products_Adapter;
import com.headydemo.Database.DatabaseHelper;
import com.headydemo.Interfaces.API_Presenter;
import com.headydemo.Models.Category;
import com.headydemo.Models.Product;
import com.headydemo.Models.ProductResponse;
import com.headydemo.Models.Ranking;
import com.headydemo.Webservices.GetWebserviceData;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {


    GetWebserviceData data;
    API_Presenter.GetWebserviceResponse.APIResponseListener finishedListener;
    ProductResponse response;
    DatabaseHelper helper;
    ArrayList<String> arrRankings=new ArrayList<>();

    ArrayList<Category> arrCategoryProducts=new ArrayList<>();
    Context mContext;
    RecyclerView recycl_cat_products;
    Category_Product_Adapter adapter;
    Spinner spRanking;
    LinearLayout lnLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mContext=this;

        Initialization(); // initialize control and database helper create.
        CallWebservices(); // get data from webservice and store into db.



    }

    private void Initialization(){
        recycl_cat_products=(RecyclerView)findViewById(R.id.recycl_cat_products);
        spRanking=(Spinner)findViewById(R.id.spRanking);
        lnLoader=(LinearLayout)findViewById(R.id.lnLoader);

        helper = new DatabaseHelper(ProductListActivity.this);
        data = new GetWebserviceData();
    }

    private void CallWebservices(){
        finishedListener = new API_Presenter.GetWebserviceResponse.APIResponseListener() {
            @Override
            public void onSuccess(String mObject) {
                Gson gson = new Gson();
                response = gson.fromJson(mObject.toString(), ProductResponse.class);

                if (response != null) {
                    helper.insertCategories(response.getCategories());
                    helper.insertRankings(response.getRankings());
                    arrRankings=helper.getRankings();
                    arrCategoryProducts=helper.getProductsByCategories();
                    if(arrCategoryProducts!=null){
                        SpinnerItemSelection();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure ", t.getMessage());
            }
        };

        if(!helper.checkDataAvailable()){
           if(MyApplication.isInternetAvailable(mContext)){
               data.getResponseObject(mContext,finishedListener);
           }else{
               lnLoader.setVisibility(View.GONE);
               ShowSnackView(recycl_cat_products,"No Internet connection Available! please try again later.");
           }

        }else{
            arrRankings=helper.getRankings();
            arrCategoryProducts=helper.getProductsByCategories();
            if(arrCategoryProducts!=null){
                SpinnerItemSelection();
            }
        }
    }

    private void setSpinnerData(){
        arrRankings.add("Catergory Wise Products");
        ArrayAdapter<String> spAdapter=new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item,
                arrRankings);

        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRanking.setAdapter(spAdapter);
        spRanking.setSelection(arrRankings.size()-1);
    }

    private void SetAdapter(){

        lnLoader.setVisibility(View.GONE);

        if(spRanking.getSelectedItemPosition()== (arrRankings.size()-1)){
            adapter=new Category_Product_Adapter(ProductListActivity.this,arrCategoryProducts);

            LinearLayoutManager llm = new LinearLayoutManager(ProductListActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recycl_cat_products.setLayoutManager(llm);
            recycl_cat_products.setItemAnimator(new DefaultItemAnimator());
            recycl_cat_products.setAdapter(adapter);
        }else{

            ArrayList<Product> arrProducts=helper.getProductsByRankings(spRanking.getSelectedItemPosition()+1);

            Products_Adapter adapter=new Products_Adapter(ProductListActivity.this,arrProducts,false);

            LinearLayoutManager llm = new LinearLayoutManager(ProductListActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recycl_cat_products.setLayoutManager(llm);
            recycl_cat_products.setItemAnimator(new DefaultItemAnimator());
            recycl_cat_products.setAdapter(adapter);

        }



    }

    private void SpinnerItemSelection(){

        setSpinnerData();

        spRanking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {



                SetAdapter();

                Log.e("position",""+spRanking.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void ShowSnackView(View view, String msg) {


        Snackbar snackbar = Snackbar.make(
                view,
                msg,
                Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        snackbar.setActionTextColor(mContext.getResources().getColor(R.color.colorWhite));

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPurple));
        snackbar.show();
    }



}
