package com.fei.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fei.demo.R;

public class St16UpdateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        findViewById(R.id.btn_update_st16_rom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent();
                updateIntent.setAction("android.intent.action.update");
                updateIntent.setPackage("com.hampoo.updatesystem");
                St16UpdateActivity.this.startService(updateIntent);
            }
        });
    }
}
