package com.example.nt118_appbookstores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payment);

        TextView btnBackToHome = findViewById(R.id.back_to_home);
        TextView btnBackToOrder = findViewById(R.id.back_to_order);
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessPayment.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnBackToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessPayment.this, Order_management.class);
                startActivity(intent);
                finish();
            }
        });
    }
}