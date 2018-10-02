package com.kohlsan.kohlsan;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivityAN extends AppCompatActivity {
    String url = "http://www.mocky.io/v2/5b927cec3300004c0020604f";
    private static final String TAG = "MainActivity";
    private List<Product> productList = new ArrayList<>();
    TextView tvTitle,tvSale,tvReg;
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    ImageView imageView;
    JSONArray jArrayFinal = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_an);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            jArrayFinal = new JSONArray(response.toString());
                            Product productPdt = getPdtDetails(jArrayFinal);
                            List<Product> productsDetailsLocal = getYouMayLikeDetails(jArrayFinal);
                            mAdapter = new ProductAdapter(MainActivityAN.this,productsDetailsLocal,productPdt);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
                try {
                    jArrayFinal = new JSONArray(JsonUtils.jsonPDT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Product productPdt = getPdtDetails(jArrayFinal);
                List<Product> productsDetailsLocal = getYouMayLikeDetails(jArrayFinal);
                mAdapter = new ProductAdapter(MainActivityAN.this,productsDetailsLocal,productPdt);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

// Adding request to request queue

       /* AppController.getInstance().addToRequestQueue(jsonObjReq);*/
    }



    private Product getPdtDetails(JSONArray jArray) {

        Product productPdt = new Product();
        try {
          //  jArray = new JSONArray(JsonUtils.jsonPDT);

            JSONObject Jobject1 = jArray.getJSONObject(0).getJSONObject("singlePdtDetail");
            String Imageurl = Jobject1.getJSONArray("visualNavTile").getJSONObject(0).getString("imageUrl");
            String productName = Jobject1.getString("pdt_name");
            String salePrice = Jobject1.getString("sale_price");
            String regPrice = Jobject1.getString("reg_price");
            productPdt.setSale_price(salePrice);
            productPdt.setPdt_name(productName);
            productPdt.setImageUrl(Imageurl);
            productPdt.setReg_price(regPrice);
            return productPdt;
        }
         catch (JSONException e) {
                e.printStackTrace();
            }
            return  productPdt;
    }
    private List<Product> getYouMayLikeDetails(JSONArray jArray) {



        try {
           // jArray = new JSONArray(JsonUtils.jsonDetails);

            JSONArray detailsArray = jArray.getJSONObject(0).getJSONArray("youMayLikeThisDetail");
            for(int i = 0 ;i<detailsArray.length();i++) {
                Product product = new Product();
                String imageurl = detailsArray.getJSONObject(i).getString("imageUrl");
                String productName = detailsArray.getJSONObject(i).getString("pdt_name");
                String salePrice = detailsArray.getJSONObject(i).getString("sale_price");
                product.setImageUrl(imageurl);
                product.setPdt_name(productName);
                product.setSale_price(salePrice);
                productList.add(product);
            }
            return productList;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}
