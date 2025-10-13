package com.gouandiaka.market.view;


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

import com.gouandiaka.market.R;
import com.gouandiaka.market.utils.Utils;


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
        String message = activity.getString(R.string.waiting_message);
        start(activity,message);
    }

    public void start(Activity activity,String message) {
        progressBar.setVisibility(VISIBLE);
        button.setVisibility(View.GONE);
        textView.setVisibility(GONE);
        this.setVisibility(VISIBLE);
        textView.setText(message);
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    public void stop(boolean success,Activity activity) {
        this.stop(success,activity,activity.getString(R.string.error_loading));

    }

    public void stop(boolean success,Activity activity,String message) {
        progressBar.setVisibility(GONE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (success) {
            setVisibility(GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(VISIBLE);
            textView.setText(message);
        }

    }
}
