package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.android.geoquiz.answer_shown";
    private static final String WAS_ANSWER_SHOWN = "was_answer_shown";
    private static final String TIPS_LEFT = "tips-left";
    private boolean mWasAnswerShown;
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private static int mTips = 3;
    private TextView mTipsLeftTextView;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null){
            mWasAnswerShown = savedInstanceState.getBoolean(WAS_ANSWER_SHOWN, false);
            setAnswerShownResult(mWasAnswerShown);
            mTips = savedInstanceState.getInt(TIPS_LEFT, 3);
        }

        mTipsLeftTextView = findViewById(R.id.tips_left_text_view);
        mTipsLeftTextView.setText("У вас осталось подсказок: " + mTips);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTips != 0) {
                    if (mAnswerIsTrue) {
                        mAnswerTextView.setText(R.string.true_button);
                    } else {
                        mAnswerTextView.setText(R.string.false_button);
                    }
                    mTips--;
                    setAnswerShownResult(true);
                    mWasAnswerShown = true;
                    mTipsLeftTextView.setText("У вас осталось подсказок: " + mTips);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        int cx = mShowAnswerButton.getWidth() / 2;
                        int cy = mShowAnswerButton.getHeight() / 2;
                        float radius = mShowAnswerButton.getWidth();
                        Animator anim = null;

                        anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);

                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mShowAnswerButton.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.start();
                    } else {
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                } else{
                    mShowAnswerButton.setEnabled(false);
                    mTipsLeftTextView.setText("У вас не осталось подсказок: ");
                }

            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(WAS_ANSWER_SHOWN, mWasAnswerShown);
        savedInstanceState.putInt(TIPS_LEFT, mTips);
    }
}
