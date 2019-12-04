package com.example.jeremy.logisticwizard;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class home_page extends AppCompatActivity implements View.OnClickListener{
    private Button assetsButton;
    private Button workOrdersButton;
    private Button tools;
    private Button profile;
    private Button calendar;
    private GridLayout gl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        gl = (GridLayout) findViewById(R.id.gridlayout);

        setSingleEvent(gl);

        // *** !!!IMPORTANT!!! ***  --> must cite the calendar interface symbol
        // Place the attribution on the credits/description page of the application.
        // <div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" 			    title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

//        workOrdersButton = (Button) findViewById(R.id.workOrdersButton);
//        assetsButton = (Button) findViewById(R.id.assetsButton);
//        tools = (Button) findViewById(R.id.toolsButton);
//        profile = (Button) findViewById(R.id.profileButton);
//        calendar = (Button) findViewById(R.id.calenderButton);
//
//
//        assetsButton.setOnClickListener(this);
//        workOrdersButton.setOnClickListener(this);
//        tools.setOnClickListener(this);
//        profile.setOnClickListener(this);
//        calendar.setOnClickListener(this);
//        tools.setOnClickListener(this);
//        profile.setOnClickListener(this);
//        calendar.setOnClickListener(this);
//        workOrdersButton = findViewById(R.id.workOrdersButton);
//        workOrdersButton.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {

        // *** could implement better this way ***
        //switch (v.getId()){
        //    case R.id.workOrdersButton:
        //        break;
        //}
//        if (v == workOrdersButton) {
//            Intent intent = new Intent(v.getContext(), workorder_main.class);
//            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
//            //overridePendingTransition(0, 0);
//        }
//        if (v == assetsButton) {
//            Intent intent = new Intent(v.getContext(), machine_main.class);
//            startActivity(intent);
//            //overridePendingTransition(0, 0);
//        }
//        if(v == tools) {
//            Intent intent = new Intent(v.getContext(), tool_main.class);
//            startActivity(intent);
//        }
//        if (v == profile) {
//            Intent intent = new Intent(v.getContext(), profile_main.class);
//            startActivity(intent);
//        }
//        if (v == calendar) {
//            Intent intent = new Intent(v.getContext(), calendar_main.class);
//            startActivity(intent);
//        }
    }

    private void setSingleEvent(GridLayout mainGrid){
        for (int i = 0; i<mainGrid.getChildCount(); i++){
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0){
                        Intent intent = new Intent(view.getContext(), workorder_main.class);
                        startActivity(intent);
                    }
                    if (finalI == 1){
                        Intent intent = new Intent(view.getContext(), workorder_add.class);
                        startActivity(intent);
                    }
                    if (finalI == 2){
                        Intent intent = new Intent(view.getContext(), machine_main.class);
                        startActivity(intent);
                    }
                    if (finalI == 3){
                        Intent intent = new Intent(view.getContext(), tool_main.class);
                        startActivity(intent);
                    }
                    if (finalI == 4){
                        Intent intent = new Intent(view.getContext(), profile_main.class);
                        startActivity(intent);
                    }
                    if (finalI == 5){
                        Intent intent = new Intent(view.getContext(), calendar_main.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
