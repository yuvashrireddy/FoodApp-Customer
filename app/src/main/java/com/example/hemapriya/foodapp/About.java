package com.example.hemapriya.foodapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

      import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

public class About extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private TextView userPh,profileChageTextBtn,closeTextBtn,saveTextButton,fullNameEditText,addressEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        fullNameEditText = (TextView) findViewById(R.id.settings_full_name);
        addressEditText = (TextView) findViewById(R.id.settings_address);
        userPh = (TextView) findViewById(R.id.settings_phone_number);
        profileChageTextBtn = (TextView) findViewById(R.id.profile_image_change_button);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);



        databaseReference=FirebaseDatabase.getInstance().getReference("Admin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone=dataSnapshot.child("phone").getValue().toString();
                userPh.setText(phone);
                String name=dataSnapshot.child("name").getValue().toString();
                fullNameEditText.setText(name);
                String addr=dataSnapshot.child("addr").getValue().toString();
                addressEditText.setText(addr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        closeTextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==closeTextBtn)
        {
            Intent intent = new Intent(About.this, MainActivity2.class);
            startActivity(intent);
        }

    }
}