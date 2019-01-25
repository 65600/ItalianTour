package it.unica.ium.italiantour;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private LoginViewModel mViewModel;

    public static RegisterFragment newInstance(String param1, String param2) {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        Button continueButton = view.findViewById(R.id.registerRegistratiButton);
        EditText mail = view.findViewById(R.id.registerEmailField);
        EditText user = view.findViewById(R.id.registerUsernameField);
        EditText pass = view.findViewById(R.id.registerPasswordField);

        continueButton.setOnClickListener(v -> {
            //todo: registration checks
            LoginUser newUser = new LoginUser(user.getText().toString(), pass.getText().toString(), mail.getText().toString());
            mViewModel.insertUser(newUser);
            //todo: Visible message about insertion.
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_pop);
        });
    }

}
