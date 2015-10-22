package com.tomtom.cookieclock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomtom.cookieclock.repository.GammerRepoHelper;
import com.tomtom.cookieclock.repository.GammerResultDAO;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private Subscription timeSubscription;
    private TextView tvClock;
    private Button startButton;
    private Button nextButton;
    private EditText name;
    private EditText email;
    private LinearLayout playerData;
    private boolean start = false;
    private GammerRepoHelper data;

    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            tvClock.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvClock = (TextView) findViewById(R.id.clock);
        startButton = (Button) findViewById(R.id.button);
        playerData = (LinearLayout) findViewById(R.id.player_data_layout);
        nextButton = (Button) findViewById(R.id.buttonNext);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndGoToResultsActivity();
            }
        });

        data = new GammerRepoHelper(getApplicationContext());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(100);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvClock.clearAnimation();
        tvClock.setText(R.string.zero_time);
        playerData.clearAnimation();
        playerData.setVisibility(View.INVISIBLE);
        startButton.clearAnimation();
        startButton.setOnTouchListener(this);

        name.setText("");
        email.setText("");
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if(!start) {
                    startTimer();
                } else {
                    stopTimer();
                    startClockAnimation();
                }
                return false;
        }
        return false;
    }

    private void startTimer() {
        start = true;
        startButton.setText(R.string.stop);
        startTime = SystemClock.uptimeMillis();
        timeSubscription = Observable.interval(103, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                updateTime();
            }
        });
    }

    private void stopTimer() {
        start = false;
        startButton.setText(R.string.start);
        if(timeSubscription != null) {
            timeSubscription.unsubscribe();
        }
    }

    private void updateTime() {
        timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
        updatedTime = timeSwapBuff + timeInMilliseconds;
        tvClock.setText(DataHelper.timeToString(updatedTime));
    }

    private void startClockAnimation() {
        Animation animClock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clock_anim);
        tvClock.startAnimation(animClock);
        playerData.setVisibility(View.VISIBLE);
        Animation animButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.go_in);
        playerData.startAnimation(animButton);
        Animation animPlayerData = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.go_out);
        startButton.startAnimation(animPlayerData);
        startButton.setOnTouchListener(null);
    }

    private void saveAndGoToResultsActivity() {
        if(isEditTextNotEmpty()) {
            GammerResultDAO gamer = new GammerResultDAO();
            gamer.setName(name.getText().toString());
            gamer.setEmail(email.getText().toString());
            gamer.setTimeinMs(updatedTime);

            data.saveGammerResult(gamer);

            Intent intent = new Intent(this, ResultsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_data_warning, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEditTextNotEmpty() {
        return !(name.getText().equals("") || email.getText().toString().equals(""));
    }
}
