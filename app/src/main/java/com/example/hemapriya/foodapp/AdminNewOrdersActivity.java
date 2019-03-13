package com.example.hemapriya.foodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hemapriya.foodapp.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminNewOrdersActivity extends AppCompatActivity implements View.OnClickListener {

        private RecyclerView orderList;
        private DatabaseReference orderRef,adminCartRef;
            String phone;
            Button btn;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    TextView uname,uphno,uaddr,udate,utotal,msg;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_new_orders);
            btn=(Button)findViewById(R.id.show_all_products);
            btn.setOnClickListener(this);

            Intent intent = getIntent();
            phone=intent.getStringExtra("phone");

            uname=(TextView)findViewById(R.id.order_user_name);
            uphno=(TextView)findViewById(R.id.order_phone_number);
            uaddr=(TextView)findViewById(R.id.order_address);
            udate=(TextView)findViewById(R.id.order_date_time);
            utotal=(TextView)findViewById(R.id.order_total_price);

            msg=(TextView)findViewById(R.id.msg1);
            checkOrderState();

        }

    private void checkOrderState() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone = dataSnapshot.child("phoneNumber").getValue().toString();

                DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(phone);
                orderRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String shippingState = dataSnapshot.child("state").getValue().toString();
                            String userName = dataSnapshot.child("name").getValue().toString();
                            uname.setText("Name : "+userName);
                            String phno = dataSnapshot.child("phone").getValue().toString();
                            uphno.setText("Phone : "+phno);
                            String addr = dataSnapshot.child("address").getValue().toString();
                            uaddr.setText("Address : "+addr);
                            String date = dataSnapshot.child("date").getValue().toString();
                            udate.setText("Order on : " +date);
                            String totalAmount = dataSnapshot.child("totalAmount").getValue().toString();
                            utotal.setText("Total Amount = RS "+totalAmount);
                            if(shippingState.equals("shipped"))
                            {
                                btn.setVisibility(View.VISIBLE);
                                Toast.makeText(AdminNewOrdersActivity.this," Thank you ..!! Enjoy your food ..!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                btn.setVisibility(View.VISIBLE);
                                Toast.makeText(AdminNewOrdersActivity.this,"Will be delivered soon..!!",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            uname.setText(" ");
                            uphno.setText(" ");
                            uaddr.setText(" ");
                            udate.setText(" ");
                            utotal.setText(" ");
                            btn.setVisibility(View.GONE);
                            msg.setVisibility(View.VISIBLE);
                            btn.setEnabled(false);
                            Toast.makeText(AdminNewOrdersActivity.this,"Place your Orders",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
        intent.putExtra("uid",phone);
        startActivity(intent);
    }
}