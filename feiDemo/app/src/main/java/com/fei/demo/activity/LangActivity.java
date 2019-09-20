package com.fei.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RadioGroup;

import com.fei.demo.R;

import java.util.Locale;

public class LangActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang);

        lang();
    }

    private void lang() {
        RadioGroup radioGroup = findViewById(R.id.lang_radioGroup);
        Locale curLocale = getResources().getConfiguration().locale;
        Log.e("yuneec0","------>" + curLocale + "---------- " + curLocale.getLanguage());
        if (curLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
            radioGroup.check(R.id.lang_chinese);
        }else {
            radioGroup.check(R.id.lang_english);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.lang_chinese:
                        setLang(LangActivity.this,Locale.SIMPLIFIED_CHINESE);
                        break;
                    case R.id.lang_english:
                        setLang(LangActivity.this,Locale.ENGLISH);
                        break;
                }
            }
        });
    }

    public void setLang(Context c, Locale lang) {
        Resources resources = c.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.locale = lang;
        resources.updateConfiguration(config, dm);
//        Intent intent = new Intent(this, FTPActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        recreate();
    }
}
