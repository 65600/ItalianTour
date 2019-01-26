package it.unica.ium.italiantour;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private AppRepository appRepo;

    private LoginUser user;
    private LiveData<List<InterestMarker>> allMarkers;
    private LiveData<List<InterestMarker>> favourites;
    private LiveData<InterestMarker> selectedMarker;

    public MainViewModel(Application application){
        super(application);
        appRepo = new AppRepository(application);
        user = null;
        allMarkers = appRepo.getAllMarkers();
        //todo: all other livedata elements from queries
    }

    public LiveData<List<InterestMarker>> getAllMarkers() {
        return allMarkers;
    }

    public LiveData<List<InterestMarker>> getFavourites() {
        return favourites;
    }

    public LiveData<InterestMarker> getSelectedMarker() {
        return selectedMarker;
    }

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }
}
