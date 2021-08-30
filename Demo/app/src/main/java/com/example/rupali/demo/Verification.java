package com.example.rupali.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.CountryData;
import com.example.rupali.demo.HelperClass.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    Bundle bundle=null;
    User user=null;
    FirebaseFirestore db =null;
    CollectionReference userReference=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        user=new User();
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        userReference=db.collection("User");

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);

        bundle=getIntent().getExtras();
        String phonenumber = bundle.getString("phonenumber");
        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(Verification.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            String userId=mAuth.getUid();
                            saveUserInfo( userId);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Verification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private void sendVerificationCode(String number) {   //send verification method
        progressBar.setVisibility(View.VISIBLE);   // set visibility of progress bar to visible
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,                  // Phone number to verify
                60,                   // Timeout duration
                TimeUnit.SECONDS,         // Unit of timeout
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {   // Exception catch when verification fails
            Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    private void saveUserInfo(String userID) {
        user.setPassword(bundle.getString("password"));
        user.setUserName(bundle.getString("username"));
        user.setFirstName(bundle.getString("fname"));
        user.setLastName(bundle.getString("lname"));
        user.setAddress(bundle.getString("address"));
        user.setEmail(bundle.getString("email"));
        user.setPhoneno(bundle.getString("phonenumber"));
        user.setUserID(userID);

        userReference.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Verification.this, "Successfully Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Verification.this, " Unable Added ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}