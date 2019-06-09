package atproj.com.linesorter.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import atproj.com.linesorter.R;
import atproj.com.linesorter.app.LinesorterApp;
import atproj.com.linesorter.presenters.FilterPresenter;
import atproj.com.linesorter.views.FilterView;


public class SetupFragment extends Fragment implements FilterView {

    private static final String ARG_URL = "url";
    private static final String ARG_FILTER = "filter";

    private EditText urlEditText;
    private EditText filterEditText;
    private Button loadButton;

    private String url;
    private String filter;

    private FilterPresenter filterPresenter;

    private OnFragmentListener listener;

    public static SetupFragment newInstance() {
        SetupFragment fragment = new SetupFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    //----------------------------------------------------------------------------------------------
    //                          Lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            url = savedInstanceState.getString(ARG_URL);
            filter = savedInstanceState.getString(ARG_FILTER);
        }

        filterPresenter = new FilterPresenter(this, LinesorterApp.getInstance().getJniLinesorter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setup, container, false);

        urlEditText = v.findViewById(R.id.url_edit_text);
        urlEditText.addTextChangedListener(onUrlTextViewTextChanged);

        filterEditText = v.findViewById(R.id.filter_edit_text);
        filterEditText.addTextChangedListener(onFilterTextViewTextChanged);

        loadButton = v.findViewById(R.id.load_button);
        loadButton.setOnClickListener(onLoadButtonClick);

        setFilterAndValidate();

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_URL, url);
        outState.putString(ARG_FILTER, filter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            listener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //                          Lifecycle
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //                          Method(s)

    private void setFilterAndValidate() {
        boolean loadButtonEnabled = false;

        if (filter != null && filter.length() > 0 && filterPresenter.setFilter(filter)) {
            if (URLUtil.isNetworkUrl(url)) {
                loadButtonEnabled = true;
            }
        }

        loadButton.setEnabled(loadButtonEnabled);
    }

    //                          Method(s)
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //                          Action(s)

    private TextWatcher onUrlTextViewTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            url = s.toString();
            setFilterAndValidate();
        }
    };

    private TextWatcher onFilterTextViewTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            filter = s.toString();
            setFilterAndValidate();
        }
    };

    private View.OnClickListener onLoadButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onSetupFragmentLoad(url);
        }
    };

    //                          Action(s)
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //                          View Impl


    //                          View Impl
    //----------------------------------------------------------------------------------------------



    public interface OnFragmentListener {

        void onSetupFragmentLoad(String url);

    }
}
