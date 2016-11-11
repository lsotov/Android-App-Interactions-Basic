package com.example.lsoto.appinteraction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends Activity {
    static final int PICK_CONTACT_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Dial(View view){
        // Implicit intents do not declare the class name of the component to start, but instead
        // declare an action to perform. The action specifies the thing you want to do,
        // such as view, edit, send, or get something.
        // Depending on the intent you want to create, the data might be a Uri, one of several
        // other data types, or the intent might not need data at all.
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        int hasPerm = checkCallingPermission(Manifest.permission.CALL_PHONE);
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(callIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        // If isIntentSafe is true, then at least one app will respond to the intent.
        // If it is false, then there aren't any apps to handle the intent.
        boolean isIntentSafe = activities.size() > 0;
        if (hasPerm == PERMISSION_GRANTED && isIntentSafe){
            startActivity(callIntent);
        } else {

        }
    }

    public void Browse(View view){
        // Although the Android platform guarantees that certain intents will resolve to one of
        // the built-in apps (such as the Phone, Email, or Calendar app), you should always
        // include a verification step before invoking an intent.
        Uri webpage = Uri.parse("http://www.android.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        // To verify there is an activity available that can respond to the intent,
        // call queryIntentActivities() to get a list of activities capable of handling your Intent.
        // If the returned List is not empty, you can safely use the intent.
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        // If isIntentSafe is true, then at least one app will respond to the intent.
        // If it is false, then there aren't any apps to handle the intent.
        boolean isIntentSafe = activities.size() > 0;
        // You should perform this check when your activity first starts in case you need to
        // disable the feature that uses the intent before the user attempts to use it.
        // If you know of a specific app that can handle the intent, you can also provide a link
        // for the user to download the app
        if (isIntentSafe) {
            startActivity(webIntent);
        }
    }

    public void BrowseWithChooser(View view){
        // Although the Android platform guarantees that certain intents will resolve to one of
        // the built-in apps (such as the Phone, Email, or Calendar app), you should always
        // include a verification step before invoking an intent.
        Uri webpage = Uri.parse("http://www.android.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent,
                PackageManager.MATCH_ALL);
        // If isIntentSafe is true, then at least one app will respond to the intent.
        // If it is false, then there aren't any apps to handle the intent.
        boolean isIntentSafe = activities.size() > 0;
        // Always use string resources for UI text.
        // This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(webIntent, title);
        // Verify the intent will resolve to at least one activity
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void Map(View view){
        Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void SendEmail(View view){
        // Other kinds of implicit intents require "extra" data that provide different data types,
        // such as a string. You can add one or more pieces of extra data using
        // the various putExtra() methods.
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
    }

    // Starting another activity doesn't have to be one-way. You can also start another activity
    // and receive a result back.
    // To receive a result, call startActivityForResult() (instead of startActivity()).
    private void pickContact() {
        // There's nothing special about the Intent object you use when starting an activity for a
        // result, but you do need to pass an additional integer argument to the
        // startActivityForResult() method.

        // The integer argument is a "request code" that identifies your request
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    // When the user is done with the subsequent activity and returns, the system calls your
    // activity's onActivityResult() method. This method includes three arguments:

    // - The request code you passed to startActivityForResult().
    // - A result code specified by the second activity. This is either RESULT_OK if the operation
    //     was successful or RESULT_CANCELED if the user backed out or the operation failed for some reason.
    // - An Intent that carries the result data.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                // Do something with the phone number...
            }
        }
    }
}
