package com.ccdp.appkebersihan5;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ccdp.appkebersihan5.api.DatasAPI;
import com.ccdp.appkebersihan5.model.Kebersihan;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddKebersihanActivity extends AppCompatActivity {

    private EditText txtKeterangan;
    private EditText txtUserid;
    private EditText txtTgllapor;
    private EditText txtLokasi;
    private ProgressDialog mProgressDialog;

    String token;
    private ImageView photo;
    File file;
    private Button btnSave;

    private Retrofit retrofit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kebersihan);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AddKebersihanActivity.this);
        token = settings.getString("com.ccdp.appkebersihan5.token","");
        txtKeterangan = (EditText) findViewById(R.id.keterangan);
        txtTgllapor = (EditText) findViewById(R.id.tgllapor);
        txtLokasi= (EditText) findViewById(R.id.lokasi);
        txtUserid= (EditText) findViewById(R.id.userid);
        btnSave = (Button) findViewById(R.id.btn_save);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final DatasAPI datasAPI = retrofit.create(DatasAPI.class);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                public void onClick(View v) {
                Call<Kebersihan> call = datasAPI.insert(txtKeterangan.getText().toString(), txtUserid.getText().toString(), txtTgllapor.getText().toString(), txtLokasi.getText().toString(), token);
                call.enqueue(new Callback<Kebersihan>() {
                    @Override
                    public void onResponse(Call<Kebersihan> call, Response<Kebersihan> response) {

                        if (response.isSuccessful()){

                            Toast.makeText(AddKebersihanActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddKebersihanActivity.this, MainActivity.class);
                            startActivity(intent);
                            onBackPressed();
                        } else {
                            Toast.makeText(AddKebersihanActivity.this,"Data gagal disimpan",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<Kebersihan> call, Throwable t) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                        Toast.makeText(AddKebersihanActivity.this, t.getMessage(),Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
            }
        });
    }
}