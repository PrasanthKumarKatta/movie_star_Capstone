package com.example.prasanthkumar.moviestar.UIScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {

    @BindView(R.id.email_id)
    EditText email;
    @BindView(R.id.user_id)
    EditText userName;
    @BindView(R.id.password_id)
    EditText password;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    ProgressDialog regDialog;
    private DatabaseReference userIdRef;
    private String email_text;
    private String password_text;
    private String userName_text;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String unamekey = "uNameKey";
    private String emailKey = "emailKey";
    private String passwordKey = "passwordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        if(savedInstanceState !=null){
            userName_text = savedInstanceState.getString(unamekey);
            email_text = savedInstanceState.getString(emailKey);
            password_text = savedInstanceState.getString(passwordKey);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("moviestar_users");

        mAuth = FirebaseAuth.getInstance();
        regDialog = new ProgressDialog(this);
        regDialog.setMessage("Registering...");
    }

    public void register(View view) {
        email_text = email.getText().toString();
        password_text = password.getText().toString().trim();
        userName_text = userName.getText().toString().trim();

        sharedPreferences = getSharedPreferences("registration_details", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userNameKey", userName_text);
        editor.putString("userEmailKey", email_text);
        editor.apply();

        if (TextUtils.isEmpty(email_text) && TextUtils.isEmpty(password_text)) {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_your_email_id), Toast.LENGTH_SHORT).show();
        } else if (password_text.length() < 6) {
            Toast.makeText(this, "Password Length must be atleast 6 charachters", Toast.LENGTH_SHORT).show();
        } else if(password_text.length() == 6) {
            regDialog.show();
            mAuth.createUserWithEmailAndPassword(email_text, password_text).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()) {
                        regDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                        userIdRef = databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        userIdRef.child("name").setValue(userName_text);
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Failed to Create user: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    regDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Password Length must 6 charachters only", Toast.LENGTH_SHORT).show();
        }
        regDialog.dismiss();
    }

    public void reset(View view) {
        email.setText("");
        password.setText("");
        userName.setText("");
    }

    public void login(View view) {

        startActivity(new Intent(this, Login.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(unamekey,userName_text);
        outState.putString(emailKey,email_text);
        outState.putString(passwordKey,password_text);
    }

   }
