package atproj.com.linesorter.presenters;

import atproj.com.linesorter.jni.JNILinesorter;
import atproj.com.linesorter.jni.NativeCallListener;
import atproj.com.linesorter.views.SearchView;

public class SearchPresenter implements ConcurentPresenter {

    private JNILinesorter linesorter;
    private SearchView view;


    public SearchPresenter(SearchView view, JNILinesorter linesorter) {
        this.view = view;
        this.linesorter = linesorter;
    }

    public void onResume() {
        linesorter.setListener(nativeCallListener);
    }

    public void onPause() {
        linesorter.removeListener();
    }

    public void addSourceBlock(final String block) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                linesorter.AddSourceBlock(block);
            }
        });
        thread.run();
    }

    private NativeCallListener nativeCallListener = new NativeCallListener() {
        @Override
        public void onProcess(String line) {
            view.onSearch(line);
        }

        @Override
        public void onComplete() {
            view.onSearchComplete();
        }

        @Override
        public void onError(String message) {
            ;
        }
    };
}
