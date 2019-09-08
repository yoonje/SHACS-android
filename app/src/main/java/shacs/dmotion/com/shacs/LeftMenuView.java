package shacs.dmotion.com.shacs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import widget.MultiDirectionSlidingDrawer;

/**
 * Created by ddrze on 2017-11-06.
 */

public class LeftMenuView {
    private Context mContext;
    private Activity mActivity;

    private MultiDirectionSlidingDrawer mDrawer;
    private Callbacks mCallbacks = menuCallbacks;
    private LinearLayout baseLayout;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks menuCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {

        }
    };

    public LeftMenuView(Activity parent, MultiDirectionSlidingDrawer drawer){
        mContext = parent;
        mActivity = parent;
        mDrawer = drawer;

        LinearLayout menu1 = (LinearLayout)mActivity.findViewById(R.id.menu1);
        LinearLayout menu2 = (LinearLayout)mActivity.findViewById(R.id.menu2);
        LinearLayout menu3 = (LinearLayout)mActivity.findViewById(R.id.menu3);
        LinearLayout menu4 = (LinearLayout)mActivity.findViewById(R.id.menu4);
        LinearLayout menu5 = (LinearLayout)mActivity.findViewById(R.id.menu5);

        baseLayout = (LinearLayout)mActivity.findViewById(R.id.baseLayout);
        baseLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.close();
                mActivity.startActivity(new Intent(mActivity, SettingActivity.class));
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.close();

                mActivity.startActivity(new Intent(mActivity, IotDeviceManagementActivity.class));
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.close();
//                Toast.makeText(mActivity, "준비중입니다.", Toast.LENGTH_LONG).show();
                mActivity.startActivity(new Intent(mActivity, IRDeviceManagementActivity.class));
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.close();
                mActivity.startActivity(new Intent(mActivity, DeviceManualActivity.class));
            }
        });

        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.close();
                mActivity.startActivity(new Intent(mActivity, BleInterfaceActivity.class));
            }
        });
    }
}
