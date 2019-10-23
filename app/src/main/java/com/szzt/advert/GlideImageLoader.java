package com.szzt.advert;


import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    private View.OnClickListener mOnClickListener;
    public GlideImageLoader(View.OnClickListener onClickListener){
        mOnClickListener = onClickListener;
    }

    @Override
    public View createImageView(Context context) {
        return super.createImageView(context);
    }

    @Override
    public void displayImage(Context context, Object path, View imageView) {
        if(imageView instanceof WebView){
            WebView webView = (WebView)imageView;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl((String) path);
            return;
        }
        if(imageView != null && mOnClickListener != null){
            imageView.setOnClickListener(mOnClickListener);
        }
        Glide.with(context).load(path).into((ImageView) imageView);
}

}



