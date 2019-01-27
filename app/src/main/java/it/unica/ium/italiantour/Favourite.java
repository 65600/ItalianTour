package it.unica.ium.italiantour;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "favourite", primaryKeys = {"user", "markerId"})
public class Favourite {

    @NonNull
    @ForeignKey(entity = LoginUser.class, parentColumns = "username", childColumns = "user")
    private String user;

    @NonNull
    @ForeignKey(entity = InterestMarker.class, parentColumns = "id", childColumns = "markerId")
    private Integer markerId;

    public Favourite(String user, Integer markerId) {
        this.user = user;
        this.markerId = markerId;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    @NonNull
    public Integer getMarkerId() {
        return markerId;
    }
}
