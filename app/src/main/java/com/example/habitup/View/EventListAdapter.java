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

    private int selected = -1;

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
    public void onBindViewHolder(final EventHolder holder, final int position) {

        final HabitEvent event = this.events.get(position);
        final View view = holder.itemView;
        holder.bindEvent(event);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = position;
                holder.highlightItem();

                // TODO: Fix unhighlighting
            }
        });
    }

    @Override
    public int getItemCount() {

        return this.events.size();
    }

    public int getPosition() {
        return this.selected;
    }

    public HabitEvent getItem(int position) {
        return events.get(position);
    }

    public void removeItem(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

}
