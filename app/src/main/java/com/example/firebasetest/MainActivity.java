package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;

import com.example.firebasetest.Fragment.ChatRoomFragment;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, chatRoomFragment,"CHATROOM")
                .commit();

    }
}