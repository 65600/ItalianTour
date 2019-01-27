package it.unica.ium.italiantour;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public void insertMarker(InterestMarker marker){ appRepo.insertMarker(marker);}

    public void insertFavourite(Integer markerID){
        Favourite favourite = new Favourite(user.getUsername(), markerID);
        appRepo.insertFavourite(favourite);}

    public void removeFavourite(Integer markerId){
        appRepo.removeFavourite(user.getUsername(), markerId);
    }

    public LiveData<List<InterestMarker>> getAllMarkers() {
        return allMarkers;
    }

    public LiveData<List<InterestMarker>> getFavourites() {
        return favourites;
    }

    public void setSelectedMarker(Integer id){
        selectedMarker = appRepo.getMarkerByID(id);
    }

    public LiveData<InterestMarker> getSelectedMarker() {
        return selectedMarker;
    }

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
        this.favourites = appRepo.getFavourites(user.getUsername());
    }
}
