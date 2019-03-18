package ren.jonah.clientv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_account:
                startActivity(new Intent(Menu.this, AccountInfo.class));
                return true;
            case R.id.action_log_out:
                SavedSharedPreferences.clear(Menu.this);
                startActivity(new Intent(Menu.this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
