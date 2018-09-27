package com.ruby.splitmoney.util;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBar();
    }

    private void setStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 確認取消半透明設置。
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE); // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // 跟系統表示要渲染 system bar 背景。
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
