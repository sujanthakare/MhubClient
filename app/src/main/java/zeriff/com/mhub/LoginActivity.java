package zeriff.com.mhub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zeriff.com.mhub.common.ApiService;
import zeriff.com.mhub.common.AppData;
import zeriff.com.mhub.common.AppSession;
import zeriff.com.mhub.common.Constants;
import zeriff.com.mhub.common.IEventCallBack;
import zeriff.com.mhub.common.KeyValueModel;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private Button signInButton;
    private EditText email;
    private EditText password;
    private Context applicationContext;
    private AppSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        bindListeners();
        applicationContext = getApplicationContext();
        session = new AppSession(applicationContext);
    }

    private void bindListeners() {
        signInButton = (Button) findViewById(R.id.signin);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                email = (EditText) findViewById(R.id.email);
                password = (EditText) findViewById(R.id.password);


                ApiService service = new ApiService("POST",
                        Constants.LOCAL_BASE_API_URL + Constants.AUTHENTICATE,
                        new IEventCallBack() {
                            @Override
                            public void OnSuccess(Object obj) {
                                Log.d("TAG", "successful");
                                String result = obj.toString();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(result);
                                    Boolean sucess = jsonObject.getBoolean("success");
                                    if(sucess){
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnFailure(Object obj) {

                            }
                        });

                ArrayList<KeyValueModel> data = new ArrayList<KeyValueModel>();
                data.add(new KeyValueModel("email", email.getText().toString()));
                data.add(new KeyValueModel("password", password.getText().toString()));
                service.setParams(data);
                service.execute();
            }
        });
    }
}