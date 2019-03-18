package ren.jonah.clientv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends Menu {

    private Button addRequestBtn;
    private ListView lv;
    private final String url = "http://yellowx.ca/php/get_user_requests.php?userId=";
    private final String url_get_request_id = "http://yellowx.ca/php/get_request_id.php?title=";
    private String userId, title_url, userId_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if(SavedSharedPreferences.getUserName(this).length() == 0) {
            startActivity(new Intent(this, Login.class));
        }

        userId = SavedSharedPreferences.getId(this);

        addRequestBtn = (Button) findViewById(R.id.addRequestBtn);
        addRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), NewRequest.class));
            }
        });

        lv = (ListView) findViewById(R.id.requests);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    RequestAdapter adapter = new RequestAdapter(MainActivity.this, jsonArray);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                title_url = URLEncoder.encode((String) lv.getAdapter().getItem(i), "utf-8");
                                userId_url = URLEncoder.encode(userId, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                            Methods.getID(view.getContext(), url_get_request_id + title_url + "&userId=" + userId_url, new Methods.VolleyCallBack() {
                                @Override
                                public void onSuccess(String response) {
                                    Intent intent = new Intent(MainActivity.this, RequestInfo.class);
                                    intent.putExtra(getString(R.string.REQUEST_ID), response);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
