package com.ccdp.appkebersihan5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccdp.appkebersihan5.api.DatasAPI;
import com.ccdp.appkebersihan5.model.Kebersihan;
import com.ccdp.appkebersihan5.model.KebersihanResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditKebersihanActivity extends AppCompatActivity {

    private EditText txtKeterangan;
    private EditText txtUserid;
    private EditText txtTgllapor;
    private EditText txtLokasi;
    private Button btnSave;

    private Retrofit retrofit;
    private int id;
    String token;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kebersihan);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(EditKebersihanActivity.this);
        token = settings.getString("com.ccdp.appkebersihan5.token", "");
        txtKeterangan = (EditText) findViewById(R.id.keterangan);
        txtTgllapor = (EditText) findViewById(R.id.tgllapor);
        txtUserid = (EditText) findViewById(R.id.userid);
        txtLokasi = (EditText) findViewById(R.id.lokasi);
        btnSave = (Button) findViewById(R.id.btn_save);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final DatasAPI datasAPI = retrofit.create(DatasAPI.class);
        Call<KebersihanResult> call = datasAPI.find(id, token);
        call.enqueue(new Callback<KebersihanResult>() {
            @Override
            public void onResponse(Call<KebersihanResult> call, Response<KebersihanResult> response) {
                try {
                    KebersihanResult results = response.body();
                    List<Kebersihan> mems = results.data;
                    Kebersihan kebersihan = mems.get(0);
                    Log.d("REQUEST = ", call.request().toString());
                    String strId = String.valueOf(kebersihan.getId());
                    txtKeterangan.setText(kebersihan.getKeterangan());
                    txtLokasi.setText(kebersihan.getLokasi());
                } catch (Exception e) {
                    Toast.makeText(EditKebersihanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<KebersihanResult> call, Throwable t) {
                Toast.makeText(EditKebersihanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Kebersihan> call = datasAPI.update(txtKeterangan.getText().toString(), txtUserid.getText().toString(), txtTgllapor.getText().toString(), txtLokasi.getText().toString(), id, token);
                call.enqueue(new Callback<Kebersihan>() {
                    @Override
                    public void onResponse(Call<Kebersihan> call, Response<Kebersihan> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditKebersihanActivity.this, "data berhasil diubah", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditKebersihanActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditKebersihanActivity.this, "Data gagal diubah", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<Kebersihan> call, Throwable t) {
                        Toast.makeText(EditKebersihanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

