package it.unica.ium.italiantour;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass
 */
public class NewMarkerFragment extends Fragment {

    private NewMarkerViewModel nmvm;
    private MainViewModel mViewModel;
    public GoogleMap mMap;

    public NewMarkerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nmvm = ViewModelProviders.of(getActivity()).get(NewMarkerViewModel.class);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_marker, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView name = view.findViewById(R.id.newMarker_name);
        TextView desc = view.findViewById(R.id.newMarker_desc);
        TextView hours = view.findViewById(R.id.newMarker_hours);

        Button addButton = view.findViewById(R.id.newMarker_button);
        TextView posText = view.findViewById(R.id.newMarker_position_text);
        ScrollView mScrollView = view.findViewById(R.id.newMarker_scroll);

        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.newMarker_fragment);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    mMap = googleMap;

                    mMap.getUiSettings().setMapToolbarEnabled(false);
                    ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.newMarker_fragment))
                            .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

                    try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getActivity(), R.raw.style_json));

                        if (!success) {
                            Log.e("map", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("map", "Can't find style. Error: ", e);
                    }

                    Marker res = addSelectionMarker();

                    mMap.setOnMapClickListener(latLng -> {
                        res.setPosition(latLng);
                        nmvm.setMarkerPos(latLng);
                    });
                }
            });
        }

        nmvm.getMarkerPos().observe(this, latLng -> {
            posText.setText("Posizione: " + latLng.latitude + ", " + latLng.longitude);
        });

        addButton.setOnClickListener( v -> {
            InterestMarker res = new InterestMarker(name.getText().toString(), mViewModel.getUser().getUsername(),
                    hours.getText().toString(), desc.getText().toString(), nmvm.getMarkerPos().getValue());
            //TODO: check if the fields are built correctly
            mViewModel.insertMarker(res);
            Navigation.findNavController(v).navigate(R.id.action_newMarkerFragment_pop);
        });
    }

    private Marker addSelectionMarker(){ //Start position selector from previous position
        Marker marker;
        if(nmvm.getMarkerPos() != null && nmvm.getMarkerPos().getValue() != null){
            marker = mMap.addMarker(new MarkerOptions().position(nmvm.getMarkerPos().getValue()).title("Selezione"));
        }else{
            Location startingLocation = mViewModel.getCurrentLocation().getValue();
            if (startingLocation != null) {
                LatLng coords = new LatLng(startingLocation.getLatitude(), startingLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 14));
                marker = mMap.addMarker(new MarkerOptions().position(coords).title("Selezione"));
                nmvm.setMarkerPos(coords);
            }else{
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Selezione"));
            }
        }
        return marker;
    }
}
