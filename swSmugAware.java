package com.guamflights;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
//
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class swSmugAware extends Fragment implements OnMapReadyCallback {

    private static final String ARG_JSONFILE = "";
    private static final String TAG = "swSmugAware.json";
    private final Handler handler = new Handler();
    private final int MYTIMER = 3000; // In milliseconds (3 seconds) change it here for faster/slower loops...
    private String mJsonFile;
    private MapView swMapView;
    private GoogleMap myGoogleMap;
    private SupportMapFragment mapFragment;
    private List<swPlaneObject> planeObjects;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    public swSmugAware() {}

    public static swSmugAware newInstance(String jsonFile) {
        swSmugAware fragment = new swSmugAware();
        Bundle args = new Bundle();
        args.putString(ARG_JSONFILE, jsonFile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mJsonFile = getArguments().getString(ARG_JSONFILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sw_smug_aware, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gMapView);
        mapFragment.getMapAsync(this);
        scheduleUpdateMap();
        planeObjects = new ArrayList<swPlaneObject>();
        return v;
    }

    @Override
    public void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        myGoogleMap = mMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear(); //clear old markers
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(13.5, 144.8))
                .zoom(8)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 500, null);
    }

    // to kill it off: handler.removeCallbacksAndMessages(null)
    public void scheduleUpdateMap() {
        handler.postDelayed(new Runnable() {
            public void run() {
                updateMap();
                handler.postDelayed(this, MYTIMER);
            }
        }, MYTIMER);
    }

    private void updateMap() {
        Log.i(TAG, "updated map.");
        getPlaneData();
        myGoogleMap.clear();
        for (int i = 0; i < planeObjects.size(); i++) {
            swPlaneObject myPlane = planeObjects.get(i);
            LatLng position = new LatLng(myPlane.getMyLat(), myPlane.getMyLon());
            int pinGraphicResId = 0;
            Integer mySquawk = myPlane.getSquawk();
            if (mySquawk == 7200) {
                pinGraphicResId = R.drawable.ic_emergencysquawk;
            } else {
                pinGraphicResId = R.drawable.ic_aircraft;
            }
            MarkerOptions tmpMarkerOptions = new MarkerOptions();
            tmpMarkerOptions.position(position);
            tmpMarkerOptions.title(myPlane.getFlightNum());
            tmpMarkerOptions.icon(BitmapDescriptorFactory.fromResource(pinGraphicResId));
            tmpMarkerOptions.rotation(myPlane.getPlaneTrack());
            Marker marker = myGoogleMap.addMarker(tmpMarkerOptions);
        }
    }

    private void getPlaneData() {
        mRequestQueue = Volley.newRequestQueue(this.getContext());
        mStringRequest = new StringRequest(Request.Method.GET, mJsonFile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void parseJson(String response) {
        planeObjects.clear();
        String jsonString =  "{\"planeItems\": " + response + "}";
        // Log.i(TAG, "Response: " + jsonString);
        //
        try {
            JSONArray planeItems = null;
            JSONObject raw = new JSONObject(jsonString);
            if(raw.has("planeItems")){
                planeItems = raw.getJSONArray("planeItems");
            }
            if (planeItems != null) {
                for (int i = 0; i < planeItems.length(); i++) {
                    JSONObject tmpJson = planeItems.getJSONObject(i);
                    swPlaneObject newPlane = new swPlaneObject();
                    if (tmpJson.has("flight")) {
                        newPlane.setFlightNum(tmpJson.getString("flight"));
                    }
                    if (tmpJson.has("track")) { newPlane.setPlaneTrack(tmpJson.getInt("track")); }
                    if (tmpJson.has("lat")) { newPlane.setMyLat(tmpJson.getDouble("lat")); }
                    if (tmpJson.has("lon")) { newPlane.setMyLon(tmpJson.getDouble("lon")); }
                    if (tmpJson.has("squawk")) { newPlane.setSquawk(tmpJson.getInt("squawk")); }
                    if (((newPlane.getMyLat() != null) && (newPlane.getMyLat() > 3)) && ((newPlane.getMyLon() != null) && (newPlane.getMyLon() > 3))) {
                        // Basically you want to check to see if it's either null, or 0/0. If it has 'any' value, it's likely to be the right value.
                        planeObjects.add(newPlane);
                    }
                } // for (int i = 0; i < planeItems.length(); i++) {
            } // if (planeItems != null) {
            Log.i(TAG, "Plane Objects: " + planeObjects.size());
        }catch(Exception e){
            Log.e(TAG, "Error: " + e.toString());
        }
        Log.i(TAG, "Made it through the Json");
    }
}