package it.unica.ium.italiantour;

import androidx.appcompat.widget.Toolbar;
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
        EditText username = view.findViewById(R.id.loginUsernameField);
        EditText password = view.findViewById(R.id.loginPasswordField);
        Button login = view.findViewById(R.id.loginAccediButton);
        Button register = view.findViewById(R.id.loginRegistratiButton);


        login.setOnClickListener(v -> {
            //todo: logging in message?
            LoginUser res = loginViewModel.validateCredentials(username.getText().toString(), password.getText().toString());
            if(res != null){
                mainViewModel.setUser(res);
                Toolbar toolbar = getActivity().findViewById(R.id.mainToolbar);
                toolbar.setVisibility(View.VISIBLE);
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mapFragment);
            }else{
                //todo: error message
            }
        });

        register.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });

    }

}
