package com.training.otp_with_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.ims.ImsStateCallback;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    EditText e1,e2;
    Button btn1,btn2;

    FirebaseAuth auth;
    String verficationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1=findViewById(R.id.editTextNumber);
        e2=findViewById(R.id.editTextNumber2);
        btn1=findViewById(R.id.button);
        btn2=findViewById(R.id.button2);

        auth=FirebaseAuth.getInstance();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value=e1.getText().toString();

                if(value.isEmpty()){
                    e1.setError("please enter Phone Number ");
                }
                else {
                    String Phone="91"+value.trim();
                    sendVerificationCode(Phone);

                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String value=e2.getText().toString();

                if(value.isEmpty()){
                    e1.setError("please enter OTP ");
                }
                else {
                    verifyCode(value);

                }
            }
        });
    }

    private void verifyCode(String value) {
        PhoneAuthCredential credential=PhoneAuthCredential.zzc(verficationId,value);  //getCredential
        sigInWithCredential(credential);

    }

    private void sigInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Is Succesfully ! OTP Verifyed. ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, " Task is not COMPLETED " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode(String phone)
    {
        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(auth).
                setPhoneNumber(phone).setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(mCallback).build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(MainActivity.this, " msg not sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verficationId=s;
        }
    };
}