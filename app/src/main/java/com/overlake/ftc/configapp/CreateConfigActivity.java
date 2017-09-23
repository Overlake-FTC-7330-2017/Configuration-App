package com.overlake.ftc.configapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

public class CreateConfigActivity extends AppCompatActivity {

    private final static String FILE_EXTENSION = ".omc";

    private Set<String> keys;
    private File externalRoot;
    private File root;
    private String data;
    private Spinner typeSelector;
    private Toolbar toolbar;
    private EditText title;
    private EditText keyInput;
    private EditText valueInput;
    private EditText dataContainer;
    private Button addToConfig;
    private Button createConfig;
    private Snackbar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = "";
        keys = new HashSet<String>();

        setContentView(R.layout.activity_create_config);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addToConfig = (Button) findViewById(R.id.addKeyValue);
        createConfig = (Button) findViewById(R.id.createConfig);
        typeSelector = (Spinner) findViewById(R.id.typeSelector);
        title = (EditText) findViewById(R.id.editText2);
        dataContainer = (EditText) findViewById(R.id.editText5);
        keyInput = (EditText) findViewById(R.id.editText3);
        valueInput = (EditText) findViewById(R.id.editText4);

        setSupportActionBar(toolbar);
        dataContainer.setKeyListener(null);
        addToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddKeyPair(v);
            }
        });

        externalRoot = Environment.getExternalStorageDirectory();
        if (isExternalStorageAvailable() || !isExternalStorageReadOnly()) {
            root = new File(getExternalFilesDir(""), "configurations");
            if (!root.exists()) {
                root.mkdirs();
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSelector.setAdapter(adapter);

        createConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreateConfig(v);
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
                startActivity(new Intent(CreateConfigActivity.this, EditConfigListActivity.class));
                return true;
            case R.id.action_delete_config:
                startActivity(new Intent(CreateConfigActivity.this, DeleteConfig.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean handleAddKeyPair(View v) {
        String keyValue = keyInput.getText().toString();
        String value = valueInput.getText().toString();
        String dataType = typeSelector.getSelectedItem().toString();
        boolean isValidType = checkType(dataType, value);
        boolean isDuplicate = keys.contains(keyValue);
        if (keyValue.length() == 0) {
            String message = "A key is needed for a valid key:value pair!";
            sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            sb.show();
        } else if (value.length() == 0) {
            String message = "A value is needed for a valid key:value pair!";
            sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            sb.show();
        } else if (isDuplicate) {
            String message = "Key \"" + keyValue + "\" already exists in the configuration!";
            sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            sb.show();
        } else if (!isValidType) {
            String message =
                    "Value \"" + value + "\" does not match selected type of \"" + dataType + "\"!";
            sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            sb.show();
        } else {
            data += String.format("[%s] %s: %s\n", dataType, keyValue, value);
            dataContainer.append(data);
            keys.add(keyValue);
        }
        return true;
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

    public boolean handleCreateConfig(View v) {
        String titleValue = title.getText().toString();
        FileOutputStream output;
        File file = new File(root, titleValue + FILE_EXTENSION);
        if (titleValue.length() == 0) {
            String message = "Configuration needs a name!";
            sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            sb.show();
        } else {
            try {
                if (file.exists()) {
                    String message = "Configuration \"" + file.getName() + "\" already exists!";
                    sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                    sb.show();
                } else {
                    output = new FileOutputStream(file);
                    output.write(data.getBytes());
                    output.flush();
                    output.close();
                    String message = "Created configuration \"" + titleValue + "\"!";
                    sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                    sb.show();
                }
            } catch(Exception e) {
                String message = "Failed to write file \"" + titleValue + "\"!\n" + e;
                sb = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                sb.show();
            }
        }
        return true;
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
}
