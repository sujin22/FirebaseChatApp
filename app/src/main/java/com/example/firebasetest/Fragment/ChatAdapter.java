package com.example.firebasetest.Fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firebasetest.PreferenceManager;
import com.example.firebasetest.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<ChatMsgVO> mValues;
    private final String my_user_id;

    public ChatAdapter(List<ChatMsgVO> items, String my_user_id){
        mValues = items;
        this.my_user_id = my_user_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //메시지 itemView 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_chat_msg, parent, false);
        //해당 itemView를 가진 ViewHolder 반환
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ChatMsgVO vo = mValues.get(position);

        //참고 예제에서는 다른 기능으로 userid가 존재해서 그걸 받아옴(my_user_id라고 하겠음)
        if(mValues.get(position).getUserid().equals(my_user_id)){
            //my_user_id와 해당 메시지의 user_id가 같을 경우
            holder.other_cl.setVisibility(View.GONE);
            holder.my_cl.setVisibility(View.VISIBLE);

            holder.my_date_tv.setText(vo.getCrt_dt());
            holder.my_content_tv.setText(vo.getContent());
        }else{
            holder.other_cl.setVisibility(View.VISIBLE);
            holder.my_cl.setVisibility(View.GONE);

            holder.other_userid_tv.setText(vo.getUserid());
            holder.other_date_tv.setText(vo.getCrt_dt());

            holder.other_content_tv.setText(vo.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout my_cl, other_cl;
        public TextView other_userid_tv, other_date_tv, other_content_tv, my_date_tv, my_content_tv;

        public ViewHolder(View view) {
            super(view);
            my_cl = view.findViewById(R.id.my_cl);
            other_cl = view.findViewById(R.id.other_cl);

            other_userid_tv = view.findViewById(R.id.other_userid_tv);
            other_date_tv = view.findViewById(R.id.other_date_tv);
            other_content_tv = view.findViewById(R.id.other_content_tv);

            my_date_tv = view.findViewById(R.id.my_date_tv);
            my_content_tv = view.findViewById(R.id.my_content_tv);
        }
    }
}