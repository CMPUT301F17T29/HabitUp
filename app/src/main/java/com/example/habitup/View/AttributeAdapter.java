package com.example.habitup.View;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.habitup.Model.Attributes;
import com.example.habitup.R;

/**
 * This is the adapter for creating a dropdown spinner menu for
 * attibutes during a create habit activity.
 *
 * @author Shari Barboza
 */

public class AttributeAdapter extends ArrayAdapter<String> {

    // The attributes list
    private String[] attributes;

    /**
     * Instantiates a new attribute adapter.
     * @param context the activity context
     * @param resource the ID of the resource layout
     * @param attributes the list of attributes
     */
    public AttributeAdapter(Context context, int resource, String[] attributes) {
        super(context, resource, attributes);
        this.attributes = attributes;
    }

    /**
     * Get the view list item in the drop down spinner menu.
     * @param position the position of the row in the spinner
     * @param convertView the view to convert
     * @param parent the parent view
     * @return
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        String attributeName = attributes[position];

        // Set attribute to its corresponding name and color
        String color = Attributes.getColour(attributeName);
        TextView attributeText = view.findViewById(R.id.spinner_text);
        attributeText.setTextColor(Color.parseColor(color));

        return view;

    }
}
