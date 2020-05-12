package com.example.restaurantplus.ui.cart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.SharedPrefManager;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.ui.product.ProductDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity {
    ArrayList<Integer> idz=new ArrayList<Integer>();
    ArrayList<String> cid=new ArrayList<String>();
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> path=new ArrayList<String>();
    ArrayList<Integer> price=new ArrayList<Integer>();
    ArrayList<Integer> quantity=new ArrayList<Integer>();
    public static TextView wartosczamowienia;
    public static ListView listView;
    public static CartProductListCustomAdapter customadapter;
    public static int suma=0;
    int pusty;
    static void minussum(int value){
        suma-=value;
        wartosczamowienia.setText("Total value : "+suma+"zł");
    }
    static void plussum(int value){
        suma+=value;
        wartosczamowienia.setText("Total value : "+suma+"zł");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pusty=1;
        setTitle("Cart");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        wartosczamowienia=(TextView) findViewById(R.id.Wartosc);
        getProducts();
        findViewById(R.id.btdelivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    Toast.makeText(getApplicationContext(), "You must be logged in", Toast.LENGTH_SHORT).show();
                } else if(pusty==1) {
                    Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    /*Intent intent = new Intent(getApplicationContext(), Delivery.class);
                    String suma1 = Integer.toString(suma);
                    intent.putExtra("suma", suma1);
                    startActivity(intent);
                    finish();*/
                }
            }
        });
    }
    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();
        suma=0;
        pusty=1;
    }
    public void getProducts() {
        pusty=1;

        class Cartc extends AsyncTask<Void, Void, String> {

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

                        JSONArray jsonArray = obj.getJSONArray("cart");

                        suma=0;
                        System.out.println("elo");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject cart = jsonArray.getJSONObject(i);
                            pusty=0;
                            System.out.println(cart.getString("price"));
                            idz.add(cart.getInt("id"));
                            cid.add(cart.getString("cid"));
                            name.add(cart.getString("name"));
                            path.add(cart.getString("path"));
                            price.add(cart.getInt("price"));
                            quantity.add(cart.getInt("quantity"));

                            suma+=price.get(i)*quantity.get(i);
                        }



                        listView=(ListView) findViewById(R.id.listviewcart);
                        customadapter = new CartProductListCustomAdapter(Cart.this,cid,idz,name,path,price,quantity,0 );
                        listView.setAdapter(customadapter);

                        wartosczamowienia.setText("Total value : "+suma+"zł");
                        wartosczamowienia.setGravity(Gravity.CENTER);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                                String idP= Long.toString(idz.get(position));
                                intent.putExtra("id_product", idP);
                                startActivity(intent);
                            }
                        });




                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();
                int id= SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids= Integer.toString(id);

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", ids);

                return requestHandler.sendPostRequest(URLs.URL_GETCART, params);
            }
        }

        Cartc ul = new Cartc();
        ul.execute();
    }
}
