package com.mrzhang.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.mrzhang.imageloader.ImageLoader;
import com.mrzhang.imageloader.R;
import com.mrzhang.imageloader.cache.DiskCache;

public class MainActivity extends AppCompatActivity {

    AppCompatImageView mImgLoader;
    AppCompatButton mBtnDisplay, mBtnClear;
    AppCompatEditText mEvUrl;
    String mGetUrl;
    String nativeUrl = "https://i1.go2yd.com/image.php?url=0JkUDu6Qar";
    ImageLoader mImageLoader;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initEvent();
    }

    private void initView() {
        mImgLoader = findViewById(R.id.image_loader);
        mBtnDisplay = findViewById(R.id.btn_display);
        mBtnClear = findViewById(R.id.btn_clear);
        mEvUrl = findViewById(R.id.tv_url);
    }

    private void initListener() {
        mImageLoader=new ImageLoader();
        mImageLoader.setImageCache(new DiskCache());

        mBtnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEvUrl.getText() == null) {
                    Toast.makeText(MainActivity.this, "Url为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mGetUrl = mEvUrl.getText().toString().trim();
                if (mGetUrl.isEmpty()) {
                    Toast.makeText(MainActivity.this, "显示默认Url", Toast.LENGTH_SHORT).show();
                    // loader.displayImage(url, mImgLoader);
                    mImageLoader.displayImage(nativeUrl, mImgLoader);
                } else {
                    Toast.makeText(MainActivity.this, "显示填充Url", Toast.LENGTH_SHORT).show();
                    // loader.displayImage(url, mImgLoader);
                    mImageLoader.displayImage(mGetUrl, mImgLoader);
                }
            }
        });

        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEvUrl.setText("");
            }
        });
    }

    private void initEvent() {
    }

}
