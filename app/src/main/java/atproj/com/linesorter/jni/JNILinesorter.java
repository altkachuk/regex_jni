package atproj.com.linesorter.jni;

import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

public class JNILinesorter implements NativeCallListener{

    private volatile NativeCallListener listener;
    private ConcurrentLinkedQueue<Action> waitActions;

    public JNILinesorter() {
        InitApplication();
        SetListener(this);
        waitActions = new ConcurrentLinkedQueue<>();
    }

    public void setListener(NativeCallListener listener) {
        this.listener = listener;
        checkQueue();
    }

    public void removeListener() {
        this.listener = null;
    }

    @Override
    public void onProcess(String line) {
        if (listener == null) {
            Log.d(JNILinesorter.class.getSimpleName(), "listener = null");
            waitActions.offer(new Action("onProcess", line));
            return;
        }
        if (waitActions.size() > 0) {
            waitActions.offer(new Action("onProcess", line));
            checkQueue();
        } else {
            listener.onProcess(line);
        }

    }

    @Override
    public void onComplete() {
        if (listener == null) {
            Log.d(JNILinesorter.class.getSimpleName(), "listener = null");
            waitActions.offer(new Action("onComplete", null));
            return;
        }
        if (waitActions.size() > 0) {
            waitActions.offer(new Action("onComplete", null));
            checkQueue();
        } else {
            listener.onComplete();
        }
    }

    @Override
    public void onError(String message) {
        if (listener == null) {
            Log.d(JNILinesorter.class.getSimpleName(), "listener = null");
            waitActions.offer(new Action("onError", message));
            return;
        }
        if (waitActions.size() > 0) {
            waitActions.offer(new Action("onError", message));
            checkQueue();
        } else {
            listener.onError(message);
        }
    }

    private void checkQueue() {
        if (listener != null) {
            while (!waitActions.isEmpty() && listener != null) {
                Action action = waitActions.poll();
                switch (action.name) {
                    case "onProcess":
                        listener.onProcess(action.getValue());
                        break;
                    case "onComplete":
                        listener.onComplete();
                        break;
                    case "onError":
                        listener.onError(action.getValue());
                        break;
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Native

    static {
        System.loadLibrary("linesorter");
    }

    public native void InitApplication();

    public native void SetListener(NativeCallListener listener);

    public native boolean SetFilter(String filter);

    public native void AddSourceBlock(String block);

    public native int NumOfMatches(String block);


    // Native
    // ---------------------------------------------------------------------------------------------

    private static class Action {

        private String name, value;

        public Action(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
