package atproj.com.linesorter.views;

/**
 * Created by andre on 17-Apr-19.
 */

public interface LineReaderView extends LoadingView {

    void onLineReadProcess(String text);
    void onLineReadComplete();
}
