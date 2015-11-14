package com.shyling.healthmanager.activity;

/**
 * Created by Mars on 2015/11/14.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.shyling.healthmanager.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChatActivity extends AppCompatActivity {

    private Button btn_send;
    private EditText massage;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        listView = (ListView) findViewById(R.id.listView);
        btn_send = (Button) findViewById(R.id.btn_sendMag);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                massage();
            }
        });
    }

    public void massage() {
        String strUrl = "服务器地址" + massage.getText().toString();
        URL url = null;
        try {
            url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String result = "";
            String line = null;
            while ((line = massage.getText().toString()) != null) {
                result += line;
            }
            inputStreamReader.close();
            httpURLConnection.disconnect();
            String[] data = new String[]{massage.getText().toString()};
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}