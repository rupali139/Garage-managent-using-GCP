package com.example.rupali.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.Owner;
import com.example.rupali.demo.HelperClass.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText eduser,edpass;
    String dbpassword=null,password=null,UID=null,garageId=null;
    CheckBox checkowner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollref=null,ownerCollref=null;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        userCollref=db.collection("User");
        ownerCollref=db.collection("Owner");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eduser=(EditText)findViewById(R.id.editText_username);
        edpass=(EditText)findViewById(R.id.editText_password);
        checkowner=(CheckBox)findViewById(R.id.checkBox) ;

    }

    public void Login(View view)
    {
        String username=eduser.getText().toString().trim();
        String passwordtxt=edpass.getText().toString();

        if(checkowner.isChecked()==true)
        {
            loginOwner( username);
        }
        else
        {
            loginuser(username);
        }

    }

    private void loginOwner(String username) {
        password=edpass.getText().toString().trim();
        ownerCollref.whereEqualTo("username",username).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {

                            Owner owner=documentSnapshot.toObject(Owner.class);
                            dbpassword=owner.getPassword();

                            if(dbpassword.equals(password))
                            {
                                garageId=owner.getGarageid();
                                gotoShowAppointmentsActivity();
                            }
                            else
                                Toast.makeText(LoginActivity.this, "Enter correct password", Toast.LENGTH_SHORT).show();

                        }

                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void loginuser(String username) {
        password=edpass.getText().toString().trim();
        userCollref.whereEqualTo("userName",username).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {

                            User user=documentSnapshot.toObject(User.class);
                            dbpassword=user.getPassword();
                            UID=user.getUserID();
                            if(dbpassword.equals(password))
                            {
                                gotoSearchActivity();
                            }
                            else
                                Toast.makeText(LoginActivity.this, "Enter correct password", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Please enter correct username", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void signUp(View view)
    {
        Intent intent=new Intent(LoginActivity.this,Registration.class);
        startActivity(intent);
    }

    public void gotoSearchActivity()
    {
        Intent intent;
        intent = new Intent(LoginActivity.this,MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("UID",UID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void gotoShowAppointmentsActivity()
    {
        Intent intent;
        Bundle bundle=new Bundle();
        bundle.putString("garageid",garageId);
        intent = new Intent(LoginActivity.this,ShowAppointmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    }



