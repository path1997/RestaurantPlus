package com.example.restaurantplus.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class RegisterFragment extends Fragment {

    EditText Fname,SName,PhoneNumber,Email,PostCode,City,Address,Password,Password1;
    TextView Regulations;
    View root;
    CheckBox checkBox;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_register, container, false);
        super.onCreate(savedInstanceState);
        Fname = (EditText) root.findViewById(R.id.FName);
        SName = (EditText) root.findViewById(R.id.SName);
        PhoneNumber = (EditText) root.findViewById(R.id.PhoneNumber);
        Email = (EditText) root.findViewById(R.id.Email);
        PostCode = (EditText) root.findViewById(R.id.PostCode);
        City = (EditText) root.findViewById(R.id.City);
        Address = (EditText) root.findViewById(R.id.Address);
        Password = (EditText) root.findViewById(R.id.Password);
        Password1 = (EditText) root.findViewById(R.id.Password1);
        checkBox= (CheckBox) root.findViewById(R.id.checkbox);
        Regulations=(TextView) root.findViewById(R.id.regulations);
        ((MainActivity) getActivity()).setActionBarTitle("Register");

        root.findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        root.findViewById(R.id.regulations).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adbuilder = new AlertDialog.Builder(getContext());
                adbuilder.setMessage("This is window from regulations")
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setTitle("Regulation");
                adbuilder.show();
            }
        });

        return root;

    }

    private void registerUser() {
        final String fname = Fname.getText().toString().trim();
        final String sname = SName.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final String password1 = Password1.getText().toString().trim();
        final String phone = PhoneNumber.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        final String postcode = PostCode.getText().toString().trim();
        final String city = City.getText().toString().trim();
        final String address = Address.getText().toString().trim();


        if (TextUtils.isEmpty(fname)) {
            Fname.setError("Please enter first name");
            Fname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sname)) {
            SName.setError("Please enter your second name");
            SName.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email");
            Email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            PhoneNumber.setError("Please enter your phone");
            PhoneNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Email.setError("Please enter your email");
            Email.requestFocus();
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
        if(password.length()<8){
            Password.setError("Password must be at least 8 characters long");
            Password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Password.setError("Enter a password");
            Password.requestFocus();
            return;
        }
        if (!password.equals(password1)) {
            Password1.setError("The password is not the same");
            Password1.requestFocus();
            return;
        }
        if(!checkBox.isChecked()){
            Regulations.setError("Please accept regulations");
            Regulations.requestFocus();
            return;
        }


        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("fname", fname);
                params.put("sname", sname);
                params.put("phone", phone);
                params.put("email", email);
                params.put("postcode", postcode);
                params.put("city", city);
                params.put("address", address);
                params.put("password", password);

                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

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
                        System.out.println(SharedPrefManager.getInstance(getActivity().getApplicationContext()).isLoggedIn());
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
                        Menu menu = navigationView.getMenu();
                        MenuItem nav_login = menu.findItem(R.id.nav_login);
                        MenuItem nav_myorders = menu.findItem(R.id.nav_my_orders);
                        nav_login.setTitle("My account");
                        View header = navigationView.getHeaderView(0);
                        TextView textView= (TextView) header.findViewById(R.id.textView);
                        textView.setText("Hi "+user.getFname()+" "+user.getSname());
                        nav_myorders.setVisible(true);

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

}
