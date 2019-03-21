package com.example.hemapriya.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hemapriya.foodapp.Model.Food;
import com.example.hemapriya.foodapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference FoodRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView userNameTextView;
    String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Curry House");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity2.this,CartActivity.class);
                intent.putExtra("phone",phone);

                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FoodRef=FirebaseDatabase.getInstance().getReference().child("Food");

        progressBar=findViewById(R.id.progressbar);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uname=dataSnapshot.child("username").getValue().toString();
                userNameTextView.setText(uname);
                phone=dataSnapshot.child("phoneNumber").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(FoodRef, Food.class)
                .build();


        FirebaseRecyclerAdapter<Food, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Food, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout,viewGroup,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return  holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Food model) {
                progressBar.setVisibility(View.GONE);
                holder.txtProductName.setText(model.getDish());
                holder.txtProductPrice.setText("Price = RS "+model.getPrice());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       // Toast.makeText(MainActivity2.this,phone,Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(MainActivity2.this,ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getDish());
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                    }
                });
            }


        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(MainActivity2.this,UpdateActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person) {
            // Handle the camera action
                Intent intent=new Intent(MainActivity2.this,SettingsActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        else if (id == R.id.nav_cart) {
            Intent intent=new Intent(MainActivity2.this,CartActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders) {
            Intent intent=new Intent(MainActivity2.this,AdminNewOrdersActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings) {
            Intent intent=new Intent(MainActivity2.this,UpdateActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {


            // Toast.makeText(this,user,Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(MainActivity2.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_about) {

            Intent intent=new Intent(MainActivity2.this,About.class);
            startActivity(intent);


        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
