package com.overlake.ftc.configapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int EXTERNAL_WRITE_PERMISSION = 1;

    private File[] files;
    private File externalRoot;
    private File root;
    private List<String> fileNames;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.configList);

        externalRoot = Environment.getExternalStorageDirectory();
        root = new File(getExternalFilesDir(""), "configurations");
        if (root.exists()) {
            files = root.listFiles();
        } else {
            root.mkdirs();
        }

        createFileList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                fileNames
        );

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ViewConfig.class);
                intent.putExtra("FILE_PATH", files[position].getAbsolutePath());
                startActivity(intent);
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                EXTERNAL_WRITE_PERMISSION);


    }

    public void createFileList() {
        fileNames = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            fileNames.add(files[i].getName());
        }
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
                startActivity(new Intent(MainActivity.this, CreateConfigActivity.class));
                return true;
            case R.id.action_view_config:
                return true;
            case R.id.action_edit_config:
                startActivity(new Intent(MainActivity.this, EditConfigListActivity.class));
                return true;
            case R.id.action_delete_config:
                startActivity(new Intent(MainActivity.this, DeleteConfig.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
