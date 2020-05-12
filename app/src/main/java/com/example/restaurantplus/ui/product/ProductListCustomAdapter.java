package com.example.restaurantplus.ui.product;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantplus.R;
import com.example.restaurantplus.DownLoadImageTask;

public class ProductListCustomAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] name;
    private String[] path;
    private String[] price;
    public ProductListCustomAdapter(Activity context, String[] name, String[] path, String[] price) {
        super(context, R.layout.listview_layout, name);
        this.context = context;
        // this.urls = urls;
        this.name = name;
        this.path = path;
        this.price=price;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview_layout, null, true);
        TextView nameTx = (TextView) listViewItem.findViewById(R.id.txname);
        TextView priceTx = (TextView) listViewItem.findViewById(R.id.txprice);
        nameTx.setText(name[position] );
        if(!price[0].equals("brak")) {
            priceTx.setText("Cena: " + price[position] + "zł");
        }
        if(!path[position].equals("brak")) {
            ImageView image = (ImageView) listViewItem.findViewById(R.id.imimage);
            new DownLoadImageTask(image).execute(path[position]);
        }
        return  listViewItem;
    }
}


