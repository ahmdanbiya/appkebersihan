package com.ccdp.appkebersihan5;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ccdp.appkebersihan5.api.DatasAPI;
import com.ccdp.appkebersihan5.model.Kebersihan;
import com.ccdp.appkebersihan5.model.KebersihanResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewKebersihanActivity extends AppCompatActivity {
    private TextView txtId;
    private TextView txtKeterangan;
    private TextView txtUserid;
    private TextView txtTgllapor;
    private TextView txtLokasi;
    private ImageView photo;
    private Button btnDelete, btnEdit, btnUpload;
    File file;
    private int id;
    private Retrofit retrofit;
    ProgressDialog mProgressDialog;
    String token;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_kebersihan);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ViewKebersihanActivity.this);
        token = settings.getString("com.ccdp.appkebersihan5.token", "");


        txtId = (TextView) findViewById(R.id.id);
        txtKeterangan = (TextView) findViewById(R.id.keterangan);
        txtUserid = (TextView) findViewById(R.id.userid);
        txtTgllapor = (TextView) findViewById(R.id.tgllapor);
        txtLokasi = (TextView) findViewById(R.id.lokasi);
        photo = (ImageView) findViewById(R.id.photo);
        btnUpload = (Button) findViewById(R.id.btn_photo);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnEdit = (Button) findViewById(R.id.btn_edit);

        //ambil data dari server
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final DatasAPI datasAPI = retrofit.create(DatasAPI.class);
        Call<KebersihanResult> call = datasAPI.find(id, token);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        call.enqueue(new Callback<KebersihanResult>() {
            @Override
            public void onResponse(Call<KebersihanResult> call, Response<KebersihanResult> response) {
                try {
                    KebersihanResult results = response.body();
                    List<Kebersihan> mems = results.data;
                    Kebersihan kebersihan = mems.get(0);
                    Log.d("REQUEST = ", call.request().toString());
                    String strId = String.valueOf(kebersihan.getId());
                    txtId.setText(strId);
                    txtKeterangan.setText(kebersihan.getKeterangan());
                    txtUserid.setText(kebersihan.getUserid());
                    txtTgllapor.setText(kebersihan.getTgllapor());
                    txtLokasi.setText(kebersihan.getLokasi());
                    String photoUrl = Constants.BASE_ASSETS + kebersihan.getId() + ".jpg";
                    Glide.with(getApplicationContext())
                            .load(photoUrl)
                            .error(R.mipmap.upload)
                            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(photo);

                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(ViewKebersihanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<KebersihanResult> call, Throwable t) {
                Toast.makeText(ViewKebersihanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });

        //BtnEdit untuk pindah halaman.
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewKebersihanActivity.this, EditKebersihanActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewKebersihanActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatasAPI datasAPI = retrofit.create(DatasAPI.class);
                        Call<Kebersihan> call = datasAPI.delete(id, token);
                        Log.d("call request", call.request().toString());
                        call.enqueue(new Callback<Kebersihan>() {
                            @Override
                            public void onResponse(Call<Kebersihan> call, Response<Kebersihan> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(ViewKebersihanActivity.this, "Data has deleted", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(ViewKebersihanActivity.this, "Data deleted has failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Kebersihan> call, Throwable t) {
                                Toast.makeText(ViewKebersihanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, Constants.CAMERA_REQUEST_CODE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile(file);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(ViewKebersihanActivity.this);
    }

    private void uploadFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);

        //execute request
        Log.d("BEGIN UPLOAD", file.getAbsolutePath());
        DatasAPI datasAPI = retrofit.create(DatasAPI.class);
        Call<ResponseBody> call = datasAPI.upload(id, body, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                onBackPressed();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.CAMERA_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(bitmap);
            photo.setDrawingCacheEnabled(true);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //String path = Environment.getExternalStorageDirectory().toString();
            file = new File(getCacheDir(), id + ".jpg");
            //file = new File(getCacheDir(), id+ ".jpg"); <--getCacheDir untuk umum, baik ada atau tidak sdk.
            try {
                FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}