package it.unica.ium.italiantour;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MainViewModel mViewModel;
    private GoogleMap mMap;
    private ConstraintLayout bottomDetails;
    private BottomSheetBehavior bsb;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View res = inflater.inflate(R.layout.fragment_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fm = getChildFragmentManager();
        final SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        NavigationView nv = getActivity().findViewById(R.id.nav_view);
        bottomDetails = res.findViewById(R.id.details_panel);
        bsb = BottomSheetBehavior.from(bottomDetails);

        //If the user hasn't authenticated itself yet, we move him into the login tab.
        if(mViewModel.getUser() == null){
            //Set to invisible until the user is authenticated.
            toolbar.setVisibility(View.GONE);
            nv.setVisibility(View.GONE);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_mapFragment_to_loginFragment);
        }



        mapFragment.getMapAsync(this);
        return res;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set up behaviour for details panel.
        bsb.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch(i){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);

        updateDetailsPanel();
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

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        //Example of our own marker structure and parsing with observer.
        InterestMarker testMarker = new InterestMarker("Cagliari", mViewModel.getUser().getUsername(),
                "0:00 - 23:59","Marker di prova per la mappa", 39.216667, 9.116667);
        if(mViewModel.getAllMarkers().getValue() == null ||  mViewModel.getAllMarkers().getValue().size() == 0){
            mViewModel.insertMarker(testMarker);
        }

        //Every time the list of markers changes (eg. we added or deleted one), redraw them on the map. Uses LiveData.observe().
        mViewModel.getAllMarkers().observe(this, list ->{
            mMap.clear();
            for( InterestMarker m : list){
                LatLng m_coords = new LatLng(m.getLat(), m.getLon());
                Marker marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()));
                marker.setTag(m);
            }
        });

        // Every time we click on a marker, we select it, and load its data into the details tab.
        mMap.setOnMarkerClickListener(m -> {
            InterestMarker data = (InterestMarker) m.getTag();
            mViewModel.setSelectedMarker(data.id);
            mViewModel.getSelectedMarker().observe( this, val -> {
                //todo: This will execute once it's finished loading. Start details panel here.
                Log.d("map", "DEBUG, marker selected: " + val.getName());
                //mViewModel.insertFavourite(val.id);
                updateDetailsPanel();
                bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(val.getLat(), val.getLon()), 5));
            });
            return true;
        });

        //If the map has been loaded from a favourites click event, pan onto the selected favourite.
        LiveData<InterestMarker> action = mViewModel.getSelectedMarker();
        if (action != null && action.getValue() != null){
            // Center on last selected item.
            LiveData<InterestMarker> data = mViewModel.getSelectedMarker();
            InterestMarker val;
            if (data != null && (val = data.getValue()) != null){
                bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(val.getLat(), val.getLon()), 5));
            }
        }

    }

    void updateDetailsPanel(){
        LiveData<InterestMarker> data = mViewModel.getSelectedMarker();
        InterestMarker val;
        if (data != null && (val = data.getValue()) != null){

            TextView details_title = getActivity().findViewById(R.id.details_title);
            TextView details_desc = getActivity().findViewById(R.id.details_desc);
            TextView details_orari = getActivity().findViewById(R.id.details_hours);
            details_title.setText(val.getName());
            details_orari.setText("Orari di apertura: " + val.getOrari());
            details_desc.setText(val.getDesc());
        }
    }

}
