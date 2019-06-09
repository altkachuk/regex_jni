package atproj.com.linesorter.presenters;

import atproj.com.linesorter.loader.TextLoader;
import atproj.com.linesorter.loader.TextLoaderListener;
import atproj.com.linesorter.views.LoaderView;

/**
 * Created by andre on 17-Apr-19.
 */

public class LoaderPresenter implements ConcurentPresenter {

    private LoaderView view;
    private TextLoader textLoader;

    public LoaderPresenter(LoaderView view, TextLoader textLoader) {
        this.view = view;
        this.textLoader = textLoader;
    }

    public void onResume() {
        textLoader.setListener(new TextLoaderListener() {
            @Override
            public void onProcess(String lines) {
                view.onLoadingProcess(lines);
            }

            @Override
            public void onComplete() {
                view.onLoadComplete();
            }

            @Override
            public void onError(String message) {
                view.onError();
            }
        });
    }

    public void onPause() {
        textLoader.removeListener();
    }

    public void load(String url) {

        textLoader.load(url);
    }
}
