package com.transjai.transjai;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.omise.android.models.Token;
import co.omise.android.ui.CreditCardActivity;

import static android.R.attr.data;
import static android.R.attr.y;
import static com.transjai.transjai.R.id.bfecha;
import static com.transjai.transjai.R.id.bhora;
import static com.transjai.transjai.R.id.btn_rub;
import static com.transjai.transjai.R.id.btn_song;
import static com.transjai.transjai.R.id.note;
import static com.transjai.transjai.R.id.tel;
import static com.transjai.transjai.R.id.total;
import static com.transjai.transjai.R.id.total_box;
import static com.transjai.transjai.R.id.total_l;
import static com.transjai.transjai.R.id.total_m;
import static com.transjai.transjai.R.id.total_s;
import static com.transjai.transjai.R.id.txt_l;
import static com.transjai.transjai.R.id.txt_m;
import static com.transjai.transjai.R.id.txt_s;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.sin;

public class order2 extends ActionBarActivity implements OnMapReadyCallback {

    private Button btnRub;
    private Button btnSong;

    private EditText name_user;
    private EditText tel;
    private EditText note;

    private String y = "";
    private Button gotopayment;
    //CM
    private static final int REQUEST_LOCATION = 1;
    private String mCurrentLocStr;
    private TextView mLocTextView;
    private GoogleMap mMapView;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 1000;
    private List<LatLng> listOfLatLng = new ArrayList<>();
    private LocationRequest mRequest;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private GoogleApiClient mApiClient;
    private Polygon polygon;
    private MarkerOptions mapMarker;
    int currentPt;
    int mAnimationZoom = 15;

    int mPinDrawables[] = new int[]{R.drawable.pin_01,
            R.drawable.pin_02,
            R.drawable.pin_03,
            R.drawable.pin_04,
            R.drawable.pin_05,
            R.drawable.pin_06,
            R.drawable.pin_07};
    private int pinCount = 0;
    private ImageView mClearMarkersBtn;
    private ImageView mAnimationBtn;
    private ImageView mGeoCodingBtn;
    private Circle circle;
    private ValueAnimator vAnimator;
    private LatLng defaultLocation;
    //end CM
    //request = 1
    private String lat_cur1 = "";
    private String lng_cur1 = "";

    private String lat_cur2 = "";
    private String lng_cur2 = "";
    double latitude1 = 0;
    double longitude1 = 0;
    double latitude2 = 0;
    double longitude2 = 0;
    LatLng start, end;
    private String km = "";
    private String income = "";


    private String locality_cur = "";
    private String admin_cur = "";
    private String postalcode_cur = "";
    private String subadmins_cur = "";
    private String Featurename_cur = "";

    //request = 2
    private String address_choose = "";
    private String lat_choose1 = "";
    private String lng_choose1 = "";
    private String lat_choose2 = "";
    private String lng_choose2 = "";
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

    private static final String OMISE_PKEY = "pkey_test_591zrhl3ps3x4wmkfe2";
    private static final int REQUEST_CC = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
        //Toolbar


        GETINTENT();


        btnRub = (Button) findViewById(btn_rub);
        btnSong = (Button) findViewById(R.id.btn_song);
        gotopayment = (Button) findViewById(R.id.gotopayment);
        name_user = (EditText) findViewById(R.id.name);
        tel = (EditText) findViewById(R.id.tel);
        note = (EditText) findViewById(R.id.note);


        gotopayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), payment.class);
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
                intent.putExtra("latcur1", lat_cur1);
                intent.putExtra("lngcur1", lng_cur1);
                intent.putExtra("lat_choose1", lat_choose1);
                intent.putExtra("lng_choose1", lng_choose1);


                intent.putExtra("latcur2", lat_cur2);
                intent.putExtra("lngcur2", lng_cur2);
                intent.putExtra("lat_choose2", lat_choose2);
                intent.putExtra("lng_choose2", lng_choose2);


                intent.putExtra("name_user", name_user.getText().toString());
                intent.putExtra("tel", tel.getText().toString());
                intent.putExtra("note", note.getText().toString());

                intent.putExtra("rub", btnRub.getText().toString());
                intent.putExtra("song", btnSong.getText().toString());
                intent.putExtra("km",km);

                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out); //ใหม่ , เก่า

            }
        });

        btnRub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnRub();
            }
        });
        btnSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSong();
            }
        });

