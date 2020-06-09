package com.example.restaurantplus.ui.login;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.restaurantplus.MainActivity;
import com.example.restaurantplus.R;
import com.example.restaurantplus.RequestHandler;
import com.example.restaurantplus.SharedPrefManager;
import com.example.restaurantplus.URLs;
import com.example.restaurantplus.User;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {
    View root;
    EditText Fname,SName,PhoneNumber,Email,PostCode,City,Address,Password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(SharedPrefManager.isLoggedIn()){
            root = inflater.inflate(R.layout.fragment_myaccount, container, false);
            Fname = (EditText) root.findViewById(R.id.FName);
            SName = (EditText) root.findViewById(R.id.SName);
            PhoneNumber = (EditText) root.findViewById(R.id.PhoneNumber);
            Email = (EditText) root.findViewById(R.id.Email);
            PostCode = (EditText) root.findViewById(R.id.PostCode);
            City = (EditText) root.findViewById(R.id.City);
            Address = (EditText) root.findViewById(R.id.Address);
            ((MainActivity) getActivity()).setActionBarTitle("My account");
            loaddata();



            root.findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changedata();
                }
            });

            root.findViewById(R.id.buttonChangePassword).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new ChangePasswordFragment()).addToBackStack(null);
                    fr.commit();
                }
            });

            root.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPrefManager.getInstance(getActivity().getApplicationContext()).logout();
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
                    Menu menu = navigationView.getMenu();
                    MenuItem nav_login = menu.findItem(R.id.nav_login);
                    nav_login.setTitle("Login");
                    View header = navigationView.getHeaderView(0);
                    TextView textView= (TextView) header.findViewById(R.id.textView);
                    textView.setText("You are not logged in");
                }
            });
        } else {

            root = inflater.inflate(R.layout.fragment_login, container, false);

            Email = (EditText) root.findViewById(R.id.Email);
            Password = (EditText) root.findViewById(R.id.Password);


            root.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userLogin();
                }
            });

            //if user presses on not registered
            root.findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new RegisterFragment()).addToBackStack(null);
                    fr.commit();
                }
            });
        }
        return root;

    }
    private void userLogin() {
        final String email = Email.getText().toString();
        final String password = Password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Email.setError("Please enter your email");
            Email.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email");
            Email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Password.setError("Please enter your password");
            Password.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        // Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("fname"),
                                userJson.getString("sname"),
                                obj.getString("email"),
                                userJson.getString("phoneNumber"),
                                userJson.getString("postCode"),
                                userJson.getString("city"),
                                userJson.getString("address")
                        );

                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);

                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
                        Menu menu = navigationView.getMenu();
                        MenuItem nav_login = menu.findItem(R.id.nav_login);
                        nav_login.setTitle("My account");
                        View header = navigationView.getHeaderView(0);
                        TextView textView= (TextView) header.findViewById(R.id.textView);
                        textView.setText("Hi "+user.getFname()+" "+user.getSname());

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    private void changedata() {
        final String fname = Fname.getText().toString();
        final String sname = SName.getText().toString();
        final String phone = PhoneNumber.getText().toString();
        final String postcode = PostCode.getText().toString();
        final String city = City.getText().toString();
        final String address = Address.getText().toString();

        if (TextUtils.isEmpty(fname)) {
            Fname.setError("Please enter your first name");
            Fname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sname)) {
            SName.setError("Please enter your second name");
            SName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            PhoneNumber.setError("Please enter your phone");
            PhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(postcode)) {
            PostCode.setError("Please enter your postcode");
            PostCode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(city)) {
            City.setError("Please enter your city");
            City.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Address.setError("Please enter your address");
            Address.requestFocus();
            return;
        }


        class UserLogin extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        User user = new User(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId(),fname,sname,SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getEmail(),phone,postcode,city,address);
                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                int id=SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getId();
                String ids=Integer.toString(id);
                HashMap<String, String> params = new HashMap<>();
                params.put("id",ids);
                params.put("fname", fname);
                params.put("sname", sname);
                params.put("phone", phone);
                params.put("postcode", postcode);
                params.put("city", city);
                params.put("address", address);

                return requestHandler.sendPostRequest(URLs.URL_CHANGEDATA, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    private void loaddata() {
        final String fname = Fname.getText().toString();
        final String sname = SName.getText().toString();
        final String phone = PhoneNumber.getText().toString();
        final String postcode = PostCode.getText().toString();
        final String city = City.getText().toString();
        final String address = Address.getText().toString();

        Fname.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getFname());
        SName.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getSname());
        PhoneNumber.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getPhone());
        PostCode.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getPostcode());
        City.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getCity());
        Address.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser().getAddress());
    }

}
