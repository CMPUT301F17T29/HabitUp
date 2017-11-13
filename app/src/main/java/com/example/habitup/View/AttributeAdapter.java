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
 * This is the adapter for creating a dropdown spinner menu for attributes during a
 * create habit activity. It displays only the attribute name with the text color set
 * accordingly for each attribute.
 *
 * @author Shari Barboza
 */

public class AttributeAdapter extends ArrayAdapter<String> {

    // The attributes list
    private String[] attributes;

    public AttributeAdapter(Context context, int resource, String[] attributes) {
        super(context, resource, attributes);
        this.attributes = attributes;
    }

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
