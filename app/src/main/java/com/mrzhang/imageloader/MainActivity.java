package com.mrzhang.imageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AppCompatImageView mImgLoader;
    AppCompatButton mBtnDisplay;
    AppCompatEditText mEvUrl;
    String url;
    String nativeUrl="https://i1.go2yd.com/image.php?url=0JkUDu6Qar";

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
        mEvUrl = findViewById(R.id.tv_url);
    }

    private void initListener() {
        mBtnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = mEvUrl.getText().toString().trim();
                if (url.isEmpty()) {
                    Toast.makeText(MainActivity.this, "显示默认Url", Toast.LENGTH_SHORT).show();
                    ImageLoader loader = new ImageLoader();
                    // loader.displayImage(url, mImgLoader);
                    loader.displayImage(nativeUrl, mImgLoader);
                }
            }
        });
    }

    private void initEvent() {
    }

}
