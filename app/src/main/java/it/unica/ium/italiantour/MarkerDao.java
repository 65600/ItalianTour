package it.unica.ium.italiantour;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MarkerDao{

    @Insert
    void insertMarker(InterestMarker marker);

    @Insert
    void insertFavourite(Favourite favourite);

    @Query("DELETE FROM marker")
    void deleteAllMarkers();

    @Query("DELETE FROM favourite WHERE user LIKE :user")
    void deleteAllFavourites(String user);

    @Query("DELETE FROM favourite WHERE user LIKE :user AND markerId = :mID")
    void deleteFavourite(String user, Integer mID);

    @Query("SELECT * FROM marker WHERE id = :mID")
    LiveData<InterestMarker> getMarkerByID(Integer mID);

    @Query("SELECT * FROM marker")
    LiveData<List<InterestMarker>> getAllMarkers();

    @Query("SELECT * FROM marker WHERE id in (SELECT markerId FROM favourite WHERE user LIKE :user )")
    LiveData<List<InterestMarker>> getFavourites(String user);

    @Query("SELECT * FROM marker WHERE categories & :categories != 0")
    LiveData<List<InterestMarker>> newFilterQuery(Integer categories);

}
