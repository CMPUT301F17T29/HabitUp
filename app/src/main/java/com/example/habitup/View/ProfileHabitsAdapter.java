package com.example.habitup.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habitup.Controller.HabitUpApplication;
import com.example.habitup.Controller.HabitUpController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.UserAccount;
import com.example.habitup.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This is the adapter for the habits that will be displayed in a user's profile screen.
 * The profile habits only contain habits that are scheduled for the current day. The information
 * displayed for each habit are the habit name, the days of the weeks it is scheduled for,
 * and a checkbox indicating whether there is a habit event completed for that habit or not.
 *
 * @author Shari Barboza
 */
public class ProfileHabitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW = 0;

    private ArrayList<Habit> habits;
    private Context context;

    public ProfileHabitsAdapter(Context context, ArrayList<Habit> habits) {
        this.habits = habits;
        this.context = context;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView nameField;
        private TextView levelField;
        private TextView levelUpField;
        private ProgressBar progressBar;

        private TextView attr2Field;
        private TextView attr1Field;
        private TextView attr3Field;
        private TextView attr4Field;
        private TextView today_subheading;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            this.profilePic = itemView.findViewById(R.id.drawer_pic);
            this.nameField = itemView.findViewById(R.id.username);
            this.levelField = itemView.findViewById(R.id.user_level);
            this.levelUpField = itemView.findViewById(R.id.level_title);
            this.progressBar = itemView.findViewById(R.id.progress_bar);

            this.attr2Field = itemView.findViewById(R.id.attribute2_value);
            this.attr1Field = itemView.findViewById(R.id.attribute1_value);
            this.attr3Field = itemView.findViewById(R.id.attribute3_value);
            this.attr4Field = itemView.findViewById(R.id.attribute4_value);

            this.today_subheading = itemView.findViewById(R.id.today_subheading);

        }

        public void bind() {
            // Get the user
            UserAccount currentUser = HabitUpApplication.getCurrentUser();

            // Set user's photo
            Bitmap photo = currentUser.getPhoto();

            if (photo != null) {
                profilePic.setImageBitmap(photo);
            }

            nameField.setText(currentUser.getRealname());

            // Set user's level
            levelField.setText("Level " + String.valueOf(currentUser.getLevel()));

            // Set user's level up in
            levelUpField.setText("Level up in " + String.valueOf(currentUser.getXPtoNext() - currentUser.getXP()) + " XP");

            // Set progress bar
            progressBar.setMax(currentUser.getXPtoNext());
            progressBar.setProgress(currentUser.getXP(), true);

            // Get user attributes
            Attributes userAttrs = HabitUpApplication.getCurrentAttrs();

            // Set user's Mental value
            attr2Field.setText(String.valueOf(userAttrs.getValue("Mental")));

            // Set user's Physical value
            attr1Field.setText(String.valueOf(userAttrs.getValue("Physical")));

            // Set user's Discipline value
            attr3Field.setText(String.valueOf(userAttrs.getValue("Social")));

            // Set user's Social value
            attr4Field.setText(String.valueOf(userAttrs.getValue("Discipline")));

            if (habits.size() == 0) {
                today_subheading.setText(R.string.no_habits);
            }
        }
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        public final View itemView;

        private final TextView habitNameView;
        private final View monView;
        private final View tueView;
        private final View wedView;
        private final View thuView;
        private final View friView;
        private final View satView;
        private final View sunView;
        public final CheckBox checkBox;

        public NormalViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.habitNameView = itemView.findViewById(R.id.habit_name);
            this.monView = itemView.findViewById(R.id.mon_box);
            this.tueView = itemView.findViewById(R.id.tue_box);
            this.wedView = itemView.findViewById(R.id.wed_box);
            this.thuView = itemView.findViewById(R.id.thu_box);
            this.friView = itemView.findViewById(R.id.fri_box);
            this.satView = itemView.findViewById(R.id.sat_box);
            this.sunView = itemView.findViewById(R.id.sun_box);
            this.checkBox = itemView.findViewById(R.id.today_habit_checkbox);

        }

        public void bindHabit(Habit habit) {

            String attributeName = habit.getHabitAttribute();
            String attributeColour = Attributes.getColour(attributeName);

            this.habitNameView.setText(habit.getHabitName());
            this.habitNameView.setTextColor(Color.parseColor(attributeColour));

            // Get habit schedule
            boolean[] schedule = habit.getHabitSchedule();
            View[] textViews = {
                    this.monView,
                    this.tueView,
                    this.wedView,
                    this.thuView,
                    this.friView,
                    this.satView,
                    this.sunView
            };

            // Display days of the month for the habit's schedule
            for (int i = 1; i < schedule.length; i++) {
                if (schedule[i]) {
                    textViews[i-1].setVisibility(View.VISIBLE);
                } else {
                    textViews[i-1].setVisibility(View.GONE);
                }
            }

            boolean doneToday = HabitUpController.habitDoneToday(habit);
            this.checkBox.setChecked(doneToday);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        if (viewType == HEADER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_banner, parent, false);
            HeaderViewHolder headerView = new HeaderViewHolder(v);

            return headerView;
        }

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todays_habits, parent, false);
        NormalViewHolder vh = new NormalViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int pos) {

        if (holder instanceof NormalViewHolder) {
            NormalViewHolder vh = (NormalViewHolder) holder;
            final Habit habit = this.habits.get(pos - 1);

            if (habit == null) {
                return;
            }

            vh.bindHabit(habit);

            if (!vh.checkBox.isChecked()) {
                vh.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addEventIntent = new Intent(context, AddHabitEventActivity.class);
                        addEventIntent.putExtra("position", pos);
                        addEventIntent.putExtra("HABIT_EVENT_HID", habit.getHID());
                        addEventIntent.putExtra("profile", 1);
                        addEventIntent.putExtra("habit_pos", pos);
                        addEventIntent.putExtra("habit", habit.getHabitName());
                        ((Activity) context).startActivityForResult(addEventIntent, 1);
                    }
                });
            } else {
                vh.checkBox.setClickable(false);
            }
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.bind();
        }
    }

    @Override
    public int getItemCount() {
        return habits.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_VIEW) {
            return HEADER_VIEW;
        }

        return 1;
    }
}

