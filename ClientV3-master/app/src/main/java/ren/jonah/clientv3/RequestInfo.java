package ren.jonah.clientv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class RequestInfo extends AppCompatActivity {

    private TextView title, description;
    private ListView responses;
    private String id;
    private final String url = "http://yellowx.ca/php/get_request_responses.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra(getString(R.string.REQUEST_ID));
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
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
                    Toast.makeText(RequestInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        responses = (ListView) findViewById(R.id.responses);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    ResponseAdapter adapter = new ResponseAdapter(RequestInfo.this, jsonArray);
                    responses.setAdapter(adapter);
                    responses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MyResponse response = (MyResponse) responses.getAdapter().getItem(i);
                            Intent intent = new Intent(RequestInfo.this, ResponseInfo.class);
                            intent.putExtra(getString(R.string.RESPONSE_ID), response.getId());
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(RequestInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RequestInfo.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                String url_delete = "http://yellowx.ca/php/delete_request.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RequestInfo.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RequestInfo.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(getString(R.string.ID_KEY), id);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                startActivity(new Intent(RequestInfo.this, MainActivity.class));
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(RequestInfo.this, EditRequest.class);
                intent.putExtra(getString(R.string.REQUEST_ID), id);
                startActivity(intent);
                return true;
            case R.id.action_my_account:
                startActivity(new Intent(RequestInfo.this, AccountInfo.class));
                return true;
            case R.id.action_log_out:
                SavedSharedPreferences.clear(RequestInfo.this);
                startActivity(new Intent(RequestInfo.this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
