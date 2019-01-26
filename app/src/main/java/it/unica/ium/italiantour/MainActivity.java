package it.unica.ium.italiantour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;


public class MainActivity extends AppCompatActivity implements FavouriteFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        NavigationUI.setupWithNavController(toolbar, Navigation.findNavController(this, R.id.nav_host_fragment));
        //Set it to invisible until the user is authenticated.
        toolbar.setVisibility(View.GONE);
        //We first redirect the user away from the main fragment. This will also inflate the toolbar once done.
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_mapFragment_to_loginFragment);
    }


    //Method called from clicking an item inside the favourites list.
    public void onListFragmentInteraction(InterestMarker item){
        //todo: go back to main map, move camera to item position and open details panel.
    }
}
