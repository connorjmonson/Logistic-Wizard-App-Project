package com.example.jeremy.logisticwizard;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class workorder_add extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    final static int galleryPic = 1;

    private EditText orderTitle;
    private EditText orderDescription;
    private Spinner orderPriority;
    private Spinner orderPlanSpinner;
    private Spinner machineSpinner;
    private EditText orderNote;
    private Spinner orderStatus;
    private TextView orderDueDate;
    private EditText orderCost;
    private Button submit;
    private ImageButton image;
    protected DatabaseReference mDatabase;
    protected DatabaseReference mDatabase1;
    private FirebaseAuth mAuth;
    private String username;
    private List<String> machineList;

    private FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;

    //added
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workorder_add);

        orderTitle = findViewById(R.id.orderTitle);
        orderDescription = findViewById(R.id.orderDescription);
        image = findViewById(R.id.imageButton);
        orderNote = findViewById(R.id.orderNote);
        //connor add:
        //------------
        orderDueDate = findViewById(R.id.dueDateInput);
        orderDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //this style works well on my emulator: android.R.style.Theme_DeviceDefault_Light_Dialo
                DatePickerDialog dialog = new DatePickerDialog(workorder_add.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener,
                        year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                orderDueDate.setText(date);
            }
        };
        //--------------------------

        orderCost = findViewById(R.id.orderCost);
        submit = findViewById(R.id.submitButton);

        orderPriority = findViewById(R.id.orderPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderPriority.setAdapter(adapter);
        orderPriority.setOnItemSelectedListener(this);

        orderStatus = findViewById(R.id.orderStatus);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStatus.setAdapter(adapter1);
        orderStatus.setOnItemSelectedListener(this);

        orderPlanSpinner = findViewById(R.id.maintenanceSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.machineQuantityStringArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderPlanSpinner.setAdapter(adapter2);
        orderPlanSpinner.setOnItemSelectedListener(this);


        storage=FirebaseStorage.getInstance("gs://logisticwizard-6d896.appspot.com/");
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //mDatabase.child(Uid).child("Name").toString();
        mDatabase.child(Uid).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                username = (String) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase1= FirebaseDatabase.getInstance().getReference("machines");
        machineList = new ArrayList<>();
        machineList.add("none");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot machineSnapshot : dataSnapshot.getChildren()) {
                    machine_info machine = machineSnapshot.getValue(machine_info.class);
                    machineList.add(machine.machine_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        machineSpinner = findViewById(R.id.machineSpinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, machineList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        machineSpinner.setAdapter(adapter3);
        machineSpinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == submit){
            addOrder();
            //Intent intent = new Intent(view.getContext(),order.class);
            //startActivity(intent);
        }
        if(view == image){
            //chooseImage();
            showNormalDialog();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }
    private void takeImage(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, 71);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && resultCode == RESULT_OK
                && data != null )
        {
            Toast.makeText(this,
                    "Error occur:"+resultCode,  Toast.LENGTH_SHORT).show();
            filePath = data.getData();
            try {
                float scale = this.getResources().getDisplayMetrics().density;
                int width = (int)(350*scale+0.5f);
                int height = (int)(200*scale+0.5f);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picasso.with(this).load(filePath).resize(width, height).into(image);
                //image.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if(data.hasExtra("data")){

                Bitmap bitMap = data.getParcelableExtra("data");

            }

        }
        else{
            boolean t = true;
            if(data.getData()==null){
                t = false;
            }
            Toast.makeText(this,
                    "Error occur:"+t,  Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * the user wants to add the information of a new order
     * return all information as a string to Inventory(include onSuccess Code)
     *
     */
    public void addOrder(){
        String order_title = orderTitle.getText().toString().trim();
        String order_descp = orderDescription.getText().toString().trim();
        String order_note = orderNote.getText().toString().trim();
        String order_DueDate = orderDueDate.getText().toString().trim();
        String order_cost = orderCost.getText().toString().trim();
        String order_image = "null";
        String order_creator=username;

        String maintainPlan = orderPlanSpinner.getSelectedItem().toString().trim();
        String order_priority = orderPriority.getSelectedItem().toString().trim();
        String order_status = orderStatus.getSelectedItem().toString().trim();
        String order_machine = machineSpinner.getSelectedItem().toString().trim();


        if (order_title.equals("")||order_descp.equals("")||order_note.equals("")||order_DueDate.equals("")
                ||order_cost.equals("")||order_priority.equals("")||maintainPlan.equals("")||order_status.equals("")
                ||order_machine.equals("")) {
            Toast.makeText(this,
                    "Please enter all information or leave NONE.", Toast.LENGTH_LONG).show();
            return;
        }else {

            Intent order_intent = new Intent();
            order_intent.putExtra("orderTitle", order_title);
            order_intent.putExtra("orderDescription", order_descp);
            order_intent.putExtra("orderNote", order_note);
            order_intent.putExtra("orderDueDate", order_DueDate);
            order_intent.putExtra("orderCost", order_cost);
            order_intent.putExtra("orderPriority", order_priority);
            order_intent.putExtra("maintainencePlan", maintainPlan);
            order_intent.putExtra("orderStatus", order_status);
            order_intent.putExtra("orderCreator", order_creator);
            order_intent.putExtra("orderMachine", order_machine);



            if(filePath != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                //progressDialog.setTitle("Uploading...");
                //progressDialog.show();

                StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                order_image = ref.getPath();
                order_intent.putExtra("orderImage", order_image);
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //progressDialog.dismiss();
                                Toast.makeText(workorder_add.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //progressDialog.dismiss();
                                Toast.makeText(workorder_add.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }


            setResult(RESULT_OK, order_intent);
            finish();
        }
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);

        normalDialog.setTitle(" Uploading picture");
        normalDialog.setMessage("Which one do you want to choose?");
        normalDialog.setPositiveButton("gallery",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseImage();
                    }
                });
        normalDialog.setNegativeButton("camera",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takeImage();
                    }
                });
        // 显示
        normalDialog.show();
    }



}
