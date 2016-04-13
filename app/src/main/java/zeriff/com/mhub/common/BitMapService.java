package zeriff.com.mhub.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SUJAN on 4/9/2016.
 */
public class BitMapService extends AsyncTask<Void, Void, Bitmap> {

    private String bitmapUrl;
    private IEventCallBack eventCallBack;

    public BitMapService(String url, IEventCallBack callBack) {
        bitmapUrl = url;
        eventCallBack = callBack;
    }


    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(bitmapUrl);
            InputStream stream = url.openConnection().getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        eventCallBack.OnSuccess(bitmap);
        super.onPostExecute(bitmap);
    }
}
