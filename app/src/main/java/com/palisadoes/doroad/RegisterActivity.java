package com.palisadoes.doroad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constants.Constants;

/**
 * Created by remario on 1/2/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputLayout mEmailWrapper, mPasswordWrapper, mFirstNameWrapper, mLastNameWrapper;
    private TextInputLayout mConfirmWrapper;
    private Spinner mVehichleTypeSpinner, mRouteSpinner;
    private TextView mLinkLogin;
    private Button mRegisterBtn;
    private EditText mEmaiEdit, mPasswordEdit, mConfirmEdit, mFirstNameEdit,mLastNameEdit;
    private Context context;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        initViews();

        mSharedPrefs = getSharedPreferences("SHARED_PREFS",Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initViews()
    {
        mEmailWrapper = (TextInputLayout) findViewById(R.id.user_email_wrapper_register);
        mPasswordWrapper = (TextInputLayout) findViewById(R.id.user_password_wrapper);
        mFirstNameWrapper = (TextInputLayout) findViewById(R.id.user_first_name_wrapper);
        mLastNameWrapper = (TextInputLayout) findViewById(R.id.user_last_name_wrapper);
        mConfirmWrapper = (TextInputLayout) findViewById(R.id.password_confirm_wrapper);

        mVehichleTypeSpinner = (Spinner) findViewById(R.id.SpinnerVehicleType);
        mRouteSpinner = (Spinner) findViewById(R.id.SpinnerRoute);


        mEmaiEdit = mEmailWrapper.getEditText();
        mPasswordEdit = mPasswordWrapper.getEditText();
        mConfirmEdit = mConfirmWrapper.getEditText();
        mFirstNameEdit = mFirstNameWrapper.getEditText();
        mLastNameEdit = mLastNameWrapper.getEditText();
        mRegisterBtn = (Button) findViewById(R.id.btn_signup);
        mLinkLogin = (TextView) findViewById(R.id.link_login);

        mRegisterBtn.setOnClickListener(this);
        mLinkLogin.setOnClickListener(this);
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

        final String firstname,email,lastname,password,confirm,vehicle_type,route;

        firstname = mFirstNameEdit.getText().toString();
        lastname = mLastNameEdit.getText().toString();
        email = mEmaiEdit.getText().toString();
        password = mPasswordEdit.getText().toString();
        confirm = mConfirmEdit.getText().toString();
        vehicle_type = mVehichleTypeSpinner.getSelectedItem().toString();
        route = mRouteSpinner.getSelectedItem().toString();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(!isEmailValid(email))
        {
            Toast.makeText(this,"Email is not valid",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(confirm)){
            Toast.makeText(this,"Please confirm password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(firstname)){
            Toast.makeText(this,"Please enter first name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(lastname)){
            Toast.makeText(this,"Please enter last name",Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.contentEquals(confirm))
        {
            Toast.makeText(this,"Password don't match",Toast.LENGTH_SHORT).show();
            return;
        }


        Log.d(Constants.LOGGER, " email: " + email + " password: " + password);
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
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

                            SharedPreferences.Editor editor = mSharedPrefs.edit();
                            editor.putString("firstname",firstname);
                            editor.putString("lastname",lastname);
                            editor.putString("vehicle_type",vehicle_type);
                            editor.putString("route",route);
                            editor.commit();

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

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
