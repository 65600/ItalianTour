package it.unica.ium.italiantour;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LoginDao extends Dao {

    @Insert
    void insert(LoginUser lu);

    @Query("DELETE FROM login_user")
    void deleteAllUsers();

    @Query("SELECT * FROM login_user WHERE username = :username")
    LiveData<LoginUser> getCredentials(String username);

    @Query("SELECT * FROM login_user")
    LiveData<List<LoginUser>> loadAllCredentials();

}
