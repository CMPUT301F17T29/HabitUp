package com.example.habitup.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
;
import com.example.habitup.Controller.FollowController;
import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.Model.UserAccountList;
import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This is the adapter for displaying the user's follow requests.
 *
 * @author Shuyang
 */
public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.FollowRequestHolder>{

    private ArrayList<String> afollowrequestList;
    private Context context;

    public static class FollowRequestHolder extends RecyclerView.ViewHolder {
        TextView FollowerName;
        TextView FollowerNickName;
        ImageView FollowerPhoto;
        Button acceptButton;
        Button ignoreButton;
        Button sendButton;

        public FollowRequestHolder(View itemView) {
            super(itemView);
            FollowerName = itemView.findViewById(R.id.person_name);
            FollowerNickName = itemView.findViewById(R.id.person_nick_name);
            FollowerPhoto = itemView.findViewById(R.id.person_photo);
            acceptButton = itemView.findViewById(R.id.accept_button);
            ignoreButton = itemView.findViewById(R.id.ignore_button);
            sendButton = itemView.findViewById(R.id.accept_send_button);
        }

        public void bindEvent(UserAccount friendRequest) {

            // Set the follower nick name
            FollowerNickName.setText(friendRequest.getRealname());

            // Set the follower name
            FollowerName.setText(friendRequest.getUsername());

            // Set follower photo
            if (friendRequest.getPhoto() != null) {
                try {
                    FollowerPhoto.setImageBitmap(friendRequest.getPhoto());
                } catch (Exception e) {
                    Log.i("Error:", "Failed to get set photo for " + friendRequest.getUsername());
                }
            }

            // Check if user is following the requester already
            try {
                UserAccountList friendList = HabitUpApplication.getCurrentUser().getFriendsList();
                if (friendList.contains(friendRequest.getUsername())) {
                    sendButton.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.i("Error:", "Failed to get friend list for " + friendRequest.getUsername());
            }
        }
    }

    public FollowRequestAdapter(Context context, ArrayList<String> followrequestList){
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
        String followerName = afollowrequestList.get(position);
        final UserAccount Follower = HabitUpApplication.getUserAccount(followerName);
        followrequestholder.bindEvent(Follower);

        final UserAccount currentUser = HabitUpApplication.getCurrentUser();

        followrequestholder.ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!HabitUpApplication.isOnline(context)) {
                    Toast.makeText(context,
                            "Error: No connection to the internet.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Remove from user's request list
                try {
                    FollowController.removeFriendRequest(currentUser, Follower);
                    removeRequest(Follower);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        followrequestholder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HabitUpApplication.isOnline(context)) {
                    Toast.makeText(context,
                            "Error: No connection to the internet.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add user to friend's list
                try {
                    FollowController.addFriend(currentUser, Follower);
                    if (currentUser.getRequestList().size() > 0) {
                        FollowController.removeFriendRequest(currentUser, Follower);
                    } else {
                        throw new IllegalArgumentException("Error: No requests.");
                    }
                    removeRequest(Follower);

                    String addMsg = "Added " + Follower.getRealname() + " as a friend.";
                    Toast.makeText(context, addMsg, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        followrequestholder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HabitUpApplication.isOnline(context)) {
                    Toast.makeText(context,
                            "Error: No connection to the internet.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    FollowController.addFriend(currentUser, Follower);
                    if (currentUser.getRequestList().size() > 0) {
                        FollowController.removeFriendRequest(currentUser, Follower);
                    } else {
                        throw new IllegalArgumentException("Error: No requests.");
                    }
                    removeRequest(Follower);

                    String name = Follower.getRealname();
                    if (currentUser.getFriendsList().contains(Follower.getUsername())) {
                        // Check if already following
                        Toast.makeText(context, "You are already following " + name, Toast.LENGTH_LONG).show();
                    } else if (Follower.getRequestList().contains(currentUser.getUsername())) {
                        // Check if already sent request
                        Toast.makeText(context, "You already sent a request to " + name, Toast.LENGTH_LONG).show();
                    } else {
                        // Send friend request to user
                        FollowController.addFriendRequest(Follower, currentUser);
                        Toast.makeText(context, "A request was sent to " + name, Toast.LENGTH_LONG).show();
                    }

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
        afollowrequestList.remove(follower.getUsername());
        notifyDataSetChanged();
        ((FollowActivity) context).updateTotal();
    }

}
