package com.disney.encodeearly;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView_explanation);

        mNfcAdapater = NfcAdapter.getDefaultAdapter(this);

        if(mNfcAdapater == null) {
            //Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(!mNfcAdapater.isEnabled()) {
            mTextView.setText("NFC is disabled");
        } else {
            mTextView.setText(R.string.explanation);
        }

        handleIntent(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();

        /**It's important that the activity is in teh foreground (resumed) otherwise
         * an Illegal StateException is thrown
         */
        setupForegroundDispatch(this, mNfcAdapater);
    }


    @Override
    protected void onPause() {
        /**Call this before onPause otherwise an IllegalArgument Exception is thrown as well */
        stopForegroundDispatch(this, mNfcAdapater);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /** This method gets called when a new Intetn gets associated with the current
         *  activity instance. Instead of creating a new activity, onNewIntent will be called
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //TODO handle intent
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        //Notice that this is the same filter as in our manifest
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity the corresponding {@link BaseActivity} requesting to stop the foreground dispatch
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

}
