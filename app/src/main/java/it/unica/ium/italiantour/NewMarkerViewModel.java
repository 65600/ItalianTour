package it.unica.ium.italiantour;

import android.app.Application;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NewMarkerViewModel extends AndroidViewModel {


    private MutableLiveData<LatLng> markerPos;

    private String markerName;
    private String desc;
    private String hours;
    private Uri imageUri;

    public NewMarkerViewModel(@NonNull Application application) {
        super(application);
        markerPos = new MutableLiveData<LatLng>();
        hours = desc = markerName = null;
    }

    public LiveData<LatLng> getMarkerPos() {
        return markerPos;
    }

    public void setMarkerPos(LatLng markerPos) {
        this.markerPos.setValue(markerPos);
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String name) {
        this.markerName = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }


    public void setAll(String name, String hours, String desc){
        setMarkerName(name);
        setDesc(desc);
        setHours(hours);
    }

    public void resetFields(){
        markerPos.setValue(null);
        hours = desc = markerName = null;
    }

}
