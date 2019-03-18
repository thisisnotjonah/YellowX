package ren.jonah.clientv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, signup;
    private String url = "http://yellowx.ca/php/login.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginBtn);
        signup = (Button) findViewById(R.id.signUpBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = "email=" + email.getText().toString();
                String passwordStr = "password=" + password.getText().toString();
                String finalUrl = url + emailStr + "&" + passwordStr;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(Login.this, R.string.LOGIN_SUCCESS, Toast.LENGTH_LONG).show();
                            SavedSharedPreferences.setUserName(Login.this, email.getText().toString());
                            Methods.getID(Login.this, "http://yellowx.ca/php/get_user_id.php?email=" + email.getText().toString(), new Methods.VolleyCallBack() {
                                @Override
                                public void onSuccess(String response) {
                                    SavedSharedPreferences.setId(Login.this, response);
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                }
                            });
                        } else {
                            Toast.makeText(Login.this, R.string.LOGIN_FAILED, Toast.LENGTH_LONG).show();
                            password.setText("");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.TAG_SUCCESS));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SignUp.class));
            }
        });
    }
}

