package com.example.cityreport.Telas;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.cityreport.Banco.LatLngProblemas;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cityreport.databinding.ActivityMapaEspacialPrefeituraBinding;

import java.util.List;

public class MapaEspacialPrefeituraActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaEspacialPrefeituraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaEspacialPrefeituraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        int i = 0;
        List<LatLngProblemas> latLngProblemasList = Problema.obterLatLngroblemas(getApplicationContext());
        for (LatLngProblemas latLngProblemas: latLngProblemasList){
            LatLng latLng = new LatLng(latLngProblemas.getLatitude(),latLngProblemas.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(latLngProblemas.getDescricao()));
            if (i==0){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            i++;
        }

    }
}