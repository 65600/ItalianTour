package it.unica.ium.italiantour;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class LogoutFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mvm = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        //Set user to null and go back to the map menu, where the user will be redirected to the login page.
        mvm.setUser(null);
        mvm.setSelectedMarker(null);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_logoutFragment_to_mapFragment);
    }
}
