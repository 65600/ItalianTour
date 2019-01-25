package it.unica.ium.italiantour;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MarkerDao{

    @Insert
    void insert(InterestMarker marker);

    @Query("DELETE FROM marker")
    void deleteAllMarkers();

    @Query("SELECT * FROM marker")
    LiveData<List<InterestMarker>> getAllMarkers();

}
