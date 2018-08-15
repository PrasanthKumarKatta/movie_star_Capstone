package com.example.prasanthkumar.moviestar.UIScreens;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    @BindView(R.id.editText_username) TextInputEditText userName;
    @BindView(R.id.editText_adminPassword) TextInputEditText password;
    ProgressDialog pd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(Login.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }
        };

    }

    public void login(View view) {

        showProgressDialog();
        mAuth.signInWithEmailAndPassword(userName.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                }
                pd.dismiss();
            }
        });
       /* Intent i = new Intent(this,MainActivity.class);
        startActivity(i);*/
    }

    private void showProgressDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage("Login...");
        pd.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_title);
        builder.setMessage(R.string.alert_msg);
        builder.setIcon(R.drawable.logout);
        builder.setPositiveButton(R.string.alert_yes_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });
        builder.setNegativeButton(R.string.alert_no_btn, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    public void register(View view) {
        Intent i = new Intent(this,Register.class);
        startActivity(i);
        finish();
    }

}
