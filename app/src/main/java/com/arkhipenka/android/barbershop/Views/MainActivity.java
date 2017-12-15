package com.arkhipenka.android.barbershop.Views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arkhipenka.android.barbershop.App;
import com.arkhipenka.android.barbershop.Entities.Accessable;
import com.arkhipenka.android.barbershop.Entities.Hairdresser;
import com.arkhipenka.android.barbershop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null){
            fragment = new HairdressersFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer,fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main,menu);
        if (App.getAccessable()!=null&&App.getAccessable().getPermissions().equals(Accessable.TYPE_ADMIN)){
            menu.getItem(1).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.account:
                if (App.getAccessable()==null){
                    fm.beginTransaction()
                        .replace(R.id.fragmentContainer, new ValidationFragment())
                        .addToBackStack(null)
                        .commit();
                    break;
                }
                else {
                    switch (App.getAccessable().getPermissions()) {
                        case Accessable.TYPE_HAIRDRESSER:
                            fm.beginTransaction()
                                    .replace(R.id.fragmentContainer, HairdresserProfileFragment.newInstance(App.getAccessable().getId()))
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        default:
                            fm.beginTransaction()
                                    .replace(R.id.fragmentContainer, UsersEntriesFragment.newInstance(App.getAccessable().getId()))
                                    .addToBackStack(null)
                                    .commit();
                            break;
                    }
                }
                break;
            case R.id.admin_menu_item:
                fm.beginTransaction()
                        .replace(R.id.fragmentContainer, new AdminMenuFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
