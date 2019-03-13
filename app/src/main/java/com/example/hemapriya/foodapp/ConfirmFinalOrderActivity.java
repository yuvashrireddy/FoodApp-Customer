package com.example.hemapriya.foodapp;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText addrEditText;
    private TextView nameEditText,phnoEditText;
    Button confirmOrderBtn;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        confirmOrderBtn=(Button)findViewById(R.id.confirm_final_order_btn);
        nameEditText=(TextView)findViewById(R.id.shipment_name);
        phnoEditText=(TextView)findViewById(R.id.shipment_phno);
        addrEditText=(EditText)findViewById(R.id.shipment_address);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        totalAmount=getIntent().getStringExtra("Total Price");
       Toast.makeText(ConfirmFinalOrderActivity.this,"Total Price = RS "+String.valueOf(totalAmount),Toast.LENGTH_SHORT).show();
        databaseReference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Check();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone=dataSnapshot.child("phoneNumber").getValue().toString();
               phnoEditText.setText(phone);
                String name=dataSnapshot.child("username").getValue().toString();
                nameEditText.setText(name);
                String addr=dataSnapshot.child("address").getValue().toString();
                addrEditText.setText(addr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Check() {
        String address=addrEditText.getText().toString();
        if(TextUtils.isEmpty(address)||address=="Update your address"){
            addrEditText.setError("Please enter the username");
            addrEditText.requestFocus();
            return;
        }
        else {
            confirmOrder();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void confirmOrder()
    {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(phnoEditText.getText().toString().trim());
        HashMap<String,Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount" ,  totalAmount);
        orderMap.put("name" , nameEditText.getText().toString());
        orderMap.put("phone" ,phnoEditText.getText().toString().trim());
        orderMap.put("address" ,addrEditText.getText().toString());

        orderMap.put("date" ,saveCurrentDate);
        orderMap.put("time" , saveCurrentTime);
        orderMap.put("state","not shipped");





        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful())
           {




               FirebaseDatabase.getInstance().getReference().child("Cart List")
                       .child("User View").child(phnoEditText.getText().toString().trim()).removeValue()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(ConfirmFinalOrderActivity.this,"Your order has been placed successfully",Toast.LENGTH_SHORT).show();

                           Intent intent= new Intent(ConfirmFinalOrderActivity.this,MainActivity2.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                           startActivity(intent);
                           finish();

                       }
                   }
               });
           }
            }
        });




    }

}
