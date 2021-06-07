package com.zajiclib.infotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button button, button2;
    ConstraintLayout rootLayout;
    ConstraintLayout helpLayout;
    TextView tv;

    HelpScreen generated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        tv = findViewById(R.id.textView);
        rootLayout = findViewById(R.id.root_layout);

        Button btn = findViewById(R.id.btntry);
        Button b = findViewById(R.id.button3);
        helpLayout = findViewById(R.id.help_layout);
        helpLayout.setVisibility(View.GONE);

        generated = new HelpScreen(MainActivity.this, helpLayout);
        generated.addViewsToInfoScreen(button, "Budliky, budliky");
        generated.addViewsToInfoScreen(button2, "Click here to display layout annotations");
        generated.addViewsToInfoScreen(btn, "testing the text length and right top margin");
        generated.addViewsToInfoScreen(tv, "hello word annotation");
        generated.addViewsToInfoScreen(b, "test button 3 bottom side");


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                generated.displayAllHelp();
                if (!generated.isHelpScreenVisible()) {
                    generated.displayHelp();
                } else {
                    generated.concealHelpScreen();
                }
            }
        });
    }



    public void test() {

    }



}