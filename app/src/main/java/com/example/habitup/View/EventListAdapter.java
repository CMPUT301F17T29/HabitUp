package com.example.habitup.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by sharidanbarboza on 2017-10-28.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventHolder> {

    private ArrayList<HabitEvent> events;
    private Context context;
    private int itemResource;
    private RecyclerView recyclerView;

    public EventListAdapter(Context context, int resource, ArrayList<HabitEvent> events, RecyclerView recyclerView) {
        this.events = events;
        this.context = context;
        this.itemResource = resource;
        this.recyclerView = recyclerView;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new EventHolder(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(EventHolder holder, final int position) {

        HabitEvent event = this.events.get(position);
        final View view = holder.itemView;
        holder.bindEvent(event);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < events.size(); i++) {
                    if (i == position) {
                        if (i == position) {
                            highlightItem(view);
                        } else {
                            unhighlightItem(recyclerView.getChildAt(i), events.get(i));
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return this.events.size();
    }

    public HabitEvent getItem(int position) {
        return events.get(position);
    }

    public void removeItem(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

    public void highlightItem(View view) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.teal));

        int whiteColor = ContextCompat.getColor(context, R.color.white);
        TextView text = view.findViewById(R.id.event_name);
        text.setTextColor(whiteColor);

        TextView comment = view.findViewById(R.id.event_comment);
        comment.setTextColor(whiteColor);

        TextView dateText = view.findViewById(R.id.event_date);
        dateText.setTextColor(whiteColor);
    }

    public void unhighlightItem(View view, HabitEvent event) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        Habit eventHabit;
        ElasticSearchController.GetHabitsTask getHabit = new ElasticSearchController.GetHabitsTask();
        getHabit.execute(String.valueOf(event.getHID()));
        try {
            eventHabit = getHabit.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "ViewHabitEventActivity - couldn't get Habit");
            eventHabit = new Habit(-1);
            eventHabit.setHabitName("ERROR");
        }

        String attribute = eventHabit.getHabitAttribute();
        String color = Attributes.getColour(attribute);

        int lightGray = ContextCompat.getColor(context, R.color.lightgray);
        TextView text = view.findViewById(R.id.event_name);
        text.setTextColor(Color.parseColor(color));

        TextView comment = view.findViewById(R.id.event_comment);
        comment.setTextColor(lightGray);

        TextView dateText = view.findViewById(R.id.event_date);
        dateText.setTextColor(lightGray);

    }
}
