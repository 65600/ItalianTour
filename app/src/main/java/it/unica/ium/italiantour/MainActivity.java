package it.unica.ium.italiantour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;


public class MainActivity extends AppCompatActivity implements FavouriteFragment.OnListFragmentInteractionListener, FilterFragment.OnListFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    private MainViewModel mViewModel;

    public final static int LOAD_PICTURE = 3;
    public static int takeFlags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = findViewById(R.id.main_container);

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView nv = findViewById(R.id.nav_view);
        NavController navController =  Navigation.findNavController(this, R.id.nav_host_fragment);

        AppBarConfiguration abc = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(mDrawerLayout)
                .build();
        NavigationUI.setupWithNavController(toolbar, navController, abc);
        NavigationUI.setupWithNavController(nv, navController);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }


    //Method called from clicking an item inside the favourites list.
    public void onListFragmentInteraction(InterestMarker item){
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.setSelectedMarker(item.id);
        mViewModel.getSelectedMarker().observe( this, val -> {
            Log.d("favourites", "DEBUG, marker category: " + val.getCategories());
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_mapFragment);
        });
    }

    public void onListFragmentInteraction(Filtri.Filtro item){
        //Everything is updated in real time inside the holder class.
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String permissions[], @NotNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "approved");
                    mViewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
                    mViewModel.restartTracker();
                } else {
                    Log.d("PERMISSION", "denied");
                }
            }
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "approved");
                } else {
                    Log.d("PERMISSION", "denied");
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == LOAD_PICTURE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                Log.d("Images", selectedImage.toString());

                takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);


                NewMarkerViewModel nmvm = ViewModelProviders.of(this).get(NewMarkerViewModel.class);
                MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

                if(null != mViewModel.getUser()){
                    //Only if we're already logged in can we navigate to the new marker screen
                    nmvm.setImageUri(selectedImage);
                    //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_newFragment); //This shouldnt be necessary, resuming the activity with proper viewmodels.
                }else{
                    Log.e("IMG", "Saving image error");
                }

            } else {
                Log.e("layout", "Immagine non selezionata");
            }
        } catch (Exception e) {
            Log.e("layout",  "Errore durante la selezione");
        }

    }

    public static void loadPictureFromUri(ImageView photo, Uri photoRes, Context a){
        try {
            if(photoRes.getScheme().equals("file")) {
                File file = new File(photoRes.toString());
                FileInputStream inputStream = new FileInputStream(file);
                photo.setImageDrawable(Drawable.createFromStream(inputStream, photoRes.toString()));
            }else{
                photo.setImageDrawable(Drawable.createFromStream(
                        a.getContentResolver().openInputStream(photoRes), photoRes.toString()));
            }
        } catch (Exception e) {
            Log.e("PHOTO", "errore di caricamento foto");
            Log.e("PHOTO", e.getMessage());
        }
    }


}
