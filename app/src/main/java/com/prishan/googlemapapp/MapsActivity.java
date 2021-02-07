package com.prishan.googlemapapp;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public RelativeLayout infoRelative;
    TextView name, address;
    RoundedImageView user_dp;

    public SupportMapFragment mapFragment;
    MyDatabase myDatabase;
    public List<LocationModel> locationModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        locationModelList = new ArrayList<>();
        init();
    }

    private void init()
    {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        infoRelative = (RelativeLayout)findViewById(R.id.infoRelative);
        infoRelative.setVisibility(View.GONE);
        user_dp = (RoundedImageView)findViewById(R.id.user_dp);
        name = (TextView) findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).fallbackToDestructiveMigration().build();
        checkIfAppLaunchedFirstTime();
        loadAllTodos();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        Resources res = this.getResources();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));


        /*mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_baseline_directions_car_24))
                .title("Marker In Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(locationModelList.size()>0)
                {
                    LatLng defaultLocation = new LatLng(18.770803, 75.7721174);
                    for(int i=0; i<locationModelList.size(); i++)
                    {
                        LocationModel locationModel = locationModelList.get(i);
                        LatLng customMarkerLocationOne = new LatLng(locationModel.latitude, locationModel.longitude);

                        int drawable = res.getIdentifier(locationModel.image_url,"drawable",MapsActivity.this.getPackageName());
                        mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                                icon(BitmapDescriptorFactory.fromBitmap(
                                        createCustomMarker(MapsActivity.this,drawable,"Manish")))).setTag(locationModel);
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                infoRelative.setVisibility(View.VISIBLE);
                                LocationModel locmodel;
                                locmodel = (LocationModel) marker.getTag();

                                int draw = res.getIdentifier(locmodel.image_url,"drawable",MapsActivity.this.getPackageName());
                                user_dp.setImageResource(draw);
                                name.setText(""+locmodel.name);
                                address.setText(""+locmodel.address);
                                return true;
                            }
                        });
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
                }
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                infoRelative.setVisibility(View.GONE);
            }
        });


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        RoundedImageView markerImage = (RoundedImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        //txt_name.setVisibility(View.VISIBLE);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTodos() {
        new AsyncTask<String, Void, List<LocationModel>>() {
            @Override
            protected List<LocationModel> doInBackground(String... params) {
                return myDatabase.daoAccess().fetchAllLocationModel();
            }

            @Override
            protected void onPostExecute(List<LocationModel> locationList) {
                locationModelList.clear();
                locationModelList.addAll(locationList);
                //todoArrayList = locationModelList;
                mapFragment.getMapAsync(MapsActivity.this);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<LocationModel> locationModelList) {
        new AsyncTask<List<LocationModel>, Void, Void>() {
            @Override
            protected Void doInBackground(List<LocationModel>... params) {
                myDatabase.daoAccess().insertLocationList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(locationModelList);

    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
            buildDummyTodos();
        }
    }

    private void buildDummyTodos() {

        //here we can get json and store in Local DB
        LocationModel locationModel = new LocationModel();
        locationModel.name = "जाळीचा देव";
        locationModel.address = "जाळीचा देव";
        locationModel.description = "जाळीचा देव";
        locationModel.image_url = "chakradharswami";
        locationModel.latitude = 20.53439;
        locationModel.longitude = 75.9702722;

        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.name = "माताखिडकी";
        locationModel.address = "पटवीपुरा, कंवर नगर, गुरुचाया कॉलनी, अमरावती, महाराष्ट्र";
        locationModel.description = "माताखिडकी";
        locationModel.image_url = "krishna";
        locationModel.latitude = 20.9293753;
        locationModel.longitude = 77.7398519;

        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.name = "महुरगड";
        locationModel.address = "महुरगड";
        locationModel.description = "महुरगड";
        locationModel.image_url = "dattaprabhu";
        locationModel.latitude = 19.8492121;
        locationModel.longitude = 77.9205966;

        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.name = "रिधापूर";
        locationModel.address = "रिधापूर";
        locationModel.description = "रिधापूर";
        locationModel.image_url = "chakradharswami";
        locationModel.latitude = 21.2348288;
        locationModel.longitude = 77.8005416;

        locationModelList.add(locationModel);

        insertList(locationModelList);
    }
}