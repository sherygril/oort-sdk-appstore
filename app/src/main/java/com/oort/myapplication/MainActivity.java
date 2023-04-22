package com.oort.sdk.apptoreDemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.oort.oortappstore.sdk.OortSdkAppstore;
import com.oortcloud.appstore.AppStoreInit;
import com.oortcloud.basemodule.login.LoginTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.gostore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!requestPermissionsAction()){
                    return;
                }


                ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        LoginTool.login(MainActivity.this, "18948726601", "12345678", new LoginTool.LoginRes() {
                            @Override
                            public void loginRes(int code, String userId, String token, String data, String data1, String data2) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            pd.dismiss();

                                            OortSdkAppstore.startAppstore(token, data);
                                        }catch (Exception e)
                                        {
                                            Exception ex = e;
                                        }
                                    }
                                });


                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }
                }).start();
            }
        });




    }


    boolean requestPermissionsAction() {
        int ACTION_REQUEST_PERMISSIONS = 0x001;
        String[] NEEDED_PERMISSIONS = new String[]{

                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA


        };

        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

    protected boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

}