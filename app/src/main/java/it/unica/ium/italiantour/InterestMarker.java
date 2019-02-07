package it.unica.ium.italiantour;


import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Marker class stored with the Room database.
 */

@Entity(tableName = "marker")
public class InterestMarker {

    final static int MONUMENTI = 1;
    final static int RISTORANTI = 2;
    final static int ARTE = 4;
    final static int SPORT = 8;
    final static int SVAGO = 16;
    final static int NATURA = 32;

    @PrimaryKey(autoGenerate = true)
    Integer id;

    @NonNull
    private String name;

    private String creator;

    private String orari;

    private String desc;

    private Double lat;
    private Double lon;
    private String photoUri;
    private Integer categories;


    public InterestMarker(@NonNull String name, String creator, String orari, String desc, Double lat, Double lon, String photoUri, Integer categories) {
        this.name = name;
        this.creator = creator;
        this.orari = orari;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
        this.photoUri = photoUri;
        this.categories = categories;
    }

    public InterestMarker(@NonNull String name, String creator, String orari, String desc, Double lat, Double lon, Uri photoUri, Integer categories) {
        this.name = name;
        this.creator = creator;
        this.orari = orari;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
        this.photoUri = photoUri.toString();
        this.categories = categories;
    }

    public InterestMarker(@NonNull String name, String creator, String orari, String desc, LatLng coords, Uri photoUri, Integer categories ) {
        this.name = name;
        this.creator = creator;
        this.orari = orari;
        this.desc = desc;
        this.lat = coords.latitude;
        this.lon = coords.longitude;
        this.photoUri = photoUri.toString();
        this.categories = categories;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @NonNull
    public String getName() {
        return name;
    }

    public String getCreator() { return creator; }

    public String getOrari() {
        return orari;
    }

    public String getDesc() {
        return desc;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }


    public Uri getPhotoUriParsed() {
        return Uri.parse(photoUri);
    }

    public String getPhotoUri() {
        return  photoUri;
    }

    public Integer getCategories() {
        return categories;
    }
}
