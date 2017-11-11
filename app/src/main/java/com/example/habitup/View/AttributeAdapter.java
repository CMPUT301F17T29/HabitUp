package com.example.habitup.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.habitup.Model.Attributes;
import com.example.habitup.R;

/**
 * Created by barboza on 11/10/17.
 */

public class AttributeAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] attributes;

    public AttributeAdapter(Context context, int resource, String[] attributes) {
        super(context, resource, attributes);
        this.context = context;
        this.attributes = attributes;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);

        String attributeName = attributes[position];
        String color = Attributes.getColour(attributeName);

        TextView attributeText = view.findViewById(R.id.attribute_text);
        attributeText.setTextColor(Color.parseColor(color));

        return view;

    }
}
