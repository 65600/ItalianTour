package it.unica.ium.italiantour;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private LoginViewModel mViewModel;
    private View layout;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        layout = view.findViewById(R.id.registerPanelLayout);
        AppCompatImageButton imageBackButton = view.findViewById(R.id.image_back_button);
        Button continueButton = view.findViewById(R.id.registerRegistratiButton);
        EditText mail = view.findViewById(R.id.registerEmailField);
        EditText user = view.findViewById(R.id.registerUsernameField);
        EditText pass = view.findViewById(R.id.registerPasswordField);

        continueButton.setOnClickListener(v -> {
            String name = user.getText().toString();
            String pw = pass.getText().toString();
            String e = mail.getText().toString();
            //Current registration checks: non-empty everything, username not taken, one @ in the email field.
            if(name.length() > 0 && !mViewModel.usernameTaken(name) && pw.length() > 0 && (e.length() > 0 && e.indexOf('@') > -1 ) ){
                LoginUser newUser = new LoginUser(name, pw, e);
                mViewModel.insertUser(newUser);
                Snackbar.make(layout, "Utente creato con successo", Snackbar.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_pop);
            }else{
                Snackbar.make(layout, "Errore nella creazione dell'utente. Verificare i dati inseriti.", Snackbar.LENGTH_SHORT).show();
                if(name.length() > 0 && mViewModel.usernameTaken(name)){
                    user.setError("Nome utente già presente");
                }else{
                    user.setError("Nome utente vuoto");
                }
                if(pw.length() < 1){
                    pass.setError("Password vuota");
                }
                if(!(e.length() > 0 && e.indexOf('@') > -1 )){
                    mail.setError("Indirizzo email non valido.");
                }

            }
        });

        imageBackButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_pop);
        });
    }

}
