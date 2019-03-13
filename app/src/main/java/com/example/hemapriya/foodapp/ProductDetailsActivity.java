package com.example.hemapriya.foodapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView product_name,product_desc,product_price ;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private ElegantNumberButton numberButton;
    private DatabaseReference databaseReference;
    private Button addToCartToButton;
    private String pdtname="",state="Normal",phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addToCartToButton=(Button) findViewById(R.id.pd_add_to_cart);
        product_name=(TextView)findViewById(R.id.product_name);
        product_price=(TextView)findViewById(R.id.product_price);
        product_desc=(TextView)findViewById(R.id.product_desc);
        numberButton=(ElegantNumberButton)findViewById(R.id.quantity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        Intent intent = getIntent();
       pdtname = intent.getStringExtra("pid");
       phone=intent.getStringExtra("phone");
        databaseReference=FirebaseDatabase.getInstance().getReference("Food").child(pdtname);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String productname=dataSnapshot.child("dish").getValue().toString();
                product_name.setText(productname);
                String productdesc=dataSnapshot.child("desc").getValue().toString();
                product_desc.setText(productdesc);
                String productprice=dataSnapshot.child("price").getValue().toString();
                product_price.setText(productprice);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addToCartToButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                addingToCartList();

                if((state.equals("Order Delivered"))||state.equals("Order Placed"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"You can order more foods once your order is confirmed or delivered",Toast.LENGTH_LONG).show();

                }
                else {
                    checkOrderState();
                }

            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addingToCartList()
    {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

//        String totalprice =
//                Integer.toString(Integer.parseInt(numberButton.getNumber())*Integer.parseInt(productprice));


        final  DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid" ,  product_name.getText().toString());
        cartMap.put("price" , product_price.getText().toString());
        cartMap.put("date" ,saveCurrentDate);
        cartMap.put("quantity" ,numberButton.getNumber());

        cartListRef.child("User View").child(phone)
                .child("Products").child(product_name.getText().toString()).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        {
                            if (task.isSuccessful()) {
                                cartListRef.child("Admin View").child(phone)
                                        .child("Products").child(product_name.getText().toString()).updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ProductDetailsActivity.this,"Added to cart",Toast.LENGTH_SHORT).show();
                                                    Intent intent= new Intent(ProductDetailsActivity.this,MainActivity2.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                            }
                        }
                        }
                });


    }
    private void checkOrderState() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(phone);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    addToCartToButton.setEnabled(false);

                    if (shippingState.equals("shipped")) {
                        state = "Order Delivered";
                    } else if (shippingState.equals("not shipped")) {
                        state = "Order Placed";
                    }
                    Toast.makeText(ProductDetailsActivity.this,"Wait for your previous order to get delivered",Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
