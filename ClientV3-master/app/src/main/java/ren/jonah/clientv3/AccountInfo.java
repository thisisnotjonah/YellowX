package ren.jonah.clientv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountInfo extends Menu {

    final String url = "http://yellowx.ca/php/get_client.php?email=";
    TextView accountTxt, emailTxt, idTxt;
    Button editBtn;
    String email, firstName, lastName, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = SavedSharedPreferences.getUserName(this);
        accountTxt = (TextView) findViewById(R.id.account_label);
        emailTxt = (TextView) findViewById(R.id.email);
        idTxt = (TextView) findViewById(R.id.id);
        editBtn = (Button) findViewById(R.id.editBtn);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    JSONObject userData = result.getJSONObject(0);
                    id = userData.getString(getString(R.string.ID_KEY));
                    firstName = userData.getString(getString(R.string.FIRST_NAME_KEY));
                    lastName = userData.getString(getString(R.string.LAST_NAME_KEY));
                    accountTxt.setText(firstName + " " + lastName + "'s AccountInfo");
                    emailTxt.setText(email);
                    idTxt.setText(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountInfo.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.TAG_SUCCESS));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), EditAccount.class));
            }
        });
    }
}
