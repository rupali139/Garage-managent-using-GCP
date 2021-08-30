package com.example.rupali.demo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.BookAppointment;
import com.example.rupali.demo.HelperClass.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowAppointmentActivity extends AppCompatActivity {

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss'Z'");
    Bundle bundle=null;
    String garageId=null;
    TextView textViewAppointment;
    FirebaseFirestore db = null;
    CollectionReference appointmentRef=null,userRef=null;
    String uid=null,service=null,vehicalType=null,strDate=null,strtime=null,strdate1=null;
    String fname=null,lname=null,phoneno=null,name=null,custname=null;
    Date time=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment);
        bundle=getIntent().getExtras();
        garageId=bundle.getString("garageid");
        textViewAppointment=(TextView)findViewById(R.id.textView);

        db=FirebaseFirestore.getInstance();
        appointmentRef=db.collection("Appointment");
        userRef=db.collection("User");
        getAppointments();
    }

    private void getAppointments() {
        appointmentRef.whereEqualTo("garageId",garageId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            String data="";
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    BookAppointment bookAppointment = documentSnapshot.toObject(BookAppointment.class);


                    custname=bookAppointment.getName();
                    phoneno=bookAppointment.getPhoneno();
                    service = bookAppointment.getService();
                    vehicalType = bookAppointment.getVehical();
                    time = (Date) bookAppointment.getTime();
                    strDate = getStringFromDate(time);
                    strdate1 = strDate.substring(0, 10);
                    strtime = getTimefromTimeStamp(strDate.substring(11, 16));

                    data += "\n         NAME            :  " + custname.trim();
                    data += "\n         PHONE NO        :  " + phoneno.trim();
                    data += "\n         DATE            :  " + strdate1.trim();
                    data += "\n         TIME            :  " + strtime.trim();
                    data += "\n         SERVICE         :  " + service.trim();
                    data += "\n         VEHICAL TYPE    :  " + vehicalType.trim()+"\n";
                    data+="-------------------------------------------------------------------------\n";
                }
                textViewAppointment.setText(data);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowAppointmentActivity.this, "Unable to read your appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public String getStringFromDate(Date date)
    {
       String strDate=null;
        strDate= dateFormat.format(date);
       return  strDate;
    }

    public String getTimefromTimeStamp(String str)
    {
        String strTime=null;
        String amPm="AM";
        int hours=Integer.parseInt(str.substring(0,2));
        if(hours>12)
        {
            hours=hours-12;
            amPm="PM";
        }
        strTime=String.valueOf(hours)+str.substring(2)+amPm;
        return  strTime;
    }




}
