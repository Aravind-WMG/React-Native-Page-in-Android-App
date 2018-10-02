package com.kohlsan.kohlsan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;
    private List<Product> productList;
    List<String> moreList = new ArrayList<String>();

    ArrayAdapter<String> adapter;
private Product productpdt;
Activity context;
String url = "http://www.mocky.io/v2/5b927dc33300006600206052";


    public ProductAdapter(Activity context , List<Product> productList, Product productpdt) {
        this.productList = productList;
        this.productpdt= productpdt;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            //Inflating header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new HeaderViewHolder(itemView);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvTitle.setText(productpdt.getPdt_name());
            headerHolder.tvSale.setText(productpdt.getSale_price());
            headerHolder.tvReg.setText(productpdt.getReg_price());
            Picasso.get().load(productpdt.getImageUrl()).placeholder(R.drawable.place_holder).error(R.drawable.error).into(headerHolder.imageView);
          /*  ReactFragment reactFragment = new ReactFragment.Builder("TestProjNew").build();
            context.getSupportFragmentManager().beginTransaction()
           .add(headerHolder.fragmentContainer, reactFragment)
           .commit();*/
       headerHolder.buttonSubmit.setOnClickListener(new View.OnClickListener() {
  @Override
    public void onClick(View view) {
        if(!headerHolder.edittextValue.getText().toString().isEmpty())
        {
            Toast.makeText(context,"Submitted Succesfully", LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Value should not be empty", LENGTH_SHORT).show();

        }
    }
});
            headerHolder.buttonGetData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getDataFromService(headerHolder);
                }
            });



        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Product product = productList.get(position);
            itemViewHolder.title.setText(product.getPdt_name());
            itemViewHolder.sale.setText(product.getSale_price());
            Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.place_holder).error(R.drawable.error).into(itemViewHolder.imageView);


        }
    }

    private void getMoredetails(HeaderViewHolder headerHolder,String jsonMore) {
        try {
            JSONArray jArray;
            jArray = new JSONArray(jsonMore);

            JSONObject Jobject1 = jArray.getJSONObject(0);
            JSONArray arraytiles = Jobject1.getJSONObject("visualNavTiles").getJSONArray("visualNavTile");

            for(int i = 0 ;i<arraytiles.length();i++)
            {
                String navLabel =  arraytiles.getJSONObject(i).getString("navLabel");
                moreList.add(navLabel);
                TextView textView = new TextView(context);
                textView.setTextSize(16);
                textView.setTextColor(Color.BLACK);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setText(navLabel);
                headerHolder.root.addView(textView);

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvSale,tvReg;
        ImageView imageView;
/*
        FrameLayout fragmentContainer;
*/
        Button buttonSubmit ,buttonGetData;
        //Button buttonGetData;
        EditText edittextValue;
        ListView listView;
        LinearLayout root;
        public HeaderViewHolder(View view) {
            super(view);
            tvTitle = (TextView)view.findViewById(R.id.textViewTitle);
            tvSale = (TextView)view.findViewById(R.id.textViewSale);
            tvReg =  (TextView)view.findViewById(R.id.textViewReg);
            imageView = (ImageView)view.findViewById(R.id.imageView);
          edittextValue = (EditText)view.findViewById(R.id.editText);
           buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
            buttonGetData = (Button) view.findViewById(R.id.buttonMore);
            //listView =(ListView)view.findViewById(R.id.listView);
            root = (LinearLayout)view.findViewById(R.id.rootView);
            //fragmentContainer = (FrameLayout)view.findViewById(R.id.container_main);
        }
    }
    private class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title, sale;
        ImageView imageView;

        public ItemViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTitle);
            sale = (TextView) view.findViewById(R.id.tvSaleValue);
            imageView = (ImageView)view.findViewById(R.id.imageViewPdt);
        }
    }
    private void getDataFromService(final HeaderViewHolder headerHolder)
    {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        getMoredetails(headerHolder,response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getMoredetails(headerHolder,JsonUtils.jsonMore);
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}
