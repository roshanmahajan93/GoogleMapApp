package com.prishan.googlemapapp;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

                LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
                LatLng customMarkerLocationTwo = new LatLng(28.583078, 77.313744);
                LatLng customMarkerLocationThree = new LatLng(28.580903, 77.317408);
                LatLng customMarkerLocationFour = new LatLng(28.580108, 77.315271);
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this,R.drawable.one,"Manish")))).setTitle("iPragmatech Solutions Pvt Lmt");
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this,R.drawable.two,"Narender")))).setTitle("Hotel Nirulas Noida");

                mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this,R.drawable.one,"Neha")))).setTitle("Acha Khao Acha Khilao");
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this,R.drawable.two,"Nupur")))).setTitle("Subway Sector 16 Noida");

                mMap.moveCamera(CameraUpdateFactory.newLatLng(customMarkerLocationOne));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
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
}