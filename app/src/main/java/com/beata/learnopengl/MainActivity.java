package com.beata.learnopengl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beata.learnopengl.orthom.OrthomActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_lessOne).setOnClickListener(this);
        findViewById(R.id.btn_orthom).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_lessOne:
                Intent intent = new Intent(this, LessonOneActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_orthom:
                Intent intent1 = new Intent(this, OrthomActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
