package com.akshaybengani.barcodereader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ezvcard.Ezvcard;
import ezvcard.VCard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.Arrays;
import java.util.List;

public class CodeResult extends AppCompatActivity implements View.OnClickListener{

    private static final int DATARESULT = 7346;
    TextView barcodeResult;
    String stringvalue = "null";
    // share browser phone email geo contact calender wifi
    ImageButton shareButton,browserButton,phoneButton,emailButton,geoButton,contactButton,calenderButton,wifiButton,scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_result);

        shareButton = findViewById(R.id.sharebutton);
        browserButton = findViewById(R.id.browserbutton);
        phoneButton = findViewById(R.id.phonebutton);
        emailButton = findViewById(R.id.emailbutton);
        geoButton = findViewById(R.id.geobutton);
        contactButton = findViewById(R.id.contactbutton);
        calenderButton = findViewById(R.id.calenderbutton);
        wifiButton = findViewById(R.id.wifibutton);
        scanButton = findViewById(R.id.scanButton);

        shareButton.setOnClickListener(this);
        browserButton.setOnClickListener(this);
        phoneButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);
        geoButton.setOnClickListener(this);
        contactButton.setOnClickListener(this);
        calenderButton.setOnClickListener(this);
        wifiButton.setOnClickListener(this);
        scanButton.setOnClickListener(this);

        shareButton.setEnabled(false);
        browserButton.setEnabled(false);
        phoneButton.setEnabled(false);
        emailButton.setEnabled(false);
        geoButton.setEnabled(false);
        contactButton.setEnabled(false);
        calenderButton.setEnabled(false);
        wifiButton.setEnabled(false);

        barcodeResult = findViewById(R.id.barcodeResult);
        Intent intent = new Intent(this,CameraPreview.class);
        startActivityForResult(intent,DATARESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (DATARESULT == requestCode)
        {

            if (data != null)
            {
                Barcode barcode = data.getParcelableExtra("Barcode");
                stringvalue = data.getStringExtra("Raw Value");
                barcodeResult.setText(stringvalue);
                dataOperations(stringvalue,barcode);
            }
            else
            {
                barcodeResult.setText("No Barcode Value");
            }

        }
       /* if (requestCode == 17667364){

            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(this,"Contact Saved",Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Cancelled Contact Save",Toast.LENGTH_SHORT).show();
            }
        }
        */

    }// end of onActivityResult



    private void dataOperations(String stringvalue,Barcode barcode) {
        // DO Clipboard and other operations from here
        // share browser phone email geo contact calender wifi
        if (stringvalue != null)
        {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("QrCodeValue",stringvalue);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(CodeResult.this,"Text Copied to device Clipboard",Toast.LENGTH_SHORT).show();

            shareButton.setEnabled(true);
            shareButton.setBackgroundResource(R.drawable.shareicon);

        }
        // share browser phone email geo contact calender wifi

        if (Patterns.WEB_URL.matcher(stringvalue).matches())
        {
            // enable the web Browser icon button and set an intent to open default browser
            browserButton.setEnabled(true);
            browserButton.setBackgroundResource(R.drawable.browsericon);
        }
        if (Patterns.PHONE.matcher(stringvalue).matches())
        {
            // enable the phone icon button and set an intent to open phone dialer
            phoneButton.setEnabled(true);
            phoneButton.setBackgroundResource(R.drawable.phoneicon);
        }
        int type = barcode.valueFormat;
        if (type == Barcode.EMAIL)
        {
            // enable the email icon button and set an intent to open email application
            emailButton.setEnabled(true);
            emailButton.setBackgroundResource(R.drawable.emailicon);
        }
        if (type == Barcode.GEO)
        {
            geoButton.setEnabled(true);
            geoButton.setBackgroundResource(R.drawable.location);
        }
        if (type == Barcode.CONTACT_INFO)
        {
            // enable the contact icon button and set an intent to save the contact to your device
            contactButton.setEnabled(true);
            contactButton.setBackgroundResource(R.drawable.contact);
        }
        if (type == Barcode.CALENDAR_EVENT)
        {
            calenderButton.setEnabled(true);
            calenderButton.setBackgroundResource(R.drawable.calender);
        }
        if (type == Barcode.WIFI)
        {
            wifiButton.setEnabled(true);
            wifiButton.setBackgroundResource(R.drawable.wifi);
        }

    }


    @Override
    public void onClick(View v) {
        // share browser phone email geo contact calender wifi

        switch (v.getId())
        {
            case R.id.sharebutton:
                // share intent
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,stringvalue);
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case R.id.browserbutton:
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(stringvalue));
                startActivity(webIntent);
                break;
            case R.id.phonebutton:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+stringvalue));
                startActivity(callIntent);
                break;
            case R.id.emailbutton:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,stringvalue);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailIntent,"Send Email"));
                break;
            case R.id.geobutton:
                Intent geoIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(stringvalue));
                geoIntent.setPackage("com.google.android.apps.maps");
                startActivity(geoIntent);
                break;
            case R.id.contactbutton:
                saveAsAContact(stringvalue);
                break;
            case R.id.calenderbutton:
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                startActivity(calIntent);
                break;
            case R.id.wifibutton:
                String[] wifiElement;
                wifiElement = stringvalue.replace("WIFI:", "").split(";");
                String ssid = getParam(wifiElement[0]);
                String type = getParam(wifiElement[1]);
                String pass = getParam(wifiElement[2]);
                Log.d("type ssid and pass","Type: "+type+" SSID: "+ssid+" Pass: "+pass);
                connectToWIFI(type,ssid,pass);
                break;
            case R.id.scanButton:
                Intent intent1 = new Intent(CodeResult.this,CodeResult.class);
                startActivity(intent1);
                break;

        }// end of switch

    }

    private String getParam(String s) {
        return s.split(":")[1];
    }


    private void connectToWIFI(String type, String ssid, String pass) {

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        switch (type) {
            case "WEP":
                wifiConfiguration.wepKeys[0] = "\"" + pass + "\"";
                wifiConfiguration.wepTxKeyIndex = 0;
                wifiConfiguration.allowedKeyManagement.set(KeyMgmt.NONE);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case "WPA":
                wifiConfiguration.preSharedKey = "\"" + pass + "\"";
                break;
            case "nopass":
                wifiConfiguration.allowedKeyManagement.set(KeyMgmt.NONE);
                break;
        }
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(wifiConfiguration);

                wifiManager.disconnect();
                wifiManager.enableNetwork(wifiConfiguration.networkId,true);
                wifiManager.reconnect();
                Toast.makeText(CodeResult.this,"Network Profile Added",Toast.LENGTH_SHORT).show();

    }

    private void saveAsAContact(String stringvalue) {

        VCard vCard = Ezvcard.parse(stringvalue).first();
        String fname = vCard.getStructuredName().getGiven();
        String lname = vCard.getStructuredName().getFamily();
        String name = vCard.getFormattedName().getValue();
        String address = " ";
        if (!(vCard.getAddresses().isEmpty())){
            address = addressBuilder(vCard);
        }
        String email = vCard.getEmails().get(0).getValue();
        String phone = vCard.getTelephoneNumbers().get(0).getText();
        String website = vCard.getUrls().get(0).getValue();
        Log.d("fname",fname);
        Log.d("lname",lname);
        Log.d("address",address);
        Log.d("email",email);
        Log.d("phone",phone);
        Log.d("website",website);

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME,name);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL,address);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL,email);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,phone);
        intent.putExtra(ContactsContract.Intents.Insert.NOTES,website);
        startActivity(intent);

    }

    private String addressBuilder(VCard vCard) {
        String address = " ";
        address = address + vCard.getAddresses().get(0).getStreetAddress()+" "+vCard.getAddresses().get(0).getLocality()+" "+vCard.getAddresses().get(0).getCountry()+" "+vCard.getAddresses().get(0).getPostalCode();
        return address;
    }


}// end of class

