package ren.jonah.clientv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NewRequest extends Menu {

    private Button submitBtn;
    private String userId, title, description, title_url, userId_url;
    private Spinner spinner;
    private final String url = "http://yellowx.ca/php/add_request.php";
    private final String url_get_request_id = "http://yellowx.ca/php/get_request_id.php?title=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = SavedSharedPreferences.getId(this);

        spinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            title = ((EditText) findViewById(R.id.title)).getText().toString();
            description = ((EditText) findViewById(R.id.description)).getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(NewRequest.this, response, Toast.LENGTH_LONG).show();
                    if (response.equals(getString(R.string.REQUEST_SUCCESS_STR))) {
                        try {
                            title_url = URLEncoder.encode(title, "utf-8");
                            userId_url = URLEncoder.encode(userId, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            Toast.makeText(NewRequest.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                        String final_url = url_get_request_id + title_url + "&userId=" + userId_url;
                        Methods.getID(NewRequest.this, final_url, new Methods.VolleyCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                Intent intent = new Intent(NewRequest.this, RequestInfo.class);
                                intent.putExtra(getString(R.string.REQUEST_ID), response);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NewRequest.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(getString(R.string.USER_ID_KEY), userId);
                    params.put(getString(R.string.TITLE_KEY), title);
                    params.put(getString(R.string.DESCRIPTION_KEY), description);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });
    }
}
