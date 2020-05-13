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


import com.example.restaurantplus.CartDb;
import com.example.restaurantplus.MainActivity;
import com.example.restaurantplus.MyAppDatabase;
import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.DownLoadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.example.restaurantplus.ui.cart.Cart.customadapter;
import static com.example.restaurantplus.ui.cart.Cart.listView;
import static com.example.restaurantplus.ui.cart.Cart.minussum;
import static com.example.restaurantplus.ui.cart.Cart.plussum;


public class CartProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    ArrayList<Integer> id;
    ArrayList<String> name;
    ArrayList<String> path;
    ArrayList<Double> price;
    ArrayList<Integer> quantity;
    int type;
    public CartProductListCustomAdapter(Activity context, ArrayList<Integer> id, ArrayList<String> name, ArrayList<String> path, ArrayList<Double> price, ArrayList<Integer> quantity, int type) {
        super(context, R.layout.cart_listview_layout, name);
        this.context = context;
        this.name = name;
        this.path = path;
        this.price=price;
        this.quantity=quantity;
        this.id=id;
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
                        removeItem(id.get(position), position);
                    } else {
                        minusItem(id.get(position),position);
                    }
                }
            });
            listViewItem.findViewById(R.id.btPlus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    plusItem(id.get(position),position);
                }
            });
            listViewItem.findViewById(R.id.btRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeItem(id.get(position), position);

                }
            });
            priceTx.setText("Price: "+price.get(position)+"zł");
            cos.setText(quantity.get(position).toString());
            sum.setText("Sum: "+quantity.get(position)*price.get(position)+"zł");
        }
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
        new DownLoadImageTask(image).execute(path.get(position));
        return  listViewItem;
    }
    private void minusItem(final int ids, final int position) {
        List<CartDb> cartDb=MainActivity.myAppDatabase.cartDao().getProductbyId(ids);
        for(CartDb cartDb1:cartDb){
            if(cartDb1.getQuantity()==1){
                removeItem(ids,position);
                return;
            } else {
                MainActivity.myAppDatabase.cartDao().changeQuantity(ids, (cartDb1.getQuantity() - 1), (cartDb1.getTotalCost() - cartDb1.getCost()));
            }
            break;
        }
        minussum(price.get(position));
        int tempprice=quantity.get(position);
        tempprice--;
        quantity.set( position, tempprice );
        customadapter.notifyDataSetChanged();

    }
    private void plusItem(final int ids, final int position) {
        List<CartDb> cartDb=MainActivity.myAppDatabase.cartDao().getProductbyId(ids);
        for(CartDb cartDb1:cartDb){
            MainActivity.myAppDatabase.cartDao().changeQuantity(ids,(cartDb1.getQuantity()+1),(cartDb1.getTotalCost()+cartDb1.getCost()));
            break;
        }
        plussum(price.get(position));
        int tempprice=quantity.get(position);
        tempprice++;
        quantity.set( position, tempprice );
        customadapter.notifyDataSetChanged();


    }
    private void removeItem(final int ids, final int position) {
        MainActivity.myAppDatabase.cartDao().deleteProductById(ids);
        minussum(quantity.get(position)*price.get(position));
        name.remove(position);
        path.remove(position);
        price.remove(position);
        quantity.remove(position);
        id.remove(position);
        listView.invalidateViews();
    }
}
