package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity {

    TextView name,mail,surname;
    TextView logout;


    TextView api_time_tv;
    TextView api_date_tv;

    DatabaseHandler databaseHandler;
    List<HashMap<String , Object>> data;
    ConstraintLayout wholeProfileLayout;
    @Override
    protected void onStart() {
        super.onStart();
        databaseHandler = new DatabaseHandler(this);
        //read
        databaseHandler.open();
        data  = databaseHandler.getTableOfContent();
        if(data.get(0).get("color").toString().equals("0"))
            wholeProfileLayout.setBackgroundColor(getResources().getColor(R.color.white));
        else
            wholeProfileLayout.setBackgroundColor(getResources().getColor(R.color.MyColor2));
        databaseHandler.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        wholeProfileLayout = findViewById(R.id.whole_profile_layout);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        logout = findViewById(R.id.btnLogout_TextView);
        name = findViewById(R.id.TextViewName);
        surname = findViewById(R.id.TextViewSurname);
        mail = findViewById(R.id.TextViewEmail);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            name.setText(signInAccount.getGivenName());
            surname.setText(signInAccount.getFamilyName());
            mail.setText(signInAccount.getEmail());

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Register.class);

                //to kill all activities
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                Toast.makeText(Profile.this, "please sign in to use the app!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    //api call for time
        api_date_tv = findViewById(R.id.api_tv);
        api_time_tv=findViewById(R.id.api_tv2);
        GetDateAndTime getDateAndTime = new GetDateAndTime(this);
        getDateAndTime.getDateTime(new GetDateAndTime.VolleyCallBack() {
            @Override
            public void onGetDataTime(String date, String time) {
                api_date_tv.setText(date);
                api_time_tv.setText(time);
            }
        });






    }
}