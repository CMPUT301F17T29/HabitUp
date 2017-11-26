package com.example.habitup.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This is the adapter for displaying the user's follow requests.
 *
 * @author Shuyang
 */
public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.FollowRequestHolder>{

    private ArrayList<UserAccount> afollowrequestList;
    private Context context;

    public static class FollowRequestHolder extends RecyclerView.ViewHolder {
        TextView FollowerName;
        TextView FollowerNickName;
        ImageView FollowerPhoto;
        Button acceptButton;
        Button ignoreButton;

        public FollowRequestHolder(View itemView) {
            super(itemView);
            FollowerName = itemView.findViewById(R.id.person_name);
            FollowerNickName = itemView.findViewById(R.id.person_nick_name);
            FollowerPhoto = itemView.findViewById(R.id.person_photo);
            acceptButton = itemView.findViewById(R.id.accept_button);
            ignoreButton = itemView.findViewById(R.id.ignore_button);
        }

        public void bindEvent(UserAccount friendRequest) {

            // Set the follower nick name
            FollowerNickName.setText(friendRequest.getRealname());

            // Set the follower name
            FollowerName.setText(friendRequest.getUsername());

            // Set follower photo
            if (friendRequest.getPhoto() != null) {
                FollowerPhoto.setImageBitmap(friendRequest.getPhoto());
            }
        }
    }

    public FollowRequestAdapter(Context context, ArrayList<UserAccount> followrequestList){
        this.afollowrequestList = followrequestList;
        this.context = context;
    }

    @Override
    public FollowRequestHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follow_request_card, viewGroup, false);
        FollowRequestHolder frh = new FollowRequestHolder(v);
        return frh;
    }

    @Override
    public void onBindViewHolder(FollowRequestHolder followrequestholder, int position) {
        final UserAccount Follower = afollowrequestList.get(position);
        followrequestholder.bindEvent(Follower);

        followrequestholder.ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove from user's request list
                try {
                    HabitUpController.removeFriendRequest(Follower);
                    removeRequest(Follower);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        followrequestholder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add user to friend's list
                try {
                    HabitUpController.addFriend(Follower);
                    removeRequest(Follower);

                    String addMsg = "Added " + Follower.getRealname() + " as a friend.";
                    Toast.makeText(context, addMsg, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    public int getItemCount(){
        return afollowrequestList.size();
    }

    private void removeRequest(UserAccount follower) {
        afollowrequestList.remove(follower);
        notifyDataSetChanged();
        ((FollowActivity) context).updateTotal();
    }

}
