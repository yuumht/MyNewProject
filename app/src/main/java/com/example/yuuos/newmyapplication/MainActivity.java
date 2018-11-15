package com.example.yuuos.newmyapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void word(View v) {
        Intent intent = new Intent(this, WordActivity.class);
        startActivity(intent);
    }
    public void memo(View v) {
        Intent intent = new Intent(this, MemoActivity.class);
        startActivity(intent);
    }
    public void body(View v) {
        Intent intent = new Intent(this, BodyActivity.class);
        startActivity(intent);
    }
    public void diary(View v) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }
    public void todo(View v) {
        Intent intent = new Intent(this, TodoActivity.class);
        startActivity(intent);
    }
    public void money(View v) {
        Intent intent = new Intent(this, MoneyActivity.class);
        startActivity(intent);
    }
}