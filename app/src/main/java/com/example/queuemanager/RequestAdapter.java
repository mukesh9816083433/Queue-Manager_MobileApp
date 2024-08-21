package com.example.queuemanager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> requests;
    private RequestButtonClickListener buttonClickListener;
 int c=1;
    public RequestAdapter(Context context, List<String> requests, RequestButtonClickListener listener) {
        super(context, R.layout.list_item_request, requests);
        this.context = context;
        this.requests = requests;
        this.buttonClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_request, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.requestTextView = convertView.findViewById(R.id.requestTextView);
            viewHolder.acceptButton = convertView.findViewById(R.id.acceptButton);
            viewHolder.deleteButton = convertView.findViewById(R.id.deleteButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String request = getItem(position);

        if (request != null) {
            viewHolder.requestTextView.setText(request);
        }

        viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onAcceptButtonClick(position,request,c);
                }
                c=c+1;
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onDeleteButtonClick(position,request);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView requestTextView;
        Button acceptButton;
        Button deleteButton;
    }

    public interface RequestButtonClickListener {
        void onAcceptButtonClick(int position, String request,int c);

        void onDeleteButtonClick(int position, String request);
    }
}

