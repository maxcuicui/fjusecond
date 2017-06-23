package com.example.sf646_a04.fjushv1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sf646_a04.fjushv1.imgurmodel.UploadtoDB;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.example.sf646_a04.fjushv1.helpers.DocumentHelper;
import com.example.sf646_a04.fjushv1.helpers.IntentHelper;
import com.example.sf646_a04.fjushv1.imgurmodel.ImageResponse;
import com.example.sf646_a04.fjushv1.imgurmodel.Upload;
import com.example.sf646_a04.fjushv1.services.UploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.attr.data;

public class UploadActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.uploadimage)
    ImageView uploadImage;
   /* @Bind(R.id.uploadimage2)
    ImageView uploadImage2;
    @Bind(R.id.uploadimage3)
    ImageView uploadImage3; */
    @Bind(R.id.uploadtitle)
    EditText uploadTitle;
    @Bind(R.id.uploaddesc)
    EditText uploadDesc;
    @Bind(R.id.uploadprice)
    EditText uploadPrice;
    @Bind(R.id.upload)
    Button uploadbut;


    private Upload upload; // Upload object containging image and meta data
    private UploadtoDB uptodb = new UploadtoDB();
    private File chosenFile; //chosen file from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Spinner spinner = (Spinner)findViewById(R.id.catalog);
        String[] catalog = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new getDBdata().execute().get();
            JSONObject jo;
            catalog = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                jo = jsonArray.getJSONObject(i);
                catalog[i] = jo.getString("name");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> catalogList = new ArrayAdapter<>(UploadActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                catalog);
        spinner.setAdapter(catalogList);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri returnUri;

        if (requestCode != IntentHelper.FILE_PICK) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        returnUri = data.getData();
        String filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);

                /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */
        Picasso.with(getBaseContext())
                .load(chosenFile)
                .placeholder(R.drawable.ic_photo_library_black)
                .fit()
                .into(uploadImage);
    }

    @OnClick(R.id.uploadimage)
    public void onChooseImage() {
        uploadDesc.clearFocus();
        uploadTitle.clearFocus();
        uploadPrice.clearFocus();
        IntentHelper.chooseFileIntent(this);
    }
    private void clearInput() {
        uploadTitle.setText("");
        uploadDesc.clearFocus();
        uploadPrice.clearFocus();
        uploadDesc.setText("");
        uploadTitle.clearFocus();
        uploadPrice.clearFocus();
        uploadPrice.setText("");
        uploadDesc.clearFocus();
        uploadTitle.clearFocus();
        uploadImage.setImageResource(R.drawable.ic_photo_library_black);
    }

    @OnClick(R.id.upload)
    public void uploadImage() {
        if (chosenFile == null) return;
        createUpload(chosenFile);

        uptodb.price = Double.parseDouble(uploadPrice.getText().toString());
        uptodb.createTime = Calendar.getInstance().getTime();
        new UploadService(this).Execute(uptodb,upload, new UiCallback());
    }

    private void createUpload(File image) {
        upload = new Upload();
        upload.image = image;
        upload.title = uploadTitle.getText().toString();
        upload.description = uploadDesc.getText().toString();
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            clearInput();
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById(R.id.rootView), "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    class getDBdata extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                String result = DBconnector.executeQuery("SELECT * FROM category");
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    return jsonArray;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray result) {
        }
    }
}