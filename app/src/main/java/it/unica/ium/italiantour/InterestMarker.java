package it.unica.ium.italiantour;


import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Marker class stored with the Room database.
 */

@Entity(tableName = "marker")
public class InterestMarker {

    //todo: add enum for marker type

    @PrimaryKey(autoGenerate = true)
    Integer id;

    @NonNull
    private String name;

    private String creator;

    private String orari;

    private String desc;

    private Double lat;
    private Double lon;

    public InterestMarker(@NonNull String name, String creator, String orari, String desc, Double lat, Double lon) {
        this.name = name;
        this.creator = creator;
        this.orari = orari;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
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
}
