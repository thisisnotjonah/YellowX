package ren.jonah.clientv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditAccount extends AppCompatActivity {

    private EditText firstNameTxt, lastNameTxt, emailTxt;
    private Button submit;
    private String email, firstName, lastName, id;
    private final String url_get_client = "http://yellowx.ca/php/get_client.php?email=";
    private final String url_update_client = "http://yellowx.ca/php/update_client.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstNameTxt = (EditText) findViewById(R.id.firstName);
        lastNameTxt = (EditText) findViewById(R.id.lastName);
        emailTxt = (EditText) findViewById(R.id.email);

        email = SavedSharedPreferences.getUserName(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_client + email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    JSONObject userData = result.getJSONObject(0);
                    firstName = userData.getString(getString(R.string.FIRST_NAME_KEY));
                    lastName = userData.getString(getString(R.string.LAST_NAME_KEY));
                    id = userData.getString(getString(R.string.ID_KEY));
                    firstNameTxt.setText(firstName);
                    lastNameTxt.setText(lastName);
                    emailTxt.setText(email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditAccount.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.TAG_SUCCESS));

        submit = (Button) findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url_update_client, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditAccount.this, response, Toast.LENGTH_LONG).show();
                        if (response.equals("User info successfully updated")) {
                            SavedSharedPreferences.setUserName(EditAccount.this, emailTxt.getText().toString());
                            startActivity(new Intent(EditAccount.this, AccountInfo.class));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditAccount.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(getString(R.string.FIRST_NAME_KEY), firstNameTxt.getText().toString());
                        params.put(getString(R.string.LAST_NAME_KEY), lastNameTxt.getText().toString());
                        params.put(getString(R.string.KEY_EMAIL), emailTxt.getText().toString());
                        params.put(getString(R.string.ID_KEY), id);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest1);
            }
        });
    }
}
