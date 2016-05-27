package com.trx.emojispaceime;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TRX on 05/25/2016.
 */
public class EmojiAdapter extends BaseAdapter {

    private Context context;
    private List<EmojiItem> emojiList;
    private LayoutInflater mInflater;

    public EmojiAdapter(Activity activity, List<EmojiItem> emojiList) {
        context = activity;
        this.emojiList = emojiList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        Log.i ("--->", "position:" + position);
        Log.i ("--->", "getUnicode_point:" + emojiList.get(position).getUnicode_point());
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.emojiitem_layout, parent, false);
        }

        TextView emojiView = (TextView) convertView.findViewById(R.id.emoji_item);
        if (emojiList.get(position).getUnicode_point() == 32) {
            emojiView.setText(context.getString(R.string.space_string_place_holder));
        } else {
            emojiView.setText(new String (Character.toChars(emojiList.get(position).getUnicode_point())));
        }
        return convertView;
    }
}
