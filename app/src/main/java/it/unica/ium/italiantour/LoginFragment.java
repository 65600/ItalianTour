package it.unica.ium.italiantour;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
//Commit Test Andres
public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private MainViewModel mainViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        Log.i(this.toString(), mainViewModel.toString());
        View layout = view.findViewById(R.id.loginPanelLayout);
        EditText username = view.findViewById(R.id.loginUsernameField);
        EditText password = view.findViewById(R.id.loginPasswordField);
        Button login = view.findViewById(R.id.loginAccediButton);
        Button register = view.findViewById(R.id.loginRegistratiButton);


        login.setOnClickListener(v -> {
            LoginUser res = loginViewModel.validateCredentials(username.getText().toString(), password.getText().toString());
            if(res != null){
                mainViewModel.setUser(res);
                Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                NavigationView nv = getActivity().findViewById(R.id.nav_view);
                DrawerLayout dl = requireActivity().findViewById(R.id.main_container);
                toolbar.setVisibility(View.VISIBLE);
                nv.setVisibility(View.VISIBLE);
                dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mapFragment);
            }else{
                Snackbar.make(layout, "Errore di accesso. Verificare nome utente e password.", Snackbar.LENGTH_LONG).show();
                username.setError("Verificare nome utente");
                password.setError("Verificare password");
            }
        });

        register.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });

    }

}
