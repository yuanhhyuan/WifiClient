package com.hy.client;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 采用TCP协议的socket，在wifi形成的局域网内，创建客户端
 */
public class MainActivity extends ActionBarActivity {

    String TAG = "060_MainActivity";
    private TextView tvServerMessage;
    Button send;
    final private String SERVER_PORT = "8080";
    int id=0;
    private String Client_Name = "Bobby";
    EditText textS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WifiManager myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        tvServerMessage = (TextView) findViewById(R.id.textViewServerMessage);
        textS = (EditText)findViewById(R.id.editText1);
        send =(Button) findViewById(R.id.button1);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientAsyncTask clientAST = new ClientAsyncTask();
                clientAST.execute(new String[] {
                        intToIP(myWifiManager.getDhcpInfo().gateway), SERVER_PORT,
                        Client_Name + " : " + textS.getText().toString() });
            }
        });
    }

    public String intToIP(int i) {
        return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF));
    }


    /**
     * AsyncTask which handles the communication with the server
     *
     * 双工
     */
    class ClientAsyncTask extends AsyncTask<String, Void, String> {

        //在新的工作线程中执行与Server数据交互任务
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                Log.e(TAG, "ServerAsyncTask doInBackground");
                //根据IP和端口创建socket
                Socket socket = new Socket(params[0],
                        Integer.parseInt(params[1]));

                //向Server传输数据
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(params[2]);

                //获取Server传输过来的数据
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                result = br.readLine();
                Log.e(TAG, "result : " + result);

                //关闭socket
                socket.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "ServerAsyncTask onPostExecute , s : " + s);
            tvServerMessage.setText(s);
        }
    }
}
