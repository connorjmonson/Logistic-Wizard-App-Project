package com.example.jeremy.logisticwizard;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class workorder_edit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText order_title;
    private Spinner order_status;
    private Spinner order_priority;
    private TextView order_Creator;
    private TextInputEditText order_description;
    private EditText order_cost;
    private TextView order_Duedate;
    private TextInputEditText order_note;
    private ImageButton order_image;
    private TextView order_machine;


    private Button save_button;
    private Button back_button;

    String orderTitle;
    String orderImage;
    String orderNote;
    String orderPlan;
    List<String> temple=new ArrayList<>();


    protected DatabaseReference mDatabase;
    protected StorageReference mStorage;
    StorageReference imageRef;

    //added
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workorder_edit);

        mDatabase  = FirebaseDatabase.getInstance().getReference("orders");
        mStorage = FirebaseStorage.getInstance().getReference();
        //imageRef = mStorage.child("/images/cfd4b4b0-6cff-424d-af23-62d4e792f340");
        order_title = findViewById(R.id.titleText);
        order_Creator = findViewById(R.id.creatorHolder);
        order_description=findViewById(R.id.descriptionInput);
        order_cost=findViewById(R.id.editText);
        order_Duedate=findViewById(R.id.editText2);
        order_image=findViewById(R.id.photoHolder);
        order_machine = findViewById(R.id.machineHolder);

        order_status = findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_status.setAdapter(adapter);
        order_status.setOnItemSelectedListener(this);

        order_priority = findViewById(R.id.prioritySpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_priority.setAdapter(adapter1);
        order_priority.setOnItemSelectedListener(this);

        save_button = findViewById(R.id.saveButton);

        back_button=findViewById(R.id.backButton);

        Intent machine_info = getIntent();
        Bundle data = machine_info.getExtras();

        orderTitle = data.get("orderTitle").toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    protected void onStart(){
        super.onStart();

        order_title.setText(orderTitle);
        mDatabase.child(orderTitle).child("order_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String orderStatus;
                orderStatus = (String) dataSnapshot.getValue();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(workorder_edit.this,
                        R.array.statuses, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                order_status.setAdapter(adapter);
                if (orderStatus != null) {
                    int spinnerPosition = adapter.getPosition(orderStatus);
                    order_status.setSelection(spinnerPosition);
                }
                //int orderStatus2 = Integer.parseInt(orderStatus);
                //order_status.setSelection(2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_priority").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String orderPriority;
                orderPriority = (String) dataSnapshot.getValue();
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(workorder_edit.this,
                        R.array.priorities, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                order_priority.setAdapter(adapter1);
                if (orderPriority != null) {
                    int spinnerPosition = adapter1.getPosition(orderPriority);
                    order_priority.setSelection(spinnerPosition);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_creator").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String orderCreator;
                orderCreator = (String) dataSnapshot.getValue();
                order_Creator.setText(orderCreator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_descrip").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String orderDescrip;
                orderDescrip = (String) dataSnapshot.getValue();
                order_description.setText(orderDescrip);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_cost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String orderCost;
                orderCost = (String) dataSnapshot.getValue();
                //System.out.println(orderCost);
                order_cost.setText(orderCost);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_dates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //String orderDuedate;
                //orderDuedate = (String) dataSnapshot.getValue();
                //order_Duedate.setText(orderDuedate);

                //connor add:
                order_Duedate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        //this style works well on my emulator: android.R.style.Theme_DeviceDefault_Light_Dialo
                        DatePickerDialog dialog = new DatePickerDialog(workorder_edit.this,
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
                        order_Duedate.setText(date);
                    }
                };
                //--------------------------

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_note").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderNote = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("maintain_plan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderPlan = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        mDatabase.child(orderTitle).child("order_machine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String orderMachine;
                orderMachine = (String) dataSnapshot.getValue();
                order_machine.setText(orderMachine);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child(orderTitle).child("order_image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                String orderImagePath;
                orderImagePath = (String) dataSnapshot.getValue();
                orderImage = orderImagePath;
                imageRef = mStorage.child(orderImagePath);
                try {
                    final File localimage = File.createTempFile(orderTitle,"jpg");
                    imageRef.getFile(localimage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                            Bitmap bitmap = BitmapFactory.decodeFile(localimage.getPath());
                            order_image.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_edition();
                Intent intent = new Intent (view.getContext(), workorder_view.class);
                intent.putExtra("orderTitle", order_title.getText().toString().trim());
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (workorder_edit.this, workorder_view.class);
                intent.putExtra("orderTitle", order_title.getText());
                startActivity(intent);
            }
        });

        order_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });

    }

    private void save_edition(){
        String orderTitle2 = order_title.getText().toString().trim();
        String orderCreator = order_Creator.getText().toString().trim();
        String orderDescrip = order_description.getText().toString().trim();
        String orderCost = order_cost.getText().toString().trim();
        String orderDuedate = order_Duedate.getText().toString().trim();

        //String orderImage = order_image.getText().toString().trim();
        String orderStatus = order_status.getSelectedItem().toString().trim();
        String orderPriority = order_priority.getSelectedItem().toString().trim();
        String orderMachine = order_machine.getText().toString().trim();

        if (orderTitle2.equals("")||orderCreator.equals("")||orderDescrip.equals("")||orderCost.equals("")
                ||orderDuedate.equals("")||orderStatus.equals("")||orderPriority.equals("")) {
            Toast.makeText(this,
                    "Please enter all information or leave NONE.", Toast.LENGTH_LONG).show();
            return;
        }else {
            if(!orderTitle2.equals(orderTitle)) {
                mDatabase.child(orderTitle).removeValue();
            }

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot orderSnapshot : dataSnapshot.getChildren()){
                        if(!orderSnapshot.getKey().equals(orderTitle)){
                            temple.add(orderSnapshot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if(temple.contains(orderTitle2)){
                Toast.makeText(workorder_edit.this,
                        "workorder already exists, please enter a new name.", Toast.LENGTH_LONG).show();
                return;

            }
            else {
                workorder_info order = new workorder_info(orderTitle2, orderDescrip, orderNote, orderDuedate,
                        orderCost, orderPriority, orderPlan, orderStatus, orderImage, orderCreator, orderMachine);
                mDatabase.child(orderTitle2).setValue(order);
            }

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

}
