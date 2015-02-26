package com.disney.encodeearly;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

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

    private void handleIntent(Intent intent) {
        //TODO handle intent
    }

}
