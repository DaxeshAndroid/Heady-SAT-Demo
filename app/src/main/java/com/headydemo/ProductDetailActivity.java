package com.headydemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.headydemo.Models.Category;
import com.headydemo.Models.Product;

import org.w3c.dom.Text;

public class ProductDetailActivity extends AppCompatActivity {

    Context mContext;
    TextView txtProduct,txtCategory,txtTaxName,txtTaxValue;
    LinearLayout lnVarients;

    Product product_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);
        mContext = this;

        Initialization(); // initialize controls and get data from previous activity
        SetData(); // set data of product
    }

    private void Initialization() {
        txtProduct=(TextView)findViewById(R.id.txtProduct);
        txtCategory=(TextView)findViewById(R.id.txtCategory);
        txtTaxName=(TextView)findViewById(R.id.txtTaxName);
        txtTaxValue=(TextView)findViewById(R.id.txtTaxValue);
        lnVarients=(LinearLayout)findViewById(R.id.lnVarients);

        if(getIntent().hasExtra("detail")){
                Gson gson = new Gson();
                String data = getIntent().getExtras().getString("detail", null);
                product_data = gson.fromJson(data, Product.class);
        }

    }

    private void SetData(){
        txtProduct.setText(product_data.getName());
        txtTaxName.setText(product_data.getTax().getName());
        txtTaxValue.setText(String.valueOf(product_data.getTax().getValue()));

        if(product_data.getVariants().size()>0)
            addDynamicvarient();

    }

    private void addDynamicvarient(){
        if(lnVarients.getChildCount()>0)
            lnVarients.removeAllViews();

        for(int i = 0; i < product_data.getVariants().size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.row_varient, null);

            TextView txtPrice=(TextView)child.findViewById(R.id.txtPrice);
            TextView txtColor=(TextView)child.findViewById(R.id.txtColor);
            TextView txtSize=(TextView)child.findViewById(R.id.txtSize);

            txtColor.setText(product_data.getVariants().get(i).getColor());
            txtPrice.setText(String.valueOf(product_data.getVariants().get(i).getPrice()));
            txtSize.setText(String.valueOf(product_data.getVariants().get(i).getSize()));

            lnVarients.addView(child);
        }


    }

}
