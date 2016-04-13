package zeriff.com.mhub.common;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by SUJAN on 4/1/2016.
 */
public class ApiService extends AsyncTask<Void, Void, String> {

    private String serviceUrl;
    private IEventCallBack eventCallBack;
    private String requestype;
    private ArrayList<KeyValueModel> params;

    public ApiService(String type, String url, IEventCallBack callBack) {
        serviceUrl = url;
        eventCallBack = callBack;
        requestype = type;
    }

    public void setParams(ArrayList<KeyValueModel> e) {
        params = e;
    }

    private void appendParams(HttpURLConnection conn) {
        if (params != null && params.size() != 0) {
            for (KeyValueModel model : params) {
                if (requestype == "GET") {
                    conn.setRequestProperty(model.Key, model.Value);
                } else if(requestype == "POST"){
                    conn.addRequestProperty(model.Key, model.Value);
                }

            }
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        try {
            URL url = new URL(serviceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestype);
            conn.setRequestProperty("Accept", "application/json");
            appendParams(conn);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                result = output;
                Log.d("TAG", output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        eventCallBack.OnSuccess(s);
    }
}
