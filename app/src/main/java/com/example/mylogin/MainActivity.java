package com.example.mylogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;

    List<Note> note;

    //my toolbar
    Toolbar toolbar;
    BlankFragment blankFragment;

    //navigation keys
    LinearLayout credits_tv;
    LinearLayout about_tv;
    LinearLayout theme_tv;
    LinearLayout profile_tv;


    DatabaseHandler databaseHandler;
    List<HashMap<String , Object>> data;
    LinearLayout wholeMainLayout ;
    @Override
    protected void onStart() {
        super.onStart();
        databaseHandler = new DatabaseHandler(this);
        //read
        databaseHandler.open();
        data  = databaseHandler.getTableOfContent();
        if(data.get(0).get("color").toString().equals("0"))
            wholeMainLayout.setBackgroundColor(getResources().getColor(R.color.white));
        else
            wholeMainLayout.setBackgroundColor(getResources().getColor(R.color.MyColor2));
        databaseHandler.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wholeMainLayout = findViewById(R.id.whole_main_layout);

        //refresh every 10 second
        final Handler handler = new Handler();
        final int delay = 20000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                presenter.getData();
                handler.postDelayed(this, delay);
            }
        }, delay);

        //toolbar
        toolbar=(Toolbar)findViewById(R.id.my_tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("HOME");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        blankFragment=(BlankFragment)getSupportFragmentManager().findFragmentById(R.id.my_frag);
        blankFragment.setup((DrawerLayout)findViewById(R.id.my_dra),toolbar);


        //navigation drawer keys
        credits_tv =   findViewById(R.id.nav_credits);
        credits_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
                startActivity(intent);
            }
        });

        about_tv =   findViewById(R.id.nav_about);
        about_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        theme_tv =  findViewById(R.id.nav_theme);
        theme_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemeActivity.class);
                startActivity(intent);
            }
        });


        profile_tv = findViewById(R.id.nav_profile);
         profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
               startActivity(intent);
            }
        });



        fab = findViewById(R.id.add);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(view ->
            startActivityForResult(new Intent(this, EditorActivity.class),INTENT_ADD)
                );

        presenter = new MainPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData()
        );

        itemClickListener = ((view, position) -> {

            int id = note.get(position).getId();
            String title = note.get(position).getTitle();
            String notes = note.get(position).getNote();
            int color = note.get(position).getColor();

            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("note", notes);
            intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);

            //  String title = note.get(position).getTitle();
           // Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            presenter.getData(); //reload data
        }
        else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            presenter.getData(); //reload data
        }

    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter(this ,notes,itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}