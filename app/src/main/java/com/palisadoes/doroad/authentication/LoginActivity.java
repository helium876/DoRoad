package com.palisadoes.doroad.authentication;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.IgnoreExtraProperties;
import com.palisadoes.doroad.R;

import static com.palisadoes.doroad.R.id.main_toolbar;

/**
 * Created by root on 12/27/16.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Droid";
    public static final int START_REGISTRATION_ACTIVITY = 12000;
    Button login_button;
    TextView create_account;
    EditText email,password;
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        login_button = (Button) findViewById(R.id.btn_login);
        create_account = (TextView) findViewById(R.id.link_signup);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        login_button.setOnClickListener(this);
        create_account.setOnClickListener(this);
        email.setOnClickListener(this);
        password.setOnClickListener(this);
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("DoRoad");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:{
                break;
            }
            case R.id.input_email:{
                break;
            }
            case R.id.input_password:{
                break;
            }
            case R.id.link_signup:{
                gotoRegistrationActivity();
                break;
            }
        }
    }
    private void showProgressDialog(){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Athenticating...");
        progressDialog.show();
    }
    private void gotoRegistrationActivity(){
            Intent intent = new Intent(getBaseContext(),RegistrationActivity.class);
        //PendingIntent pendingIntent = TaskStackBuilder.create(getBaseContext()).addNextIntentWithParentStack(intent).
         //       getPendingIntent(START_REGISTRATION_ACTIVITY,PendingIntent.FLAG_UPDATE_CURRENT);
      TaskStackBuilder.create(getBaseContext()).addNextIntentWithParentStack(intent).startActivities();
    }
}
