package ren.jonah.clientv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseAdapter extends BaseAdapter{

    private ArrayList<MyResponse> myResponses = new ArrayList<>();
    private LayoutInflater inflater = null;

    public ResponseAdapter(Context context, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString(context.getResources().getString(R.string.ID_KEY));
                String responderId = jsonObject.getString(context.getString(R.string.RESPONDER_ID_KEY));
                String description = jsonObject.getString(context.getResources().getString(R.string.DESCRIPTION_KEY));
                String firstName = jsonObject.getString(context.getResources().getString(R.string.FIRST_NAME_KEY));
                String lastName = jsonObject.getString(context.getResources().getString(R.string.LAST_NAME_KEY));
                String email = jsonObject.getString(context.getResources().getString(R.string.KEY_EMAIL));
                myResponses.add(new MyResponse(id, responderId, firstName, lastName, email, description));
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myResponses.size();
    }

    @Override
    public MyResponse getItem(int position) {
        return myResponses.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null)
            v = inflater.inflate(R.layout.item_response, null);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(myResponses.get(i).getContractorName());
        return v;
    }
}
