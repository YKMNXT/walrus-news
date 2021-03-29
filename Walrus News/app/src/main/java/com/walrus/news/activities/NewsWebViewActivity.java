package com.walrus.news.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.walrus.news.R;
import com.walrus.news.databinding.ActivityNewsWebViewBinding;
import com.walrus.news.utility.AppConstants;
import com.walrus.news.utility.Utility;

public class NewsWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean called;
    private ActivityNewsWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_web_view);
        initUI();
        getDataFromIntent();
        setWebViewConfiguration();

    }

    private void initUI() {
        binding.closeBtn.setOnClickListener(this);
    }

    private void getDataFromIntent() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppConstants.EXTRA_URL_TO_LOAD) && bundle.containsKey(AppConstants.EXTRA_SCREEN_TITLE)) {
                String pageUrl = bundle.getString(AppConstants.EXTRA_URL_TO_LOAD);
                String pageTitle = bundle.getString(AppConstants.EXTRA_SCREEN_TITLE);
                if (pageUrl != null) {
                    binding.headerTitle.setText(pageTitle);
                    if (Utility.isNetworkOnline(this)) {
                        Utility.showProgressDialog("Please wait...", NewsWebViewActivity.this);
                        new Handler().postDelayed(() -> binding.genericWebview.loadUrl(pageUrl), 300);

                    } else {
                        Toast.makeText(this, getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void setWebViewConfiguration() {
        binding.genericWebview.getSettings().setJavaScriptEnabled(true);
        binding.genericWebview.setBackgroundColor(Color.TRANSPARENT);
        binding.genericWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        binding.genericWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.genericWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        binding.genericWebview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        binding.genericWebview.setLongClickable(false);
        called = false;
        binding.genericWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 99) {
                    Utility.dismissProgressDialog();
                    called = false;
                }
            }
        });
        binding.genericWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Utility.dismissProgressDialog();
                called = false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!called) {
                    called = true;
                }
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (error.getDescription() != null && request.getUrl() != null) {
                    onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
                } else {
                    onReceivedError(view, error.getErrorCode(), AppConstants.NETWORK_ERROR, AppConstants.EMPTY_STRING_WITH_SPACE);
                }
                Utility.dismissProgressDialog();
                called = false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.close_btn:
                onBackPressed();
        }
    }

}
