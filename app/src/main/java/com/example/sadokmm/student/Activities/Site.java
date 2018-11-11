package com.example.sadokmm.student.Activities;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.sadokmm.student.R;

public class Site extends AppCompatActivity {


    private Toolbar toolbar;
    private WebView siteView;
    private String url,titre;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        siteView = (WebView) findViewById(R.id.siteView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getIntent().getExtras().getString("url");
        titre = getIntent().getExtras().getString("titre");

        getSupportActionBar().setTitle(titre);

        siteView.setWebViewClient(new Browser());
        siteView.getSettings().setLoadsImagesAutomatically(true);
        siteView.getSettings().setJavaScriptEnabled(true);
        siteView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        siteView.loadUrl(url);
        siteView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Téléchargement de fichier", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });


    }


    public class Browser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            getSupportActionBar().setTitle("Site de l'ISG");
            return true;
        }
    }


}
