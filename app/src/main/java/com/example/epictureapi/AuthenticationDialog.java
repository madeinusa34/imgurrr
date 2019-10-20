package com.example.epictureapi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import static com.example.epictureapi.test.accessToken;


public class AuthenticationDialog extends Dialog {
    private final String request_url;
    private final String redirect_url;
    public  AuthenticationListener listener;
    public static String accessToken;
    private Context context;

    public AuthenticationDialog(Context context, AuthenticationListener listener) {
        super(context);
        this.listener = listener;
        this.context = context;
        this.redirect_url = context.getResources().getString(R.string.redirect_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth2/authorize?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&response_type=token&state=state";
    }

    //Intent.ACTION_VIEW, Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=480cdf2c81306bc&response_type=token&state=state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.onlog_view);
        initializeWebView();
    }

    private void initializeWebView() {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("ALLELUIA JU CA MARCHE!");
            if (url.startsWith(redirect_url)) {
                AuthenticationDialog.this.dismiss();
                Intent redir = new Intent(context, Menu1.class);
                   context.startActivity(redir);
                return true;
            }
            return false;
        }

        @Override

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.contains("access_token=")) {
                Uri uri = Uri.EMPTY.parse(url);
                accessToken = uri.getEncodedFragment();
                Log.e("all_url", accessToken);
                accessToken = accessToken.substring(accessToken.indexOf("=") + 1);
                accessToken = accessToken.substring(0, accessToken.indexOf("&"));
                Log.e("access_token", accessToken);

                //listener.onTokenReceived(access_token);
                dismiss();
            } else if (url.contains("?error")) {
                Log.e("access_token", "il semble qu'il ne s'agisse pas de votre access token");
                dismiss();
            }
        }

    };

    public static String getAccess_token(String access_token)
    {
        return access_token;
    }

  //  public String getValeur(String access_token) {
    //    return access_token;
   // }
}
