package com.example.hemapriya.foodapp;

import android.content.Intent;
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

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private TextView userPh,profileChageTextBtn,closeTextBtn,saveTextButton;

    private EditText fullNameEditText,addressEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        userPh = (TextView) findViewById(R.id.settings_phone_number);
        profileChageTextBtn = (TextView) findViewById(R.id.profile_image_change_button);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        saveTextButton = (TextView) findViewById(R.id.update_settings);



        databaseReference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone=dataSnapshot.child("phoneNumber").getValue().toString();
                userPh.setText(phone);
                String name=dataSnapshot.child("username").getValue().toString();
                fullNameEditText.setText(name);
                String addr=dataSnapshot.child("address").getValue().toString();
                addressEditText.setText(addr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        closeTextBtn.setOnClickListener(this);
        saveTextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==saveTextButton)
        {
            String name,addr,phone;
            phone=userPh.getText().toString();

            name = fullNameEditText.getText().toString().toUpperCase();
            addr=addressEditText.getText().toString();
            User user=new User(name,phone,addr);
            if(TextUtils.isEmpty(name)){
                fullNameEditText.setError("Please enter the username");
                fullNameEditText.requestFocus();
                return;}

            if(TextUtils.isEmpty(addr) ){
                addressEditText.setError("Enter a valid mobile");
                addressEditText.requestFocus();
                return;
            }
            firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            String uid=firebaseUser.getUid();
            DatabaseReference databaseUser= FirebaseDatabase.getInstance().getReference("User");
            databaseUser.child(uid).setValue(user);
            Intent intent = new Intent(UpdateActivity.this, SettingsActivity.class);
            startActivity(intent);

            Toast.makeText(this,"Details Updated",Toast.LENGTH_SHORT).show();

        }
        else if(v==closeTextBtn)
        {
            Intent intent = new Intent(UpdateActivity.this, MainActivity2.class);
            startActivity(intent);
        }

    }
}