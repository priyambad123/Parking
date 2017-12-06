package com.apkglobal.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.apkglobal.parking.R.id.no_of_spots;

public class BookActivity extends AppCompatActivity implements OnSeatSelected, View.OnClickListener {

    private static final int COLUMNS = 5;
    private TextView txtSeatSelected;
    Button bookparking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);


        txtSeatSelected = (TextView)findViewById(R.id.txt_seat_selected);
        bookparking=  (Button)findViewById(R.id.bookparking) ;
        bookparking.setOnClickListener(this);

        List<AbstractItem> items = new ArrayList<>();
        for (int i=0; i<30; i++) {

            if (i%COLUMNS==0 || i%COLUMNS==4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i%COLUMNS==1 || i%COLUMNS==3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        AirplaneAdapter adapter = new AirplaneAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSeatSelected(int count) {

        txtSeatSelected.setText(""+count+" Slot Selected");

    }


    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Slot Booked Thank you!", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(BookActivity.this,ConfirmationActivity.class);
        startActivity(intent);
    }


}
