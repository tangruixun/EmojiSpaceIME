package com.trx.emojispaceime;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * Created by TRX on 05/23/2016.
 */
public class IMEService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    SharedPreferences preferences;
    Context context;

    public static final int KEYCODE_SYMBOLKEYBOARD = -2;
    public static final int KEYCODE_LANGUAGESWITCH = -101;
    public static final int KEYCODE_SPACE = 32;
    int layoutIndex = 0;

    private MyKeyboardView kv;
    private Keyboard qwertyKeyboard;
    private Keyboard symbolKeyboard;
    private Keyboard symbolShiftedKeyboard;
    private Keyboard keyboard;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        final int [] LayoutArray = {R.xml.qwerty, R.xml.symbols, R.xml.symbols_shift};
        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        layoutIndex = 0;
        kv = (MyKeyboardView) getLayoutInflater().inflate(R.layout.keyboardview, null);
        qwertyKeyboard = new Keyboard(this, LayoutArray[0]);
        symbolKeyboard = new Keyboard(this, LayoutArray[1]);
        symbolShiftedKeyboard = new Keyboard(this, LayoutArray[2]);
        keyboard = qwertyKeyboard;
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    /**
     * Called when the input view is being shown and input has started on
     * a new editor.  This will always be called after {@link #onStartInput},
     * allowing you to do your general setup there and just view-specific
     * setup here.  You are guaranteed that {@link #onCreateInputView()} will
     * have been called some time before this function is called.
     *
     * @param info       Description of the type of text being edited.
     * @param restarting Set to true if we are restarting input on the
     */
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

        CharSequence spaceCharacter = preferences.getString(getString(R.string.emoji_picker_key), " ");
        List<Keyboard.Key> keys = kv.getKeyboard().getKeys();
        for(Keyboard.Key key : keys) {
            if (key.label != null) {
                if(key.label.toString().equals(" ") && spaceCharacter != " ") {
                    key.label = spaceCharacter;
                    //invalidateKey (32);
                    kv.invalidateAllKeys();
                }
            }
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        boolean nClickSound = preferences.getBoolean(getString(R.string.pref_press_button_sound_key), true);
        boolean nClickVibrate = preferences.getBoolean(getString(R.string.pref_press_button_vibrate_key), true);
        if (nClickSound) {
            playClick(primaryCode);
            if (nClickVibrate) {
                if (primaryCode != Keyboard.KEYCODE_DELETE) {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 30 milliseconds
                    v.vibrate(30);
                }
            }
        }
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                if (kv.getKeyboard() == qwertyKeyboard) {
                    caps = !caps;
                    keyboard.setShifted(caps);
                } else if (kv.getKeyboard() == symbolKeyboard) {
                    keyboard = symbolShiftedKeyboard;
                    kv.setKeyboard(keyboard);
                } else if (kv.getKeyboard() == symbolShiftedKeyboard) {
                    keyboard = symbolKeyboard;
                    kv.setKeyboard(keyboard);
                }
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case KEYCODE_SYMBOLKEYBOARD:
                if (kv.getKeyboard() == qwertyKeyboard) {
                    keyboard = symbolKeyboard;
                } else {
                    keyboard = qwertyKeyboard;
                }
                kv.setKeyboard(keyboard);
                kv.invalidateAllKeys();
                break;
            case KEYCODE_LANGUAGESWITCH:
                InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();
                break;
            case KEYCODE_SPACE:
                CharSequence spaceCharacter = preferences.getString(getString(R.string.emoji_picker_key), " ");
                ic.commitText(spaceCharacter, 1);
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }
}
