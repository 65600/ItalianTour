package it.unica.ium.italiantour;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Database created with the Room library.
 * Contains tables login_user and marker.
 */

@Database(entities = {LoginUser.class, InterestMarker.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LoginDao loginDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
