package com.overlake.ftc.configapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditConfigListActivity extends AppCompatActivity {

    private ConfigParser p;

    private File[] files;
    private File externalRoot;
    private File root;
    private List<String> fileNames;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.editList);

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
                Intent intent = new Intent(getBaseContext(), EditConfiguration.class);
                intent.putExtra("FILE_PATH", files[position].getAbsolutePath());
                startActivity(intent);
            }
        });
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
                startActivity(new Intent(EditConfigListActivity.this, MainActivity.class));
                return true;
            case R.id.action_view_config:
                startActivity(new Intent(EditConfigListActivity.this, MainActivity.class));
                return true;
            case R.id.action_edit_config:
                return true;
            case R.id.action_delete_config:
                startActivity(new Intent(EditConfigListActivity.this, DeleteConfig.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
