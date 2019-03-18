package ren.jonah.clientv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

public class ResponseInfo extends Menu {

    private String id, contractorId;
    private TextView description;
    private Button title;
    private final String url = "http://yellowx.ca/php/get_response.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_info);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra(getString(R.string.RESPONSE_ID));

        title = (Button) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    JSONObject responseData = jsonArray.getJSONObject(0);
                    contractorId = responseData.getString(getString(R.string.CONTRACTOR_ID_KEY));
                    String firstName = responseData.getString(getString(R.string.CONTRACTOR_FIRST_NAME_KEY));
                    String lastName = responseData.getString(getString(R.string.CONTRACTOR_LAST_NAME_KEY));
                    title.setText(firstName + " " + lastName);
                    description.setText(responseData.getString(getString(R.string.DESCRIPTION_KEY)));
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ResponseInfo.this, ContractorProfile.class);
                            intent.putExtra(getString(R.string.CONTRACTOR_ID_KEY), contractorId);
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(ResponseInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResponseInfo.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
