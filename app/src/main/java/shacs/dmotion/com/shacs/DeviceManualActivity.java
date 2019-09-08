package shacs.dmotion.com.shacs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DeviceManualActivity extends AppCompatActivity {
    private RelativeLayout btn1;
    private RelativeLayout btn2;
    private RelativeLayout btn3;
    private RelativeLayout btn4;
    private RelativeLayout btn5;
    private RelativeLayout btn6;

    private RelativeLayout btn7;
    private RelativeLayout btn8;
    private RelativeLayout btn9;

    private boolean bBtn1 = false;
    private boolean bBtn2 = false;
    private boolean bBtn3 = false;
    private boolean bBtn4 = false;
    private boolean bBtn5 = false;
    private boolean bBtn6 = false;
    private boolean bBtn7 = false;
    private boolean bBtn8 = false;
    private boolean bBtn9 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manual);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(DeviceManualActivity.this);
        String strIot1 = pref.getString("IOT01", "");
        String strIot2 = pref.getString("IOT02", "");
        String strIot3 = pref.getString("IOT03", "");
        String strIot4 = pref.getString("IOT04", "");
        String strIot5 = pref.getString("IOT05", "");
        String strIot6 = pref.getString("IOT06", "");

        String strIR1 = pref.getString("IR01", "");
        String strIR2 = pref.getString("IR02", "");
        String strIR3 = pref.getString("IR03", "");

        btn1 = (RelativeLayout)findViewById(R.id.btn1);
        btn2 = (RelativeLayout)findViewById(R.id.btn2);
        btn3 = (RelativeLayout)findViewById(R.id.btn3);
        btn4 = (RelativeLayout)findViewById(R.id.btn4);
        btn5 = (RelativeLayout)findViewById(R.id.btn5);
        btn6 = (RelativeLayout)findViewById(R.id.btn6);
        btn7 = (RelativeLayout)findViewById(R.id.btn7);
        btn8 = (RelativeLayout)findViewById(R.id.btn8);
        btn9 = (RelativeLayout)findViewById(R.id.btn9);

        Button mainBtn = (Button)findViewById(R.id.mainBtn);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(strIot1.length() > 0) {
            TextView btn1TextView = (TextView)findViewById(R.id.btn1TextView);
            btn1TextView.setText(strIot1);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn1) {
                            byte[] command={0x03, 0x03, 0x02, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x02, 0x00};
                            MainActivity.usbService.write(command);
                        }

                        bBtn1 = !bBtn1;
                    } else {
                        if(!bBtn1) {
                            byte[] command={0x03, 0x03, 0x02, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x02, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }

                        bBtn1 = !bBtn1;
                    }
                }
            });
        }

        if(strIot2.length() > 0) {
            TextView btn2TextView = (TextView)findViewById(R.id.btn2TextView);
            btn2TextView.setText(strIot2);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn2) {
                            byte[] command={0x03, 0x03, 0x03, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x03, 0x00};
                            MainActivity.usbService.write(command);
                        }

                        bBtn2= !bBtn2;
                    } else {
                        if(!bBtn2) {
                            byte[] command={0x03, 0x03, 0x03, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x03, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }

                        bBtn2= !bBtn2;
                    }
                }
            });
        }

        if(strIot3.length() > 0) {
            TextView btn3TextView = (TextView)findViewById(R.id.btn3TextView);
            btn3TextView.setText(strIot3);
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn3) {
                            byte[] command={0x03, 0x03, 0x04, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x04, 0x00};
                            MainActivity.usbService.write(command);
                        }

                        bBtn3= !bBtn3;
                    } else {
                        if(!bBtn3) {
                            byte[] command={0x03, 0x03, 0x04, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x04, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn3= !bBtn3;
                    }
                }
            });
        }

        if(strIot4.length() > 0) {
            TextView btn4TextView = (TextView)findViewById(R.id.btn4TextView);
            btn4TextView.setText(strIot4);
            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn4) {
                            byte[] command={0x03, 0x03, 0x05, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x05, 0x00};
                            MainActivity.usbService.write(command);
                        }
                        bBtn4= !bBtn4;
                    } else {
                        if(!bBtn4) {
                            byte[] command={0x03, 0x03, 0x05, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x05, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn4= !bBtn4;
                    }
                }
            });
        }

        if(strIot5.length() > 0) {
            TextView btn5TextView = (TextView)findViewById(R.id.btn5TextView);
            btn5TextView.setText(strIot5);
            btn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn5) {
                            byte[] command={0x03, 0x03, 0x06, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x06, 0x00};
                            MainActivity.usbService.write(command);
                        }
                        bBtn5= !bBtn5;
                    } else {
                        if(!bBtn5) {
                            byte[] command={0x03, 0x03, 0x06, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x06, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn5= !bBtn5;
                    }
                }
            });
        }

        if(strIot6.length() > 0) {
            TextView btn6TextView = (TextView)findViewById(R.id.btn6TextView);
            btn6TextView.setText(strIot6);
            btn6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn6) {
                            byte[] command={0x03, 0x03, 0x07, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x07, 0x00};
                            MainActivity.usbService.write(command);
                        }
                        bBtn6= !bBtn6;
                    } else {
                        if(!bBtn6) {
                            byte[] command={0x03, 0x03, 0x07, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x07, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn6= !bBtn6;
                    }
                }
            });
        }

        if(strIR1.length() > 0) {
            TextView btn7TextView = (TextView)findViewById(R.id.btn7TextView);
            btn7TextView.setText(strIR1);
            btn7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn7) {
                            byte[] command={0x03, 0x03, 0x40, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x40, 0x00};
                            MainActivity.usbService.write(command);
                        }
                        bBtn7= !bBtn7;
                    } else {
                        if(!bBtn7) {
                            byte[] command={0x03, 0x03, 0x40, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x40, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn7= !bBtn7;
                    }
                }
            });
        }

        if(strIR2.length() > 0) {
            TextView btn8TextView = (TextView)findViewById(R.id.btn8TextView);
            btn8TextView.setText(strIR2);
            btn8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn8) {
                            byte[] command={0x03, 0x03, 0x41, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x41, 0x00};
                            MainActivity.usbService.write(command);
                        }
                        bBtn8= !bBtn8;
                    } else {
                        if(!bBtn8) {
                            byte[] command={0x03, 0x03, 0x41, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x41, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn8= !bBtn8;
                    }
                }
            });
        }

        if(strIR3.length() > 0) {
            TextView btn9TextView = (TextView)findViewById(R.id.btn9TextView);
            btn9TextView.setText(strIR3);
            btn9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.usbService != null && MainActivity.bUsbConnection == true) {
                        if(!bBtn9) {
                            byte[] command={0x03, 0x03, 0x42, 0x01};
                            MainActivity.usbService.write(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x42, 0x00};
                            MainActivity.usbService.write(command);
                        }
                        bBtn9= !bBtn9;
                    } else {
                        if(!bBtn9) {
                            byte[] command={0x03, 0x03, 0x42, 0x01};
                            Utils.mService.sendMessageToRemote(command);
                        } else {
                            byte[] command={0x03, 0x03, 0x42, 0x00};
                            Utils.mService.sendMessageToRemote(command);
                        }
                        bBtn9= !bBtn9;
                    }
                }
            });
        }
    }

    private String getCommand(int nIndex) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String strCommand = String.format("COMMAND_%02d", nIndex);
        return pref.getString(strCommand, "");
    }
}