package atproj.com.linesorter.jni;

/**
 * Created by andre on 08-Apr-19.
 */

public interface NativeCallListener {

    void onProcess(String line);
    void onComplete();
    void onError(String message);
}
