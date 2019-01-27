package it.unica.ium.italiantour;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import androidx.lifecycle.LiveData;

/**
 * Repository from which the Viewmodel data is retrieved.
 * The repository retrieves data from the appropriate DAO.
 */

public class AppRepository {

    private LoginDao mLogindDao;
    private MarkerDao mMarkerDao;
    private LiveData<List<LoginUser>> mAllCreds;
    private LiveData<List<InterestMarker>> mAllMarkers;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mLogindDao = db.loginDao();
        mMarkerDao = db.markerDao();
        mAllCreds = mLogindDao.loadAllCredentials();
        mAllMarkers = mMarkerDao.getAllMarkers();
    }

    LiveData<List<LoginUser>> getAllCreds() {
        return mAllCreds;
    }

    LiveData<List<InterestMarker>> getAllMarkers() {
        return mAllMarkers;
    }


    public void insertUser (LoginUser loginUser) {
        new insertUserAsyncTask(mLogindDao).execute(loginUser);
    }

    public void insertFavourite (Favourite favourite) {
        new insertFavouriteAsyncTask(mMarkerDao).execute(favourite);
    }


    public LoginUser validateCredentials(LoginUser creds){
            AsyncTask task = new validateUserAsyncTask(mLogindDao).execute(creds);
            try{
                return (LoginUser)task.get();
            }catch(Exception e){
                Log.e(this.toString(), e.toString());
                return null;
            }
    }

    public void insertMarker (InterestMarker marker){
        new insertMarkerAsyncTask(mMarkerDao).execute(marker);
    }

    public LiveData<InterestMarker> getMarkerByID(Integer mID){
        GetMarkerByIDAsyncTask task = new GetMarkerByIDAsyncTask(mMarkerDao);
        try{
            return task.execute(mID).get();
        }catch(Exception e){
            return null;
        }
    }

    public LiveData<List<InterestMarker>> getFavourites(String user) {
        GetFavouritesAsyncTask task = new GetFavouritesAsyncTask(mMarkerDao);
        try{
            return task.execute(user).get();
        }catch(Exception e){
            return null;
        }
    }

    public void removeFavourite(String user, Integer markerID){

        new RemoveFavouriteAsyncTask(mMarkerDao).execute(user, markerID.toString());
    }










    //AsyncTasks

    private static class insertUserAsyncTask extends AsyncTask<LoginUser, Void, Void> {
        private LoginDao mAsyncTaskDao;

        insertUserAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final LoginUser... params) {
            try{
                mAsyncTaskDao.insert(params[0]);
            }catch(Exception e){
                Log.d("user", e.getMessage());
            }
            return null;
        }
    }

    private static class validateUserAsyncTask extends AsyncTask<LoginUser, Void, LoginUser> {
        private LoginDao mAsyncTaskDao;

        validateUserAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LoginUser doInBackground(final LoginUser... params) {
            LoginUser u = params[0];
            String username = u.getUsername();
            String password = u.getPassword();
            LoginUser registered = mAsyncTaskDao.getCredentials(username);
            if(registered != null && registered.getPassword() != null &&  registered.getPassword().equals(password)){
                return registered;
            }else{
                return null;
            }
        }
    }

    private static class insertMarkerAsyncTask extends AsyncTask<InterestMarker, Void, Void>{
        private MarkerDao mAsyncTaskDao;

        insertMarkerAsyncTask(MarkerDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InterestMarker... params) {
            try{
                mAsyncTaskDao.insertMarker(params[0]);
            }catch(Exception e){
                Log.d("marker", e.getMessage());
            }
            return null;
        }
    }

    private static class insertFavouriteAsyncTask extends AsyncTask<Favourite, Void, Void>{
        private MarkerDao mAsyncTaskDao;

        insertFavouriteAsyncTask(MarkerDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favourite... params) {
            try{
                mAsyncTaskDao.insertFavourite(params[0]);
            }catch(Exception e){
                Log.d("favourite", e.getMessage());
            }
            return null;
        }
    }

    private static class GetMarkerByIDAsyncTask extends AsyncTask<Integer, Void, LiveData<InterestMarker>> {
        private MarkerDao mAsyncTaskDao;

        GetMarkerByIDAsyncTask(MarkerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<InterestMarker> doInBackground(final Integer... params) {
            return mAsyncTaskDao.getMarkerByID(params[0]);
        }
    }

    private static class GetFavouritesAsyncTask extends AsyncTask<String, Void, LiveData<List<InterestMarker>>> {
        private MarkerDao mAsyncTaskDao;

        GetFavouritesAsyncTask(MarkerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<InterestMarker>> doInBackground(final String... params) {
            return mAsyncTaskDao.getFavourites(params[0]);
        }
    }

    private static class RemoveFavouriteAsyncTask extends AsyncTask<String, Void, Void>{
        private MarkerDao mAsyncTaskDao;

        RemoveFavouriteAsyncTask(MarkerDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            try{
                mAsyncTaskDao.deleteFavourite(params[0], Integer.valueOf(params[1]));
            }catch(Exception e){
                Log.d("favourite removal", e.getMessage());
            }
            return null;
        }
    }
}
