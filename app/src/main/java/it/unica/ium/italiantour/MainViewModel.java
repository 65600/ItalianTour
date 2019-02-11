package it.unica.ium.italiantour;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class MainViewModel extends AndroidViewModel {
    private AppRepository appRepo;

    private LoginUser user;
    private MutableLiveData<Integer> filter;
    private LiveData<List<InterestMarker>> allMarkers;
    private LiveData<List<InterestMarker>> filteredMarkers;
    private LiveData<List<InterestMarker>> favourites;
    private LiveData<InterestMarker> selectedMarker;
    private MutableLiveData<Location> currentLocation;
    private GPSTracker tracker;

    public MainViewModel(Application application) {
        super(application);
        appRepo = new AppRepository(application);
        user = null;
        allMarkers = appRepo.getAllMarkers();
        currentLocation = new MutableLiveData<>();
        filter = new MutableLiveData<>();
        //Start with all values selected
        filter.setValue(63);
        tracker = new GPSTracker(getApplication(), currentLocation);
        filteredMarkers = Transformations.switchMap(filter, i ->{
            return appRepo.getFilteredMarkers(i);
        });
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
        if(null != user) {
            this.favourites = appRepo.getFavourites(user.getUsername());
        }else{
            this.favourites = null;
        }
    }

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void restartTracker(){
        tracker = new GPSTracker(getApplication(), currentLocation);
    }

    public LiveData<List<InterestMarker>> getFilteredMarkers() {
        return filteredMarkers;
    }

    public void newFilter(int categories){
        filter.setValue(categories);
    }

    public Integer getFilter(){
        return filter.getValue();
    }
}
