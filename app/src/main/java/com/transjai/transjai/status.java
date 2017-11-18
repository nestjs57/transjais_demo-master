package com.transjai.transjai;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class status extends Fragment implements OnMapReadyCallback {


    private Button btnSearchDriver;
    private Boolean stopMed = false;

    private GoogleMap nMapView;
    private GoogleApiClient mApiClient;

    private LatLng origin = null;
    private LatLng latLng = null;

    private static final int REQUEST_LOCATION = 1;
    private String mCurrentLocStr;
    private TextView mLocTextView;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 1000;
    private List<LatLng> listOfLatLng = new ArrayList<>();
    private LocationRequest mRequest;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private Polygon polygon;
    private MarkerOptions mapMarker;
    int currentPt;
    int mAnimationZoom = 15;
    private ProgressDialog progressDialog;


    private String lat_start_cur = "";
    private String lng_start_cur = "";
    //private String lat_end_cur = "";
    private String[] lat_end_cur = {""};

    private String lng_end_cur = "";

    private String lat_start_choose = "";
    private String lng_start_choose = "";
    private String lat_end_choose = "";
    private String lng_end_choose = "";

    private String status = "";

    private LinearLayout linearLayout;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("order");

    private TextView txtDelivering;
    private TextView txtArrivedTo;
    private TextView txtSetupComplete;
    private TextView txtGetBack;
    private TextView txtArrived;
    private Button btnDel;


    public status() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        progressDialog = new ProgressDialog(getActivity());
        //byWidget
        bindWidget(v);
        setEvent();
        //onMapReady(nMapView);
        FragmentManager fragmentMgr = getFragmentManager();
        SupportMapFragment mMapViewFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mMapView);
        mMapViewFragment.getMapAsync(this);
        linearLayout = (LinearLayout) v.findViewById(R.id.allStatus);
        linearLayout.setVisibility(View.INVISIBLE);
        firebaseConnect();
        return v;
    }

    private void setEvent() {
        btnSearchDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setCancelable(true);
                Button button1 = (Button) dialog.findViewById(R.id.button1);
                button1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext()
                                , "Close dialog", Toast.LENGTH_SHORT);
                        dialog.cancel();
                    }
                });
                TextView textView2 = (TextView) dialog.findViewById(R.id.textView2);
                textView2.setText("please wait \nWe are looking for a driver.");
                dialog.show();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("ต้องการจะลบออเดอร์หรือไม่?");
                builder.setPositiveButton("ลบ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "ขอบคุณครับ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void bindWidget(View v) {
        txtDelivering = (TextView) v.findViewById(R.id.txtDelivering);
        txtArrivedTo = (TextView) v.findViewById(R.id.txtArrivedTo);
        txtSetupComplete = (TextView) v.findViewById(R.id.txtSetupComplete);
        txtGetBack = (TextView) v.findViewById(R.id.txtGetBack);
        txtArrived = (TextView) v.findViewById(R.id.txtArrived);

        btnSearchDriver = (Button) v.findViewById(R.id.btnSearchDriver);
        btnDel = (Button) v.findViewById(R.id.btndel);
    }


    private void firebaseConnect() {
        database.orderByChild("user").equalTo(auth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.setVisibility(View.INVISIBLE);

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    linearLayout.setVisibility(View.VISIBLE);
                    lat_start_cur = (String) child.child("latCur_start").getValue();
                    lng_start_cur = (String) child.child("lngCur_start").getValue();
                    lat_end_cur[0] = (String) child.child("latCur_end").getValue();
                    lng_end_cur = (String) child.child("lngCur_end").getValue();

                    lat_start_choose = (String) child.child("lat_choose_start").getValue();
                    lng_start_choose = (String) child.child("lng_choose_start").getValue();
                    lat_end_choose = (String) child.child("lat_choose_end").getValue();
                    lng_end_choose = (String) child.child("lng_choose_end").getValue();

                    status = (String) child.child("status").getValue();

                    //Toast.makeText(getActivity(), lng_start_cur, Toast.LENGTH_LONG).show();

                    try {
                        drawDirection();
                    } catch (Exception e) {
                    }

                    try {
                        if (status.equals("find_driver")) {

                            txtDelivering.setTextColor(Color.parseColor("#D3D3D3"));
                            txtArrivedTo.setTextColor(Color.parseColor("#D3D3D3"));
                            txtSetupComplete.setTextColor(Color.parseColor("#D3D3D3"));
                            txtGetBack.setTextColor(Color.parseColor("#D3D3D3"));
                            txtArrived.setTextColor(Color.parseColor("#D3D3D3"));

                        } else if (status.equals("delivery")){
                            txtArrivedTo.setTextColor(Color.parseColor("#D3D3D3"));
                            txtSetupComplete.setTextColor(Color.parseColor("#D3D3D3"));
                            txtGetBack.setTextColor(Color.parseColor("#D3D3D3"));
                            txtArrived.setTextColor(Color.parseColor("#D3D3D3"));
                        }else if (status.equals("arrived_at_destination")){
                            txtSetupComplete.setTextColor(Color.parseColor("#D3D3D3"));
                            txtGetBack.setTextColor(Color.parseColor("#D3D3D3"));
                            txtArrived.setTextColor(Color.parseColor("#D3D3D3"));
                        }
                        else if (status.equals("setup_complete")){
                            txtGetBack.setTextColor(Color.parseColor("#D3D3D3"));
                            txtArrived.setTextColor(Color.parseColor("#D3D3D3"));
                        }
                        else if (status.equals("getBack")){

                            txtArrived.setTextColor(Color.parseColor("#D3D3D3"));
                        }
                        else if (status.equals("Arrived")){
                            linearLayout.setVisibility(View.INVISIBLE);
                        }
                    }catch (Exception e) {
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void drawDirection() {
        //Toast.makeText(getActivity(), lat_end_cur[0], Toast.LENGTH_LONG).show();
        origin = new LatLng(Double.valueOf(lat_start_cur), Double.valueOf(lng_start_cur));
        latLng = new LatLng(Double.valueOf(lat_end_choose), Double.valueOf(lng_end_choose));
        Toast.makeText(getActivity(), lat_start_cur + "TEST", Toast.LENGTH_LONG).show();
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(origin);
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));

        MarkerOptions markerOption1 = new MarkerOptions();
        markerOption1.position(latLng);
        markerOption1.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));

        nMapView.addMarker(markerOption);
        nMapView.addMarker(markerOption1);

        progressDialog.setMessage("CameraUpdateFactoring ...");
        if (stopMed == false) {
            progressDialog.show();
        }
        new DirectionsAPI(getApplicationContext()).drawDirection(nMapView, origin, latLng, Color.BLACK, new DirectionsJSONParser.OnDirectionAPIListener() {
            @Override
            public void onFinished(DirectionsAPI api, ArrayList<LatLng> points) {
                //zoomTwoPosition(origin, latLng);
                if (stopMed == false) {
                    firebaseConnect();
                    Toast.makeText(getActivity(), "getActivity()", Toast.LENGTH_SHORT).show();
                    LatLngBounds AUSTRALIA = new LatLngBounds(
                            new LatLng(Double.parseDouble(lat_start_cur), Double.parseDouble(lng_start_cur)), new LatLng(Double.parseDouble(lat_end_choose), Double.parseDouble(lng_end_choose)));

                    nMapView.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 60));
                    progressDialog.dismiss();
                    stopMed = true;
                    //Toast.makeText(getApplicationContext(), "Rounte Distance: " + api.routeDistance, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void zoomTwoPosition(LatLng origin, LatLng dest) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(dest);

        LatLngBounds bounds = builder.build();
        int padding = 300; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        nMapView.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMapView = googleMap;
        //drawDirection();
        firebaseConnect();
    }


}
