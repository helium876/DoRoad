package com.example.remario.doroadv1.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remario.doroadv1.R;
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
    TextView link_login;
    Button register;
    EditText email, password, name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        email_wrapper = (TextInputLayout) findViewById(R.id.user_email_wrapper_register);
        password_wrapper = (TextInputLayout) findViewById(R.id.user_passowrd_wrapper_register);
        name_wrapper = (TextInputLayout) findViewById(R.id.user_name_wrapper_register);
        email = email_wrapper.getEditText();
        password = password_wrapper.getEditText();
        name = name_wrapper.getEditText();

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
        String name_,email_,password_;
        name_ = name.getText().toString();
        email_ = email.getText().toString();
        password_ = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(Constants.LOGGER, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_PopupOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_login: {
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
