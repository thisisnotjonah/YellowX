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

/**
 * Created by Jonah on 2017-04-29.
 */

public class RequestAdapter extends BaseAdapter {

    private ArrayList<String> titles = new ArrayList<>();
    private LayoutInflater inflater = null;

    public RequestAdapter(Context context, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                titles.add(jsonObject.getString(context.getResources().getString(R.string.TITLE_KEY)));
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public String getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null)
            v = inflater.inflate(R.layout.item_request, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(titles.get(i));
        return v;
    }
}
