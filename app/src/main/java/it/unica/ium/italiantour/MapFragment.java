package it.unica.ium.italiantour;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static it.unica.ium.italiantour.MainActivity.loadPictureFromUri;


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
        mViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View res = inflater.inflate(R.layout.fragment_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fm = getChildFragmentManager();
        final SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);

        bottomDetails = res.findViewById(R.id.details_panel);
        Button filterButton = res.findViewById(R.id.filter_button);
        bsb = BottomSheetBehavior.from(bottomDetails);



        filterButton.setOnClickListener(v -> {

            //Check if filter panel is currently on screen
            if(res.findViewById(R.id.filterLayout) == null){
                //Move info panel a little out of the way
                if(bsb.getState() == BottomSheetBehavior.STATE_EXPANDED || bsb.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED)
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //Spawn filter selecting fragment
                FragmentManager fragmentManager = requireFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //Custom fragment animation on start and end fragment
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);

                Fragment filterFragment = new FilterFragment();
                fragmentTransaction.add(R.id.coordinator, filterFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                filterButton.setVisibility(View.GONE);
            }
        });



        mapFragment.getMapAsync(this);
        return res;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Hide keyboard on load.
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        //Set up behaviour for details panel.
        bsb.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch(i){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
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


        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        NavigationView nv = requireActivity().findViewById(R.id.nav_view);
        DrawerLayout drawerContainer = requireActivity().findViewById(R.id.main_container);

        //If the user hasn't authenticated itself yet, we move him into the login tab.
        if(mViewModel.getUser() == null){
            //Set to invisible until the user is authenticated.
            toolbar.setVisibility(View.GONE);
            nv.setVisibility(View.GONE);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_mapFragment_to_loginFragment);
            drawerContainer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

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



        //Example of our own marker structure and parsing with observer. We don't need to add it in the code anymore.
        /*InterestMarker testMarker = new InterestMarker("Cagliari", mViewModel.getUser().getUsername(),
                "0:00 - 23:59","Marker di prova per la mappa", 39.216667, 9.116667,"", InterestMarker.MONUMENTI);
        if(mViewModel.getAllMarkers().getValue() == null ||  mViewModel.getAllMarkers().getValue().size() == 0){
            mViewModel.insertMarker(testMarker);
        }
        */

        //Every time the list of markers changes (eg. we added or deleted one), redraw them on the map. Uses LiveData.observe().
        mViewModel.getFilteredMarkers().observe(this, list ->{
            mMap.clear();
            addFollowingMarker();
            for( InterestMarker m : list){
                LatLng m_coords = new LatLng(m.getLat(), m.getLon());
                //TODO: color code them based on category?
                Marker marker;
                float[] hsv = new float[3];
                switch (m.getCategories()) {
                    case InterestMarker.MONUMENTI:
                        Color.colorToHSV(ContextCompat.getColor(requireContext(), R.color.colorMonument), hsv);
                        marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()).icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
                        break;
                    case InterestMarker.RISTORANTI:
                        Color.colorToHSV(ContextCompat.getColor(requireContext(), R.color.colorRestaurant), hsv);
                        marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()).icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
                        break;
                    case InterestMarker.ARTE:
                        Color.colorToHSV(ContextCompat.getColor(requireContext(), R.color.colorArt), hsv);
                        marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()).icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
                        break;
                    case InterestMarker.SPORT:
                        Color.colorToHSV(ContextCompat.getColor(requireContext(), R.color.colorSport), hsv);
                        marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()).icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
                        break;
                    case InterestMarker.SVAGO:
                        Color.colorToHSV(ContextCompat.getColor(requireContext(), R.color.colorLeisure), hsv);
                        marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()).icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
                        break;
                    case InterestMarker.NATURA:
                        Color.colorToHSV(ContextCompat.getColor(requireContext(), R.color.colorNature), hsv);
                        marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()).icon(BitmapDescriptorFactory.defaultMarker(hsv[0])));
                        break;
                    default:
                         marker = mMap.addMarker(new MarkerOptions().position(m_coords).title(m.getName()));
                }
                marker.setTag(m);
            }
        });

        // Every time we click on a marker, we select it, and load its data into the details tab.
        mMap.setOnMarkerClickListener(m -> {
            InterestMarker data = (InterestMarker) m.getTag();
            if (data != null) {
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                m.showInfoWindow();
                mViewModel.setSelectedMarker(data.id);
                mViewModel.getSelectedMarker().observe( this, val -> {
                    Log.d("map", "DEBUG, marker selected: " + val.getName());
                    //mViewModel.insertFavourite(val.id);
                    updateDetailsPanel();
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(val.getLat(), val.getLon())));
                });
                return true;
            }else{
                m.showInfoWindow();
                return true;
            }
        });



        //If the map has been loaded with an item selected, pan onto the selected item.
        LiveData<InterestMarker> action = mViewModel.getSelectedMarker();
        if (action != null && action.getValue() != null){
            // Center on last selected item.
            updateDetailsPanel();
            LiveData<InterestMarker> data = mViewModel.getSelectedMarker();
            InterestMarker val;
            if (data != null && (val = data.getValue()) != null){
                bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(val.getLat(), val.getLon()),14));
            }
        }else { //Starts map screen zoomed in on your current position, if available (eg. given location permission).
            addFollowingMarker();
            if (mViewModel.getCurrentLocation().getValue() != null){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mViewModel.getCurrentLocation().getValue().getLatitude(),
                        mViewModel.getCurrentLocation().getValue().getLongitude()), 14));
            }
            else{
                Snackbar.make(requireActivity().findViewById(R.id.filter_container), "Errore nella verifica della posizione GPS. Funzionalità app limitate.", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    private void addFollowingMarker(){
        mViewModel.getCurrentLocation().observe(this, startingLocation -> {
            if (startingLocation!= null) {
                LatLng coords = new LatLng(startingLocation.getLatitude(), startingLocation.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(coords).title("Tu sei qui")
                        .icon(getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_person_pin_circle_blue_24dp))));
                mViewModel.getCurrentLocation().removeObservers(this); //Replace this observer with just a simple marker update
                mViewModel.getCurrentLocation().observe(this,  newLocation-> {
                    LatLng newc = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                    marker.setPosition(newc);
                });
            }
        });
    }

    private void updateDetailsPanel(){
        LiveData<InterestMarker> data = mViewModel.getSelectedMarker();
        InterestMarker val;
        if (data != null && (val = data.getValue()) != null){

            TextView details_title = requireActivity().findViewById(R.id.details_title);
            TextView details_desc = requireActivity().findViewById(R.id.details_desc);
            TextView details_orari = requireActivity().findViewById(R.id.details_hours);
            ImageView details_thumb = requireActivity().findViewById(R.id.details_thumbnail);
            Button favButton = requireActivity().findViewById(R.id.details_favButton);
            Button navButton = requireActivity().findViewById(R.id.details_nav);

            if(mViewModel.getUser() != null) {
                details_title.setText(val.getName());
                details_orari.setText("Aperto " + val.getOrari());
                details_desc.setText(val.getDesc());
                loadPictureFromUri(details_thumb, val.getPhotoUriParsed(), getContext());
                //Contextually change favourites button
                mViewModel.getFavourites().observe(this, interestMarkers -> {
                    if (containsID(interestMarkers, val.getId())) {
                        buttonLayoutRemove(favButton);
                        favButton.setOnClickListener(view -> {
                            mViewModel.removeFavourite(val.getId());
                        });
                    } else {
                        buttonLayoutAdd(favButton);
                        favButton.setOnClickListener(view -> {
                            mViewModel.insertFavourite(val.getId());
                        });
                    }
                });
                navButton.setOnClickListener(view -> {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+val.getLat()+","+val.getLon());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                });
            }

        }
    }

    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public boolean containsID(final List<InterestMarker> list, final Integer id){
        for( InterestMarker item : list){
            if(item.getId() == id){
                return true;
            }
        }
        return false;
    }

    void buttonLayoutRemove(Button b){
        b.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_white_24dp), null, null, null);
        b.setText("Rimuovi");
    }
    void buttonLayoutAdd(Button b){

        b.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp), null, null, null);
        b.setText("Aggiungi");
    }


}
