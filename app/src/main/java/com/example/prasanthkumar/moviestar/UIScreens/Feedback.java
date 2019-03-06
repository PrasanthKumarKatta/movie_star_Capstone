package com.example.prasanthkumar.moviestar.UIScreens;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.CheckInternet.InternetConnection;
import com.example.prasanthkumar.moviestar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {

    @BindView(R.id.submitFeedback) Button feedBackSubmit;
    @BindView(R.id.edit_feedback) EditText feedback_edittext;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextView userName;
    private DatabaseReference user_name_ref;
    private String userName_firebase;

    public Feedback() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(this, v);
        mAuth = FirebaseAuth.getInstance();

        feedBackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("User_Feedback");
                SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("registration_details", MODE_PRIVATE);
                String userName_shr = sharedPreferences.getString("userNameKey", null);

                if (feedback_edittext.getText().toString().length() != 0) {
                    myRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("UserName").setValue(userName_shr);
                    //myRef.child(mAuth.getCurrentUser().getUid()).child("UserName").setValue(userName_firebase);
                    myRef.child(mAuth.getCurrentUser().getUid()).child("feedback").setValue("" + feedback_edittext.getText().toString().trim());
                    Toast.makeText(getContext(), R.string.eoorsFeedBakMsg, Toast.LENGTH_SHORT).show();
                    feedback_edittext.setText(null);

                } else {
                    Toast.makeText(getContext(), R.string.errorSomethingMsg, Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;

    }

    private void checkInternet() {
        if (InternetConnection.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            Toast.makeText(getContext(), R.string.internetConnneted, Toast.LENGTH_SHORT).show();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle(R.string.permissions);
            builder.setMessage(R.string.error_dialog_internet);
            builder.setIcon(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            builder.setPositiveButton(getString(R.string.goto_settings_positive_btn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton(R.string.cancel_alert, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}