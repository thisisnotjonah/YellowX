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

public class EditRequest extends Menu {

    private EditText title, description;
    private Button submitBtn;
    private String id;
    private final String url = "http://yellowx.ca/php/update_request.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        id = extras.getString(getString(R.string.REQUEST_ID));
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        Methods.getRequest(this, id, new Methods.VolleyCallBack() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    JSONObject userData = jsonArray.getJSONObject(0);
                    title.setText(userData.getString(getString(R.string.TITLE_KEY)));
                    description.setText(userData.getString(getString(R.string.DESCRIPTION_KEY)));
                } catch (JSONException e) {
                    Toast.makeText(EditRequest.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditRequest.this, response, Toast.LENGTH_LONG).show();
                        if (response.equals("Request info successfully updated")) {
                            Intent intent = new Intent(EditRequest.this, RequestInfo.class);
                            intent.putExtra(getString(R.string.REQUEST_ID), id);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditRequest.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(getString(R.string.TITLE_KEY), title.getText().toString());
                        params.put(getString(R.string.DESCRIPTION_KEY), description.getText().toString());
                        params.put(getString(R.string.ID_KEY), id);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });
    }
}
