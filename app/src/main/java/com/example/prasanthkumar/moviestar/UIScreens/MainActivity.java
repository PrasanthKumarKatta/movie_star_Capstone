package com.example.prasanthkumar.moviestar.UIScreens;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasanthkumar.moviestar.Adapters.MovieAdapter;
import com.example.prasanthkumar.moviestar.CheckInternet.InternetConnection;
import com.example.prasanthkumar.moviestar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    public static final String TAG1 = MovieAdapter.class.getName();

    private TextView userMail;
    private TextView userName;
    private DatabaseReference user_name_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        userMail = header.findViewById(R.id.email_id_nav);
        userName = header.findViewById(R.id.username_id_nav);

        checkInternet();
    }

    private void checkInternet() {
        if (InternetConnection.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();
            userMail.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
            user_name_ref = FirebaseDatabase.getInstance().getReference().child("moviestar_users").child(mAuth.getCurrentUser().getUid()).child("name");
            user_name_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String userName_firebase = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                    userName.setText(userName_firebase);
                    userName.setAllCaps(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            displaySelectedScreen(R.id.home);

        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Exit!");
        builder.setMessage("do you want to exit?");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setPositiveButton(R.string.ok_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Okay continue...", Toast.LENGTH_SHORT).show();
                checkInternet();
            }
        });
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.home:
                fragment = new Home();
                break;
            case R.id.chat_box:
                fragment = new Chat();
                break;
            case R.id.favorites:
                Intent i = new Intent(this, FavoritesActivity.class);
                startActivity(i,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_title);
                builder.setMessage(R.string.alert_logout_msg);
                builder.setIcon(R.drawable.logout);
                builder.setPositiveButton(R.string.alert_yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        finish();
                        Toast.makeText(MainActivity.this, R.string.logout_success_msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.alert_no_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.share:
                DatabaseReference apk_ref = FirebaseDatabase.getInstance().getReference().child("MovieStar_APK").child("APK");
                apk_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String apk_firebase_db = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        shareAPK(apk_firebase_db);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.feedback:
                fragment = new Feedback();
                break;
            case R.id.developer:
                Intent i1 = new Intent(MainActivity.this, DeveloperActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i1,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void shareAPK(String apk_firebase_db) {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType(getString(R.string.share_mimetype))
                .setText("Movie Star \n" + "\n" + apk_firebase_db)
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }
}
