package com.example.wakeparkby.pchell.maveri.Chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wakeparkby.pchell.maveri.Database.DatabaseMeeting;
import com.example.wakeparkby.pchell.maveri.Meeting.Meeting;
import com.example.wakeparkby.pchell.maveri.ObserverMessage;
import com.example.wakeparkby.pchell.maveri.Profile.Profile;
import com.example.wakeparkby.pchell.maveri.Profile.ProfileFriend;
import com.example.wakeparkby.pchell.maveri.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * класс для обработки интерфейса чата
 */
public class ActivityChat extends AppCompatActivity implements View.OnClickListener {
    AdapterChat adapterChat = Profile.getInstance().getAdapterChat();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefMeessage;
    private ImageView sendButton;
    private ImageView selectPlaceButton;
    private EditText messageArea;
    private RecyclerView recyclerViewChat;
    private List<String> chatList;
    private HashMap<Integer, HashMap<String, String>> listMeetingChat;
    private String placeName;
    private String invateUserName;
    private String invateUserKey;
    private String date;
    private String coordinates;
    private final int IDD_THREE_BUTTONS = 0;
    private LatLng latLng = new LatLng(18.5259949, 109.3576236);
    protected MessageController messageController;


    ObserverMessage observer;

    /**
     * метод создания объекта
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        observer = new ObserverMessage("Chat") {

            /**
             * перегруженный метод обновления для частного экземпляра класса наблюдатель
             * в данном случае при обновлении обновляется либо основной чат, либо чат встреч
             */

            @Override
            public void update() {
                int n = observer.getStatus();
                if (n == 10) {

                    if (observer.getId() == 1) {
                        refreshChat();
                        observer.setId(0);
                    } else {
                    }
                    if (observer.getId() == 2) {
                        refreshChatMeeting();
                        observer.setId(0);
                    } else {
                    }
                }
            }


        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerViewChat = findViewById(R.id.chatWindow);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        sendButton.setOnClickListener(this);
         messageController = new MessageController();
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(messageController);
        // adapterChat.getListMessage(profile.getUserKey(),);
        //groupId = adapterChat.getGroupId();
        selectPlaceButton = findViewById(R.id.placeButton);
        selectPlaceButton.setOnClickListener(this);
        int a=observer.getId();




        System.out.print("");

    }


    /**
     * метод обновляющий чат
     */
    public void refreshChat() {

        String messageUserName = null;
        String messageDate = null;
        String messageUser = null;
        chatList = Profile.getInstance().getAdapterChat().getListMessage().getMessages();
        messageController.messageList.clear();
        for (int i = 0; i < chatList.size(); i++){
            String message = chatList.get(i);
            int fl = 0;
            int fl1 = 0;
            for (int j = 0 ; j< message.length(); j++){

                if (String.valueOf(message.charAt(j)).equals(" "))
                {
                    fl++;
                    if (fl == 1) {
                        messageUserName = message.substring(0, j);
                    }
                    if (fl == 1){
                        fl1 = j;
                    }
                    if (fl == 3){
                        messageDate = message.substring(fl1,j);
                        messageUser = message.substring(j+1,message.length());
                        System.out.print("");
                    }

                }
            }
            if (messageUserName.equals(Profile.getInstance().getFirstName())){
                messageController.messageList.add(new Message(messageUser, messageDate , true));
            }else {
                messageController.messageList.add(new Message(messageUser, messageDate , false));
            }
        }
       int i= messageController.getItemCount();
        messageController.notifyDataSetChanged();


        // chatList.clear();
    }

    /**
     * метод обновляющий чат встреч
     */
    public void refreshChatMeeting() {
        listMeetingChat = Profile.getInstance().getAdapterChat().getlistMeetingChat().getListMeetingChat();
        if (listMeetingChat.size() != 0) {
            for (Map.Entry entry : listMeetingChat.entrySet()) {
                int key = (int) entry.getKey();
                //values1.add((String[]) entry.getValue());
                this.invateUserKey = listMeetingChat.get(key).get("UserKey");
                this.placeName = listMeetingChat.get(key).get("PlaceName");
                this.date = listMeetingChat.get(key).get("Date");
                this.coordinates = listMeetingChat.get(key).get("LatLng");
                this.invateUserName = listMeetingChat.get(key).get("UserName");
                showDialog(IDD_THREE_BUTTONS);
            }
        }
    }

    /**
     * метод для обработки нажатия на кнопку
     * @param v сигнал нажатия
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendButton: {
                String messageText = messageArea.getText().toString();
                adapterChat.sendMessage(messageText);
                messageArea.setText("");
                break;
            }

            case R.id.placeButton: {
                AdapterChat.startActivityMap(this);
                observer.removeFromList(observer);
                // showDialog(IDD_THREE_BUTTONS);

                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        observer.removeFromList(observer);
    }

    /**
     * метод для обработки пришедшего предложения о встрече
     * @param id код встречи
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_THREE_BUTTONS:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(invateUserName + " предложил встречу");
                builder.setMessage("Дата: " + date + System.lineSeparator() + "Место: " + placeName)
                        .setCancelable(false)
                        .setPositiveButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Место на карте",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        startActivityMaps(coordinates);
                                    }
                                })
                        .setNeutralButton("Согласиться",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        DatabaseMeeting databaseMeeting = new DatabaseMeeting();
                                        databaseMeeting.addNewMeetingUser(invateUserKey,invateUserName,coordinates,date,placeName);
                                        databaseMeeting.addNewMeetingInvateUser(invateUserKey,invateUserName,coordinates,date,placeName);
                                        databaseMeeting.removeMeetingChat(invateUserKey);
                                    }
                                });

                return builder.create();
            default:
                return null;
        }
    }

    /**
     * метод создающий окно выбора встреч
     * @param coordinates
     */
    private void startActivityMaps(String coordinates) {
        AdapterChat.startActivityMeetingOnMaps(this, coordinates);
    }


}
