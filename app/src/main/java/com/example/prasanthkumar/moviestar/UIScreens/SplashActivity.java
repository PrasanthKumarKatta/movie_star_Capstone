package com.example.prasanthkumar.moviestar.UIScreens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prasanthkumar.moviestar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.imageView) ImageView img;
    @BindView(R.id.marquee_textview_splash) TextView marquee_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        img.setAnimation(animation);

        marquee_text.setSelected(true);
        marquee_text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        marquee_text.setSingleLine(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this,Login.class);
                startActivity(i);

            }
        },5000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit!");
        builder.setMessage("Do you want to Exit from App?");
        builder.setIcon(R.drawable.logout);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
}
