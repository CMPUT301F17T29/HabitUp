package com.example.habitup.View;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.FollowRequestHolder>{
    private ArrayList<UserAccount> afollowrequestList;

    public static class FollowRequestHolder extends RecyclerView.ViewHolder {
        TextView FollowerName;
        TextView FollowerNickName;
        ImageView FollowerPhoto;

        FollowRequestHolder(View itemView) {
            super(itemView);
            FollowerName = itemView.findViewById(R.id.person_name);
            FollowerNickName = itemView.findViewById(R.id.person_nick_name);
            FollowerPhoto = itemView.findViewById(R.id.person_photo);
        }
    }

    public FollowRequestAdapter(ArrayList<UserAccount> followrequestList){
        afollowrequestList = followrequestList;

    }
    @Override
    public FollowRequestHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follow_requst_card, viewGroup, false);
        FollowRequestHolder frh = new FollowRequestHolder(v);
        return frh;
    }

    @Override
    public void onBindViewHolder(FollowRequestHolder followrequestholder, int position) {
        UserAccount Follower = afollowrequestList.get(position);
        followrequestholder.FollowerName.setText(Follower.getUsername());
        followrequestholder.FollowerNickName.setText(Follower.getLevel());
//        followrequestholder.FollowerPhoto.setImageResource(Follower.getPhoto());
    }
    @Override
    public int getItemCount(){
        return afollowrequestList.size();
    }

}
