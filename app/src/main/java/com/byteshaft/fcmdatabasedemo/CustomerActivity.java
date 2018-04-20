package com.byteshaft.fcmdatabasedemo;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerActivity extends AppCompatActivity {

    private TextView textView;
    private TextView networkStatus;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mStatusRef = mRootRef.child("status");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        networkStatus = findViewById(R.id.network_status_customer);
        textView = findViewById(R.id.status_for_customer);


        if (isNetworkAvailable()) {
            networkStatus.setText("Connected");
            networkStatus.setTextColor(Color.GREEN);
        } else if (!isNetworkAvailable()) {
            networkStatus.setText("No Connection");
            networkStatus.setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                assert text != null;
                if (text.contains("Close")) {
                    textView.setText(text);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    textView.setTextColor(Color.RED);
                } else if (text.contains("Open")) {
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
