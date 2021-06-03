package com.zajiclib.infotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button button, button1;
    ConstraintLayout rootLayout;
    RelativeLayout helpLayout;
    TextView tv;

    HelpScreen generated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button2);
        tv = findViewById(R.id.textView);
        rootLayout = findViewById(R.id.root_layout);

        helpLayout = findViewById(R.id.help_layout);
        helpLayout.setVisibility(View.GONE);

        generated = new HelpScreen(MainActivity.this, helpLayout);
        generated.addViewsToInfoScreen(button, "Budliky, budliky");
//        generated.addViewsToInfoScreen(button1, "cukrfliky");
        //generated.addViewsToInfoScreen(tv, "neco");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                generated.displayAllHelp();
                generated.displayHelp();
            }
        });
    }



    public void test() {

    }



}