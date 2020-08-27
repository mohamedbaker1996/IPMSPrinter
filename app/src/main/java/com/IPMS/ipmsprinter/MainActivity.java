package com.IPMS.ipmsprinter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

//import java.awt.Button;

public class MainActivity extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private WebView myWebView;
    public TextView webtxt ;
   // private ESCPOSPrinter posPtr;
   // public String unicode = "\u0048\u0065\u006Cl\u006F";

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        myWebView = findViewById(R.id.webView);
        // WebSettings webSettings = myWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        // WebView webView = new WebView( context );
        // webSettings.setAppCacheMaxSize( 10 * 1024 * 1024 ); // 10MB
        //   webSettings.setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
        // webSettings.setAllowFileAccess( true );
        //webSettings.setAppCacheEnabled( true );
        //webSettings.setJavaScriptEnabled( true );
        //webSettings.setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default


        myWebView.loadUrl("http://movep.ipmserp.com/reports/invoice_order.aspx?id=1");
        myWebView.setWebViewClient(new WebViewClient());
        //get text from web view
        getBodyText();
        webtxt = (TextView) findViewById(R.id.webtxt);


        mScan = findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(MainActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mPrint = findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

                Thread t = new Thread() {
                    public void run() {
                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            // String BillWebView = myWebView.toString();
                            String BILL = "";


                            BILL = "                   IPMS Software   \n"
                                    + "                   XX.AA.BB.CC.     \n " +
                                    "                 NO 25 ABC ABCDE    \n" +
                                    "                  XXXXX YYYYYY      \n" +
                                    "                   MMM 590019091      \n"
                                    //StandardCharsets.UTF_16LE

                                    + String.format(webtxt.getText().toString().trim());
                    /*       StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(webtxt);
                           // stringBuilder.append("\u0649");
                            webtxt.append("\u0649");
                          //  BILL = stringBuilder.toString();
                           // webtxt.setText(webtxt.toString());
                          BILL = "/n"+ webtxt.getText().toString();
                                                                        */

                            // BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Totel");
                            //BILL = BILL + "\n";
                            //BILL = BILL
                              //      + "-----------------------------------------------";
                         //   BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00");
                           // BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
                            //BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
                            //BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

                           // BILL = BILL
                             //       + "\n-----------------------------------------------";
                            //BILL = BILL + "\n\n ";

                           // BILL = BILL + "                   Total Qty:" + "      " + "85" + "\n";
                           // BILL = BILL + "                   Total Value:" + "     " + "700.00" + "\n";

                           // BILL = BILL
                             //       + "-----------------------------------------------\n";
                            BILL = BILL + "\n\n ";

                            // os.write(webtxt.getBytes() );
                            //.getBytes(StandardCharsets.UTF_16LE)
                          //  StandardCharsets.UTF_8.encode(BILL);
                           // byte[] bytes;
                            //bytes = BILL.getBytes(UTF_8);
                           // BILL = BILL.getBytes(UTF_8);

                            os.write(BILL.getBytes(StandardCharsets.UTF_8));
                            //This is printer specific code you can comment ==== > Start

                            // Setting height
                            int gs = 29;
                            os.write(intToByteArray(gs));
                            int h = 104;
                            os.write(intToByteArray(h));
                            int n = 162;
                            os.write(intToByteArray(n));

                            // Setting Width
                            int gs_width = 29;
                            os.write(intToByteArray(gs_width));
                            int w = 119;
                            os.write(intToByteArray(w));
                            int n_width = 2;
                            os.write(intToByteArray(n_width));
                           // mBluetoothDevice.printAndroidFont("Arabic Font", nLineWidth, 24, ESCPOSConst.LK_ALIGNMENT_LEFT);


                        } catch (Exception e) {
                            Log.e("MainActivity", "Exe ", e);
                        }
                    }
                };
                t.start();
            }
        });

        mDisc = findViewById(R.id.dis);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();
            }
        });

    }// onCreate


    private String getBodyText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    String url = "http://movep.ipmserp.com/reports/invoice_order.aspx?id=1";//your website url
                    Document doc = Jsoup.connect(url).get();

                    Element body = doc.body();
                    builder.append(body.text());

                } catch (Exception e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        webtxt.getText().toString().getBytes(UTF_8);
                         // webtxt.append("\u0649");

                    webtxt.setText(builder.toString());





                        //webtxt.setText(builder.toString());

                    }
                });
            }
        }).start();


        //return webtxt;
        // }
       return String.valueOf(webtxt);
    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();

        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();

        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(MainActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(MainActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

}