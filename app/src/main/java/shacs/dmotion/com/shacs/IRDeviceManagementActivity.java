package shacs.dmotion.com.shacs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IRDeviceManagementActivity extends AppCompatActivity {

    private Button btn1_1, btn1_2, btn2_1, btn2_2, btn3_1, btn3_2;
    EditText device01, device02, device03;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irdevice_management);

        btn1_1 = (Button)findViewById(R.id.btn1_1);
        btn1_2 = (Button)findViewById(R.id.btn1_2);
        btn2_1 = (Button)findViewById(R.id.btn2_1);
        btn2_2 = (Button)findViewById(R.id.btn2_2);
        btn3_1 = (Button)findViewById(R.id.btn3_1);
        btn3_2 = (Button)findViewById(R.id.btn3_2);

        device01 = (EditText)findViewById(R.id.device01);
        device02 = (EditText)findViewById(R.id.device02);
        device03 = (EditText)findViewById(R.id.device03);

        device01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn1_1.setText(editable.toString() + " 켜");
                btn1_2.setText(editable.toString() + " 꺼");
            }
        });

        device02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn2_1.setText(editable.toString() + " 켜");
                btn2_2.setText(editable.toString() + " 꺼");
            }
        });

        device03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn3_1.setText(editable.toString() + " 켜");
                btn3_2.setText(editable.toString() + " 꺼");
            }
        });

        btn1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x40, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x40, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x40, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x40, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x41, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x41, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x41, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x41, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x42, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x42, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x42, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x42, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        Button scanBtn = (Button)findViewById(R.id.scanBtn);
        Button saveBtn = (Button)findViewById(R.id.saveBtn);
        Button delBtn = (Button)findViewById(R.id.delBtn);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] command = {0x01, 0x02};
                MainActivity.usbService.write(command);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = pref.edit();
                String strDevice1 = device01.getText().toString();
                String strDevice2 = device02.getText().toString();
                String strDevice3 = device03.getText().toString();
                strDevice1 = strDevice1.replaceAll(" ", "");
                strDevice2 = strDevice2.replaceAll(" ", "");
                strDevice3 = strDevice3.replaceAll(" ", "");

                if(strDevice1.length() > 0)
                    editor.putString("IR01",strDevice1);

                if(strDevice2.length() > 0)
                    editor.putString("IR02",strDevice2);

                if(strDevice3.length() > 0)
                    editor.putString("IR03",strDevice3);

                editor.commit();

                Toast.makeText(IRDeviceManagementActivity.this, "저장되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(IRDeviceManagementActivity.this);
        String strIR1 = pref.getString("IR01", "");
        String strIR2 = pref.getString("IR02", "");
        String strIR3 = pref.getString("IR03", "");

        device01.setText(strIR1);
        device02.setText(strIR2);
        device03.setText(strIR3);
    }
}
