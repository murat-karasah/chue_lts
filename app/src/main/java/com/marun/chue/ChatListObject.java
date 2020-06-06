package com.marun.chue;


public class ChatListObject {
    private String userId;
    private String name;
    private String last;
    private String profileImageUrl;
    public ChatListObject (String userId, String name, String profileImageUrl,String last){
        this.userId = userId;
        this.name = name;
        this.last = last;
        this.profileImageUrl = profileImageUrl;
    }


    public String getLast() {
        return last;
    }
    public void setLast(String last) {
        this.last = last;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
