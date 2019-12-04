package com.example.jeremy.logisticwizard;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.jeremy.logisticwizard.R.*;

public class workorder_main extends AppCompatActivity implements View.OnClickListener {

    Button newOrder;
    SearchView sv;
    View v1;
    View top;
    ListView order_view;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<workorder_info> workorder_infoList;
    TextView bt;

    protected DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.workorder_main);

        v1 = (View) findViewById(id.list_view);
        order_view = (ListView) findViewById(id.order_list);
        top = (View) findViewById(id.top_view);
        sv = (SearchView) findViewById(id.search_workorders);
        bt = (TextView) findViewById(R.id.button_text);

        workorder_infoList = new ArrayList<>();

        // Code for initializing RecyclerView
        recyclerView = findViewById(id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // Calls LinearLayoutManager for use on the recycler
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(), workorder_infoList));

        mDatabase = FirebaseDatabase.getInstance().getReference("orders");

        newOrder = (Button) findViewById(R.id.new_order);
        newOrder.setOnClickListener(this);

        BottomNavigationView bottomNav  = findViewById(id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().getItem(0).setCheckable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if (workorder_infoList != null){
                    workorder_infoList.clear();
                //}
                for(DataSnapshot machineSnapshot : dataSnapshot.getChildren()){
                    workorder_info workorder = machineSnapshot.getValue(workorder_info.class);
                    workorder_infoList.add(workorder);
                }
                // Specify the adapter
                mAdapter = new RecyclerViewAdapter(getApplicationContext(), workorder_infoList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29 && resultCode == RESULT_OK) {

            String order_title = data.getStringExtra("orderTitle");
            String order_description = data.getStringExtra("orderDescription");
            String order_note = data.getStringExtra("orderNote");
            String order_DueDate = data.getStringExtra("orderDueDate");
            String order_cost = data.getStringExtra("orderCost");
            String order_priority = data.getStringExtra("orderPriority");
            String order_plan = data.getStringExtra("maintainencePlan");
            String order_status = data.getStringExtra("orderStatus");
            String order_image = data.getStringExtra("orderImage");
            String order_creator = data.getStringExtra("orderCreator");
            String order_machine = data.getStringExtra("orderMachine");


            saveorderToDB(order_title, order_description, order_note, order_DueDate,
                    order_cost, order_priority, order_plan, order_status, order_image, order_creator,
                    order_machine);
        }
    }


    private void saveorderToDB(String order_title, String order_description, String order_note,
                               String order_DueDate, String order_cost, String order_priority,
                               String order_plan, String order_status, String order_image, String order_creator,
                               String order_machine) {
        workorder_info order = new workorder_info(order_title, order_description, order_note, order_DueDate,
                order_cost, order_priority, order_plan, order_status, order_image, order_creator, order_machine);
        mDatabase.child(order_title).setValue(order);

    }

    @Override
    public void onClick(View view) {
        if (view == newOrder) {
            Intent intent = new Intent(view.getContext(), workorder_add.class);
            startActivityForResult(intent, 29);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){
                        case id.nav_home:
                            //menuItem.setCheckable(true);
                            Intent intent = new Intent(workorder_main.this, home_page.class);
                            startActivity(intent);
                            //selectedFragment = new HomeFragment();
                            break;
                        case id.nav_orders:
                            v1.setVisibility(View.INVISIBLE);
                            order_view.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            top.setVisibility(View.INVISIBLE);
                            newOrder.setVisibility(View.INVISIBLE);
                            sv.setVisibility(View.INVISIBLE);
                            bt.setVisibility(View.INVISIBLE);

                            //setContentView(R.layout.calendar_main_fragment);
                            //recyclerView.setVisibility(View.GONE);
                            //need other layouts

                            menuItem.setCheckable(true);
                            selectedFragment = new calendar_main_fragment();
                            getSupportFragmentManager().beginTransaction().replace(id.fragment_container,
                                    selectedFragment).commit();
                            break;
                        case id.nav_profile:


                            v1.setVisibility(View.INVISIBLE);
                            order_view.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            top.setVisibility(View.INVISIBLE);
                            newOrder.setVisibility(View.INVISIBLE);
                            sv.setVisibility(View.INVISIBLE);
                            bt.setVisibility(View.INVISIBLE);
                            //need other layouts

                            menuItem.setCheckable(true);
                            selectedFragment = new profile_main_fragment();
                            getSupportFragmentManager().beginTransaction().replace(id.fragment_container,
                                    selectedFragment).commit();
                            break;
                    }
                    return true; //return clicked item
                }

            };


}