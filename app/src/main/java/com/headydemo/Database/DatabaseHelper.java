package com.headydemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.headydemo.Models.Category;
import com.headydemo.Models.Product;
import com.headydemo.Models.Ranking;
import com.headydemo.Models.RankingProducts;
import com.headydemo.Models.Tax;
import com.headydemo.Models.Variant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DATABASE_NAME = "headydemo";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_RANKING = "ranking";


    private static final String CATEGORY_NAME="name";

    private static  final String PRODUCT_NAME="name";
    private static final String CAT_ID="cat_id";
    private static final String _ID="id";
    private static final String VARIENTS="variants";
    private static final String TAX="tax";

    private static final String PRODUCTS="products";

    private SQLiteDatabase db;

    private OpenHelper openHelper;


	public DatabaseHelper(Context context) {

		openHelper = new OpenHelper(context);
		this.db = openHelper.getWritableDatabase();


	}

	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_CATEGORIES
                    + "("+_ID+" INTEGER PRIMARY KEY, "
                    + CATEGORY_NAME + " TEXT) ");

            db.execSQL("CREATE TABLE " + TABLE_PRODUCT
                    + "("+_ID+" INTEGER , "
                    + CAT_ID + " INTEGER, "
                    + PRODUCT_NAME + " TEXT, "
                    + VARIENTS + " TEXT, "
                    + TAX + " TEXT )");

            db.execSQL("CREATE TABLE " + TABLE_RANKING
                    + "("+_ID+" INTEGER PRIMARY KEY, "
                    + CATEGORY_NAME + " TEXT, "
                    + PRODUCTS + " TEXT )");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

	}

    public void insertCategories(List<Category> category_data) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            for(int i=0;i<category_data.size();i++){
                Category data = category_data.get(i);
                values.put(_ID, data.getId());
                values.put(CATEGORY_NAME, data.getName());
                db.insert(TABLE_CATEGORIES, null, values); // insertcategory and it's products

                insertCategoriesProducts(data.getId(),category_data.get(i).getProducts());
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            Log.e("Insert category",e.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }

    public void insertCategoriesProducts(Integer cat_id,List<Product> product_data) {

        try {
            ContentValues values = new ContentValues();
            Gson gson = new Gson();

            for (Product data : product_data) {
                values.put(_ID, data.getId());
                values.put(CAT_ID,cat_id);
                values.put(PRODUCT_NAME, data.getName());
                values.put(VARIENTS, gson.toJson(
                        data.getVariants(),
                        new TypeToken<ArrayList<Variant>>() {}.getType()));
                values.put(TAX, ""+gson.toJson(data.getTax()));
                db.insert(TABLE_PRODUCT, null, values);
            }

        }
        catch (Exception e){
            Log.e("Insert Product",e.getMessage());

        } finally{

        }
    }

    public void insertRankings(List<Ranking> ranking_data) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            Gson gson = new Gson();
            for(int i=0;i<ranking_data.size();i++){
                Ranking data = ranking_data.get(i);
                values.put(_ID, ""+(i+1));
                values.put(CATEGORY_NAME, data.getRanking());
                values.put(PRODUCTS, gson.toJson(
                        data.getProducts(),
                        new TypeToken<ArrayList<RankingProducts>>() {}.getType()));
                db.insert(TABLE_RANKING, null, values); // insertcategory and it's products
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            Log.e("Insert category",e.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }

	
	public boolean checkDataAvailable() {
        Cursor cursor = this.db.query(TABLE_CATEGORIES, new String[] {_ID,CATEGORY_NAME }, null,
                        null, null, null, null);
        if (cursor.getCount() > 0) {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return true;
        } else {
        	if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
            return false;
        }
    }


    public ArrayList<String> getRankings(){

	    ArrayList<String> arrRanking=new ArrayList<>();
        Cursor cursor = db.rawQuery("select "+CATEGORY_NAME+" from " + TABLE_RANKING, null);
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {

                do{
                    arrRanking.add(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)));

                }while(cursor.moveToNext());


            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return  arrRanking;

    }

    public ArrayList<Category> getProductsByCategories(){

        ArrayList<Category> arrCategories=new ArrayList<>();

        Cursor cursor = db.rawQuery("select "+_ID+", " +CATEGORY_NAME+" from " + TABLE_CATEGORIES, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do{

                    Category modeldata=new Category();
                    modeldata.setProducts(getProductsByCatID(cursor.getInt(cursor.getColumnIndex(_ID))));
                    modeldata.setName(cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)));
                    arrCategories.add(modeldata);
                }while(cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return arrCategories;

    }

    private ArrayList<Product> getProductsByCatID(int id){

        ArrayList<Product> arrProducts=new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_PRODUCT +" Where "+CAT_ID+"="+id, null);
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do{

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Variant>>(){}.getType();

                    Product product_data=new Product();
                    product_data.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                    product_data.setName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                    List<Variant> varients = gson.fromJson(cursor.getString(cursor.getColumnIndex(VARIENTS)), type);
                    product_data.setVariants(varients);
                    Tax taxdata = gson.fromJson(cursor.getString(cursor.getColumnIndex(TAX)), Tax.class);
                    product_data.setTax(taxdata);

                    arrProducts.add(product_data);


                }while(cursor.moveToNext());

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return arrProducts;

    }

    public  ArrayList<Product> getProductsByRankings(int id){

        ArrayList<Product> arrProducts=new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_RANKING +" Where "+_ID+"="+id, null);
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do{

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<RankingProducts>>(){}.getType();

                    List<RankingProducts> rankingProducts = gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCTS)), type);

                    for (RankingProducts products : rankingProducts)
                    {
                        arrProducts.add(getProductsByID(products.getId()));
                    }

                }while(cursor.moveToNext());

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return arrProducts;

    }


    private Product getProductsByID(int id){

        Product product_data=new Product();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PRODUCT +" Where "+_ID+"="+id, null);
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do{
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Variant>>(){}.getType();


                    product_data.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                    product_data.setName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                    List<Variant> varients = gson.fromJson(cursor.getString(cursor.getColumnIndex(VARIENTS)), type);
                    product_data.setVariants(varients);
                    Tax taxdata = gson.fromJson(cursor.getString(cursor.getColumnIndex(TAX)), Tax.class);
                    product_data.setTax(taxdata);

                }while(cursor.moveToNext());

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return product_data;

    }

}
