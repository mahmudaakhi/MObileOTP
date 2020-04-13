package com.example.mobileotp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText e1, e2;
    String Verification_code;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = findViewById(R.id.phone_number);
        e2 = findViewById(R.id.verification_code);
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Verification_code=s;
                Toast.makeText(getApplicationContext(),"code sent to the number",Toast.LENGTH_SHORT).show();
            }
        };
    }
    public void send_sms (View v){
        String number=e1.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS,this,mCallback
        );
    }
    public void signinWithPhone(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User signed in sucessfully",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void verify(View view){
        String input_code=e2.getText().toString();
        if (Verification_code.equals("")){
            verifyPhoneNumber(Verification_code,input_code);
        }
    }

    private void verifyPhoneNumber(String verification_code, String input_code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verification_code, input_code);
        signinWithPhone(credential);
        }
    }


