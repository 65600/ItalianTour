package it.unica.ium.italiantour;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User class representing entity in a database made from the Room persistence library.
 * */

@Entity(tableName = "login_user")
public class LoginUser {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    private String mUsername;

    @NonNull
    @ColumnInfo(name = "password")
    private String mPassword;

    //Constructor
    public LoginUser(String u, String p){
        this.mUsername = u;
        this.mPassword = p;
    }

    //Get statement
    public String getUsername(){
        return mUsername;
    }

    public String getPassword(){
        return mPassword;
    }
}
