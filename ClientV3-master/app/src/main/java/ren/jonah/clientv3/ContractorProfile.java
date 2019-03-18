package ren.jonah.clientv3;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContractorProfile extends Menu {

    private String contractorId;
    private TextView contractorName, description;
    private final String url = "http://yellowx.ca/php/get_contractor.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contractorId = getIntent().getStringExtra(getString(R.string.CONTRACTOR_ID_KEY));
        contractorName = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + contractorId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.RESULT_TAG));
                    JSONObject contractorData = jsonArray.getJSONObject(0);
                    String firstName = contractorData.getString(getString(R.string.FIRST_NAME_KEY));
                    String lastName = contractorData.getString(getString(R.string.LAST_NAME_KEY));
                    contractorName.setText(firstName + " " + lastName);
                    description.setText(contractorData.getString(getString(R.string.DESCRIPTION_KEY)));
                } catch (JSONException e) {
                    Toast.makeText(ContractorProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContractorProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
