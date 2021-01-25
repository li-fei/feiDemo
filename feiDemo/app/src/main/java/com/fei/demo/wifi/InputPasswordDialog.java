package com.fei.demo.wifi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.fei.demo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputPasswordDialog extends Dialog {
    Context context;
    private int dialogBackgroundColor;
    private String title = "";
    private EditText editText;
    private ResultListener resultListener;

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public interface ResultListener {
        void onOK(String password);
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public InputPasswordDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.fragment_input_password);
        initView();
    }


    public void initView() {
        handleCardViewBackground();
        handleTitleText();
        handleEditText();
        handleCancelButton();
        handleOKButton();
    }

    private void handleEditText() {
        editText = findViewById(R.id.editText);
    }

    private void handleOKButton() {
        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString();
                Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                Matcher matcher = pattern.matcher(password);
                if (password.length() < 8
                        || password.length() > 20
                        || matcher.find()) {
                    editText.setError("Enter 8 to 20 letters or numbers !");
                    return;
                }
                if (resultListener != null) {
                    if (password.isEmpty()) {
                        password = "1234567890";
                    }
                    resultListener.onOK(password);
                }
                dismiss();
            }
        });
    }


    private void handleCancelButton() {
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void handleTitleText() {
        TextView title = findViewById(R.id.title_text);
        title.setText(this.title);
    }

    private void handleCardViewBackground() {
        CardView cardView = findViewById(R.id.cardView);
        Drawable wrapper = DrawableCompat.wrap(cardView.getBackground());
        DrawableCompat.setTint(wrapper, dialogBackgroundColor);
    }
}
