package com.transjai.transjai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class check extends ActionBarActivity {

    private String btnRub;
    private String btnSong;

    private String name_user;
    private String tel;
    private String note;


    private String name = "";

    private String txt_s = "";
    private String txt_m = "";
    private String txt_l = "";

    private String total_s = "";
    private String total_m = "";
    private String total_l = "";

    private String total_box = "";
    private String total = "";

    private String type = "";
    private String detail = "";

    private String time = "";
    private String day = "";

    private String latcur1 = "";
    private String latcur2 = "";
    private String lngcur1 = "";
    private String lngcur2 = "";

    private String lat_choose1 = "";
    private String lng_choose1 = "";
    private String lat_choose2 = "";
    private String lng_choose2 = "";

    //////////////////

    private TextView totals;
    private TextView totalm;
    private TextView totall;
    private TextView address_name;
    private TextView address_tel;
    private TextView address_rub;
    private TextView address_song;
    private Button timeButton;
    private Button payment;
    private Button btnfinish;
    private TextView totalText;

    //Storage firebase

    private StorageReference mStorage;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String km = "";
    private String income = "";
    //progressDialog

    private ProgressDialog mProgressDialog;

    private static final String AUTH_KEY = "key=AAAAZbCNh2I:APA91bGQklqYv9eRg6McEB-yN0Eblo-_pTyQTRE1wBFjQHMSANfrPrAW81Kea-i3LbrXgYFtp4EiDBvS7vk22Rk81tB3yniwYfeEQRrQrNkC-9iKl_jpqTX-06DZRFlI7isDKfhyx4h7";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        //Storage firebase
        mStorage = FirebaseStorage.getInstance().getReference();

        //ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
        //Toolbar

        bidwidjet();

        btnfinish = (Button) findViewById(R.id.btnfinish);
        INTENT();
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //ProgressDialog
                mProgressDialog.setMessage("รอสักครู่กำลังทำรายการ ...");
                mProgressDialog.show();


                final StorageReference filepath = mStorage.child("photo").child(auth.getCurrentUser().getUid().toString()).child((Uri.parse(type)).getLastPathSegment());
                filepath.putFile(Uri.parse(type)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference();
                        final DatabaseReference x = ref.child("order").push();

                        String path = filepath.getPath();

                        x.child("driveName").setValue("null");
                        x.child("user").setValue(auth.getCurrentUser().getUid().toString());
                        x.child("driver").setValue("null");
                        x.child("status").setValue("find_driver");
                        x.child("finish").setValue("false");
                        x.child("Name").setValue(name);
                        x.child("NumOfSize_s").setValue(txt_s);
                        x.child("NumOfSize_m").setValue(txt_m);
                        x.child("NumOfSize_l").setValue(txt_l);
                        x.child("Path_img").setValue(path);
                        x.child("Detail").setValue(detail);
                        x.child("Time").setValue(time);
                        x.child("Day").setValue(day);
                        x.child("Name_User").setValue(name_user);
                        x.child("Tel").setValue(tel);
                        x.child("Note").setValue(note);
                        x.child("Location_From").setValue(btnRub);
                        x.child("Location_To").setValue(btnSong);
                        x.child("latCur_start").setValue(latcur1);
                        x.child("lngCur_start").setValue(lngcur1);
                        x.child("latCur_end").setValue(latcur2);
                        x.child("lngCur_end").setValue(lngcur2);

                        x.child("lat_choose_start").setValue(lat_choose1);
                        x.child("lng_choose_start").setValue(lng_choose1);
                        x.child("lat_choose_end").setValue(lat_choose2);
                        x.child("lng_choose_end").setValue(lng_choose2);
                        x.child("km").setValue(km);
                        x.child("income").setValue(total);

                        mProgressDialog.dismiss();
                        Toast.makeText(check.this, "จองเสร็จเรียบร้อย", Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        finishAffinity();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        sendWithOtherThread("token");
                    }
                });


            }
        });

        SETTEXT();

    }

    private void SETTEXT() {
        totals.setText("Amount : " + txt_s);
        totalm.setText("Amount : " + txt_m);
        totall.setText("Amount : " + txt_l);

        address_name.setText(name_user);
        address_tel.setText(tel);
        address_rub.setText(btnRub);
        address_song.setText(btnSong);

        timeButton.setText(day + " เวลา " + time);
        payment.setText("เครดิตการ์ด      *0865");

        totalText.setText(total);
    }

    private void bidwidjet() {

        totals = (TextView) findViewById(R.id.totles);
        totalm = (TextView) findViewById(R.id.totalm);
        totall = (TextView) findViewById(R.id.totall);

        address_name = (TextView) findViewById(R.id.address_name);
        address_tel = (TextView) findViewById(R.id.address_tel);
        address_rub = (TextView) findViewById(R.id.address_rub);
        address_song = (TextView) findViewById(R.id.address_song);

        timeButton = (Button) findViewById(R.id.time);
        payment = (Button) findViewById(R.id.payment);

        totalText = (TextView) findViewById(R.id.total);


    }

    private void INTENT() {

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        txt_s = intent.getStringExtra("txt_s");
        txt_m = intent.getStringExtra("txt_m");
        txt_l = intent.getStringExtra("txt_l");

        total_s = intent.getStringExtra("total_s");
        total_m = intent.getStringExtra("total_m");
        total_l = intent.getStringExtra("total_l");

        total_box = intent.getStringExtra("total_box");
        total = intent.getStringExtra("total");

        type = intent.getStringExtra("type");
        detail = intent.getStringExtra("detail");

        time = intent.getStringExtra("time");
        day = intent.getStringExtra("day");

        type = intent.getStringExtra("type");

        name_user = intent.getStringExtra("name_user");
        tel = intent.getStringExtra("tel");
        note = intent.getStringExtra("note");

        btnRub = intent.getStringExtra("rub");
        btnSong = intent.getStringExtra("song");

        latcur1 = intent.getStringExtra("latcur1");
        latcur2 = intent.getStringExtra("lngcur1");
        lngcur1 = intent.getStringExtra("lngcur1");
        lngcur2 = intent.getStringExtra("lngcur2");


        lat_choose1 = intent.getStringExtra("lat_choose1");
        lng_choose1 = intent.getStringExtra("lng_choose1");
        lat_choose2 = intent.getStringExtra("lat_choose2");
        lng_choose2 = intent.getStringExtra("lng_choose2");
        km = intent.getStringExtra("km");


        Toast.makeText(check.this, btnSong, Toast.LENGTH_SHORT).show();

    }


    // notification

    public void showToken(View view) {
        Log.i("token", FirebaseInstanceId.getInstance().getToken());
    }
    public void sendToken(View view) {
        sendWithOtherThread("token");
    }
    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(type);
            }
        }).start();
    }
    private void pushNotification(String type) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "มีงานใหม่ใกล้คุณเมื่อสักครู่ ...");
            jNotification.put("body", "คุณมีเวลา 60 วินาทีในการตัดสินใจรับงานนี้");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "ThirdActivity");
            jNotification.put("icon", "ic_notification");

            jData.put("picture", "http://opsbug.com/static/google-io.jpg");

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("eermwB1wrSA:APA91bHZMyqgsINscRmKfFDf2o7EmqxdH-Vrebwq3y7YsbzP_Xpq_W0Q0LveVhYu26ejtyVTARMXkjyuoZ-SwjwkE5Xgm9Ly69sSg1fYcailmtCw0IXygVxxVP4HH8gvrRT2y4gfGJx4");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/news");
                    break;
                case "condition":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    break;
                default:
                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken());
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    //mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
