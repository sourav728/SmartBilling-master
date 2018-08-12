package com.transvision.mbc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.transvision.mbc.adapters.InfoWindowCustom;
import com.transvision.mbc.adapters.RoleAdapter;
import com.transvision.mbc.other.DataParser;
import com.transvision.mbc.values.GetSetValues;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Sourav
 */
public class Location extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, DirectionCallback {

    ArrayList<GetSetValues> array_list;
    RoleAdapter roleAdapter;
    List<GetSetValues> objects = new ArrayList<GetSetValues>();
    ArrayList<GetSetValues> role_list;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    android.location.Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    String state = "", country = "", subLocality = "";
    Double lati,longi;
    Double double_lati, double_longi;
    TextView tvdistance;
    Button location;
    Spinner spinner;
    ArrayList<Polyline> polylines;
    Polyline line;
    private LatLng origin;
    private LatLng destination;
    LatLng latLng;
    TextView tvDistanceDuration;
    TextView duration_text, distance_text,empty_text,travelmode_text;
    String loc_lat="",loc_long="", mrname="", mrcode="";
    String startAddress="",endAddress="",mode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_location);
        array_list = (ArrayList<GetSetValues>) intent.getSerializableExtra("list");
        //tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
        duration_text = (TextView) findViewById(R.id.txt_duration_value);
        distance_text = (TextView) findViewById(R.id.txt_distance_value);


        role_list = new ArrayList<>();
        roleAdapter = new RoleAdapter(role_list, this);

        loc_lat = intent.getStringExtra("LAT");
        loc_long = intent.getStringExtra("LONG");
        mrname = intent.getStringExtra("MRNAME");
        mrcode=intent.getStringExtra("MRcode");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setInfoWindowAdapter(new InfoWindowCustom(this));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        //Showing Current Location Marker on Map
        latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Your Location"));

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        /**************It will display MR's location*************/

        /*****Getting latitude and longitude from PreviousFragment*****/
        if (!StringUtils.equalsIgnoreCase(loc_lat,"") && !StringUtils.equalsIgnoreCase(loc_long,"") && !StringUtils.equalsIgnoreCase(loc_lat,"NA") && !StringUtils.equalsIgnoreCase(loc_long,"NA") && !StringUtils.equalsIgnoreCase(loc_lat,"0") && !StringUtils.equalsIgnoreCase(loc_long,"0") && !StringUtils.equalsIgnoreCase(loc_lat,"0.0") && !StringUtils.equalsIgnoreCase(loc_long,"0.0"))
        {
            try {
                lati = Double.parseDouble(loc_lat);
                longi = Double.parseDouble(loc_long);
                mMap.addMarker(new MarkerOptions().title(mrname).snippet(mrcode).position(new LatLng(lati, longi)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Location not yet updated!!", Toast.LENGTH_SHORT).show();
            finish();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        polylines = new ArrayList<>();
        /********Click on a marker below code will be execute*********/
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("debug", "OnInfoClick");
                String url = getUrl(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                Log.d("onMapClick", url.toString());
                FetchUrl FetchUrl = new FetchUrl();
                // Start downloading json data from Google Directions API
                FetchUrl.execute(url);

                return false;
            }
        });
        //this code st  ops location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());
                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());
            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

          /*  if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }*/

            if (polylines.size() > 0) {
                polylines.get(0).remove();
                polylines.clear();
            }
            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) {    // Get distance from the list
                            distance = (String) point.get("distance");
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            duration = (String) point.get("duration");
                            continue;
                        }

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);
                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(Location.this, "Please ckeck your internet connection!!", Toast.LENGTH_SHORT).show();
            }

            startAddress = DataParser.mMyAppsBundle.getString("startAddress");
            Toast.makeText(Location.this, "Start Address.."+startAddress, Toast.LENGTH_SHORT).show();
            endAddress = DataParser.mMyAppsBundle.getString("endAddress");
            Toast.makeText(Location.this, "End Address.."+endAddress, Toast.LENGTH_SHORT).show();
           // mode = DataParser.mMyAppsBundle.getString("travel_mode");

            distance_text.setVisibility(View.VISIBLE);
            duration_text.setVisibility(View.VISIBLE);
           // travelmode_text.setVisibility(View.VISIBLE);
            //empty_text.setVisibility(View.VISIBLE);

            distance_text.setText(distance);
            duration_text.setText(duration);
            //travelmode_text.setText(mode+" "+"Mode");

            // tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                line = mMap.addPolyline(lineOptions);
                polylines.add(line);
            } else Log.d("onPostExecute", "without Polylines drawn");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Double latitide = marker.getPosition().latitude;
        Double longitude = marker.getPosition().longitude;
        return false;
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Toast.makeText(Location.this, "Success with status : " + direction.getStatus(), Toast.LENGTH_SHORT).show();
        if (direction.isOK()) {
            mMap.addMarker(new MarkerOptions().position(origin));
            mMap.addMarker(new MarkerOptions().position(destination));
            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.BLUE));
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(Location.this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
