package com.palisadoes.doroad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import constants.Constants;

/**
 * Created by remario on 1/2/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputLayout email_wrapper, password_wrapper, name_wrapper;
    private TextView link_login;
    private Button register;
    private EditText email, password, name;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        email_wrapper = (TextInputLayout) findViewById(R.id.user_email_wrapper_register);
        password_wrapper = (TextInputLayout) findViewById(R.id.user_passowrd_wrapper_register);
        name_wrapper = (TextInputLayout) findViewById(R.id.user_name_wrapper_register);
        email = email_wrapper.getEditText();
        password = password_wrapper.getEditText();
        name = name_wrapper.getEditText();

        context = this;

        register = (Button) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.link_login);

        register.setOnClickListener(this);
        link_login.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerUser() {
        String name_, email_, password_;
        name_ = name.getText().toString();
        email_ = email.getText().toString();
        password_ = password.getText().toString();
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email_)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password_)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(Constants.LOGGER, " email: " + email_ + " password: " + password_);
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(Constants.LOGGER, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "user created.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            finish();
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(RegisterActivity.this,
                ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_login: {
                finish();
                break;
            }
            case R.id.btn_signup: {
                registerUser();
                break;
            }
            default:
                break;
        }
    }

}
