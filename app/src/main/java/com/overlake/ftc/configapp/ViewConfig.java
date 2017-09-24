package com.overlake.ftc.configapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class ViewConfig extends AppCompatActivity {
    private TextView configName;
    private TextView configData;
    private Button backButton;

    private File currentFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentFile = new File(getIntent().getStringExtra("FILE_PATH"));

        configName = (TextView) findViewById(R.id.toolbar_title);
        configData = (TextView) findViewById(R.id.view_config_data);
        backButton = (Button) findViewById(R.id.back_button);

        configName.setText(currentFile.getName());

        configData.setText("");
        try {
            FileInputStream fis = new FileInputStream(currentFile);
            Scanner input = new Scanner(fis);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                configData.append(line + "\n");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
