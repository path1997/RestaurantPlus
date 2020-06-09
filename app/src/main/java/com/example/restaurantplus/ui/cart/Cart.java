package com.example.restaurantplus.ui.cart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.restaurantplus.CartDb;
import com.example.restaurantplus.MainActivity;
import com.example.restaurantplus.MyAppDatabase;
import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.SharedPrefManager;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.ui.product.ProductDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart extends AppCompatActivity {
    ArrayList<Integer> idz=new ArrayList<Integer>();
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> path=new ArrayList<String>();
    ArrayList<Double> price= new ArrayList<>();
    ArrayList<Integer> quantity=new ArrayList<Integer>();
    TextView tvZaloguj;
    Button btZamow;
    public static TextView wartosczamowienia;
    public static ListView listView;
    public static CartProductListCustomAdapter customadapter;
    public static double suma=0;
    int pusty;
    static void minussum(double value){
        suma-=value;
        wartosczamowienia.setText("Total value : "+suma+"zł");
    }
    static void plussum(double value){
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
        tvZaloguj=(TextView) findViewById(R.id.txZaloguj);
        btZamow=(Button) findViewById(R.id.btZamowienie);
        if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            btZamow.setVisibility(View.INVISIBLE);
            tvZaloguj.setVisibility(View.VISIBLE);
        }
        if(MainActivity.myAppDatabase.cartDao().getCount()==0) {
            btZamow.setVisibility(View.INVISIBLE);
            tvZaloguj.setVisibility(View.VISIBLE);
            tvZaloguj.setText("Cart is empty");
        }
        suma=0;
        List<CartDb> cartDbs=MainActivity.myAppDatabase.cartDao().getProducts();
        for(CartDb cartDb:cartDbs) {
            idz.add(cartDb.getProductid());
            name.add(cartDb.getName());
            path.add(cartDb.getPhotoUrl());
            price.add(cartDb.getCost());
            quantity.add(cartDb.getQuantity());
            suma+=cartDb.getTotalCost();
        }
        System.out.println("-----------------------PATH:"+path);

        listView=(ListView) findViewById(R.id.listviewcart);
        customadapter = new CartProductListCustomAdapter(Cart.this,idz,name,path,price,quantity,0 );
        listView.setAdapter(customadapter);

        wartosczamowienia.setText("Total value : "+suma+"zł");
        wartosczamowienia.setGravity(Gravity.CENTER);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                String idP= Long.toString(idz.get(position));
                intent.putExtra("id_product", idP);
                intent.putExtra("obiekt", "produkt");
                intent.putExtra("path", path.get(position));
                startActivity(intent);
            }
        });

        btZamow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrder(MainActivity.myAppDatabase.cartDao().getProducts());
            }
        });
    }

    void addOrder(final List<CartDb> koszyk){
        class Deliveryc extends AsyncTask<Void, Void, String> {
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
                        MainActivity.myAppDatabase.cartDao().deleteAllProduct();
                        recreate();


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
                int id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
                String ids = Integer.toString(id);
                int count=MainActivity.myAppDatabase.cartDao().productCount();
                String countString=Integer.toString(count);
                int totalcostOrder=MainActivity.myAppDatabase.cartDao().totalCost();
                HashMap<String, String> params = new HashMap<>();
                params.put("count",countString);
                params.put("user_id", ids);
                params.put("totalCostOrder",Integer.toString(totalcostOrder));
                int licznik=0;
                for (CartDb x:koszyk) {
                    params.put("name"+licznik,x.name);
                    params.put("photo"+licznik,x.photoUrl);
                    params.put("cost"+licznik,Double.toString(x.cost));
                    params.put("totalCost"+licznik,Double.toString(x.totalCost));
                    params.put("quantity"+licznik,Double.toString(x.quantity));
                    licznik++;
                }

                return requestHandler.sendPostRequest(URLs.URL_CONFIRMORDER, params);
            }
        }
        Deliveryc ul = new Deliveryc();
        ul.execute();
    }

}
