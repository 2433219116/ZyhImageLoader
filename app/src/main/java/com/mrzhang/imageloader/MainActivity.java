package com.mrzhang.imageloader;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    AppCompatImageView mImgLoader;
    AppCompatButton mBtnDisplay, mBtnClear;
    AppCompatEditText mEvUrl;
    String mGetUrl;
    String nativeUrl = "https://i1.go2yd.com/image.php?url=0JkUDu6Qar";

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
                    ImageLoader loader = new ImageLoader();
                    // loader.displayImage(url, mImgLoader);
                    loader.displayImage(nativeUrl, mImgLoader);
                } else {
                    try {
                        new URL(mGetUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Url error", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(MainActivity.this, "显示填充Url", Toast.LENGTH_SHORT).show();
                    ImageLoader loader = new ImageLoader();
                    // loader.displayImage(url, mImgLoader);
                    loader.displayImage(mGetUrl, mImgLoader);
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
