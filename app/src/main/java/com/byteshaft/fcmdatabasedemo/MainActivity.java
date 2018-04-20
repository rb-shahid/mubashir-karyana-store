package com.byteshaft.fcmdatabasedemo;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button buttonOpen, buttonClose;
    private TextView networkStatus;
    private TextView textView;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mStatusRef = mRootRef.child("status");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOpen = findViewById(R.id.button_open);
        buttonClose = findViewById(R.id.button_close);
        networkStatus = findViewById(R.id.network_status);
        textView = findViewById(R.id.text_view_status);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    mStatusRef.setValue("Close");
                } else {
                    Toast.makeText(getApplicationContext(), "NO Network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    mStatusRef.setValue("Open");
                } else {
                    Toast.makeText(getApplicationContext(), "NO Network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (isNetworkAvailable()) {
            networkStatus.setText("Connected");
            networkStatus.setTextColor(Color.GREEN);
        } else if (!isNetworkAvailable()) {
            networkStatus.setText("No Connection");
            networkStatus.setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                assert text != null;
                if (text.contains("Close")) {
                    textView.setText(text);
                    networkStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    textView.setText(text);
                    textView.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
