package it.unica.ium.italiantour;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button continueButton = getActivity().findViewById(R.id.registerRegistratiButton);

        continueButton.setOnClickListener(v -> {
            //todo: registration checks
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_pop);
        });
    }

}
