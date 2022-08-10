package com.example.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreditsActivity extends AppCompatActivity {

    ProgressDialog progressdialog;

    TextView btn_tv_email;

    TextView tv_name;
    TextView tv_surname;
    TextView tv_prof;
    TextView tv_course;
    TextView tv_email;
    TextView tv_app_version;
    TextView tv_msg;
    TextView tv_year;
    TextView tv_major;

    ImageView imageView;

    FirebaseDatabase rootNode ;
    DatabaseReference reference_creator;

    List<HashMap<String , String>> list_of_DB;
    HashMap<String , String> temp;



    DatabaseHandler databaseHandler;
    List<HashMap<String , Object>> data;
    LinearLayout wholeCreditsLayout;


    @Override
    protected void onStart() {
        super.onStart();
        //change color of background
        databaseHandler = new DatabaseHandler(this);
        //read
        databaseHandler.open();
        data  = databaseHandler.getTableOfContent();
        if(data.get(0).get("color").toString().equals("0"))
            wholeCreditsLayout.setBackgroundColor(getResources().getColor(R.color.white));
        else
            wholeCreditsLayout.setBackgroundColor(getResources().getColor(R.color.MyColor2));
        databaseHandler.close();
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        wholeCreditsLayout = findViewById(R.id.whole_credit_layout);

        getSupportActionBar().setTitle("Credits");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        tv_name = findViewById(R.id.name);
        tv_surname = findViewById(R.id.surname);
        tv_prof = findViewById(R.id.prof);
        tv_course = findViewById(R.id.course);
        tv_email = findViewById(R.id.email);
        tv_app_version = findViewById(R.id.version);
        tv_msg = findViewById(R.id.tv_msg);
        tv_year = findViewById(R.id.year);
        tv_major = findViewById(R.id.major);



        imageView = findViewById(R.id.my_img);

        temp = new HashMap<>();
        list_of_DB = new ArrayList<>();





        rootNode = FirebaseDatabase.getInstance();
        reference_creator = rootNode.getReference("creator");
        //this also works like the line above
        //reference_creator = rootNode.getReference().child("creator");



        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();




        // loading the list of hashmap from the data in the firebase
        reference_creator.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list_of_DB.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {
                    if (datasnapshot.exists()) {
                        temp.put(snapshot.getKey(),snapshot.getValue().toString());
                        //list.add(snapshot.getValue().toString());
                    }
                    list_of_DB.add(temp);

                }

                //tv_test_tt.setText(list.get(0));
                tv_name.setText(list_of_DB.get(0).get("name"));
                tv_surname.setText(list_of_DB.get(0).get("surname"));
                tv_prof.setText(list_of_DB.get(0).get("prof_name"));
                tv_course.setText(list_of_DB.get(0).get("course"));
                tv_email.setText(list_of_DB.get(0).get("email"));
                tv_app_version.setText(list_of_DB.get(0).get("version"));
                tv_msg.setText(list_of_DB.get(0).get("message"));
                tv_year.setText(list_of_DB.get(0).get("academic_year"));
                tv_major.setText(list_of_DB.get(0).get("major"));

                String imageUri = list_of_DB.get(0).get("pic");
                Picasso.with(getApplicationContext()).load(imageUri).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(CreditsActivity.this, "Network error cannot load the content!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("TAG : ", "Failed to read value.", error.toException());
            }
        });


        btn_tv_email = findViewById(R.id.btn_support);
        btn_tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:akbari.1918204@studenti.uniroma1.it")); // only email apps should handle this
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
            }
        });


    }


}