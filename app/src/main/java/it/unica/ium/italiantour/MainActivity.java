package it.unica.ium.italiantour;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements FavouriteFragment.OnListFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;

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

    }


    //Method called from clicking an item inside the favourites list.
    public void onListFragmentInteraction(InterestMarker item){
        //todo: go back to main map, move camera to item position and open details panel.
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
}
