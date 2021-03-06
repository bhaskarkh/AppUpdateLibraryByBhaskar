package com.bhaskar.forceappupdate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;


public class AppUpdateChecker {
    String TAG="AppUpdateCheckerLibrary";





    private Activity activity;
    public AppUpdateChecker(Activity activity) {
        this.activity = activity;

    }



    //current version of app installed in the device
    private String getCurrentVersion(){
        PackageManager pm = activity.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(activity.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return pInfo.versionName;
    }
    private class GetLatestVersion extends AsyncTask<String, String, String> {
        private String latestVersion;
        private ProgressDialog progressDialog;
        private boolean manualCheck;
        GetLatestVersion(boolean manualCheck) {
            this.manualCheck = manualCheck;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String currentVersion = getCurrentVersion();
            Log.d(TAG,"Current Version Installed= "+currentVersion);
            //If the versions are not the same
            if(!currentVersion.equals(latestVersion)&&latestVersion!=null){

             /*   final Dialog epicDialog=new Dialog(activity);
                epicDialog.setContentView(R.layout.app_update_layout);
                ImageView btnYes=epicDialog.findViewById(R.id.yesUpdate);
                ImageView btnNo=epicDialog.findViewById(R.id.noUpdate);
                epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                epicDialog.show();
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(FragmentMain.this,"Yes",Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
                        epicDialog.dismiss();


                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(FragmentMain.this,"No",Toast.LENGTH_SHORT).show();
                        epicDialog.dismiss();

                    }
                });

*/



                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("An Update is Available");
                builder.setMessage("Its better to update now");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel button action
                    }
                });
                builder.setCancelable(false);
                builder.show();



            }else {
                if (manualCheck) {
                    Toast.makeText(activity, "No Update Available", Toast.LENGTH_SHORT).show();
                }
            }
            //


        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (manualCheck) {
                progressDialog=new ProgressDialog(activity);
                progressDialog.setMessage("Checking For Update.....");
                progressDialog.setCancelable(false);
                progressDialog.show();



            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
               latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
               Log.d(TAG,"Latest Version from Play Store= "+latestVersion);
                return latestVersion;
            } catch (Exception e) {
                return latestVersion;
            }
        }
    }
    public void checkForUpdate(boolean manualCheck)
    {
        new GetLatestVersion(manualCheck).execute();
    }
}
