package it.unica.ium.italiantour;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NewMarkerViewModel extends AndroidViewModel {


    private MutableLiveData<LatLng> markerPos;

    public NewMarkerViewModel(@NonNull Application application) {
        super(application);
        markerPos = new MutableLiveData<LatLng>();
    }

    public LiveData<LatLng> getMarkerPos() {
        return markerPos;
    }

    public void setMarkerPos(LatLng markerPos) {
        this.markerPos.setValue(markerPos);
    }
}
