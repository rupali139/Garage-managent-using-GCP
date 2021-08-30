
package com.example.rupali.demo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.BookAppointment;
import com.example.rupali.demo.HelperClass.DatePickerFragment;
import com.example.rupali.demo.HelperClass.TimePickerFragment;
import com.example.rupali.demo.HelperClass.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Appointment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
                                                                ,TimePickerDialog.OnTimeSetListener{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference=db.collection("Appointment");
    CollectionReference userRef=null;
    String[] vehicals={"Vehical type","2-Wheeler","3-Wheeler","4-Wheeler"};
    String[] facility={"Servicing","puncture","Washing","Oiling","Air"};
    Spinner spinVehiType,spinService;
    ArrayAdapter vehicalType,service;
    String datestr=null,timestr=null,vehical="2",servicestr=null,garageId=null,UID=null;
    String fname,lname,phoneno,name,custname,custPhone;
    private EditText eddate,chooseTime;
    Bundle bundle=null;
    private Button btngocalendar,btngoClock,btnBookAppointment=null;;
    static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss'Z'");

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        bundle=getIntent().getExtras();
        garageId=bundle.getString("IdGarage");
        UID=bundle.getString("UID");
        userRef=db.collection("User");
        getNameinfo(UID);
        getPhoneinfo(UID);
        eddate = (EditText) findViewById(R.id.eddate);
        chooseTime=(EditText)findViewById(R.id.edtime);
        btngocalendar = (Button) findViewById(R.id.btngocalender);
        btngoClock=(Button)findViewById(R.id.btn_Time);
        btnBookAppointment=(Button)findViewById(R.id.btnConfirmAppt);
        setSpinner();

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);

        String currentDate=DateFormat.getDateInstance().format(calendar.getTime());
        String date =  day+"/"+month+"/"+year ;
        EditText edDate=(EditText)findViewById(R.id.eddate);
        edDate.setText(date);
        datestr=date.trim()+'T';

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        String amPm=null;
        timestr=String.valueOf(hour)+":"+String.valueOf(minutes)+":"+"00Z";
        if (hour >= 12) {
            hour=hour-12;
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        chooseTime.setText(hour +":"+minutes+amPm);
    }


    public Date getDateFromString(String datetoSaved){
        try {
            Date date = format.parse(datetoSaved);
            return date ;
        } catch (ParseException e){
            return null;
        }

    }



    public void displayCalender(View view)
    {
        DialogFragment datePicker=new DatePickerFragment();

        datePicker.show(getSupportFragmentManager(),"date picker");
    }
    public  void  displayClock(View view)
    {
        DialogFragment timePicker=new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");
    }

    public void saveAppointment(View view)
    {
        datestr+=timestr;
        Object time=getDateFromString(datestr);
        BookAppointment bookAppointment=new BookAppointment(time,vehical,servicestr,garageId,name,phoneno);
        collectionReference.add(bookAppointment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Appointment.this, "Successfully book your Appointment", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Appointment.this, "Sorry unable to book your Appointment", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

    public void setSpinner()
    {
        spinVehiType=(Spinner)findViewById(R.id.vehicaltype);
        vehicalType= new ArrayAdapter(this,android.R.layout.simple_spinner_item,vehicals);
        vehicalType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinVehiType.setAdapter(vehicalType);

        spinService=(Spinner)findViewById(R.id.menu);
        service=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,facility);
        service.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinService.setAdapter(service);

        spinVehiType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehical=vehicals[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                servicestr=facility[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void gotoProfile(View view)
    {
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
    private void getPhoneinfo(String uid) {
       String custphone=null;
        userRef.whereEqualTo("userID",uid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                        {
                            User user=documentSnapshot.toObject(User.class);
                            phoneno=user.getPhoneno();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Appointment.this, "User is not persent", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void getNameinfo(String userid)
    {
        userRef.whereEqualTo("userID",userid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                        {
                            User user=documentSnapshot.toObject(User.class);
                            fname=user.getFirstName();
                            lname=user.getLastName();
                            name=fname+ "   "+lname;
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Appointment.this, "User is not persent", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
