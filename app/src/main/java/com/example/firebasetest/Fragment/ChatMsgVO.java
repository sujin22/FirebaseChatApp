package com.example.firebasetest.Fragment;

//채팅 메시지를 담을 클래스
public class ChatMsgVO {
    private String userid;
    private String crt_dt;
    private String content;

    public ChatMsgVO(){

    }

    public ChatMsgVO(String userid, String crt_dt, String content){
        this.userid = userid;
        this.crt_dt = crt_dt;
        this.content = content;
    }

    public String getUserid(){
        return userid;
    }

    public String getCrt_dt(){
        return crt_dt;
    }

    public String getContent(){
        return content;
    }

    @Override
    public String toString(){
        return "ChatMsgVO(" +
                "userid = '"+ userid +'\'' +
                "crt_dt= '" + crt_dt +'\'' +
                "content= '" + content + '\'' +
                ")";
    }
}
