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

    private static class insertUserAsyncTask extends AsyncTask<LoginUser, Void, Void> {
        private LoginDao mAsyncTaskDao;

        insertUserAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final LoginUser... params) {
            mAsyncTaskDao.insert(params[0]);
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
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
