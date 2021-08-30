package com.example.rupali.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class Registration extends AppCompatActivity {
    public EditText editTextfname,editTextlname,editTextusername,editTextemail,landmark,editTextpass1,editTextpass2;
    public TextView perInfo;
    public Button b_next;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

        editTextfname=(EditText)findViewById(R.id.editText_fname);
        editTextlname=(EditText)findViewById(R.id.editText_Lname);
        editTextusername=(EditText)findViewById(R.id.editText_Rusername);
        editTextemail=(EditText)findViewById(R.id.editText_email);
        editTextpass1=(EditText)findViewById(R.id.editText_pass1);
        editTextpass2=(EditText)findViewById(R.id.editText_pass2);


        b_next=(Button)findViewById(R.id.button_next);
    }

    public void goToNext(View view)
    {
        valid_data();

    }

 public void valid_data()
 {

     awesomeValidation.addValidation(Registration.this, R.id.editText_fname, "[a-zA-Z\\s]+", R.string.err_fname);
     awesomeValidation.addValidation(Registration.this, R.id.editText_username, "[a-zA-Z\\s]+", R.string.err_uname);

     awesomeValidation.addValidation(Registration.this, R.id.editText_Lname, "[a-zA-Z\\s]+", R.string.err_lname);
     awesomeValidation.addValidation(Registration.this, R.id.editText_email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);

     String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
     awesomeValidation.addValidation(Registration.this, R.id.editText_pass1, regexPassword, R.string.err_pass1);
// to validate a confirmation field (don't validate any rule other than confirmation on confirmation field)
     awesomeValidation.addValidation(Registration.this, R.id.editText_pass2, R.id.editText_pass1, R.string.err_pass2);

     if(awesomeValidation.validate())
     {
         Toast.makeText(this, "Data enter successfully", Toast.LENGTH_SHORT).show();
         Bundle bundle=new Bundle();
         bundle.putString("fname",editTextfname.getText().toString());
         bundle.putString("lname",editTextlname.getText().toString());
         bundle.putString("email",editTextemail.getText().toString());
         bundle.putString("username",editTextusername.getText().toString());
         bundle.putString("password",editTextpass1.getText().toString());
         Intent intent=new Intent(Registration.this,Address_fill.class);
         intent.putExtras(bundle);
         startActivity(intent);

     }
     else
     {
         Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show();
     }
 }

}
