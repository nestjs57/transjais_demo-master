package com.transjai.transjai;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class location_insert extends AppCompatActivity implements OnMapReadyCallback {


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

    private String lat_cur = "";
    private String lng_cur = "";
    private String locality_cur = "";
    private String admin_cur = "";
    private String postalcode_cur = "";
    private String subadmins_cur = "";
    private String Featurename_cur = "";
    private Button btncurrent;

    private TextView googleplace;
    private Button btnchoose;
    private String address_choose = "";
    private String lat_choose = "";
    private String lng_choose = "";


    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_insert);
        bindWidjet();
        setEvent();
        mProgressDialog = new ProgressDialog(this);


    }

    private void setEvent() {
        btnchoose = (Button) findViewById(R.id.btnchoose);
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.show();
                Toast.makeText(getApplicationContext(), address_choose, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("address_choose", address_choose);
                intent.putExtra("lat_choose", lat_choose);
                intent.putExtra("lng_choose", lng_choose);
                setResult(RESULT_FIRST_USER, intent);
                finish();
            }
        });
    }

    private void bindWidjet() {
        mLocTextView = (TextView) findViewById(R.id.mLocationTextView);
//        mGeoCodingBtn = (ImageView) findViewById(R.id.mGeoCodingBtn);
//        mAnimationBtn = (ImageView) findViewById(R.id.mAnimationBtn);
//        mClearMarkersBtn = (ImageView) findViewById(R.id.clearMarkersBtn);

        googleplace = (TextView) findViewById(R.id.googleplace);
        googleplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("กำลังค้นหา ..");
                mProgressDialog.show();
                try {

                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().
                            setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(location_insert.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        //binf frahment map
        FragmentManager fragmentMgr = getSupportFragmentManager();
        SupportMapFragment mMapViewFragment = (SupportMapFragment) fragmentMgr
                .findFragmentById(R.id.mMapView);
        mMapViewFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapView = googleMap;
        setupMap();
        requestRealTimePermission();


    }


    public void startLocationTracking() {

        mMapView.setMyLocationEnabled(true);
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                        // create location tracking request
                        mRequest = LocationRequest.create();
                        mRequest.setInterval(UPDATE_INTERVAL);
                        mRequest.setFastestInterval(FASTEST_INTERVAL);
                        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mRequest, new LocationListener() {
                            @Override
                            public void onLocationChanged(final Location location) {
                                //updateLocationTextView(location); set toolbar is latlng

//                                LatLng po = new LatLng(6.589524, 100.061150);
//                                mMapView.addMarker(new MarkerOptions()
//                                        .position(po)
//                                        .title("ที่อยู่ปัจจุบัน")
//                                );


                                btncurrent = (Button) findViewById(R.id.btncurrent);
                                btncurrent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        mProgressDialog.show();

                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        Geocoder geocoder = new Geocoder(getApplicationContext());


                                        try {
                                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            locality_cur = addressList.get(0).getLocality();
                                            admin_cur = addressList.get(0).getAdminArea();
                                            postalcode_cur = addressList.get(0).getPostalCode();
                                            subadmins_cur = addressList.get(0).getSubAdminArea();
                                            Featurename_cur = addressList.get(0).getFeatureName();
                                            lat_cur = String.valueOf(location.getLatitude());
                                            lng_cur = String.valueOf(location.getLongitude());


                                            Intent intent = new Intent();
                                            intent.putExtra("locallity", locality_cur);
                                            intent.putExtra("admin_cur", admin_cur);
                                            intent.putExtra("postalcode_cur", postalcode_cur);
                                            intent.putExtra("subadmins_cur", subadmins_cur);
                                            intent.putExtra("Featurename_cur", Featurename_cur);
                                            intent.putExtra("lat_cur", lat_cur);
                                            intent.putExtra("lng_cur", lng_cur);

                                            setResult(RESULT_OK, intent);

                                            //Toast.makeText(getApplicationContext(), admin_cur, Toast.LENGTH_SHORT).show();
                                            mMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                        } catch (Exception e) {

                                        }

                                        finish();


                                    }
                                });

//                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                                    mMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).build();

        mApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //mApiClient.disconnect();
    }

    private void updateLocationTextView(Location location) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        final String lat = formatter.format(location.getLatitude());
        final String lng = formatter.format(location.getLongitude());

        mCurrentLocStr = String.format("Lat: %s°, Long: %s°", lat, lng);
        mLocTextView.setText(mCurrentLocStr);


//        LatLng po = new LatLng(location.getLatitude(), location.getLongitude());
//        mMapView.addMarker(new MarkerOptions()
//                .position(po)
//                .title("ที่อยู่ปัจจุบัน")
//        );
    }


    public void setupMap() {

        mMapView.getUiSettings().setZoomControlsEnabled(false);
        mMapView.setTrafficEnabled(true);
        /*
         * MAP_TYPE_NONE No base map tiles. MAP_TYPE_NORMAL Basic maps.
		 * MAP_TYPE_SATELLITE Satellite maps with no labels. MAP_TYPE_HYBRID
		 * Satellite maps with a transparent layer of major streets.
		 * MAP_TYPE_TERRAIN
		 */
        mMapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void requestRealTimePermission() {
        Nammu.askForPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                startLocationTracking();
            }

            @Override
            public void permissionRefused() {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //place.getLatLng().latitude
        mProgressDialog.dismiss();

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                address_choose = String.valueOf(place.getAddress());
                lat_choose = String.valueOf(place.getLatLng().latitude);
                lng_choose = String.valueOf(place.getLatLng().longitude);

                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(latLng);
                markerOption.title(address_choose);
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
                mMapView.addMarker(markerOption);

                mMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                //Toast.makeText(getApplicationContext(), address_choose, Toast.LENGTH_SHORT).show();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                //Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
