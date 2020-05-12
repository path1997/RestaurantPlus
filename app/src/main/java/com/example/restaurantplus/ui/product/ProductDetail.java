package com.example.restaurantplus.ui.product;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;


import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.SharedPrefManager;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.ui.cart.Cart;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductDetail extends AppCompatActivity {
    String id_product;
    ViewFlipper v_flipper;
    EditText ilosc;
    String rodzaj;
    WebView webView;
    TextView tvTitle,tvDesc,tvPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Bundle extras = getIntent().getExtras();
        id_product=extras.getString("id_product");
        rodzaj=extras.getString("obiekt");
        ilosc=(EditText) findViewById(R.id.Ilosc);
        getDetails();
        Button buy=(Button) findViewById(R.id.btBuy);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        //tvDesc= (TextView) findViewById(R.id.tvDesc);
        tvPrice= (TextView) findViewById(R.id.tvPrice);
        webView=(WebView) findViewById(R.id.web);
        webView.setBackgroundColor(Color.TRANSPARENT);
        if(rodzaj.equals("artykul")){
            buy.setVisibility(View.GONE);
            ilosc.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bt_cart) {
            Intent intent1 = new Intent(this, Cart.class);
            this.startActivity(intent1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private void getDetails() {
        class Productdetail extends AsyncTask<Void, Void, String> {
            SliderView sliderView;
            ProgressBar progressBar;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }
            private ArrayList<String> arrayList;
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    JSONObject obj = new JSONObject(s);


                    if (!obj.getBoolean("error")) {
                        JSONArray jsonArray = obj.getJSONArray("productdetail");
                        Button buy=(Button) findViewById(R.id.btBuy);
                        String[] name = new String[jsonArray.length()];
                        String[] description = new String[jsonArray.length()];
                        //String[] price = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject productdetail = jsonArray.getJSONObject(i);
                            name[i]= productdetail.getString("post_title");
                            description[i]= productdetail.getString("post_content");
                            //price[i]= productdetail.getString("price");

                        }
                        setTitle(name[0]);
                        //jsonArray = obj.getJSONArray("productphotos");

                        /*String[] path = new String[jsonArray.length()];
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            path[i]= category.getString("path");

                        }
                        System.out.println("WIELKOSC"+ jsonArray.length());

                        final SliderAdapter adapter = new SliderAdapter(getApplicationContext());
                        adapter.setCount(jsonArray.length());
                        adapter.setPath(path);

                        sliderView.setSliderAdapter(adapter);
                        sliderView.setAutoCycle(false);


                        TextView tvAvailable= (TextView) findViewById(R.id.tvAvailable);
                        TextView tvDescription= (TextView) findViewById(R.id.tvDesc);
                        TextView tvPrice= (TextView) findViewById(R.id.tvPrice);*/
                        tvTitle.setText(name[0]);
                        //tvAvailable.setText("Available: "+ String.valueOf(available[0])+" pieces");
                        String temp="<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head></head>\n" +
                                "<body>"+description[0]+"</body>\n" +
                                "</html>";
                        webView.loadData(temp, "text/html", "UTF-8");
                       /* tvDesc.setText(Html.fromHtml("<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "<head></head>\n" +
                                "<body>"+description[0]+"</body>\n" +
                                "</html>"), TextView.BufferType.SPANNABLE);*/

                        //tvPrice.setText("Price: "+price[0]+"zł");
                        /*if(available[0]>0){
                            buy.setEnabled(true);
                        }
                        findViewById(R.id.btBuy).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int ilosc1= Integer.parseInt(ilosc.getText().toString());
                                if(available[0]>=ilosc1) {
                                    addToCart();
                                } else {
                                    Toast.makeText(getApplicationContext(), "We have not this product anymore", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/


                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("productid", id_product);


                return requestHandler.sendPostRequest(URLs.URL_PRODUCTDETAIL, params);
            }
        }

        Productdetail ul = new Productdetail();
        ul.execute();
    }
    private void addToCart() {


        class Productdetail extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();
               String ilosc1=ilosc.getText().toString();
                int id= SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids= Integer.toString(id);
                System.out.println(ids);
                System.out.println(ilosc1);
                System.out.println(id_product);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("quantity",ilosc1);
                params.put("id_product", id_product);
                params.put("id_user",ids);

                return requestHandler.sendPostRequest(URLs.URL_ADDPRODUCT, params);
            }
        }

        Productdetail ul = new Productdetail();
        ul.execute();
    }
}
