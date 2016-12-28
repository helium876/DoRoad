package com.palisadoes.doroad.authentication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.palisadoes.doroad.R;

/**
 * Created by root on 12/27/16.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "Droid";
    Button login_button;
    TextView create_account;
    EditText email,password;
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
                break;
            }
        }
    }
}
