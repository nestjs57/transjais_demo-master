package com.transjai.transjai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import co.omise.android.models.Token;
import co.omise.android.ui.AuthorizingPaymentActivity;
import co.omise.android.ui.CreditCardActivity;

import static com.transjai.transjai.MainActivity.AUTHORIZING_PAYMENT_REQUEST_CODE;
import static com.transjai.transjai.R.id.editText5;
import static com.transjai.transjai.R.id.tel;

public class payment extends ActionBarActivity {
    private static final Object AUTHORIZED_URL = "";
    private Button button3;
    private Spinner spinner2;
    private Spinner spinner1;
    private int j = 0;
    private int ii = 0;
    //
    private String address_choose = "";
    private String lat_choose = "";
    private String lng_choose = "";

    private String lat_choose1 = "";
    private String lng_choose1 = "";
    private String lat_choose2 = "";
    private String lng_choose2 = "";

    private String name = "";
    private String km = "";
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

    private String name_user = "";
    private String Tel = "";
    private String note = "";
    private String rub = "";
    private String song = "";

    private String latcur1 = "";
    private String latcur2 = "";
    private String lngcur1 = "";
    private String lngcur2 = "";


    private static final String OMISE_PKEY = "pkey_test_591zrhl3ps3x4wmkfe2";
    private static final int REQUEST_CC = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreditCardForm();
            }
        });
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
        //Toolbar

        Spinner2();
        Spinner1();

        GETINTENT();

    }

    private void showCreditCardForm() {
//        Intent intent = new Intent(this, CreditCardActivity.class);
//        intent.putExtra(CreditCardActivity.EXTRA_PKEY, OMISE_PKEY);
//        startActivityForResult(intent, REQUEST_CC);

        //Intent intent1 = new Intent(this,check.class);
        Intent intent = new Intent(getApplication(), check.class);
//                intent.putExtra(CreditCardActivity.EXTRA_PKEY, OMISE_PKEY);
//                startActivityForResult(intent, REQUEST_CC);
        intent.putExtra("name", name);

        intent.putExtra("txt_s", txt_s);
        intent.putExtra("txt_m", txt_m);
        intent.putExtra("txt_l", txt_l);

        intent.putExtra("total_s", total_s);
        intent.putExtra("total_m", total_m);
        intent.putExtra("total_l", total_l);

        intent.putExtra("total_box", total_box);
        intent.putExtra("total", total);


        intent.putExtra("type", type);

        intent.putExtra("detail", detail);

        intent.putExtra("time", time);
        intent.putExtra("day", day);


        intent.putExtra("name_user",name_user);
        intent.putExtra("tel",Tel);
        intent.putExtra("note",note);

        intent.putExtra("rub",rub);
        intent.putExtra("song",song);

        intent.putExtra("latcur1",latcur1);
        intent.putExtra("latcur2",latcur2);
        intent.putExtra("lngcur1",lngcur1);
        intent.putExtra("lngcur2",lngcur2);

        intent.putExtra("lat_choose1",lat_choose1);
        intent.putExtra("lng_choose1",lng_choose1);
        intent.putExtra("lat_choose2",lat_choose2);
        intent.putExtra("lng_choose2",lng_choose2);
        intent.putExtra("km",km);




        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out); //ใหม่ , เก่า

        //startActivity(intent1);
        overridePendingTransition(R.anim.right_in, R.anim.left_out); //ใหม่ , เก่า

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CC:
                if (resultCode == CreditCardActivity.RESULT_CANCEL) {
                    return;
                }

                Token token = data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT);

                // process your token here.

            default:
                super.onActivityResult(requestCode, resultCode, data);
                Toast.makeText(payment.this, data.getStringExtra(CreditCardActivity.EXTRA_TOKEN), Toast.LENGTH_SHORT).show();
                Toast.makeText(payment.this, data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT) +"", Toast.LENGTH_SHORT).show();
                Toast.makeText(payment.this, data.getParcelableExtra(CreditCardActivity.EXTRA_CARD_OBJECT)+"", Toast.LENGTH_SHORT).show();



        }
    }


    private void Spinner1() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            ii = ii + 1;
            list.add(String.valueOf(ii));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(dataAdapter);

        }

    }

    private void Spinner2() {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String formattedDate = df.format(c.getTime());
        j = Integer.parseInt(formattedDate);
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < 11; i++) {
            j = j + 1;
            list.add(String.valueOf(j));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(dataAdapter);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        finish(); // close this activity and return to preview activity (if there is any)
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right); //ใหม่ , เก่า
        return super.onOptionsItemSelected(item);
    }
    private void GETINTENT() {
        Intent i = getIntent();
        name = i.getStringExtra("name");

        txt_s = i.getStringExtra("txt_s");
        txt_m = i.getStringExtra("txt_m");
        txt_l = i.getStringExtra("txt_l");

        total_s = i.getStringExtra("total_s");
        total_m = i.getStringExtra("total_m");
        total_l = i.getStringExtra("total_l");

        total_box = i.getStringExtra("total_box");
        total = i.getStringExtra("total");

        type = i.getStringExtra("type");
        detail = i.getStringExtra("detail");

        time = i.getStringExtra("time");
        day = i.getStringExtra("day");

        type = i.getStringExtra("type");

        name_user = i.getStringExtra("name_user");
        Tel = i.getStringExtra("tel");
        note = i.getStringExtra("note");
        rub = i.getStringExtra("rub");
        song = i.getStringExtra("song");

        latcur1 = i.getStringExtra("latcur1");
        latcur2 = i.getStringExtra("latcur2");
        lngcur1 = i.getStringExtra("lngcur1");
        lngcur2 = i.getStringExtra("lngcur2");


         lat_choose1 = i.getStringExtra("lat_choose1");;
         lng_choose1 = i.getStringExtra("lng_choose1");;
         lat_choose2 = i.getStringExtra("lat_choose2");;
         lng_choose2 = i.getStringExtra("lng_choose2");;
          km = i.getStringExtra("km");;






        Toast.makeText(payment.this, time, Toast.LENGTH_SHORT).show();
    }
}

