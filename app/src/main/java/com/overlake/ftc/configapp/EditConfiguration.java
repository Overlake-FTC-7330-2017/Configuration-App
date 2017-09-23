package com.overlake.ftc.configapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EditConfiguration extends AppCompatActivity {

    private final static String FILE_EXTENSION = ".omc";

    private TextView title;
    private TextView keyInput;
    private TextView valueInput;
    private Spinner typeSpinner;
    private Spinner valueSpinner;
    private Switch addPair;
    private Button deletePair;
    private Button updatePair;
    private Button backButton;
    private Snackbar sb;

    private Map<String, ConfigData> configData;
    private List<String> keys;
    private ArrayAdapter<String> arrayAdapter;

    private String currentKey;
    private File currentFile;
    private File externalRoot;
    private File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentFile = new File(getIntent().getStringExtra("FILE_PATH"));
        currentKey = null;

        readData();

        title = (TextView) findViewById(R.id.toolbar_title);
        keyInput = (TextView) findViewById(R.id.editKey);
        valueInput = (TextView) findViewById(R.id.editValue);
        typeSpinner = (Spinner) findViewById(R.id.typeSelector);
        valueSpinner = (Spinner) findViewById(R.id.valueSelector);
        addPair = (Switch) findViewById(R.id.addSwitch);
        updatePair = (Button) findViewById(R.id.updatePair);
        deletePair = (Button) findViewById(R.id.deletePair);
        backButton = (Button) findViewById(R.id.backButton);

        title.setText(currentFile.getName());
        if (configData.size() > 0) {
            Log.d("KEY", currentKey);
            Log.d("KEY", configData.get(currentKey).toString());
            keyInput.setText(currentKey);
            valueInput.setText(configData.get(currentKey).value);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, keys
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        valueSpinner.setAdapter(arrayAdapter);
        typeSpinner.setAdapter(adapter);

        valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                keyInput.setText(keys.get(position));
                currentKey = keys.get(position);
                valueInput.setText(configData.get(currentKey).value);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        updatePair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigData c;
                if (addPair.isChecked()) {

                    //add data
                    c = new ConfigData("", "", "");
                    if (keys.indexOf(keyInput.getText().toString()) == -1) {
                        configData.put(keyInput.getText().toString(), c);
                        keys.add(keyInput.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                    } else {

                        return;
                    }
                } else {
                    //update data
                    c = new ConfigData("", "", "");
                    if (keys.size() > 0 && keys.indexOf(keyInput.getText().toString()) == -1) {
                        c = configData.get(currentKey);
                        keys.set(keys.indexOf(currentKey), keyInput.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                currentKey = keyInput.getText().toString();
                valueSpinner.setSelection(keys.indexOf(currentKey));
                c.key = keyInput.getText().toString();
                c.value = valueInput.getText().toString();
                c.type = typeSpinner.getSelectedItem().toString();
                if (checkType(c.type, c.value) && keys.size() > 0) {
                    writeFile(v);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EditConfigListActivity.class);
                startActivity(intent);
            }
        });

        deletePair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configData.remove(currentKey);
                keys.remove(currentKey);
                arrayAdapter.notifyDataSetChanged();
                if (keys.size() > 0) {
                    keyInput.setText(configData.get(valueSpinner.getSelectedItem().toString()).key);
                    valueInput.setText(configData.get(valueSpinner.getSelectedItem().toString()).value);
                    currentKey = keyInput.getText().toString();
                } else {
                    currentKey = "";
                    keyInput.setText("No Data To Edit");
                    valueInput.setText("No Data To Edit");
                }
                writeFile(v);
            }
        });

        externalRoot = Environment.getExternalStorageDirectory();
        if (isExternalStorageAvailable() || !isExternalStorageReadOnly()) {
            root = new File(getExternalFilesDir(""), "configurations");
            if (!root.exists()) {
                root.mkdirs();
            }
        }
    }

    public void writeFile(View v) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : configData.keySet()) {
            ConfigData c = configData.get(key);
            stringBuilder.append(c.toString() + "\n");
        }
        String titleValue = title.getText().toString();
        FileOutputStream output;
        File file = currentFile;
        if (titleValue.length() == 0) {
            String message = "Configuration needs a name!";
            sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            sb.show();
        } else {
            try {
                if (!file.exists()) {
                    String message = "Configuration \"" + file.getName() + "\" does not exist!";
                    sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                    sb.show();
                } else {
                    output = new FileOutputStream(file);
                    output.write(stringBuilder.toString().getBytes());
                    output.flush();
                    output.close();
                    String message = "Updated configuration \"" + titleValue + "\"!";
                    sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                    sb.show();
                }
            } catch(Exception e) {
                String message = "Failed to update configuration \"" + titleValue + "\"!\n" + e;
                sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                sb.show();
            }
        }
    }

    public void readData() {
        configData = new HashMap<String, ConfigData>();
        keys = new ArrayList<String>();
        FileInputStream fis;
        try {
            fis = new FileInputStream(currentFile);
            Scanner input = new Scanner(fis);
            while (input.hasNextLine()) {
                String[] args = input.nextLine().split(" ");
                String type = args[0].substring(1, args[0].length() - 1).trim();
                String key = args[1].split(":")[0].trim();
                String value = args[2].trim();
                if (currentKey == null) {
                    currentKey = key;
                }
                configData.put(key, new ConfigData(type, key, value));
                keys.add(key);
            }
            Log.d("D", configData.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public boolean checkType(String type, String value) {
        type = type.toLowerCase();
        boolean success = false;
        switch (type) {
            case "int":
                try {
                    Integer.parseInt(value);
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
                return success;
            case "float":
                try {
                    Float.parseFloat(value);
                    success = true;
                } catch(Exception e) {
                    success = false;
                }
                return success;
            case "double":
                try {
                    Double.parseDouble(value);
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
                return success;
            case "char":
                if (value.length() == 1) {
                    success = true;
                } else {
                    success = false;
                }
                return success;
            case "byte":
                try {
                    Byte.parseByte(value);
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
                return success;
            case "long":
                try {
                    Long.parseLong(value);
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
                return success;
            case "short":
                try {
                    Short.parseShort(value);
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
                return success;
            case "boolean":
                try {
                    Boolean.parseBoolean(value);
                    try {
                        int test = Integer.parseInt(value);
                        if (test >= 0 && test <= 1) {
                            success = true;
                        } else {
                            success = false;
                        }
                    } catch (Exception e) {
                        success = false;
                    }
                    success = true;
                } catch (Exception e) {
                    success = false;
                }
                return success;
            case "string":
                return true;
            default:
                return success;
        }
    }

}
