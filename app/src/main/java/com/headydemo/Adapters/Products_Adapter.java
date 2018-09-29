package com.headydemo.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.headydemo.Models.Category;
import com.headydemo.Models.Product;
import com.headydemo.ProductDetailActivity;
import com.headydemo.R;

import java.util.ArrayList;

public class Products_Adapter extends RecyclerView.Adapter<Products_Adapter.MyViewHolder> {

    private ArrayList<Product> arrCategoryProducts;
    private Activity activityContext;
    boolean subProduct=false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductname;
        public LinearLayout lnMain;


        public MyViewHolder(View view) {
            super(view);
            txtProductname = (TextView) view.findViewById(R.id.txtProductname);
            lnMain=(LinearLayout)view.findViewById(R.id.lnMain);
        }
    }


    public Products_Adapter(Activity activity, ArrayList<Product> arr_data,boolean isSub) {
        this.arrCategoryProducts =arr_data;
        this.activityContext=activity;
        this.subProduct=isSub;
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        if(subProduct){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_products, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_products_ranking, parent, false);
        }

 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Product data= arrCategoryProducts.get(position);

        holder.txtProductname.setText(data.getName());

        holder.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String productdetail = gson.toJson(data);
                Intent product_detail=new Intent(activityContext, ProductDetailActivity.class);
                product_detail.putExtra("detail",productdetail);
                activityContext.startActivity(product_detail);

            }
        });


    }
 
    @Override
    public int getItemCount() {
        return arrCategoryProducts.size();
    }

}