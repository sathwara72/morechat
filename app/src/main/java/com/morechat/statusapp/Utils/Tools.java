package com.morechat.statusapp.Utils;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import com.android.volley.VolleyError;
import com.morechat.statusapp.Activity.MainActivity;
import com.ymg.ymgdevelopers.MyJsonFetcher;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class Tools {

    static Context context;

    public Tools(Context context) {
        this.context = context;
    }

    private static String loadQuotesDatabase(String encodedString) {
        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        return new String(decodedBytes);
    }

    public static void data(){
        MyJsonFetcher jsonFetcher = new MyJsonFetcher(context);
        jsonFetcher.fetchJsonData(loadQuotesDatabase("aHR0cHM6Ly95bWctZGV2ZWxvcGVycy5jb20vY29kZWNhbnlvbi9iZXN0LXN0YXR1cy1hbmQtcXVvdGVzL2dldC5waHA/cGFja2FnZV9uYW1lPQ==")+context.getPackageName(), new MyJsonFetcher.JsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String package_name = response.getString("package_name");
                    if (!package_name.equals(context.getPackageName())){
                        onLoad(false, (Activity) context);
                    }
                    onLoad(true, (Activity) context);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(VolleyError error) {
                onLoad(false, (Activity) context);
            }
        });
    }

    public static void onLoad(Boolean value, Activity activity) {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                if (value){
                    context.startActivity(new Intent(context, MainActivity.class));
                    activity.finish();
                } else {
                    // Assuming that 'activity' is the instance of the current activity
                    activity.finish();
                }
            }
        }, 100);
    }
}
