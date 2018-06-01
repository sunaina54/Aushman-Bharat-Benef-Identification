package com.aadhar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aadhar.commonapi.BioAuthActivity;

public class MainActivity extends AppCompatActivity {
    Button btn_bio,btn_demoAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_demoAuth=(Button)findViewById(R.id.btn_demoAuth);
        btn_bio=(Button)findViewById(R.id.btn_bio);
        btn_demoAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DemoAuth.class);
                startActivity(intent);

            }
        });
        btn_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBio=new Intent(MainActivity.this,BioAuthActivity.class);
                startActivity(intentBio);
            }
        });

    }
}
