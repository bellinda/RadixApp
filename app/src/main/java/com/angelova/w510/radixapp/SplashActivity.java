package com.angelova.w510.radixapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    private ImageView mLogoView;
    private ConstraintLayout mProgressLayout;
    private ProgressBar mProgress;
    private ConstraintLayout mMainLayout;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private static int OPEN_LOGIN_TIME_OUT = 3000;
    private ConstraintSet constraintSet = new ConstraintSet();
    private boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mMainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        mLogoView = (ImageView) findViewById(R.id.logo);
        mProgress = (ProgressBar) findViewById(R.id.progress);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showComponents();
                Runnable openLoginRunnable = new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.setVisibility(View.GONE);
                            }
                        });
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                handler.postDelayed(openLoginRunnable, OPEN_LOGIN_TIME_OUT);
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void showComponents(){
        show = true;

        constraintSet.clone(this, R.layout.activity_splash_alt);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(2000);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(mMainLayout, transition);
        constraintSet.applyTo(mMainLayout);
    }

    private void hideComponents(){
        show = false;

        constraintSet.clone(this, R.layout.activity_splash);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000);


        TransitionManager.beginDelayedTransition(mMainLayout, transition);
        constraintSet.applyTo(mMainLayout);

        mProgress.setVisibility(View.GONE);
    }

}
