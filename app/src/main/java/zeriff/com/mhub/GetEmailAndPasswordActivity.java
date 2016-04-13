package zeriff.com.mhub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import zeriff.com.mhub.common.ApiService;
import zeriff.com.mhub.common.AppSession;
import zeriff.com.mhub.common.Constants;
import zeriff.com.mhub.common.IEventCallBack;
import zeriff.com.mhub.common.KeyValueModel;

public class GetEmailAndPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_email_and_password);
        Button btn = (Button) findViewById(R.id.setuprofile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpProfile();
            }
        });
    }

    private void setUpProfile() {
        TextView email = (TextView) findViewById(R.id.email_profile);
        TextView password = (TextView) findViewById(R.id.password_profile);

        final AppSession session = new AppSession(getApplicationContext());
        String id = session.getUserId();

        ApiService service = new ApiService("GET", Constants.LOCAL_BASE_API_URL + "adddetails", new IEventCallBack() {
            @Override
            public void OnSuccess(Object obj) {
                try {
                    JSONObject result = new JSONObject(obj.toString());
                    if (result.getBoolean("success")) {
                        session.setToken(result.getString("token"));
                        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailure(Object obj) {

            }
        });
        ArrayList<KeyValueModel> lst = new ArrayList<>();
        lst.add(new KeyValueModel("email", email.getText().toString()));
        lst.add(new KeyValueModel("password", password.getText().toString()));
        lst.add(new KeyValueModel("userid", id));
        service.setParams(lst);
        service.execute();

    }
}
