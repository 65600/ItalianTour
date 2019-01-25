package it.unica.ium.italiantour;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class LoginViewModel extends AndroidViewModel {

    private AppRepository appRepo;
    private LiveData<List<LoginUser>> mAllCreds;

    public LoginViewModel(Application application){
        super(application);
        appRepo = new AppRepository(application);
        mAllCreds = appRepo.getAllCreds();
    }

    LiveData<List<LoginUser>> getAllCreds() { return mAllCreds; }

    public void insert(LoginUser newUser) { appRepo.insertUser(newUser); }

}
