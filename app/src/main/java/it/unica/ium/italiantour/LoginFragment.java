package it.unica.ium.italiantour;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

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
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        EditText username = view.findViewById(R.id.loginUsernameField);
        EditText password = view.findViewById(R.id.loginPasswordField);
        Button login = view.findViewById(R.id.loginAccediButton);
        Button register = view.findViewById(R.id.loginRegistratiButton);


        login.setOnClickListener(v -> {
            //todo: logging in message?
            if(mViewModel.validateCredentials(username.getText().toString(), password.getText().toString())){
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainMapActivity);
            }else{
                //todo: error message
            }
        });

        register.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });

    }

}