//        SupportMapFragment mySupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mMapView);
//        mySupportMapFragment.getMapAsync(this);
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

        //Toast.makeText(order2.this, time, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        finish(); // close this activity and return to preview activity (if there is any)
        return super.onOptionsItemSelected(item);
    }


    private void OnRub() {
        Intent intent = new Intent(this, location_insert.class);
        startActivityForResult(intent, 1);
    }

    private void OnSong() {
        Intent intent = new Intent(this, location_insert.class);
        startActivityForResult(intent, 2);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMapView = googleMap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_CC:
                if (resultCode == CreditCardActivity.RESULT_CANCEL) {
                    return;
                }

                Token token = data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT);

                // process your token here.
//                Toast.makeText(order2.this, data.getStringExtra(CreditCardActivity.EXTRA_TOKEN), Toast.LENGTH_SHORT).show();
//                Toast.makeText(order2.this, data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT) +"", Toast.LENGTH_SHORT).show();
//                Toast.makeText(order2.this, data.getParcelableExtra(CreditCardActivity.EXTRA_CARD_OBJECT)+"", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, check.class);
                startActivity(intent);

            default:
                super.onActivityResult(requestCode, resultCode, data);


        }

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Featurename_cur = data.getStringExtra("Featurename_cur");

                locality_cur = data.getStringExtra("locallity");
                admin_cur = data.getStringExtra("admin_cur");
                postalcode_cur = data.getStringExtra("postalcode_cur");
                subadmins_cur = data.getStringExtra("subadmins_cur");
                lat_cur1 = data.getStringExtra("lat_cur");
                lng_cur1 = data.getStringExtra("lng_cur");

                latitude1 = Double.parseDouble(lat_cur1);
                longitude1 = Double.parseDouble(lng_cur1);
                start = new LatLng(latitude1, longitude1);
                btnRub.setText(Featurename_cur + " " + locality_cur + " " + subadmins_cur + " " + admin_cur + " " + postalcode_cur);


            } else {

                address_choose = data.getStringExtra("address_choose");
                lat_choose1 = data.getStringExtra("lat_choose");
                lng_choose1 = data.getStringExtra("lng_choose");
                //Toast.makeText(getApplication(),"ว่าง",Toast.LENGTH_SHORT).show();
                btnRub.setText(address_choose);


                latitude1 = Double.parseDouble(lat_choose1);
                longitude1 = Double.parseDouble(lng_choose1);
                start = new LatLng(latitude1, longitude1);

            }
        } else {
            //Toast.makeText(getApplication(),"2",Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                Featurename_cur = data.getStringExtra("Featurename_cur");
                locality_cur = data.getStringExtra("localflity");
                admin_cur = data.getStringExtra("admin_cur");
                postalcode_cur = data.getStringExtra("postalcode_cur");
                subadmins_cur = data.getStringExtra("subadmins_cur");
                lat_cur2 = data.getStringExtra("lat_cur");
                lng_cur2 = data.getStringExtra("lng_cur");


                //castToLatLng
                latitude2 = Double.parseDouble(lat_cur2);
                longitude2 = Double.parseDouble(lng_cur2);
                end = new LatLng(latitude2, longitude2);
                //setText
                btnSong.setText(Featurename_cur + " " + locality_cur + " " + subadmins_cur + " " + admin_cur + " " + postalcode_cur);

                km = ""+(int) (Double.parseDouble(String.valueOf(SphericalUtil.computeDistanceBetween(start, end) / 1000)));
                Toast.makeText(getApplication(), (int) (Double.parseDouble(String.valueOf(SphericalUtil.computeDistanceBetween(start, end) / 1000))) + " กิโลเมตร", Toast.LENGTH_SHORT).show();

            } else {
                address_choose = data.getStringExtra("address_choose");
                lat_choose2 = data.getStringExtra("lat_choose");
                lng_choose2 = data.getStringExtra("lng_choose");
                //castToLatLng
                latitude2 = Double.parseDouble(lat_choose2);
                longitude2 = Double.parseDouble(lng_choose2);
                end = new LatLng(latitude2, longitude2);
                //setText
                btnSong.setText(address_choose);
                km = ""+(int) (Double.parseDouble(String.valueOf(SphericalUtil.computeDistanceBetween(start, end) / 1000)));
                Toast.makeText(getApplication(), (int) (Double.parseDouble(String.valueOf(SphericalUtil.computeDistanceBetween(start, end) / 1000))) + " กิโลเมตร", Toast.LENGTH_SHORT).show();

            }
        }

        //zoomAllMarkers();

    }

    public String getDistance(LatLng my_latlong, LatLng frnd_latlong) {
        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);
        String dist = distance + " M";

        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = distance + " KM";
        }
        return dist;
    }

}



