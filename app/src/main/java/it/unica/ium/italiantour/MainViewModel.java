package it.unica.ium.italiantour;

import android.app.Application;
import android.location.Location;

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
    private MutableLiveData<Location> currentLocation;
    private GPSTracker tracker;

    public MainViewModel(Application application){
        super(application);
        appRepo = new AppRepository(application);
        user = null;
        allMarkers = appRepo.getAllMarkers();
        currentLocation = new MutableLiveData<>();
        tracker = new GPSTracker(getApplication(), currentLocation);
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

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void restartTracker(){
        tracker = new GPSTracker(getApplication(), currentLocation);
    }
}
