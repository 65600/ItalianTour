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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements FavouriteFragment.OnListFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private MainViewModel mViewModel;

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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

    }


    //Method called from clicking an item inside the favourites list.
    public void onListFragmentInteraction(InterestMarker item){
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.setSelectedMarker(item.id);
        mViewModel.getSelectedMarker().observe( this, val -> {
            Log.d("favourites", "DEBUG, marker selected: " + val.getName());
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_favouriteFragment_pop);
        });
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

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
