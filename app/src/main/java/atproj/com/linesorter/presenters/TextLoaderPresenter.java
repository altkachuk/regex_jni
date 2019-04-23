package atproj.com.linesorter.presenters;

import atproj.com.linesorter.tasks.TextLoader;
import atproj.com.linesorter.views.TextLoaderView;

/**
 * Created by andre on 17-Apr-19.
 */

public class TextLoaderPresenter {

    private TextLoaderView view;
    private String filePath;

    public TextLoaderPresenter(TextLoaderView view, String filePath) {
        this.view = view;
        this.filePath = filePath;
    }

    public void load(String url) {
        view.showLoading();

        TextLoader textLoader = new TextLoader(new TextLoader.TextLoaderListener() {
            @Override
            public void onComplete() {
                view.onTextLoaded();
            }

            @Override
            public void onError() {
                view.showError();
            }
        });

        textLoader.execute(url, filePath);
    }
}
