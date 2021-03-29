package com.walrus.news.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.walrus.news.R;
import com.walrus.news.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setTextViewClicked(true);
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setButton1Clicked(true);
                binding.setButton2Clicked(false);
                binding.setTextViewClicked(false);
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setButton1Clicked(false);
                binding.setButton2Clicked(true);
                binding.setTextViewClicked(false);
            }
        });

        binding.tapToViewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setButton1Clicked(false);
                binding.setButton2Clicked(false);
                binding.setTextViewClicked(true);
                startActivity(new Intent(MainActivity.this, WalrusNewsActivity.class));
            }
        });
    }
}
