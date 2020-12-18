package com.example.firebasetest.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebasetest.PreferenceManager;
import com.example.firebasetest.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ChatMsgFragment extends Fragment {
    //로그용 TAG
    private final String TAG = getClass().getSimpleName();

    //전체 constraintLayout(화면크기계산용)
    ConstraintLayout constraintLayout;
    boolean isKeyboardUp = false;

    //채팅을 입력할 입력창과 전송 버튼
    EditText content_et;
    ImageView send_img;

    //채팅 내용을 뿌려줄 RecycleView와 Adapter
    RecyclerView rv;
    ChatAdapter mAdapter;

    //채팅 방 이름
    String chatroom = "";

    //채팅 내용을 담을 배열
    List<ChatMsgVO> msgList = new ArrayList<>();

    //FirebaseDatabase 연결용 객체들
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    //임시 my_user_id
    String my_user_id = "";




    public ChatMsgFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChatMsgFragment newInstance(int columnCount) {
        ChatMsgFragment fragment = new ChatMsgFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_msg, container, false);

        content_et = view.findViewById(R.id.content_et);
        send_img = view.findViewById(R.id.send_img);

        rv = view.findViewById(R.id.recyclerView);

        my_user_id = PreferenceManager.getString(getActivity(),"my_user_id");

        constraintLayout = (ConstraintLayout)view.findViewById(R.id.constraintLayout);
        constraintLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        int rootViewHeight = constraintLayout.getRootView().getHeight();
                        int constraintLayoutHeight = constraintLayout.getHeight();
                        int diff = rootViewHeight - constraintLayoutHeight ;
                        Log.d("키보드","diff: "+diff +" / rootViewHeight: "+ rootViewHeight + "/ constHeight: " +constraintLayoutHeight);

                        if(diff > 500){
                            if(!isKeyboardUp){
                                rv.scrollToPosition(msgList.size() -1);
                                isKeyboardUp = true;
                                Toast.makeText(getActivity(), "키보드가 위로 올라왔습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            isKeyboardUp = false;
                            Toast.makeText(getActivity(), "키보드가 내려갔습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        //전송 버튼 클릭 리스너 설정
        send_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content_et.getText().toString().trim().length() >= 1){
                    Log.d(TAG, "입력처리");

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    //Database에 저장할 객체 만들기
                    ChatMsgVO msgVO = new ChatMsgVO(my_user_id, df.format(new Date()).toString(), content_et.getText().toString().trim());

                    //해당 DB에 값 저장시키기
                    myRef.push().setValue(msgVO);

                    //입력 필드 초기화
                    content_et.setText("");
                }else{
                    Toast.makeText(getActivity(),"메시지를 입력하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //ChatRoomFragment에서 받는 채팅방 이름
        chatroom = getArguments().getString("chatroom");

        mAdapter = new ChatAdapter(msgList,my_user_id);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(mAdapter);

        //Firebase Database 초기화
        myRef = database.getReference(chatroom);

        //Firebase Database Listener 붙이기
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Firebase의 해당 DB에 값이 추가될 경우 호출, 생성 후 최초 1번은 실행됨
                Log.d(TAG, "onChild added");
                Log.d(TAG, "onChild = "+ snapshot.getValue(ChatMsgVO.class).toString()); //오버라이드해둔 toString

                //Database의 정보를 ChatMsgVO 객체에 담음
                ChatMsgVO chatMsgVO = snapshot.getValue(ChatMsgVO.class);
                msgList.add(chatMsgVO);

                //채팅 메시지 배열에 담고 RecyclerView 다시 그리기
                mAdapter = new ChatAdapter(msgList, my_user_id);
                rv.setAdapter(mAdapter);

                rv.scrollToPosition(msgList.size() -1);
                Log.d(TAG, msgList.size()+"");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //TODO: 데이터베이스에서 받아와서 MsgList에 추가하고, RecyclerView 다시 그리기
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //TODO: 데이터베이스에서 받아와서 MsgList에 추가하고, RecyclerView 다시 그리기
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d(TAG, "chatroom = "+chatroom);

        return view;
    }
}