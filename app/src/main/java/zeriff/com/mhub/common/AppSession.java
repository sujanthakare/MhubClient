package zeriff.com.mhub.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SUJAN on 4/5/2016.
 */
public class AppSession {

    private SharedPreferences session;
    private SharedPreferences.Editor editor;
    private Context appContext;


    private static String APP_SESSION = "app_session";

    public AppSession(Context ctx) {
        appContext = ctx;
        session = appContext.getSharedPreferences(APP_SESSION, appContext.MODE_PRIVATE);
        editor = session.edit();
    }

    public void storeAccessToken(String Id, String Full_Name, String Profile_Picture, String token) {
        editor.putString("Id", Id);
        editor.putString("Profile_Picture", Profile_Picture);
        editor.putString("Full_Name", Full_Name);
        editor.putString("Access_token", token);
        editor.apply();
    }

    public void setToken(String token) {
        editor.putString("Access_token", token);
        editor.apply();
    }

    public String getToken() {
        return session.getString("Access_token", null);
    }

    public String getUserId() {
        return session.getString("Id", null);
        //return "57040c09c77dd0080dc3c576";
    }

    public String getProfilePicture() {
        return session.getString("Profile_Picture", "");
    }
}
