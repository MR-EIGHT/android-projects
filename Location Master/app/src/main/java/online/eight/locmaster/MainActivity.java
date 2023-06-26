package online.eight.locmaster;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private LocationManager locationManager;
    private Location lastLocation;
    private MapView mapView;
    private TextView Lattxt;
    private TextView Lontxt;

    private TextView inf;

//    public String ApiURL;

    private final ActivityResultLauncher<String[]> locationPermRequest = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(
                        android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    Toast.makeText(this, "Precise location permission granted", Toast.LENGTH_LONG).show();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    Toast.makeText(this, "Approximate location permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Location permission not granted", Toast.LENGTH_LONG).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map_view);

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        Lattxt = findViewById(R.id.textView4);
        Lontxt = findViewById(R.id.textView5);
        inf = findViewById(R.id.extraInfo);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }


    public void setMapLocation(GeoPoint location) {
        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(location);
        mapView.invalidate();
        Marker locMarker = new Marker(mapView);
        locMarker.setPosition(location);
        locMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(locMarker);
    }

    public static final String BASE_URL_BTS = "https://opencellid.org/";

    public void bts_click(View v) {
        BTSInfoGet bts = new BTSInfoGet(this);
        Map<String, String> btsDict = bts.getBTSDict();

        LocationBTSService apiService = getGeoApiServiceBTS();
        apiService.getLocation(btsDict.get("MCC"), btsDict.get("MNC"), btsDict.get("TAC"), btsDict.get("CellID")).enqueue(new Callback<BTSAPIResponse>() {
            @Override
            public void onResponse(Call<BTSAPIResponse> call, Response<BTSAPIResponse> response) {
                String latitude = response.body().getLatitude();
                String longitude = response.body().getLongitude();
                setTexts(Double.parseDouble(latitude), Double.parseDouble(longitude));
                inf.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<BTSAPIResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public static LocationBTSService getGeoApiServiceBTS() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_BTS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocationBTSService.class);

    }


    public static LocationApiService getGeoApiService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocationApiService.class);
    }


    public static final String BASE_URL_IP = "http://ip-api.com/";


    public void ip_click(View v) {
        LocationApiService apiService = getGeoApiService();
        apiService.getLocation().enqueue(new Callback<GeoResponse>() {
            @Override
            public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
                double latitude = response.body().getLatitude();
                double longitude = response.body().getLongitude();
                setTexts(latitude, longitude);
                inf.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<GeoResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void wifi_click(View v) {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LSLocationListener());
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location == null) {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_LONG).show();
            return;
        }
        lastLocation = location;
        setTexts(location.getLatitude(), location.getLongitude());
        inf.setText("Provider:" + location.getProvider() + "\nAltitude: " + location.getAltitude() + "\nSpeed:" + location.getSpeed() + "\nAccuracy: " + location.getAccuracy());
    }

    public void gps_click(View v) {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LSLocationListener());
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_LONG).show();
            return;
        }
        lastLocation = location;
        setTexts(location.getLatitude(), location.getLongitude());
        inf.setText("Provider:" + location.getProvider() + "\nAltitude: " + location.getAltitude() + "\nSpeed:" + location.getSpeed() + "\nAccuracy: " + location.getAccuracy());
    }

    public void map_btn(View v) {
        double lat = Double.parseDouble(new StringBuilder(Lattxt.getText()).replace(0, 4, "").toString());
        double lon = Double.parseDouble(new StringBuilder(Lontxt.getText()).replace(0, 4, "").toString());
        setMapLocation(new GeoPoint(lat, lon));

    }

    public void setTexts(double lat, double lon) {
        Lattxt.setText(new StringBuilder().append("Lat:").append(String.valueOf(lat)).toString());
        Lontxt.setText(new StringBuilder().append("Lon:").append(String.valueOf(lon)).toString());

    }

    private static class LSLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
        }
    }


    public boolean isEmulator() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (provider != null && provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
            return true;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("No permission!");
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        if (lastKnownLocation != null && lastKnownLocation.getAccuracy() < 0) {
            return true;
        }

        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        if (model != null && model.equalsIgnoreCase("sdk")) {
            return true;
        }
        return manufacturer != null && manufacturer.equalsIgnoreCase("unknown");
    }


    public void emulatorReal(View v) {
        if (isEmulator())
            Toast.makeText(this, "Location is probably coming from an emulator!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Location is probably coming from a real sensor!", Toast.LENGTH_LONG).show();

    }


}