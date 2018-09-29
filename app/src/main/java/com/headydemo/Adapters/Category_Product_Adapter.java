package com.headydemo.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.headydemo.Models.Category;
import com.headydemo.Models.Product;
import com.headydemo.ProductListActivity;
import com.headydemo.R;

import java.util.ArrayList;
import java.util.List;

public class Category_Product_Adapter extends RecyclerView.Adapter<Category_Product_Adapter.MyViewHolder> {

    private ArrayList<Category> arrCategoryProducts;
    private Activity activityContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCategory,txtNoProducts;
        public RecyclerView recycl_products;

        public MyViewHolder(View view) {
            super(view);
            txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            txtNoProducts=(TextView)view.findViewById(R.id.txtNoProducts);
            recycl_products=(RecyclerView)view.findViewById(R.id.recycl_products);
        }
    }


    public Category_Product_Adapter(Activity activity, ArrayList<Category> arr_data) {
        this.arrCategoryProducts =arr_data;
        this.activityContext=activity;
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_productcategory, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Category data= arrCategoryProducts.get(position);

        holder.txtCategory.setText(data.getName());

        Products_Adapter adapter=new Products_Adapter(activityContext,new ArrayList<Product>(data.getProducts()),true);

        LinearLayoutManager llm = new LinearLayoutManager(activityContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recycl_products.setLayoutManager(llm);
        holder.recycl_products.setItemAnimator(new DefaultItemAnimator());
        holder.recycl_products.setAdapter(adapter);

        if(data.getProducts().size()>0){
            holder.txtNoProducts.setVisibility(View.GONE);
            holder.recycl_products.setVisibility(View.VISIBLE);
        }else{
            holder.txtNoProducts.setVisibility(View.VISIBLE);
            holder.recycl_products.setVisibility(View.GONE);
        }



    }
 
    @Override
    public int getItemCount() {
        return arrCategoryProducts.size();
    }

}