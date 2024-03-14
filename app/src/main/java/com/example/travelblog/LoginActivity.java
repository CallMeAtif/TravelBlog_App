package com.example.travelblog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalTime;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userName;
    private TextInputLayout passWord;
    private Button loginButton;
    private ProgressBar progressBar;
    private BlogPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.textUsername);
        passWord = findViewById(R.id.textPasswordInput);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressbar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.onLoginClicked();
            }
        });

        preferences = new BlogPreferences(this);
        if(preferences.isLoggedIn()){
            startMainActivity();
            finish();
            return;
        }
    }
    private void onLoginClicked(){

        String username = userName.getEditText().getText().toString();
        String password = passWord.getEditText().getText().toString();

        if(username.isEmpty()){
            userName.setError("Username must not be empty");
        } else if (password.isEmpty()) {
            passWord.setError("Password must not be empty");
        } else if(!username.equals("admin") && !password.equals("admin")){
            showErrorDialog();
        } else {
            performLogin();
        }

        userName.getEditText().addTextChangedListener(createTextWatcher(userName));
        passWord.getEditText().addTextChangedListener(createTextWatcher(passWord));
    }

    //TextWatcher is used to check if text was changed after empty error was thrown from onLoginClicked() to make it null

    private TextWatcher createTextWatcher(TextInputLayout textPasswordInput){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPasswordInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nothing
            }
        };
    }

    // AlertDialog.Builder is used to create dialogs
    private void showErrorDialog(){
        new AlertDialog.Builder(this).setTitle("Login Failed").setMessage("Username or password is not correct. Please try again.").setPositiveButton("ok",((dialog, which) -> dialog.dismiss())).show();
    }

    private void performLogin(){
        preferences.setLoggedIn(true);
        userName.setEnabled(false);
        passWord.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //handler is used to delay our code and do something in this case changing out intent(Activity)
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startMainActivity();
            finish();//used to close activity
        }, 5000);
    }
    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

//
//    private static String DynamicDate(){
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DATE, 1);
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
//        return format1.format(c.getTime());
//    }

}