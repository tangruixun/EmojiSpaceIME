package com.trx.emojispaceime;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by TRX on 05/30/2016.
 */
public class MyKeyboardView extends KeyboardView {
    Context _context;
    SharedPreferences preferences;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        CharSequence spaceCharacter = preferences.getString(_context.getString(R.string.emoji_picker_key), " ");

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key : keys) {
            if (key.label != null) {
                if(key.label.toString().equals(" ") && spaceCharacter != " ") {
                    key.label = spaceCharacter;
                    //invalidateKey (32);
                    invalidateAllKeys();
                }
            }
        }
    }
}
