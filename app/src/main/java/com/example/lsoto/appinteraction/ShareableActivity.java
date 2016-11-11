package com.example.lsoto.appinteraction;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//  if your app can perform an action that might be useful to another app, your app should
// be prepared to respond to action requests from other apps. For instance, if you build a social
// app that can share messages or photos with the user's friends, it's in your best interest to
// support the ACTION_SEND intent so users can initiate a "share" action from another app and
// launch your app to perform the action.

// To allow other apps to start your activity, you need to add an <intent-filter> element in
// your manifest file for the corresponding <activity> element.
public class ShareableActivity extends Activity {

    @Override
    // In order to decide what action to take in your activity, you can read the
    // Intent that was used to start it.
    // As your activity starts, call getIntent() to retrieve the Intent that started the activity.
    // You can do so at any time during the lifecycle of the activity, but you should generally do
    // so during early callbacks such as onCreate() or onStart().
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareable);

        // Get the intent that started this activity
        Intent intent = getIntent();
        Uri data = intent.getData();

        // Figure out what to do based on the intent type
        if (intent.getType().indexOf("image/") != -1) {
            // Handle intents with image data ...
        } else if (intent.getType().equals("text/plain")) {
            // Handle intents with text ...
        }
    }

    // If you want to return a result to the activity that invoked yours, simply call setResult()
    // to specify the result code and result Intent. When your operation is done and the user
    // should return to the original activity, call finish() to close (and destroy) your activity.
    // You must always specify a result code with the result.
    // Generally, it's either RESULT_OK or RESULT_CANCELED.
    // You can then provide additional data with an Intent, as necessary.
    // There's no need to check whether your activity was started with
    // startActivity() or startActivityForResult(). Simply call setResult() if the intent that
    // started your activity might expect a result. If the originating activity had called
    // startActivityForResult(), then the system delivers it the result you supply to setResult();
    // otherwise, the result is ignored.
    public void ReturnResultToActivity(){
        // Create intent to deliver some kind of result data
        Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri"));
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
