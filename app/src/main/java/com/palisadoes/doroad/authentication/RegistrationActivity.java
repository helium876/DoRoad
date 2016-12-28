package com.palisadoes.doroad.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.palisadoes.doroad.R;

/**
 * Created by root on 12/27/16.
 */

public class RegistrationActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "Droid";
    Button register_button;
    TextView login_account;
    EditText email, password;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        register_button = (Button) findViewById(R.id.btn_register);
        login_account = (TextView) findViewById(R.id.link_login);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        register_button.setOnClickListener(this);
        login_account.setOnClickListener(this);
        email.setOnClickListener(this);
        password.setOnClickListener(this);
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:{
                break;
            }
            case R.id.input_email:{
                break;
            }
            case R.id.input_password:{
                break;
            }
            case R.id.link_login:{
                break;
            }
        }
    }
    private void showProgressDialog(){
        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
    }
    private void registerUser(){

        String user_email = email.getText().toString();
        String user_password = password.getText().toString();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //checking if success
                        if(task.isSuccessful()){
                            Toast.makeText(getBaseContext(),"Success user registered",Toast.LENGTH_SHORT).show();
                            //display some message here
                        }else{
                            //display some message here
                            Log.d(TAG,task.getException().getMessage());
                        }

                    }
                });

    }
}
