package com.example.restaurantplus.ui.menu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.ui.product.ProductList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_menu, container, false);
        categoryList();
        return root;
    }
    private void categoryList() {
        System.out.println("jestem");

        class CategoryList extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }
            ArrayList<String> arrayList;
            ArrayList<String> idc;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = obj.getJSONArray("category");

                        final ListView listView=(ListView) getActivity().findViewById(R.id.rl);

                        arrayList = new ArrayList<String>();
                        idc = new ArrayList<String>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject category = jsonArray.getJSONObject(i);
                            arrayList.add(category.getString("name"));
                            idc.add(category.getString("term_id"));

                        }
                        ArrayAdapter<String> aAdaptor = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.row, arrayList ) {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                TextView textView = (TextView) super.getView(position, convertView, parent);
                                return textView;
                            }
                        };
                        listView.setAdapter( aAdaptor );
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), ProductList.class);
                                String item = arrayList.get(position);
                                intent.putExtra("name_cat", item);
                                intent.putExtra("id", idc.get(position));
                                startActivity(intent);
                            }
                        });

                        System.out.println("udalo sie");

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                System.out.println("zapytalo");
                return requestHandler.sendPostRequest(URLs.URL_CATEGORY);
            }

        }
        CategoryList ca = new CategoryList();
        ca.execute();
    }
}
