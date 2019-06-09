package atproj.com.linesorter.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import atproj.com.linesorter.R;
import atproj.com.linesorter.app.LinesorterApp;
import atproj.com.linesorter.model.SessionContext;
import atproj.com.linesorter.presenters.LoaderPresenter;
import atproj.com.linesorter.presenters.SearchPresenter;
import atproj.com.linesorter.ui.adapter.LineAdapter;
import atproj.com.linesorter.ui.decorator.DividerItemDecoration;
import atproj.com.linesorter.utils.AppFileUtil;
import atproj.com.linesorter.views.LoaderView;
import atproj.com.linesorter.views.SearchView;

public class LinesFragment extends Fragment implements LoaderView, SearchView {

    static private final String LOG_FILE_NAME = "results.log";

    private static final String ARG_URL = "url";
    private static final String ARG_LOAD = "load";

    private static final String KEY_URL = "keyUrl";
    private static final String KEY_LOAD = "keyLoad";
    private static final String KEY_TRY_VISIBLE = "keyTryVisible";
    private static final String KEY_IS_LOADING = "keyIsLoading";

    private TextView selectedTextView;
    private ImageView copyButton;
    private RecyclerView linesRecylerView;
    private Button tryAgainButton;
    private TextView loaderTextView;

    private SessionContext sessionContext = LinesorterApp.getInstance().getSessionContext();

    private String url;
    private boolean load = false;
    private int tryVisible = View.GONE;
    private boolean isLoading = false;

    private LineAdapter lineAdapter;

    private OnFragmentListener listener;

    private LoaderPresenter loaderPresenter;
    private SearchPresenter searchPresenter;

    public static LinesFragment newInstance(String url) {
        LinesFragment fragment = new LinesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putBoolean(ARG_LOAD, true);
        fragment.setArguments(args);
        return fragment;
    }

    //----------------------------------------------------------------------------------------------
    //                          Lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            url = savedInstanceState.getString(KEY_URL);
            load = savedInstanceState.getBoolean(KEY_LOAD);
            tryVisible = savedInstanceState.getInt(KEY_TRY_VISIBLE);
            isLoading = savedInstanceState.getBoolean(KEY_IS_LOADING);
        } else if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            load = getArguments().getBoolean(ARG_LOAD);
        }

        if (load) {
            sessionContext.clear();
        }

        loaderPresenter = new LoaderPresenter(this, LinesorterApp.getInstance().getTextLoader());

        searchPresenter = new SearchPresenter(this, LinesorterApp.getInstance().getJniLinesorter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lines, container, false);

        selectedTextView = v.findViewById(R.id.selected_text_view);
        selectedTextView.setText(String.valueOf(sessionContext.getSelectedItems().size()));

        copyButton = v.findViewById(R.id.copy_button);
        copyButton.setEnabled(sessionContext.getSelectedItems().size() > 0);
        copyButton.setOnClickListener(onCopyButtonClick);

        tryAgainButton = v.findViewById(R.id.try_again_button);
        tryAgainButton.setVisibility(tryVisible);
        tryAgainButton.setOnClickListener(onTryAgainButtonClick);

        loaderTextView = v.findViewById(R.id.loader_text_view);
        loaderTextView.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        linesRecylerView = v.findViewById(R.id.lines_recycler_view);
        initRecyclerView();

        if (load) {
            loaderPresenter.load(url);

            isLoading = true;
            loaderTextView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }

        loaderPresenter.onResume();
        searchPresenter.onResume();

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_URL, url);
        outState.putBoolean(KEY_LOAD, false);
        outState.putInt(KEY_TRY_VISIBLE, tryVisible);
        outState.putBoolean(KEY_IS_LOADING, isLoading);

        sessionContext.setItems(lineAdapter.getItems());
        sessionContext.setSelectedItems(lineAdapter.getSelectedItems());
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

    @Override
    public void onDestroyView() {
        loaderPresenter.onPause();
        searchPresenter.onPause();
        super.onDestroyView();
    }

    //                          Lifecycle
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //                          Method(s)

    private void initRecyclerView() {
        lineAdapter = new LineAdapter(sessionContext.getSelectedItems(), new LineAdapter.OnChangeSelectedItems() {
            @Override
            public void onChange(int size) {
                copyButton.setEnabled(size > 0);
                selectedTextView.setText(String.valueOf(size));
            }
        });
        lineAdapter.setItems(sessionContext.getItems());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        linesRecylerView.setHasFixedSize(true);
        linesRecylerView.setLayoutManager(layoutManager);
        linesRecylerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                R.drawable.divider_line));
        linesRecylerView.setAdapter(lineAdapter);
    }

    //                          Method(s)
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //                          Action(s)

    private View.OnClickListener onCopyButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";
            for (int key : lineAdapter.getSelectedItems().keySet()) {
                if (text.length() > 0)
                    text += "\n";
                text += lineAdapter.getSelectedItems().get(key);

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text copied", text);
                clipboard.setPrimaryClip(clip);
            }

            lineAdapter.clearSelectedItems();

            selectedTextView.setText(String.valueOf(sessionContext.getSelectedItems().size()));
            copyButton.setEnabled(sessionContext.getSelectedItems().size() > 0);

            Toast.makeText(getActivity(), "Copied", Toast.LENGTH_LONG);
        }
    };

    private View.OnClickListener onTryAgainButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lineAdapter.clear();
            tryVisible = View.GONE;
            tryAgainButton.setVisibility(tryVisible);
            loaderPresenter.load(url);
        }
    };

    //                          Action(s)
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //                          View Impl

    @Override
    public void onLoadingProcess(String lines) {
        searchPresenter.addSourceBlock(lines);
    }

    @Override
    public void onLoadComplete() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isLoading = false;
                loaderTextView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onLinesFragmentLoadError();
                tryVisible = View.VISIBLE;
                tryAgainButton.setVisibility(tryVisible);

                isLoading = false;
                loaderTextView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onSearch(final String line) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineAdapter.addItem(line);
                AppFileUtil.addLineInFile(getActivity(), LOG_FILE_NAME, line);
            }
        });
    }

    @Override
    public void onSearchComplete() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ;
            }
        });
    }


    //                          View Impl
    //----------------------------------------------------------------------------------------------

    public interface OnFragmentListener {

        void onLinesFragmentLoadError();

    }
}
