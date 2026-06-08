package com.whebtos.e_chiro.ui.help;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.whebtos.e_chiro.R;


public class HelpAdapter extends ArrayAdapter {
    private String[] title;
    private String[] body;

    private Activity context;

    public HelpAdapter(Activity context, String[] helpHeader, String[] helpBody) {
        super(context.getApplicationContext(), R.layout.list_item, helpHeader);
        this.context = context;
        this.title = helpHeader;
        this.body = helpBody;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.list_item, null, true);
        TextView textViewCountry = (TextView) row.findViewById(R.id.help_section_header);
        TextView textViewCapital = (TextView) row.findViewById(R.id.help_section_body);

        textViewCountry.setText(title[position]);
        textViewCapital.setText(body[position]);
        return  row;
    }
}