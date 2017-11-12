package com.example.habitup.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habitup.Controller.ElasticSearchController;
import com.example.habitup.Model.Attributes;
import com.example.habitup.Model.Habit;
import com.example.habitup.Model.HabitEvent;
import com.example.habitup.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by sharidanbarboza on 2017-10-28.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventHolder> {

    private ArrayList<HabitEvent> events;
    private Context context;
    private int position;

    private View.OnLongClickListener longClickListener;
    private OnItemClickListener clickListener;

    public EventListAdapter(Context context, ArrayList<HabitEvent> events) {
        this.events = events;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new EventHolder(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(final EventHolder holder, final int pos) {

        final HabitEvent event = this.events.get(pos);
        holder.bindEvent(event);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                position = pos;
                return false;
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

    public void setPosition(int pos) {
        this.position = pos;
    }

    public int getPosition() {
        return this.position;
    }

    public class EventHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {

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

            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener!= null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(v, position);
                        }
                    }
                }
            });
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            longClickListener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick(v);
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action");
            menu.add(0, 1, 1, "Edit");
            menu.add(0, 2, 2, "Delete");
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

    }

}
