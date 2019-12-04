package com.example.jeremy.logisticwizard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import com.example.jeremy.logisticwizard.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button SignupButton;
    private Button LoginButton;
    private EditText user_name;
    private EditText password;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog2;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(8);
        setContentView(R.layout.activity_login);

        //invoking Button
        SignupButton=(Button)findViewById(R.id.SignUpBut);
        SignupButton.setOnClickListener(this);
        LoginButton = (Button)findViewById(R.id.LoginBut);
        LoginButton.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        user_name = (EditText)findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.Password);

        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            // then go into to profile_main page
//            finish();
//
//            startActivity(new Intent(getApplicationContext(), UserProfile.class));
//        }


//        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_DONE) {
//                    //Toast.makeText(LoginActivity.this, "Press Enter", Toast.LENGTH_SHORT).show();
//                    String user_name_s = user_name.getText().toString();
//                    final String password_s = password.getText().toString();
//                    mDatabase.child(user_name_s).child("password").addValueEventListener(new ValueEventListener(){
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                if(password_s.equals(dataSnapshot.getValue())){
//                                    Toast.makeText(Login.this, "Login succeed", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(Login.this, password_s, Toast.LENGTH_SHORT).show();
//                                }
//
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Toast.makeText(Login.this, "No permission", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                return false;
//            }
//        });



        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

    }


    @Override
    protected void onStart() {
        super.onStart();
        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        //decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

    }
    private void UserLogin(){
        String infoEmail = user_name.getText().toString().trim();
        String infoPassword = password.getText().toString().trim();
        if (TextUtils.isEmpty(infoEmail)) {
            Toast.makeText(this, "Please enter an email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(infoPassword)) {
            Toast.makeText(this, "Please enter password !", Toast.LENGTH_SHORT).show();
            return;
        }
        // let user to see the process of login
        //function to receive user email and password with firebase

        mAuth.signInWithEmailAndPassword(infoEmail, infoPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog2.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), home_page.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            String grab_error = task.getException().getMessage();
                            Toast.makeText(Login.this,
                                    "Error occur:" + grab_error, Toast.LENGTH_SHORT).show();
                        }
                        //progressDialog2.dismiss();

                    }
                });
    }


    @Override
    public void onClick(View view) {
        if (view == SignupButton) {
            Intent intent = new Intent(view.getContext(), Register.class);
            startActivity(intent);

        }
        if (view == LoginButton) {
            UserLogin();
        }
    }
}
