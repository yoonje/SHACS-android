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

public class IotDeviceManagementActivity extends AppCompatActivity {
    private Button btn1_1, btn1_2, btn2_1, btn2_2, btn3_1, btn3_2, btn4_1, btn4_2, btn5_1, btn5_2, btn6_1, btn6_2;

    EditText device01, device02, device03, device04, device05, device06;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_device_management);

        btn1_1 = (Button)findViewById(R.id.btn1_1);
        btn1_2 = (Button)findViewById(R.id.btn1_2);
        btn2_1 = (Button)findViewById(R.id.btn2_1);
        btn2_2 = (Button)findViewById(R.id.btn2_2);
        btn3_1 = (Button)findViewById(R.id.btn3_1);
        btn3_2 = (Button)findViewById(R.id.btn3_2);
        btn4_1 = (Button)findViewById(R.id.btn4_1);
        btn4_2 = (Button)findViewById(R.id.btn4_2);
        btn5_1 = (Button)findViewById(R.id.btn5_1);
        btn5_2 = (Button)findViewById(R.id.btn5_2);
        btn6_1 = (Button)findViewById(R.id.btn6_1);
        btn6_2 = (Button)findViewById(R.id.btn6_2);

        device01 = (EditText)findViewById(R.id.device01);
        device02 = (EditText)findViewById(R.id.device02);
        device03 = (EditText)findViewById(R.id.device03);
        device04 = (EditText)findViewById(R.id.device04);
        device05 = (EditText)findViewById(R.id.device05);
        device06 = (EditText)findViewById(R.id.device06);

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

        device04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn4_1.setText(editable.toString() + " 켜");
                btn4_2.setText(editable.toString() + " 꺼");
            }
        });

        device05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn5_1.setText(editable.toString() + " 켜");
                btn5_2.setText(editable.toString() + " 꺼");
            }
        });

        device06.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn6_1.setText(editable.toString() + " 켜");
                btn6_2.setText(editable.toString() + " 꺼");
            }
        });

        btn1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x02, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x02, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x02, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x02, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x03, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x03, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x03, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x03, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x04, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x04, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x04, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x04, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x05, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x05, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x05, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x05, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x06, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x06, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x06, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x06, 0x00};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn6_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x07, 0x01};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x07, 0x01};
                    Utils.mService.sendMessageToRemote(command);
                }
            }
        });

        btn6_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                    byte[] command={0x03, 0x03, 0x07, 0x00};
                    MainActivity.usbService.write(command);
                } else {
                    byte[] command={0x03, 0x03, 0x07, 0x00};
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
                String strDevice4 = device04.getText().toString();
                String strDevice5 = device05.getText().toString();
                String strDevice6 = device06.getText().toString();

                strDevice1 = strDevice1.replaceAll(" ", "");
                strDevice2 = strDevice2.replaceAll(" ", "");
                strDevice3 = strDevice3.replaceAll(" ", "");
                strDevice4 = strDevice4.replaceAll(" ", "");
                strDevice5 = strDevice5.replaceAll(" ", "");
                strDevice6 = strDevice6.replaceAll(" ", "");

                if(strDevice1.length() > 0)
                    editor.putString("IOT01",strDevice1);

                if(strDevice2.length() > 0)
                    editor.putString("IOT02",strDevice2);

                if(strDevice3.length() > 0)
                    editor.putString("IOT03",strDevice3);

                if(strDevice4.length() > 0)
                    editor.putString("IOT04",strDevice4);

                if(strDevice5.length() > 0)
                    editor.putString("IOT05",strDevice5);

                if(strDevice6.length() > 0)
                    editor.putString("IOT06",strDevice6);

                editor.commit();

                Toast.makeText(IotDeviceManagementActivity.this, "저장되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(IotDeviceManagementActivity.this);
        String strIot1 = pref.getString("IOT01", "");
        String strIot2 = pref.getString("IOT02", "");
        String strIot3 = pref.getString("IOT03", "");
        String strIot4 = pref.getString("IOT04", "");
        String strIot5 = pref.getString("IOT05", "");
        String strIot6 = pref.getString("IOT06", "");

        device01.setText(strIot1);
        device02.setText(strIot2);
        device03.setText(strIot3);
        device04.setText(strIot4);
        device05.setText(strIot5);
        device06.setText(strIot6);
    }
}
