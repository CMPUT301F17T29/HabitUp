package com.example.habitup.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by barboza on 11/11/17.
 */

public class EventHolder extends RecyclerView.ViewHolder {

    public final View itemView;
    private final TextView eventNameView;
    private final TextView eventDateView;
    private final TextView eventCommentView;
    private final ImageView photoIcon;
    private final ImageView markerIcon;

    private HabitEvent event;
    private Habit eventHabit;
    private Context context;

    public EventHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;

        this.itemView = itemView;
        this.eventNameView = itemView.findViewById(R.id.event_name);
        this.eventDateView = itemView.findViewById(R.id.event_date);
        this.eventCommentView = itemView.findViewById(R.id.event_comment);
        this.photoIcon = itemView.findViewById(R.id.event_has_image);
        this.markerIcon = itemView.findViewById(R.id.event_has_location);
    }

    public void bindEvent(HabitEvent event) {
        this.event = event;

        ElasticSearchController.GetHabitsTask getHabit = new ElasticSearchController.GetHabitsTask();
        getHabit.execute(String.valueOf(event.getHID()));
        try {
            eventHabit = getHabit.get().get(0);
        } catch (Exception e) {
            Log.i("HabitUpDEBUG", "EventListAdaptor - couldn't get Habit");
            eventHabit = new Habit(-1);
            eventHabit.setHabitName("ERROR");
        }

        String eventName = eventHabit.getHabitName();
        String eventAttribute = eventHabit.getHabitAttribute();
        String attributeColour = Attributes.getColour(eventAttribute);
        LocalDate eventDate = event.getCompletedate();
        String eventComment = event.getComment();

        // Set habit name
        this.eventNameView.setText(eventName);
        this.eventNameView.setTextColor(Color.parseColor(attributeColour));

        // Set event date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        this.eventDateView.setText(eventDate.format(formatter).toUpperCase());

        // Set event comment (if any)
        this.eventCommentView.setText(eventComment);

        // Change color of camera and map marker icons
        int tintColour = ContextCompat.getColor(this.context, R.color.teal);
        int defaultColour = ContextCompat.getColor(this.context, R.color.tintgray);

        // Set if event has corresponding image
        if (event.hasImage()) {
            photoIcon.setColorFilter(tintColour);
        } else {
            photoIcon.setColorFilter(defaultColour);
        }

        // Set if event has corresponding location
        if (event.hasLocation()) {
            markerIcon.setColorFilter(tintColour);
        } else {
            markerIcon.setColorFilter(defaultColour);
        }

    }

    public void highlightItem() {
        itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.teal));

        int whiteColor = ContextCompat.getColor(context, R.color.white);
        eventNameView.setTextColor(whiteColor);
        eventCommentView.setTextColor(whiteColor);
        eventDateView.setTextColor(whiteColor);
    }

    public void unhighlightItem() {
        itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        String attribute = eventHabit.getHabitAttribute();
        String color = Attributes.getColour(attribute);

        int lightGray = ContextCompat.getColor(context, R.color.lightgray);
        eventNameView.setTextColor(Color.parseColor(color));
        eventCommentView.setTextColor(lightGray);
        eventDateView.setTextColor(lightGray);

    }

}
