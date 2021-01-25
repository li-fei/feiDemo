package com.fei.demo.wifi;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.fei.demo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hellopenggao@gmail.com
 */

public class InputPasswordFragment extends DialogFragment {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        dialogBackgroundColor = ContextCompat.getColor(getContext(), R.color.accent_blue_grey);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleCardViewBackground(view);
        handleTitleText(view);
        handleEditText(view);
        handleCancelButton(view);
        handleOKButton(view);
    }

    private void handleEditText(View view) {
        editText = view.findViewById(R.id.editText);
    }

    private void handleOKButton(View view) {
        view.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(getView(), "alpha", enter ? 0 : 1, enter ? 1 : 0);
        animator.setDuration(333).setInterpolator(new AccelerateInterpolator());
        return animator;
    }

    private void handleCancelButton(View view) {
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void handleTitleText(View view) {
        TextView title = view.findViewById(R.id.title_text);
        title.setText(this.title);
    }

    private void handleCardViewBackground(View view) {
        CardView cardView = view.findViewById(R.id.cardView);
        Drawable wrapper = DrawableCompat.wrap(cardView.getBackground());
        DrawableCompat.setTint(wrapper, dialogBackgroundColor);
    }
}
