package com.overlake.ftc.configapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tx = (TextView) findViewById(R.id.editText5);
        tx.setKeyListener(null);
        Button addToConfig = (Button) findViewById(R.id.addKeyValue);
        addToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddKeyPair(v);
            }
        });
        Button createConfig = (Button) findViewById(R.id.createConfig);
        createConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                writeFileData
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_create_config:
                return true;
            case R.id.action_view_config:
                startActivity(new Intent(CreateConfigActivity.this, MainActivity.class));
                return true;
            case R.id.action_edit_config:
                startActivity(new Intent(CreateConfigActivity.this, EditConfigActivity.class));
                return true;
            case R.id.action_delete_config:
                startActivity(new Intent(CreateConfigActivity.this, DeleteConfig.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean handleAddKeyPair(View v) {
        EditText key = (EditText) findViewById(R.id.editText3);
        EditText value = (EditText) findViewById(R.id.editText4);
        EditText configView = (EditText) findViewById(R.id.editText5);
        String keyValue = key.getText().toString();
        String textValue = key.getText().toString();
        if (keyValue.length() > 0 && textValue.length() > 0) {
            configView.append("\t\t"+ keyValue +": " + textValue + "\n");
        }
        return true;
    }
}
