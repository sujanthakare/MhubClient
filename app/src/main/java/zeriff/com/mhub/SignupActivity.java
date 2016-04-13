package zeriff.com.mhub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import zeriff.com.mhub.common.ApiService;
import zeriff.com.mhub.common.AppData;
import zeriff.com.mhub.common.AppSession;
import zeriff.com.mhub.common.AuthDialog;
import zeriff.com.mhub.common.Constants;
import zeriff.com.mhub.common.IEventCallBack;

public class SignupActivity extends AppCompatActivity {

    private ImageView instagramButton;
    private ImageView facebookButton;
    private Context currentContext;
    private AppSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new AppSession(this);
        setContentView(R.layout.activity_signup);
        currentContext = this;
        bindButtonEvents();
    }


    private void bindButtonEvents() {
        instagramButton = (ImageView) findViewById(R.id.instagram);
        facebookButton = (ImageView) findViewById(R.id.facebook);

        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this, "Signup with instagram", Toast.LENGTH_SHORT).show();
                signupWithInstagram();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this, "Signup with facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signupWithInstagram() {
        final AuthDialog instaAuthDialog = new AuthDialog(currentContext);
        instaAuthDialog.setWebViewUrl(Constants.LOCAL_BASE_API_URL + Constants.AUTHORIZE_USER);
        instaAuthDialog.setEventCallBack(new IEventCallBack() {
            @Override
            public void OnSuccess(Object obj) {
                Log.d("Tag", obj.toString());
                try {
                    JSONObject result = new JSONObject(obj.toString());
                    if (result.getBoolean("success")) {
                        JSONObject user = result.getJSONObject("user");
                        String id = user.getString("userid");
                        String fullname = user.getString("full_name");
                        String dp = user.getString("profile_picture");
                        session.storeAccessToken(id, fullname, dp, "");
                        Intent intent = new Intent(currentContext, GetEmailAndPasswordActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailure(Object obj) {

            }
        });
        instaAuthDialog.show();
    }
}
