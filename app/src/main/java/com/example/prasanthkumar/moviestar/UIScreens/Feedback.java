package com.example.prasanthkumar.moviestar.UIScreens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {

    @BindView(R.id.submitFeedback) Button feedBackSubmit;
    @BindView(R.id.edit_feedback) EditText feedback_edittext;
    DatabaseReference databaseReference;
    private DatabaseReference userIdRef;
    FirebaseAuth mAuth;

    public Feedback() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(this,v);

        feedBackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("moviestar_feedback");
                userIdRef = databaseReference.child(mAuth.getCurrentUser().getUid());
                userIdRef.child("feedback").setValue(feedback_edittext.getText().toString());

                Toast.makeText(getContext(),"FeedBack Stored Sccessfully", Toast.LENGTH_SHORT).show();

            }
        });
        return v;


    }

}
