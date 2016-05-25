package com.trx.emojispaceime;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TRX on 05/25/2016.
 */
public class EmojiAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EmojiItem> emojiList;

    public EmojiAdapter(Activity activity, List<EmojiItem> emojiList) {
        context = activity;
        emojiList = this.emojiList;
    }

    @Override
    public int getCount() {
        return emojiList.size();
    }

    @Override
    public Object getItem(int position) {
        return emojiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.emojiitem_layout, null);
        }

        TextView emojiView = (TextView) convertView.findViewById(R.id.emoji_item);
        emojiView.setText(new String (Character.toChars(emojiList.get(position).getUnicode_point())));

        return convertView;
    }
}
