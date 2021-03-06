package it.unica.ium.italiantour;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.appcompat.widget.AppCompatImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static it.unica.ium.italiantour.MainActivity.LOAD_PICTURE;
import static it.unica.ium.italiantour.MainActivity.loadPictureFromUri;


/**
 * A simple {@link Fragment} subclass
 */
public class NewMarkerFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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
        nmvm = ViewModelProviders.of(requireActivity()).get(NewMarkerViewModel.class);
        mViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_marker, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView name = view.findViewById(R.id.newMarker_name);
        TextView desc = view.findViewById(R.id.newMarker_desc);
        TextView hours = view.findViewById(R.id.newMarker_hours);
        AppCompatImageView photo = view.findViewById(R.id.newMarker_imageView);

        Button addButton = view.findViewById(R.id.newMarker_add_button);
        TextView posText = view.findViewById(R.id.newMarker_position_text);
        ScrollView mScrollView = view.findViewById(R.id.newMarker_scroll);

        if(nmvm.getMarkerName() != null){
            name.setText(nmvm.getMarkerName());
            desc.setText(nmvm.getDesc());
            hours.setText(nmvm.getHours());
        }

        //Load photo when it's ready
        LiveData<Uri> photoRes = nmvm.getImageUri();
        photoRes.observe( this, u ->{
            if(u != null){
                loadPictureFromUri(photo, u, requireActivity());
                photo.setVisibility(View.VISIBLE);
            }
        });


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
                                        requireActivity(), R.raw.style_json));

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
            if(latLng != null) {
                posText.setText("Posizione: " + latLng.latitude + ", " + latLng.longitude);
            }else{
                posText.setText("Posizione:");
            }
        });


        //Set categories in spinner.
        Spinner catSpinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(aa);
        catSpinner.setOnItemSelectedListener(this);




        addButton.setOnClickListener( v -> {
            nmvm.setAll(name.getText().toString(), hours.getText().toString(), desc.getText().toString());
            if(nmvm.getMarkerName().length() > 0 && //Username not
                    mViewModel.getUser().getUsername().length() > 0 &&
                    nmvm.getHours().length() > 0 &&
                    nmvm.getDesc().length() > 0 &&
                    nmvm.getMarkerPos() != null &&
                    nmvm.getImageUri() != null && nmvm.getImageUri().getValue() != null && //Should we allow not to set a picture?
                    nmvm.getCategory() != 0){
            InterestMarker res = new InterestMarker(nmvm.getMarkerName(), mViewModel.getUser().getUsername(), nmvm.getHours(), nmvm.getDesc(), nmvm.getMarkerPos().getValue(), nmvm.getImageUri().getValue(), nmvm.getCategory());
            mViewModel.insertMarker(res);
            nmvm.resetFields();
            Navigation.findNavController(v).navigate(R.id.action_newMarkerFragment_pop);
            }else{
                View layout = requireActivity().findViewById(R.id.newMarker_layout);
                if(nmvm.getMarkerName().length() <= 0){
                    name.setError("Campo vuoto");
                }
                if(nmvm.getHours().length() <= 0){
                    hours.setError("Campo vuoto");
                }
                if(nmvm.getDesc().length() <= 0){
                    desc.setError("Campo vuoto");
                }
                if( nmvm.getImageUri() == null || nmvm.getImageUri().getValue() == null){
                    TextView tv= requireActivity().findViewById(R.id.newMarker_fotoText);
                    tv.requestFocus();
                    tv.setError("Foto del luogo obbligatoria");
                }
                Snackbar.make(layout, "Errore di inserimento: uno o più campi risultano vuoti.", Snackbar.LENGTH_LONG).show();
            }
        });

        photo.setOnClickListener( v -> {
            nmvm.setAll(name.getText().toString(), hours.getText().toString(), desc.getText().toString());
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            // Start the Intent

            if( ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            }else{ //If given storage permission, start image picking intent.
                requireActivity().startActivityForResult(galleryIntent, LOAD_PICTURE);
            }

        });



    }

    private Marker addSelectionMarker(){ //Start position selector from previous position
        Marker marker;
        if(nmvm.getMarkerPos() != null && nmvm.getMarkerPos().getValue() != null){
            marker = mMap.addMarker(new MarkerOptions().position(nmvm.getMarkerPos().getValue()).title("Selezione"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nmvm.getMarkerPos().getValue(), 14));
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
        marker.setDraggable(true);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                nmvm.setMarkerPos(marker.getPosition());
            }
        });
        return marker;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        NewMarkerViewModel nmvm = ViewModelProviders.of(requireActivity()).get(NewMarkerViewModel.class);
        nmvm.setCategory(1<<pos);
        Log.d("NEW_CAT", Integer.toString(nmvm.getCategory()));
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}
