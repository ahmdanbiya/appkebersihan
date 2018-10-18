package com.ccdp.appkebersihan5;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ccdp.appkebersihan5.adapter.KebersihanAdapter;
import com.ccdp.appkebersihan5.api.DatasAPI;
import com.ccdp.appkebersihan5.api.UserAPI;
import com.ccdp.appkebersihan5.model.Kebersihan;
import com.ccdp.appkebersihan5.model.KebersihanResult;
import com.ccdp.appkebersihan5.model.LoginResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Kebersihan> kebersihans;
    private static KebersihanAdapter adapter;
    Retrofit retrofit;
    DatasAPI datasAPI;
    UserAPI userAPI;
    private ProgressDialog mProgressDialog;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        token = settings.getString("com.ccdp.appkebersihan5.token","");

        listView = (ListView)findViewById(R.id.lvnotes);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        datasAPI = retrofit.create(DatasAPI.class);
        Call<KebersihanResult> call = datasAPI.all(token);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        call.enqueue(new Callback<KebersihanResult>() {
            @Override
            public void onResponse(Call<KebersihanResult> call, Response<KebersihanResult> response) {
                try {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    kebersihans.clear();
                    KebersihanResult results = response.body();
                    List<Kebersihan> mems = results.data;
                    for(Kebersihan m : mems){
                        kebersihans.add(m);
                    }
                    adapter.notifyDataSetChanged();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }catch (Exception e){
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KebersihanResult> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });

        if(kebersihans == null){
            kebersihans = new ArrayList<>();
        }

        adapter= new KebersihanAdapter(getApplicationContext(),R.layout.row_item,kebersihans);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Kebersihan kebersihan = kebersihans.get(position);
                Intent intent = new Intent(MainActivity.this,ViewKebersihanActivity.class);
                intent.putExtra("id",kebersihan.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this,AddKebersihanActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logout:
                logout();
                break;

            case R.id.action_exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Call<LoginResult> call = userAPI.logout(token);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                try {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    LoginResult result = response.body();
                    Log.d("LoginActivity", result.message);
                    if (result.success){
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove("com.ccdp.appkebersihan5.token");
                        editor.remove("com.ccdp.appkebersihan5.isLogin");
                        editor.remove("com.ccdp.appkebersihan5.username");
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });
    }
}
