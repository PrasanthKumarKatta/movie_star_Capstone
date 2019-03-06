package com.example.prasanthkumar.moviestar.UIScreens;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.prasanthkumar.moviestar.Model.Message;
import com.example.prasanthkumar.moviestar.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {
    private DatabaseReference chat_data_ref;
    private DatabaseReference user_name_ref;
    private FirebaseAuth mAuth;
    private String name="";
    private HashMap<String,String> map;
    @BindView(R.id.edittext) EditText editText;
    @BindView(R.id.listview) ListView listView;
    @BindView(R.id.send_btn_chat) ImageView sendBtn;
    public Chat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,v);

        mAuth= FirebaseAuth.getInstance();
        chat_data_ref= FirebaseDatabase.getInstance().getReference().child("chat_data");
        user_name_ref=FirebaseDatabase.getInstance().getReference().child("moviestar_users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("name");
        map=new HashMap<>();
        user_name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                name= Objects.requireNonNull(dataSnapshot.getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
        FirebaseListAdapter<Message> adapter=new FirebaseListAdapter<Message>(
                (Activity) getContext(),Message.class,R.layout.indidual_row_msg,chat_data_ref) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void populateView(View v, Message model, final int position) {
                TextView msg= v.findViewById(R.id.textView1);
                msg.setText(model.getUser_name()+" : "+model.getMessage());

                //for listview updated position
                listView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listView.setSelection(position);
                        View v = listView.getChildAt(position);
                        if (v != null)
                        {
                            v.requestFocus();
                        }
                    }
                });

            }
        };

        listView.setAdapter(adapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chat_data_ref.push().setValue(new Message(editText.getText().toString().trim(),name));//storing actual msg with name of the user
                editText.setText("");//clear the msg in edittext
            }
        });
        return v;
    }
}
