package atproj.com.linesorter.app;

import android.app.Application;

import atproj.com.linesorter.jni.JNILinesorter;
import atproj.com.linesorter.loader.HttpTextLoader;
import atproj.com.linesorter.loader.TextLoader;
import atproj.com.linesorter.model.SessionContext;

public class LinesorterApp extends Application {

    private static LinesorterApp instance;

    public static LinesorterApp getInstance() {
        return instance;
    }

    private JNILinesorter jniLinesorter;

    private TextLoader textLoader;

    private SessionContext sessionContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        jniLinesorter = new JNILinesorter();

        textLoader = new HttpTextLoader();

        sessionContext = new SessionContext();
    }

    public JNILinesorter getJniLinesorter() {
        return jniLinesorter;
    }

    public TextLoader getTextLoader() {
        return textLoader;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

}
