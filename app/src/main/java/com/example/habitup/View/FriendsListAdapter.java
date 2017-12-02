package com.example.habitup.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.Model.HabitEventList;
import com.example.habitup.Model.HabitList;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sharidanbarboza on 2017-11-15.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int HEADER = 0;
    private final static int CHILD = 1;

    private Context context;
    private ArrayList<Item> data;

    private int lastPos = -1;

    public FriendsListAdapter(Context context, ArrayList<Integer> friends) {
        this.context = context;

        this.data = new ArrayList<>();
        for (int i = 0; i < friends.size(); i++) {
            UserAccount friend = HabitUpApplication.getUserAccountByUID(friends.get(i));

            Item friendItem = new Item(HEADER, friend, i);
            data.add(friendItem);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        if (viewType == HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
            FriendHolder headerView = new FriendHolder(v);

            return headerView;
        }

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_habit, parent, false);
        HabitHolder vh = new HabitHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Item item = data.get(position);

        if (item.type == HEADER) {
            final Item friendItem = data.get(position);
            final UserAccount friend = friendItem.friend;

            final FriendHolder fh = (FriendHolder) holder;
            fh.bind(friend);

            final HabitList habitList = friend.getHabitList();
            final ArrayList<Habit> habits = habitList.getHabits();
            Collections.sort(habits);

            fh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastPos = position;
                    if (friendItem.invisibleChildren == null) {
                        friendItem.invisibleChildren = new ArrayList<>();

                        // Convert habits to items
                        ArrayList<Item> habitItems = new ArrayList<>();
                        for (Habit habit : habits) {
                            Item item = new Item(CHILD, habit, friend, friendItem.friendIndex);

                            habitItems.add(item);
                        }
                        friendItem.invisibleChildren.addAll(habitItems);
                    }

                    if (!friendItem.clicked && habits.size() > 0) {
                        openList(position);
                    } else if (friendItem.clicked) {
                        closeList(position);
                    } else {
                        String noHabitsMsg = friend.getRealname() + " has no habits.";
                        Toast.makeText(context, noHabitsMsg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            final Item habitItem = data.get(position);
            final Habit habit = habitItem.habit;
            final UserAccount friend = habitItem.friend;
            HabitHolder hh = (HabitHolder) holder;
            hh.bind(habit);

            hh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewIntent = new Intent(context, EditHabitEventActivity.class);

                    int hid = habit.getHID();
                    HabitEventList eventList = friend.getEventList();
                    HabitEvent recentEvent = eventList.getRecentEventFromHabit(hid);
                    int pos = eventList.getEvents().indexOf(recentEvent);

                    if (recentEvent != null) {
                        String eid = recentEvent.getEID();
                        viewIntent.putExtra("HABIT_EVENT_UID", friend.getUID());
                        viewIntent.putExtra("HABIT_EVENT_HID", habit.getHID());
                        viewIntent.putExtra("HABIT_EVENT_EID", eid);
                        viewIntent.putExtra("HABIT_EVENT_ACTION", ViewHabitEventActivity.VIEW_EVENT);
                        viewIntent.putExtra("profile", 0);
                        viewIntent.putExtra("EVENT POSITION", pos);
                        viewIntent.putExtra("FRIEND_INDEX", habitItem.friendIndex);
                        ((ViewFriendsActivity) context).startActivityForResult(viewIntent, 1);
                    } else {
                        Toast.makeText(context, "This habit has no events.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    public void openList(int pos) {
        Log.i("HabitUpDEBUG", "Opened friend's habits");
        // Habit list is closed and hidden
        int index = pos + 1;

        // Remove from adapter
        Item friendItem = data.get(pos);
        for (Item i : friendItem.invisibleChildren) {
            data.add(index, i);
            index++;
        }
        notifyItemRangeInserted(pos + 1, index - pos - 1);
        friendItem.clicked = true;
    }

    public void closeList(int pos) {
        Log.i("HabitUpDEBUG", "Closed friend's habits");
        // Habit list is open and displayed
        int count = 0;

        // Add to adapter
        while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
            data.remove(pos + 1);
            count++;
        }
        notifyItemRangeRemoved(pos + 1, count);
        data.get(pos).clicked = false;
    }

    public int getLastPos() {
        return this.lastPos;
    }

    public class FriendHolder extends RecyclerView.ViewHolder {

        public final View itemView;
        private final ImageView friendPhoto;
        private final TextView userName;
        private final TextView fullName;
        private final TextView level;

        private boolean clicked;

        public FriendHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.friendPhoto = itemView.findViewById(R.id.friend_pic);
            this.fullName = itemView.findViewById(R.id.friend_name);
            this.userName = itemView.findViewById(R.id.friend_username);
            this.level = itemView.findViewById(R.id.friend_level);
            this.clicked = false;
        }

        public void bind(UserAccount friend) {
            // Set friend's name
            String name = friend.getRealname();
            this.fullName.setText(name);

            // Set friend's username
            String username = friend.getUsername();
            this.userName.setText(username);

            // Set friend's level
            int level = friend.getLevel();
            this.level.setText("Level " + String.valueOf(level));

            // Set friend's profile pic
            Bitmap profilePic = friend.getPhoto();
            if (profilePic != null) {
                try {
                    friendPhoto.setImageBitmap(profilePic);
                } catch (Exception e) {
                    Log.i("Error:", "Failed to set photo for " + name);
                }
            }
        }

    }

    public class HabitHolder extends RecyclerView.ViewHolder {

        public final View itemView;
        private final TextView habitName;
        private final ProgressBar habitProgress;
        private final TextView habitStatus;

        public HabitHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.habitName = itemView.findViewById(R.id.friend_habit_name);
            this.habitProgress = itemView.findViewById(R.id.friend_habit_progress);
            this.habitStatus = itemView.findViewById(R.id.friend_habit_status);
        }

        public void bind(Habit habit) {
            // Set habit name
            String name = habit.getHabitName();
            this.habitName.setText(name);

            // Set habit name color
            String color = Attributes.getColour(habit.getHabitAttribute());
            this.habitName.setTextColor(Color.parseColor(color));

            // Set habit progress
            habitProgress.setProgress(habit.getPercent());

            // Set habit status
            int xValue = habit.getHabitsDone();
            int yValue = habit.getHabitsPossible();
            habitStatus.setText(xValue + "/" + yValue);

        }
    }

    public static class Item {

        public int type;
        public ArrayList<Item> invisibleChildren;
        public UserAccount friend;
        public Habit habit;
        public int friendIndex;
        public boolean clicked;

        public Item(int type, UserAccount friend, int friendIndex) {
            this.type = type;
            this.friend = friend;
            this.friendIndex = friendIndex;
            this.clicked = false;
        }

        public Item(int type, Habit habit, UserAccount friend, int friendIndex) {
            this.type = type;
            this.habit = habit;
            this.friend = friend;
            this.friendIndex = friendIndex;
            this.clicked = false;
        }
    }
}

