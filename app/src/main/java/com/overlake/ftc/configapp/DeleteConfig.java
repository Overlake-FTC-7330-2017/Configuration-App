package com.overlake.ftc.configapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteConfig extends AppCompatActivity {

    private ListView listView;
    private File[] files;
    private File externalRoot;
    private File root;
    private File toDelete;
    private View globalView;

    private List<String> fileNames;
    private ArrayAdapter<String> arrayAdapter;

    private Snackbar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.deleteList);

        externalRoot = Environment.getExternalStorageDirectory();
        root = new File(getExternalFilesDir(""), "configurations");
        if (root.exists()) {
            files = root.listFiles();
        } else {
            root.mkdirs();
        }

        createFileList();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                fileNames
        );

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toDelete = files[position];
                globalView = view;
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(DeleteConfig.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(DeleteConfig.this);
                }
                builder.setTitle("Delete configuration")
                        .setMessage("Are you sure you want to delete this configuration?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String message = "Configuration \"" + toDelete.getName() + "\" was deleted!";
                                fileNames.remove(toDelete.getName());
                                arrayAdapter.notifyDataSetChanged();
                                toDelete.delete();

                                sb = Snackbar.make(globalView, message, Snackbar.LENGTH_SHORT);
                                sb.show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    public void createFileList() {
        fileNames = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            fileNames.add(files[i].getName());
            Log.d("FILE", files[i].toString());
        }
        Log.d("FILE", fileNames.toString());
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
                startActivity(new Intent(DeleteConfig.this, CreateConfigActivity.class));
                return true;
            case R.id.action_view_config:
                startActivity(new Intent(DeleteConfig.this, MainActivity.class));
                return true;
            case R.id.action_edit_config:
                startActivity(new Intent(DeleteConfig.this, EditConfigListActivity.class));
                return true;
            case R.id.action_delete_config:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
