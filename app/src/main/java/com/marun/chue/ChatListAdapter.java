package com.marun.chue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolders> {
    private List<ChatListObject> chatListList;
    private Context context;

    public ChatListAdapter(List<ChatListObject> chatListList, Context context) {
        this.chatListList = chatListList;
        this.context = context;
    }


    @Override
    public ChatListViewHolders onCreateViewHolder( ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatListViewHolders rcv = new ChatListViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolders holder, int position) {
        holder.mMatchId.setText(chatListList.get(position).getUserId());
        holder.LastMessage.setText(chatListList.get(position).getLast());
     holder.mMatchName.setText(chatListList.get(position).getName());

       if(!chatListList.get(position).getProfileImageUrl().equals("default")){
           Glide.with(context).load(chatListList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
       }

    }

    @Override
    public int getItemCount() {
        return this.chatListList.size();
    }

}
