package atproj.com.linesorter.presenters;

import atproj.com.linesorter.listener.NativeCallListener;
import atproj.com.linesorter.views.JNIView;

/**
 * Created by andre on 17-Apr-19.
 */

public class JNIPresenter implements NativeCallListener {

    private JNIView view;

    public JNIPresenter(JNIView view) {
        this.view = view;

        InitApplication();
        SetListener(this);
    }

    public boolean setFilter(String filter) {
        return SetFilter(filter);
    }

    public void start(String block) {
        if (AddSourceBlock(block)) {
            StartParsing();
        } else {
            view.onParsingComplete();
        }
    }

    public int testStart(String block) {
        if (AddSourceBlock(block)) {
            return TestParsing();
        }

        return -1;
    }


    @Override
    public void onProcess(String line) {
        view.onParsingProcess(line);
    }

    @Override
    public void onComplete() {
        view.onParsingComplete();
    }

    @Override
    public void onError(String message) {

    }


    // ---------------------------------------------------------------------------------------------
    // Native

    static {
        System.loadLibrary("linesorter");
    }

    public native void InitApplication();
    public native void SetListener(NativeCallListener listener);
    public native boolean SetFilter(String filter);
    public native boolean AddSourceBlock(String block);
    public native void StartParsing();
    public native int TestParsing();


    // Native
    // ---------------------------------------------------------------------------------------------
}
