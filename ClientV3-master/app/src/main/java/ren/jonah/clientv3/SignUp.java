package ren.jonah.clientv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText firstName, lastName, email, password;
    private final String url_add_client = "http://yellowx.ca/php/add_client.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        Button submit = (Button) findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_add_client, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                        SavedSharedPreferences.setUserName(SignUp.this, email.getText().toString());
                        Methods.getID(SignUp.this, "http://yellowx.ca/php/get_user_id.php?email=" + email.getText().toString(), new Methods.VolleyCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                SavedSharedPreferences.setId(SignUp.this, response);
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(getString(R.string.FIRST_NAME_KEY), firstName.getText().toString());
                        params.put(getString(R.string.LAST_NAME_KEY), lastName.getText().toString());
                        params.put(getString(R.string.KEY_EMAIL), email.getText().toString());
                        params.put(getString(R.string.PASSWORD_KEY), password.getText().toString());
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest, "success");
            }
        });
    }
}
