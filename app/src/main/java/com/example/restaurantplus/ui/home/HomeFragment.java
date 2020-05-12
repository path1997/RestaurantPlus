package com.example.restaurantplus.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.ui.product.ProductDetail;
import com.example.restaurantplus.ui.product.ProductListCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    View root=null;
    ActionBar actionBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        getHome();

        return root;
    }

    public void getHome(){

        class HomeList extends AsyncTask<Void, Void, String> {
            private String ida[];
            private ImageView imageView;
            private ArrayList<String> arrayList;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*actionBar.hide();
                imageView=(ImageView) view.findViewById(R.id.imagelogo);
                imageView.setVisibility(View.VISIBLE);*/
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    JSONObject obj = new JSONObject(s);
                    System.out.println("------------------ELO");
                    if(!obj.getBoolean("error")){
                        JSONArray jsonArray = obj.getJSONArray("articles");

                        ida=new String[jsonArray.length()];
                        String[] name = new String[jsonArray.length()];
                        String[] path = new String[jsonArray.length()];
                        String[] price = new String[1];
                        price[0]="brak";
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject producthome = jsonArray.getJSONObject(i);
                            ida[i]= producthome.getString("ID");
                            name[i]= producthome.getString("post_title");
                            path[i]= "brak";
                        }
                        for(int n=0;n<jsonArray.length();n++){
                            JSONArray jsonArray1 = obj.getJSONArray("photo"+n);
                            for(int m=0;m<jsonArray1.length();m++) {
                                JSONObject x = jsonArray1.getJSONObject(m);
                                String temp= x.getString("guid");
                                String idm= x.getString("post_parent");
                                for(int p=0;p<jsonArray.length();p++){
                                    if(ida[p].equals(idm)){
                                        path[p]="";
                                        for(int c=0;c<temp.length();c++){
                                            if(temp.charAt(c)!='\\')  {
                                                path[p]+=temp.charAt(c);
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        ProductListCustomAdapter customadapter1;
                        final ListView listView=(ListView) getActivity().findViewById(R.id.lv_home);
                        customadapter1 = new ProductListCustomAdapter(getActivity(),name,path,price);
                        listView.setAdapter(customadapter1);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), ProductDetail.class);
                                String idP=ida[position];
                                intent.putExtra("id_product", idP);
                                intent.putExtra("obiekt","artykul");
                                startActivity(intent);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                System.out.println("zapytalo");
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PRODUCTLISTFORHOME);
            }
        }
        HomeList hl = new HomeList();
        hl.execute();
    }
}
