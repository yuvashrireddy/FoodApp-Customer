package com.example.hemapriya.foodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hemapriya.foodapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button next;
    private TextView txtTotalAmount,msg1;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    int oneTypeProductPrice;
    private int overallTotalPrice=0;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        next=(Button)findViewById(R.id.next_process_button);
        txtTotalAmount=(TextView)findViewById(R.id.total_price);
        msg1=(TextView)findViewById(R.id.msg1);

        Intent intent = getIntent();
        phone=intent.getStringExtra("phone");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(overallTotalPrice==0)
                {
                    Toast.makeText(CartActivity.this,"Place your orders in the cart to proceed",Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        checkOrderState();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        overallTotalPrice=0;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
        .setQuery(cartListRef.child("User View")
                .child(phone)
                .child("Products"),Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Quantity "+model.getQuantity());
                holder.txtProductName.setText("Food Name "+ model.getPid());
                holder.txtProductPrice.setText("Price "+model.getPrice());

                oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))*(Integer.valueOf(model.getQuantity())));
                overallTotalPrice=overallTotalPrice+oneTypeProductPrice;
                txtTotalAmount.setText("Total Price = RS "+String.valueOf(overallTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] =new  CharSequence[]
                        {
                            "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if(i==0)
                                {
                                   // Toast.makeText(CartActivity.this,model.getPid(),Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);

                                   intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1)
                                {
                                    cartListRef.child("User View").child(phone).child("Products")
                                            .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//
                                            if(task.isSuccessful())
                                            {
                                                holder.txtProductQuantity.setText("Quantity "+model.getQuantity());
                                                holder.txtProductName.setText("Food Name "+ model.getPid());
                                                holder.txtProductPrice.setText("Price "+model.getPrice());

                                                oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))*(Integer.valueOf(model.getQuantity())));
                                                overallTotalPrice=overallTotalPrice-oneTypeProductPrice;
                                                txtTotalAmount.setText("Total Price = RS "+String.valueOf(overallTotalPrice));

                                                Toast.makeText(CartActivity.this,"Item Removed From Cart",Toast.LENGTH_SHORT).show();
                                               txtTotalAmount.setText("Total Price = RS "+String.valueOf(overallTotalPrice));



                                            }
                                        }
                                    });


                                    cartListRef.child("Admin View").child(phone).child("Products")
                                            .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this,"Item Removed From Cart",Toast.LENGTH_SHORT).show();

//                                                oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))*(Integer.valueOf(model.getQuantity())));
//                                                overallTotalPrice=overallTotalPrice-oneTypeProductPrice;
                                                txtTotalAmount.setText("Total Price = RS "+String.valueOf(overallTotalPrice));



                                            }
                                        }
                                    });

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
               View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout,viewGroup,false);
               CartViewHolder holder = new CartViewHolder(view);
               return  holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private void checkOrderState()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(phone);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           if(dataSnapshot.exists())
           {
               String shippingState = dataSnapshot.child("state").getValue().toString();
               String userName = dataSnapshot.child("name").getValue().toString();
               String totalAmount = dataSnapshot.child("totalAmount").getValue().toString();
               if(shippingState.equals("shipped"))
               {
                   txtTotalAmount.setText("Dear "+ userName +"\n Order is delivered successfully.");
                   recyclerView.setVisibility(View.GONE);
                   msg1.setVisibility(View.VISIBLE);
                   msg1.setText("Congratulations your final order has been placed successfully. Soon it'll be delivered");
                   next.setVisibility(View.GONE);
                   Toast.makeText(CartActivity.this,"You can order more foods once you receive your food",Toast.LENGTH_SHORT).show();

               }
               else if(shippingState.equals("not shipped"))
               {
                   txtTotalAmount.setText("Delivery State = Not Delivered");
                   recyclerView.setVisibility(View.GONE);
                   msg1.setVisibility(View.VISIBLE);
                   next.setVisibility(View.GONE);
                   Toast.makeText(CartActivity.this,"You can order more foods once your order is delivered",Toast.LENGTH_SHORT).show();

               }

           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
