package com.example.habitup.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
 * This is the adapter for creating the list of habit events, which displays the event's
 * habit name, icons to display whether the event has an image and location, the event's comment,
 * and the event's completion date.
 *
 * @see RecyclerView.Adapter
 * @see EventHolder
 * @see HabitEvent
 *
 * @author Shari Barboza
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventHolder> {

    private ArrayList<HabitEvent> events;
    private Context context;
    private int position;

    // Click listeners
    private View.OnLongClickListener longClickListener;
    private OnItemClickListener clickListener;

    /**
     * Instantiates a new HabitEvent list adapter.
     * @param context the activity context
     * @param events the array list of events
     */
    public EventListAdapter(Context context, ArrayList<HabitEvent> events) {
        this.events = events;
        this.context = context;
    }

    /**
     * Creates and inflates the layout for the view holder.
     * @param parent the parent view
     * @param viewType the type of view
     * @return
     */
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);
        return new EventHolder(this.context, view);
    }

    /**
     * Bind data for each view holder.
     * @param holder the view holder
     * @param pos the position of the holder
     */
    @Override
    public void onBindViewHolder(final EventHolder holder, final int pos) {

        // Get the habit event and bind it's data to the holder
        final HabitEvent event = this.events.get(pos);
        holder.bindEvent(event);

        // Set position when item is clicked
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                position = pos;
                return false;
            }
        });
    }

    /**
     * Gets the current size of the adapter.
     * @return the size of the events array list
     */
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

    /**
     * Sets the position of the view that was currently clicked.
     * @param pos the position of the view in the adapter
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    /**
     * Gets the position of the view that was recently clicked.
     * @return the position of the view in the adapter
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Allow the adapter to implement an on item click listener with a custom on click method.
     */
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    /**
     * Listen for a click on the view item.
     * @param listener the on item click listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Listen for a long click on the view item.
     * @param listener the on long click listener
     */
    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    /**
     * This is the inner class that will be utilized in the RecycleView adapter and extend to
     * the EventListAdapter. This will allow the adapter to pass data to the holder to store.
     *
     * @see RecyclerView.ViewHolder
     * @see EventListAdapter
     * @see android.view.View.OnLongClickListener
     * @see android.view.View.OnCreateContextMenuListener
     *
     */
    public class EventHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {

        // The views to set in the list view row
        public final View itemView;
        private final TextView eventNameView;
        private final TextView eventDateView;
        private final TextView eventCommentView;
        private final ImageView photoIcon;
        private final ImageView markerIcon;

        private HabitEvent event;
        private Habit eventHabit;
        private Context context;

        /**
         * Instantiates a new event holder.
         * @param context the activity context
         * @param itemView the view of the list view item
         */
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

            // Call onItemClick interface to pass position when list view item is clicked
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

        /**
         * The click method that will be overridden when the on long click item
         * listener is called.
         * @param v the view of the item that was long-clicked
         * @return false
         */
        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick(v);
            return false;
        }

        /**
         * Initialize the context menu when a list item is long-clicked.
         * This will allow actions to be performed on the HabitEvent.
         * @param menu the context menu
         * @param v the view of the list item
         * @param menuInfo the menu info
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action");
            menu.add(0, 1, 1, "Edit");
            menu.add(0, 2, 2, "Delete");
        }

        /**
         * This will bind a HabitEvent's data to the holder's stored views.
         * @param event the HabitEvent in the list view
         */
        public void bindEvent(HabitEvent event) {
            this.event = event;

            // Get the habit associated with the event
            ElasticSearchController.GetHabitsTask getHabit = new ElasticSearchController.GetHabitsTask();
            getHabit.execute(String.valueOf(event.getHID()));
            try {
                eventHabit = getHabit.get().get(0);
            } catch (Exception e) {
                eventHabit = new Habit(-1);
                eventHabit.setHabitName("ERROR");
            }

            // Set the event's habit name with it's corresponding Attribute color
            String eventName = eventHabit.getHabitName();
            String eventAttribute = eventHabit.getHabitAttribute();
            String attributeColour = Attributes.getColour(eventAttribute);
            this.eventNameView.setText(eventName);
            this.eventNameView.setTextColor(Color.parseColor(attributeColour));

            // Set event date
            LocalDate eventDate = event.getCompletedate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            this.eventDateView.setText(eventDate.format(formatter).toUpperCase());

            // Set event comment (if any)
            String eventComment = event.getComment();
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
