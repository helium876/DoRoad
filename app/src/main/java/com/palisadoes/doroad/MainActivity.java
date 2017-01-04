package com.palisadoes.doroad;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import constants.Constants;


public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private TextView lngText, latText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private boolean isSignedIn;
    private TextInputLayout email_wrapper, password_wrapper;
    TextView link_register;
    Button login;
    EditText email, password;
    private ProgressDialog progressDialog;


    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasGooglePlayServices()) {
            Toast.makeText(this, "Google play services not found.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    isSignedIn = true;
                    Log.d(Constants.LOGGER, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    isSignedIn = false;
                    Log.d(Constants.LOGGER, "onAuthStateChanged:signed_out");
                }

            }
        };
        checkPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
    private void initViews()
    {
        email_wrapper = (TextInputLayout) findViewById(R.id.user_email_wrapper);
        password_wrapper = (TextInputLayout) findViewById(R.id.user_password_wrapper);
        email = email_wrapper.getEditText();
        password = password_wrapper.getEditText();
        login = (Button) findViewById(R.id.btn_login);
        link_register = (TextView) findViewById(R.id.link_signup);
    }





    private void checkPermissions() {
        if (isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            setUpGoogleApiClient();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    synchronized void setUpGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpGoogleApiClient();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Intervals millis
        mLocationRequest.setFastestInterval(500); //If avaible sooner

        if (!isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            return;
        }

        if (!checkGPSisOpen()) {
            Toast.makeText(this, "Enable location services for accurate data.", Toast.LENGTH_SHORT)
                    .show();
            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(viewIntent);
        } else {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            getCoords(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
        }
    }

    //Coordinates Getter
    private void getCoords(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            //latText.setText(getString(R.string.latitude_label, latitude));
            //lngText.setText(getString(R.string.longitude_label, longitude));
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        getCoords(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setUpGoogleApiClient();
    }

    //If GooglePlay Services is active
    private boolean hasGooglePlayServices() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }

    //
    private boolean checkGPSisOpen() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

}