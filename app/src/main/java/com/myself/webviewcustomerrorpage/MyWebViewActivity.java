package com.myself.webviewcustomerrorpage;
/***
 * 时间:2017年 3月13日
 * 功能:主要处理 webview在加载网页和加载网页失败时的过程
 * 当 webview在请求网页的这段时间:让它显示一个自定义的页面
 * 当 webview请求失败时:让它显示自定义的网页
 * 看了如果还是不懂就加我QQ:917641472
 */

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class MyWebViewActivity extends AppCompatActivity {
    private String url = "http://blog.csdn.net/github_35033182";

    private WebView webview;
    private View mErrorView;
    private LinearLayout loading_over;

    private WebSettings mWebSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        webview = (WebView) findViewById(R.id.webview);
        loading_over = (LinearLayout) findViewById(R.id.loading_over);

        setUpView();
    }

    private void setUpView() {
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        mWebSettings = webview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);    //允许加载javascript
        mWebSettings.setSupportZoom(true);          //允许缩放
        mWebSettings.setBuiltInZoomControls(true);  //原网页基础上缩放
        mWebSettings.setUseWideViewPort(true);      //任意比例缩放
        webview.setWebViewClient(mWebViewClient);   //设置Web视图
        webview.setWebChromeClient(mWebChromeClient);
    }

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        private ProgressBar mBar;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.e("####", "onProgressChanged: --" + newProgress);

            ViewGroup mParent = (ViewGroup) view.getParent();
            if (mBar == null) {
                mBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_B57DE4)));
                }
                ViewGroup.LayoutParams lp0 = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        5);
                ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                mParent.removeView(view);
                mParent.addView(mBar, 0, lp0);
                mParent.addView(view, 1, lp1);
            }

            if (newProgress == 100) {
                mBar.setVisibility(View.GONE);
            } else {
                mBar.setProgress(newProgress);
            }
        }
    };

    /***
     * 设置Web视图的方法
     */
    WebViewClient mWebViewClient = new WebViewClient() {
        //处理网页加载失败时
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            showErrorPage();//显示错误页面
        }

        public void onPageFinished(WebView view, String url) {//处理网页加载成功时
            loading_over.setVisibility(View.GONE);
        }
    };
    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    boolean mIsErrorPage;

    protected void showErrorPage() {
        LinearLayout webParentView = (LinearLayout) webview.getParent();


        initErrorPage();//初始化自定义页面
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        webParentView.addView(mErrorView, 0, lp);
        mIsErrorPage = true;
    }

    /***
     * 显示加载失败时自定义的网页
     */
    protected void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.layout_url_error, null);
            RelativeLayout button = (RelativeLayout) mErrorView.findViewById(R.id.online_error_btn_retry);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    webview.reload();
                }
            });
            mErrorView.setOnClickListener(null);
        }
    }
}
