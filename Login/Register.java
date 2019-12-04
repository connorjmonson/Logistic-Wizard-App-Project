package com.example.jeremy.logisticwizard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity implements View.OnClickListener{
    private Button subButton;
    private Button backButton;
    private DatabaseReference mDatabase;
    private EditText getusername;
    private EditText getpassword;
    private EditText getName;
    private EditText getPhone;
    private boolean gonextpage;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    Pattern pswPattern = Pattern.compile("^[a-zA-Z]\\w{5,15}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //connect to firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // evoking the button and edittext from signup.xml and make them clickable
        subButton = (Button)findViewById(R.id.subBtn);
        subButton.setOnClickListener(this);


        getusername = (EditText)findViewById(R.id.enterUser);
        getpassword = (EditText)findViewById(R.id.enterPassword);
        getName = (EditText)findViewById(R.id.enterName);
        getPhone = (EditText)findViewById(R.id.enterPhone);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

    }

    private void UserRegister(){
        final String infoUsername = getusername.getText().toString().trim();
        String infoPassword = getpassword.getText().toString().trim();
        final String infoName =  getName.getText().toString().trim();
        final String infoPhone =  getPhone.getText().toString().trim();

        //if the user doesnot enter username
        if (TextUtils.isEmpty(infoUsername)||TextUtils.isEmpty(infoPassword)||TextUtils.isEmpty(infoName)||TextUtils.isEmpty(infoPhone)) {
            Toast.makeText(this, "Please enter all information!", Toast.LENGTH_SHORT).show();
            return;
        }
//        // if the user does not enter password
//        if (TextUtils.isEmpty(infoPassword)) {
//            Toast.makeText(this, "Please enter password !", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // if the user does not enter email
//        if (TextUtils.isEmpty(infoName)) {
//            Toast.makeText(this, "Please enter name !", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // if the user does not enter phone number
//        if (TextUtils.isEmpty(infoPhone)) {
//            Toast.makeText(this, "Please enter phone number !", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Check email format
        if(emailPattern.matcher(infoUsername).matches() == false){
            Toast.makeText(this, "Please enter correct email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check password format
        if(pswPattern.matcher(infoPassword).matches() == false){
            Toast.makeText(this, "Password format is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(infoUsername, infoPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Request is sent to Administrator.");
                            String user_id = mAuth.getCurrentUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance().
                                    getReference().child("users").child(user_id);
                            HashMap user_info = new HashMap();
                            user_info.put("Email", infoUsername);
                            user_info.put("Name", infoName);
                            user_info.put("Phone", infoPhone);

                            //user_info.put("profileImageUrl",
                                    //"http://img.icons8.com/color/1600/circled-user-male-skin-type-1-2.png");
                            mDatabase.setValue(user_info);
                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            mAuth.signOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    //push user input information into firebase
//    private void push_data_into_firebase() {
//        final String infoUsername = getusername.getText().toString().trim();
//        final String infoPassword = getpassword.getText().toString().trim();
//        final String infoName = getName.getText().toString().trim();
//        final String infoPhone = getPhone.getText().toString().trim();

//        user_info user = new user_info(infoUsername, infoPassword, infoName, infoPhone);
//
//
//        mDatabase.child("user").child(infoUsername).setValue(user);

    //}


    @Override
    public void onClick(View view) {
        if (view == subButton) {
            UserRegister();

        }
    }

}