package com.example.habitup.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

public class EventListAdapter extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> events;

    public EventListAdapter(Context context, int resource, ArrayList<HabitEvent> events) {
        super(context, resource, events);
        this.events = events;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.event_list_item, null);
        }

        HabitEvent event = events.get(position);

//        Habit eventHabit = event.getHabit();
//        String eventName = eventHabit.getHabitName();
//        String eventAttribute = eventHabit.getHabitAttribute();
//        String attributeColour = Attributes.getColour(eventAttribute);
        LocalDate eventDate = event.getCompletedate();
        String eventComment = event.getComment();

        // Set habit name
        TextView eventNameView = v.findViewById(R.id.event_name);
//        eventNameView.setText(eventName);
//        eventNameView.setTextColor(Color.parseColor(attributeColour));

        // Set event date
        TextView eventDateView = v.findViewById(R.id.event_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        eventDateView.setText(eventDate.format(formatter).toUpperCase());

        // Set event comment (if any)
        TextView eventCommentView = v.findViewById(R.id.event_comment);
        eventCommentView.setText(eventComment);

        // Change color of camera and map marker icons
        int tintColour = ContextCompat.getColor(getContext(), R.color.teal);
        int defaultColour = ContextCompat.getColor(getContext(), R.color.tintgray);

        // Set if event has corresponding image
        ImageView photoIcon = v.findViewById(R.id.event_has_image);
        if (event.hasImage()) {
            photoIcon.setColorFilter(tintColour);
        } else {
            photoIcon.setColorFilter(defaultColour);
        }

        // Set if event has corresponding location
        ImageView markerIcon = v.findViewById(R.id.event_has_location);
        if (event.hasLocation()) {
            markerIcon.setColorFilter(tintColour);
        } else {
            markerIcon.setColorFilter(defaultColour);
        }

        return v;
    }
}
