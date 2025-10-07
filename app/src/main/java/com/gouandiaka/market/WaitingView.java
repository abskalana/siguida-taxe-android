package com.gouandiaka.market;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class WaitingView extends LinearLayout {


    private final ProgressBar progressBar;
    private final TextView textView;

    private final Button button;

    public WaitingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.waiting_view, this, true);
        progressBar = findViewById(R.id.waiting_progressBar);
        textView = findViewById(R.id.tv_waiting_message);
        button = findViewById(R.id.waiting_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(GONE);
            }
        });
    }

    public void start(Activity activity) {
        progressBar.setVisibility(VISIBLE);
        button.setVisibility(View.GONE);
        textView.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        textView.setText(R.string.waiting_message);
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    public void stop(boolean success,Activity activity) {
        progressBar.setVisibility(GONE);
        if (success) {
            setVisibility(GONE);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(VISIBLE);
            textView.setText(R.string.error_loading);
        }

    }
}
