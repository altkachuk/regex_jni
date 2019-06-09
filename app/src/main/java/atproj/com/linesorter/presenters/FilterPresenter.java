package atproj.com.linesorter.presenters;

import atproj.com.linesorter.jni.JNILinesorter;
import atproj.com.linesorter.jni.NativeCallListener;
import atproj.com.linesorter.views.FilterView;
import atproj.com.linesorter.views.JNIView;

/**
 * Created by andre on 17-Apr-19.
 */

public class FilterPresenter {

    private JNILinesorter linesorter;
    private FilterView view;


    public FilterPresenter(FilterView view, JNILinesorter linesorter) {
        this.view = view;
        this.linesorter = linesorter;
    }

    public boolean setFilter(String filter) {
        boolean isValid = linesorter.SetFilter(filter);
        return isValid;
    }

}
