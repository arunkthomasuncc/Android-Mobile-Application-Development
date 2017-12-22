package com.example.arun.googlemapdemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.WeakHashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private boolean FirstClick = false;
    Location currentUserLocation;
    Location startLocation;
    Location previousLocation;
    Location destinationLocation;
    LatLngBounds.Builder bounds;


    private static final int MY_PERMISSIONS_REQUEST_GPS = 100;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Tracking App");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //no image is given
        //getSupportActionBar().setLogo(R.drawable.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    /**
     * Manipulates the map once available.
     * This ca where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMapLongClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
       Location location=currentUserLocation;
        if(location!=null)
        {
            Log.d("Inside","Null");
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(8)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else
        {
            Log.d("Error","reading location");
        }

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
        //mMap.setMyLocationEnabled(true);


    }
    @Override
    protected void onResume() {
        super.onResume();

      if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("GPS not enabled").setMessage("Would you like to enable GPS settings")
                  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {

                          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                          startActivity(intent);

                      }
                  })
                  .setNegativeButton("No", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.cancel();
                          finish();
                      }
                  });

          AlertDialog dialog = builder.create();
          dialog.show();

      }
      else
      {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Demo", "User Current Location" + location.getLatitude() + "," + location.getLongitude());

                previousLocation=currentUserLocation;
                currentUserLocation = location;




                if(FirstClick) {
                    LatLng previousLocLatLng=new LatLng(previousLocation.getLatitude(),previousLocation.getLongitude());
                    LatLng currentLocLatLng=new LatLng(currentUserLocation.getLatitude(),currentUserLocation.getLongitude());

                    Polyline polyline = mMap.addPolyline(new PolylineOptions().add(previousLocLatLng, currentLocLatLng));
                    polyline.setColor(Color.BLUE);
                    polyline.setWidth(8);
                    bounds.include(currentLocLatLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,100,locationListener);

            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GPS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                return;
            }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);


          Criteria criteria = new Criteria();
          Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
          currentUserLocation=location;
          SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                  .findFragmentById(R.id.map);
          mapFragment.getMapAsync(this);

        } }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){ /*{

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
*/

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(!FirstClick) {
            FirstClick =true;
            if(currentUserLocation !=null) {

                Toast.makeText(getApplicationContext(), "Start Location Tracking", Toast.LENGTH_LONG).show();
                LatLng startingLoc = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(startingLoc).title("Starting Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(startingLoc));
                startLocation = currentUserLocation;
                bounds = new LatLngBounds.Builder();
                bounds.include(startingLoc);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please send location again",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(currentUserLocation !=null) {
                Toast.makeText(getApplicationContext(), "Stop Location Tracking", Toast.LENGTH_LONG).show();
                LatLng lastLoc = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(lastLoc).title("Ending Location"));
                //   mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLoc));
                //   bounds.include(lastLoc);
                FirstClick = false;
            }
        }


    }
}