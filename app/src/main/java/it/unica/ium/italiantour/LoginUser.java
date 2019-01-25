package it.unica.ium.italiantour;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User class representing entity in a database made from the Room persistence library.
 * */

@Entity(tableName = "login_user")
public class LoginUser {

    @PrimaryKey
    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String email;

    //Constructor
    public LoginUser(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Get statements

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }
}
