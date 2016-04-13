package zeriff.com.mhub.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by SUJAN on 4/1/2016.
 */
public class AuthDialog extends Dialog {

    private ProgressDialog spinner;
    private IEventCallBack eventCallBack;
    private String webViewUrl;

    public AuthDialog(Context context) {
        super(context);
        spinner = buildSpinner("Loading...");
    }

    public void setEventCallBack(IEventCallBack e) {
        eventCallBack = e;
    }

    public void setWebViewUrl(String url) {
        webViewUrl = url;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getWebView());
        setLayoutParams();
    }

    private WebView getWebView() {
        WebView webView = new WebView(getContext());
        webView.loadUrl(webViewUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                spinner.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                spinner.dismiss();
                Uri uri = Uri.parse(url);
                if (Constants.HOST.equals(uri.getHost()) && uri.getQueryParameter("state") != null) {
                    AuthDialog.this.hide();
                    spinner.show();
                    ApiService service = new ApiService("GET", url, new IEventCallBack() {
                        @Override
                        public void OnSuccess(Object obj) {
                            eventCallBack.OnSuccess(obj);
                            spinner.dismiss();
                            AuthDialog.this.dismiss();
                        }

                        @Override
                        public void OnFailure(Object obj) {
                            spinner.dismiss();
                        }
                    });
                    ArrayList<KeyValueModel> a = new ArrayList<KeyValueModel>();
                    KeyValueModel model = new KeyValueModel("callback", "true");
                    a.add(model);
                    service.setParams(a);
                    service.execute();
                }
                super.onPageFinished(view, url);
            }
        });
        return webView;
    }

    private void setLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(this.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);
    }

    private ProgressDialog buildSpinner(CharSequence message) {
        ProgressDialog spinner = new ProgressDialog(getContext());
        spinner.setMessage(message);
        spinner.setCancelable(false);
        return spinner;
    }
}
