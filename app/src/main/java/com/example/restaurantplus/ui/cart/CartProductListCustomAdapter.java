package com.example.restaurantplus.ui.cart;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.DownLoadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import static com.example.restaurantplus.ui.cart.Cart.customadapter;
import static com.example.restaurantplus.ui.cart.Cart.listView;
import static com.example.restaurantplus.ui.cart.Cart.minussum;
import static com.example.restaurantplus.ui.cart.Cart.plussum;


public class CartProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    ArrayList<Integer> id;
    ArrayList<String> cid;
    ArrayList<String> name;
    ArrayList<String> path;
    ArrayList<Integer> price;
    ArrayList<Integer> quantity;
    int type;
    public CartProductListCustomAdapter(Activity context, ArrayList<String> cid, ArrayList<Integer> id, ArrayList<String> name, ArrayList<String> path, ArrayList<Integer> price, ArrayList<Integer> quantity, int type) {
        super(context, R.layout.cart_listview_layout, name);
        this.context = context;
        this.name = name;
        this.path = path;
        this.price=price;
        this.quantity=quantity;
        this.id=id;
        this.cid=cid;
        this.type=type;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.cart_listview_layout, null, true);
        TextView nameTx = (TextView) listViewItem.findViewById(R.id.txname);
        TextView priceTx = (TextView) listViewItem.findViewById(R.id.txprice);
        TextView plus=(TextView) listViewItem.findViewById(R.id.btPlus);
        TextView minus=(TextView) listViewItem.findViewById(R.id.btMinus);
        TextView remove=(TextView) listViewItem.findViewById(R.id.btRemove);
        TextView cos=(TextView) listViewItem.findViewById(R.id.txquantity);
        TextView sum=(TextView) listViewItem.findViewById(R.id.txitemsum);
        nameTx.setText(name.get(position));
        final String ids= Integer.toString(id.get(position));
        if(type==1){
            plus.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
            priceTx.setText(quantity.get(position)+"x"+price.get(position)+"zł="+quantity.get(position)*price.get(position)+"zł");

        } else {


            listViewItem.findViewById(R.id.btMinus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quantity.get(position) == 1) {
                        removeItem(cid.get(position), position);
                    } else {
                        minusItem(cid.get(position),position);
                    }
                }
            });
            listViewItem.findViewById(R.id.btPlus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    plusItem(cid.get(position),position);
                }
            });
            listViewItem.findViewById(R.id.btRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeItem(cid.get(position), position);

                }
            });
            priceTx.setText("Price: "+price.get(position)+"zł");
            cos.setText(quantity.get(position).toString());
            sum.setText("Sum: "+quantity.get(position)*price.get(position)+"zł");
        }
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(URLs.URL_PPHOTO+path.get(position));
        return  listViewItem;
    }
    private void minusItem(final String ids, final int position) {
        class MinusItem extends AsyncTask<Void, Void, String> {
            String idp=ids;
            ProgressBar progressBar;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
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
                        minussum(price.get(position));
                        int tempprice=quantity.get(position);
                        tempprice--;
                        quantity.set( position, tempprice );
                        customadapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);


                return requestHandler.sendPostRequest(URLs.URL_MINUSITEM, params);
            }
        }

        MinusItem ul = new MinusItem();
        ul.execute();
    }
    private void plusItem(final String ids, final int position) {
        class PlusItem extends AsyncTask<Void, Void, String> {
            String idp=ids;
            ProgressBar progressBar;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
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
                        plussum(price.get(position));
                        int tempprice=quantity.get(position);
                        tempprice++;
                        quantity.set( position, tempprice );
                        customadapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);
                System.out.println(id.get(position).toString());
                params.put("idp",id.get(position).toString());


                return requestHandler.sendPostRequest(URLs.URL_PLUSITEM, params);
            }
        }

        PlusItem ul = new PlusItem();
        ul.execute();
    }
    private void removeItem(final String ids, final int position) {
        class RemoveItem extends AsyncTask<Void, Void, String> {
            String idp=ids;
            ProgressBar progressBar;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) context.findViewById(R.id.progressBar);
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
                        //context.recreate();
                        minussum(quantity.get(position)*price.get(position));
                        name.remove(position);
                        path.remove(position);
                        price.remove(position);
                        quantity.remove(position);
                        id.remove(position);
                        cid.remove(position);
                        listView.invalidateViews();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("id", idp);


                return requestHandler.sendPostRequest(URLs.URL_REMOVEITEM, params);
            }
        }

        RemoveItem ul = new RemoveItem();
        ul.execute();
    }
}
