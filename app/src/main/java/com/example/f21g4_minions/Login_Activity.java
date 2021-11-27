package com.example.f21g4_minions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.f21g4_minions.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {


    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String parentDBName = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //Creating the elements
        LoginButton = findViewById(R.id.login_btn);
        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }


        });
    }


    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login into Account");
            loadingBar.setMessage("Please wait, we are checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);

        }
    }

     private void AllowAccessToAccount(String phone, String password) {

         final DatabaseReference Rootref;
         Rootref = FirebaseDatabase.getInstance().getReference();

         //Now see if user is available or not

         Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.child(parentDBName).child(phone).exists()){
                     //We are getting the data and we will pass the data to the users class
                     Users usersData = snapshot.child(parentDBName).child(phone).getValue(Users.class);

                     //Here we are checking if the input data is equals to the database data
                     if(usersData.getPhone().equals(phone)){
                         if(usersData.getPassword().equals(password)){

                             Toast.makeText(Login_Activity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                             startActivity(new Intent(Login_Activity.this, HomeActivity.class));

                         }else{
                             Toast.makeText(Login_Activity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                         }
                     }
                 }
                 else{
                     Toast.makeText(Login_Activity.this, "Account with this "+phone+" do not exist", Toast.LENGTH_LONG).show();
                     loadingBar.dismiss();
                     Toast.makeText(Login_Activity.this, "You have to create new account", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });


     }
}