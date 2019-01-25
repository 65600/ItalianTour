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
    private int id;

    public InterestMarker(@NonNull String name, String orari, String desc, LatLng coords) {
        this.name = name;
        this.orari = orari;
        this.desc = desc;
        this.coords = coords;
    }

    @NonNull
    private String name;

    private String orari;

    private String desc;

    private LatLng coords;


    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getOrari() {
        return orari;
    }

    public String getDesc() {
        return desc;
    }

    public LatLng getCoords() {
        return coords;
    }
}
