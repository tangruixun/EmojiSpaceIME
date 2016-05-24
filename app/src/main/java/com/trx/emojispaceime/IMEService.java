package com.trx.emojispaceime;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by TRX on 05/23/2016.
 */
public class IMEService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    public static final int KEYCODE_SYMBOLKEYBOARD = -2;
    public static final int KEYCODE_LANGUAGESWITCH = -101;
    int layoutIndex = 0;

    private KeyboardView kv;
    private Keyboard qwertyKeyboard;
    private Keyboard symbolKeyboard;
    private Keyboard symbolShiftedKeyboard;
    private Keyboard keyboard;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        final int [] LayoutArray = {R.xml.qwerty, R.xml.symbols, R.xml.symbols_shift};
        layoutIndex = 0;
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboardview, null);
        qwertyKeyboard = new Keyboard(this, LayoutArray[0]);
        symbolKeyboard = new Keyboard(this, LayoutArray[1]);
        symbolShiftedKeyboard = new Keyboard(this, LayoutArray[2]);
        keyboard = qwertyKeyboard;
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
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
