package atproj.com.linesorter.views;

/**
 * Created by andre on 17-Apr-19.
 */

public interface LoaderView extends LoadingView {

    void onLoadingProcess(String lines);

    void onLoadComplete();

}
