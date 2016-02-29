package com.shyling.healthmanager.activity;

/**
 * 聊天页面
 * Created by Mars on 2015/11/14.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.shyling.healthmanager.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChatActivity extends AppCompatActivity {

    private Button btn_send;
    private EditText et_message;
    private ListView listView;
    //    private ArrayAdapter<String> adapter;
    private EMConversation conversation;
    private String toChatUserName;
    private DataAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);

//        toChatUserName = getIntent().getStringExtra("userId");
        toChatUserName = "test2";
        listView = (ListView) findViewById(R.id.listView);
        et_message = (EditText) findViewById(R.id.et_massage);
        btn_send = (Button) findViewById(R.id.btn_sendMag);

        conversation = EMChatManager.getInstance().getConversation(toChatUserName);
        adapter = new DataAdapter();
        listView.setAdapter(adapter);

//        注册接收新消息的监听广播
        NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

//        最后要通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                message();

                //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                EMConversation conversation = EMChatManager.getInstance().getConversation(toChatUserName);
                //创建一条文本消息
                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                //如果是群聊，设置chattype,默认是单聊
//                message.setChatType(EMMessage.ChatType.GroupChat);
                //设置消息body
                TextMessageBody txtBody = new TextMessageBody(et_message.getText().toString());
                message.addBody(txtBody);
                //设置接收人
                message.setReceipt(toChatUserName);
                //把消息加入到此会话对象中
                conversation.addMessage(message);

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                listView.setSelection(listView.getCount() - 1);
                et_message.setText("");
                //发送消息
                EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    //    注册接收新消息的监听广播
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 注销广播
            abortBroadcast();

            // 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
            String msgId = intent.getStringExtra("msgid");
            //发送方
            String username = intent.getStringExtra("from");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            EMConversation conversation = EMChatManager.getInstance().getConversation(username);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(username)) {
                // 消息不是发给当前会话，return
                return;
            }

            conversation.addMessage(message);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setSelection(listView.getCount() - 1);
        }
    }

    private class DataAdapter extends BaseAdapter {

        TextView textView;

        @Override
        public int getCount() {
            return conversation.getAllMessages().size();
        }

        @Override
        public Object getItem(int position) {
            return conversation.getAllMessages().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EMMessage message = conversation.getAllMessages().get(position);
            TextMessageBody body = (TextMessageBody) message.getBody();
            if (message.direct == EMMessage.Direct.RECEIVE) {
                if (message.getType() == EMMessage.Type.TXT) {
                    convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.row_received_message, null);
                    textView = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                    textView.setText(message.getFrom());
                } else {
                    if (message.getType() == EMMessage.Type.TXT)
                        convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.row_sent_message, null);
                }
                TextView textViewContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                textViewContent.setText(body.getMessage());
            }
            return convertView;
        }
    }

//    public void message() {
//        String strUrl = "服务器地址" + massage.getText().toString();
//        URL url = null;
//        try {
//            url = new URL(strUrl);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
//
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String result = "";
//            String line = null;
//            while ((line = massage.getText().toString()) != null) {
//                result += line;
//            }
//            inputStreamReader.close();
//            httpURLConnection.disconnect();
//            String[] data = new String[]{massage.getText().toString()};
//            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
//            listView.setAdapter(adapter);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}