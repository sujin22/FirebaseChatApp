package com.example.firebasetest.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebasetest.PreferenceManager;
import com.example.firebasetest.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRoomFragment extends Fragment{
    private static final String TAG = "ChatRoomFragment";
    private static final int MY_USER_ID_ET = 0;
    ImageView profile_img;

    EditText chatroom_et;
    Button enter_btn;


    public ChatRoomFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatRoomFragment newInstance(String param1, String param2) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);

        //user_id 입력창 실행 버튼
        profile_img = rootView.findViewById(R.id.profile_img);
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileAlert();
            }
        });

        //chatroom 이름 입력 editText
        chatroom_et = rootView.findViewById(R.id.chatroom_et);

        //chatroom 입장 버튼
        enter_btn = rootView.findViewById(R.id.enter_btn);
        enter_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(chatroom_et.getText().toString().trim().length()>=1 &&
                        PreferenceManager.getString(getActivity(),"my_user_id").length()>=1){
                    //chatroom, user_id 둘 다 입력되었으면

                    Log.d(TAG, "입장처리");

                    //원하는 데이터를 담을 객체
                    Bundle argu = new Bundle();
                    argu.putString("chatroom", chatroom_et.getText().toString());

                    //이동할 Fragment 선언
                    ChatMsgFragment chatMsgFragment = new ChatMsgFragment();

                    //이동할 Fragment에 데이터 객체 담기
                    chatMsgFragment.setArguments(argu);

                    //chatMsgFragment로 이동
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment,chatMsgFragment, "CHATMSG")
                            .addToBackStack(null)
                            .commit();
                }else{
                    if(chatroom_et.getText().toString().trim().length()<1)
                        Toast.makeText(getActivity(),"채팅방 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    else if(PreferenceManager.getString(getActivity(),"my_user_id").length()<1){
                        Toast.makeText(getActivity(),"user id를 등록하세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }

    public void showProfileAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("사용자 이름 설정");
        builder.setView(R.layout.alert_dialog_user_profile);

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(),"userID 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        final AlertDialog dialog = builder.show();

        final EditText editText = dialog.findViewById(R.id.my_user_id_et);
        //기존 등록된 user_id editText에 입력되어있도록
        editText.setText(PreferenceManager.getString(getActivity(),"my_user_id"));

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"사용할 ID를 입력하세요",Toast.LENGTH_SHORT).show();
                }else{
                    //TODO: user_id 바뀔 때, DB 내용도 바꾸어주는 코드 작성 필요
                    PreferenceManager.setString(getActivity(),"my_user_id", editText.getText().toString());

                    Toast.makeText(getActivity(),"userID 설정이 "+
                            PreferenceManager.getString(getActivity(),"my_user_id") +
                            "로 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }
    public void UpdateUserIdInDB(String user_id, String new_user_id){
        //FirebaseDatabase 연결용 객체들
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        //TODO: Database 내부 데이터의 user_id가 (인자)user_id일 경우, new_user_id로 바꿔주는 코드 작성 필요
    }
}