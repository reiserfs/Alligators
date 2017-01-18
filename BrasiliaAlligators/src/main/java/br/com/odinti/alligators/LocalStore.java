package br.com.odinti.alligators;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by thiago on 18/01/17.
 */

public class LocalStore {
    public static String seedValue = "9923021004050607BA090AAB0C0D0E0F";
    public static final String SP_NAME = "userDetails";
    SharedPreferences localDatabase;

    public LocalStore(Context context) {
        localDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeData(Login user){
        SharedPreferences.Editor spEditor = localDatabase.edit();
        try {
            spEditor.putString("user",CryptoHelper.encrypt(user.user,seedValue));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            spEditor.putString("password",CryptoHelper.encrypt(user.password,seedValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        spEditor.commit();
    }

    public String getUsername() {
        String user = localDatabase.getString("user","");
        return user;
    }

    public Login getLoggedIn() {
        String user = null;
        String password = null;
        try {
            user = CryptoHelper.decrypt(localDatabase.getString("user",""),seedValue);
        } catch (Exception e) {
            //e.printStackTrace();
            //Log.d("V",String.valueOf(e));
        }
        try {
            password = CryptoHelper.decrypt(localDatabase.getString("password",""),seedValue);
        } catch (Exception e) {
            //e.printStackTrace();
            //Log.d("V",String.valueOf(e));
        }

        Login storedLogin = new Login(user,password);

        return storedLogin;
    }

    public void clearData() {
        SharedPreferences.Editor spEditor = localDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
