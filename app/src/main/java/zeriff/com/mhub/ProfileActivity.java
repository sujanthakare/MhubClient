package zeriff.com.mhub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import zeriff.com.mhub.common.ApiService;
import zeriff.com.mhub.common.AppSession;
import zeriff.com.mhub.common.BitMapService;
import zeriff.com.mhub.common.Constants;
import zeriff.com.mhub.common.IEventCallBack;
import zeriff.com.mhub.common.KeyValueModel;

public class ProfileActivity extends AppCompatActivity {

    private Context currentContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_template);
        setContentView(R.layout.activity_profile);
        currentContext = this;
        setUserData();
        setListData();

    }

    private void setListData() {

        final ListView lstView = (ListView) findViewById(R.id.feedlist);
        AppSession session = new AppSession(getApplicationContext());
        ApiService service = new ApiService("GET", Constants.LOCAL_BASE_API_URL + "getpopular", new IEventCallBack() {
            @Override
            public void OnSuccess(Object obj) {
                try {
                    JSONArray array = new JSONArray(obj.toString());
                    CustomAdapter adapter = new CustomAdapter(currentContext,array);
                    lstView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFailure(Object obj) {

            }
        });

        ArrayList<KeyValueModel> lst = new ArrayList<>();
        lst.add(new KeyValueModel("x-access-token",session.getToken()));
        lst.add(new KeyValueModel("userId", session.getUserId()));
        service.setParams(lst);
        service.execute();
    }

    private void setUserData() {

        ApiService service = new ApiService("GET", Constants.LOCAL_BASE_API_URL + "getUser", new IEventCallBack() {
            @Override
            public void OnSuccess(Object obj) {
                try {
                    JSONObject user = new JSONObject(obj.toString()).getJSONObject("user");
                    TextView textView = (TextView) findViewById(R.id.username);
                    textView.setText(user.getString("Full_Name"));
                    String bitmapUrl = user.getString("Profile_Picture");
                    BitMapService bitMapService = new BitMapService(bitmapUrl, new IEventCallBack() {
                        @Override
                        public void OnSuccess(Object obj) {
                            ImageView imageView = (ImageView) findViewById(R.id.profile_picture);
                            Bitmap bitmap = (Bitmap) obj;
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void OnFailure(Object obj) {

                        }
                    });
                    bitMapService.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void OnFailure(Object obj) {

            }
        });
        AppSession session = new AppSession(getApplicationContext());
        String userId = session.getUserId();
        KeyValueModel model = new KeyValueModel("userId", userId);
        ArrayList<KeyValueModel> lst = new ArrayList<KeyValueModel>();
        lst.add(model);
        service.setParams(lst);
        service.execute();
    }
}

class CustomAdapter extends BaseAdapter {

    private JSONArray data;
    private Context context;

    CustomAdapter(Context c, JSONArray d) {
        data = d;
        context = c;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        Object result = null;
        try {
            result = data.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        try {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item, parent, false);

            final ImageView imageView = (ImageView) row.findViewById(R.id.feeditem);
            JSONObject obj = (JSONObject) data.get(position);
            String imageUrl = obj.getJSONObject("images").getJSONObject("low_resolution").getString("url");

            BitMapService service = new BitMapService(imageUrl, new IEventCallBack() {
                @Override
                public void OnSuccess(Object obj) {
                    imageView.setImageBitmap((Bitmap)obj);
                }

                @Override
                public void OnFailure(Object obj) {

                }
            });
            service.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return row;
    }
}
