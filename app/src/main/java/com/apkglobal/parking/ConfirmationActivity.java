package com.apkglobal.parking;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apkglobal.parking.easypark.GetParkingLots;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    TextView no_of_spots,booking_id;
    Button button_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        button_confirm=(Button)findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(this);

        booking_id=(TextView)findViewById(R.id.booking_id);
        no_of_spots=(TextView)findViewById(R.id.no_of_spots);
        int random = (int)Math.ceil(Math.random()*100);
        booking_id.setText(""+ random*45);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, h:mm a");
        String dateString = sdf.format(date);
        no_of_spots.setText(dateString);

    }


    @Override
    public void onClick(View view) {
        Intent intent =new Intent(ConfirmationActivity.this,GetParkingLots.class);
        startActivity(intent);
    }
}
