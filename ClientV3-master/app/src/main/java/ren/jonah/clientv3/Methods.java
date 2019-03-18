package ren.jonah.clientv3;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Methods {

    private static final String url_get_request = "http://yellowx.ca/php/get_request.php?id=";

    public static void getID(final Context ctx, String url, final VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(ctx.getResources().getString(R.string.RESULT_TAG));
                    JSONObject userData = result.getJSONObject(0);
                    callBack.onSuccess(userData.getString(ctx.getResources().getString(R.string.ID_KEY)));
                } catch (JSONException e) {
                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public interface VolleyCallBack {
        void onSuccess(String response);
    }

    public static void getRequest(final Context ctx, String id, final VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_request + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
