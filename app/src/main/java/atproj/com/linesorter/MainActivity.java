package atproj.com.linesorter;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import atproj.com.linesorter.adapter.LineArrayAdapter;
import atproj.com.linesorter.presenters.JNIPresenter;
import atproj.com.linesorter.presenters.LineReaderPresenter;
import atproj.com.linesorter.presenters.TextLoaderPresenter;
import atproj.com.linesorter.utils.ActivityHelper;
import atproj.com.linesorter.utils.AppFileUtil;
import atproj.com.linesorter.views.JNIView;
import atproj.com.linesorter.views.LineReaderView;
import atproj.com.linesorter.views.TextLoaderView;

public class MainActivity extends Activity implements JNIView, TextLoaderView, LineReaderView {

    static private final String TEMP_FILE_NAME = "tmp_line_sorter.txt";
    static private final String LOG_FILE_NAME = "results.log";

    static private final int CHUNK_OF_LINES = 100;

    private EditText urlText;
    private EditText filterText;
    private Button startButton;
    private Button tryAgainButton;
    private TextView loadingText;
    private Button copyButton;
    private ListView linesListView;

    private HashMap<Integer, String> selectedLines;

    private JNIPresenter jniPresenter;
    private TextLoaderPresenter textLoaderPresenter;
    private LineReaderPresenter lineReaderPresenter;

    private LineArrayAdapter linesAdapter;

    private boolean linesRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
        deleteTempTextFile();
    }

    private void initContent() {
        urlText = findViewById(R.id.url_text);

        filterText = findViewById(R.id.filter_text);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(onStartButtonClick);

        tryAgainButton = findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(onStartButtonClick);

        loadingText = findViewById(R.id.loading_text);

        copyButton = findViewById(R.id.copy_button);
        copyButton.setOnClickListener(onCopyButtonClick);

        linesListView = findViewById(R.id.lines_list_view);

        linesListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        linesAdapter = new LineArrayAdapter(this);
        linesAdapter.setOnChangeSelectedListener(new LineArrayAdapter.OnChangeSelectedListener() {
            @Override
            public void onChange(boolean isSelected, int position) {
                if (isSelected) {
                    selectedLines.put(position, linesAdapter.getItem(position));
                } else {
                    selectedLines.remove(position);
                }

                if (selectedLines.size() > 0) {
                    copyButton.setVisibility(View.VISIBLE);
                } else {
                    copyButton.setVisibility(View.GONE);
                }
            }
        });
        linesListView.setAdapter(linesAdapter);

        String tempFilePath = AppFileUtil.getPath(getApplicationContext(), LOG_FILE_NAME);

        jniPresenter = new JNIPresenter(this);
        textLoaderPresenter = new TextLoaderPresenter(this, tempFilePath);
        lineReaderPresenter = new LineReaderPresenter(this, tempFilePath, CHUNK_OF_LINES);

        // Uncomment for test
        //testRegex();
    }


    private void requestPermisions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int res = grantResults[i];

            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && res == PackageManager.PERMISSION_GRANTED) {
                loadText();
            }
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }


    // ---------------------------------------------------------------------------------------------
    // Listeners

    protected View.OnClickListener onStartButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkPermissions())
                loadText();
            else
                requestPermisions();
        }
    };

    protected View.OnClickListener onCopyButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String text = "";
            for (int key : selectedLines.keySet()) {
                if (text.length() > 0)
                    text += "\n";
                text += selectedLines.get(key);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text copied", text);
                clipboard.setPrimaryClip(clip);
            }

            Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG);
        }
    };

    // Listeners
    // ---------------------------------------------------------------------------------------------





    // ---------------------------------------------------------------------------------------------
    // LineReaderView methods

    @Override
    public void onLineReadProcess(String text) {
        jniPresenter.start(text);
    }

    @Override
    public void onLineReadComplete() {
        linesRead = true;
    }

    // LineReaderView methods
    // ---------------------------------------------------------------------------------------------





    // ---------------------------------------------------------------------------------------------
    // TextLoaderView methods

    @Override
    public void onTextLoaded() {
        linesRead = false;

        lineReaderPresenter.startRead();
    }

    // TextLoaderView methods
    // ---------------------------------------------------------------------------------------------




    // ---------------------------------------------------------------------------------------------
    // JNIView methods

    @Override
    public void onParsingComplete() {
        if (!linesRead) {
            lineReaderPresenter.readNext();
        } else {
            showLoaded();
            deleteTempTextFile();
        }
    }

    @Override
    public void onParsingProcess(String line) {
        linesAdapter.add(line);
        AppFileUtil.addLineInFile(getApplicationContext(), LOG_FILE_NAME, line);
    }

    // JNIView methods
    // ---------------------------------------------------------------------------------------------




    // ---------------------------------------------------------------------------------------------
    // LoadingView methods

    @Override
    public void showLoading() {
        filterText.setEnabled(false);
        urlText.setEnabled(false);

        startButton.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.GONE);
        loadingText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoaded() {
        filterText.setEnabled(true);
        urlText.setEnabled(true);

        startButton.setVisibility(View.VISIBLE);
        tryAgainButton.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        filterText.setEnabled(true);
        urlText.setEnabled(true);

        startButton.setVisibility(View.GONE);
        tryAgainButton.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.GONE);
    }

    // LoadingView methods
    // ---------------------------------------------------------------------------------------------


    private void loadText() {
        deleteTempTextFile();
        selectedLines = new HashMap<>();
        copyButton.setVisibility(View.GONE);
        ActivityHelper.hideKeyboard(MainActivity.this);

        String filter = filterText.getText().toString();

        if (jniPresenter.setFilter(filter)) {
            deleteTempTextFile();
            String url = urlText.getText().toString();

            textLoaderPresenter.load(url);
        } else {
            Toast.makeText(getApplicationContext(), "Filter is incorrect", Toast.LENGTH_LONG);
        }
    }


    private void deleteTempTextFile() {
        File file = AppFileUtil.getFile(getApplicationContext(), TEMP_FILE_NAME);
        if (file.exists())
            file.delete();
    }

    // ---------------------------------------------------------------------------------------------
    // Test regex methods

    public void testRegex() {
        // test filter
        if (jniPresenter.setFilter("\\*\\")) {
            Log.d("Error Test", "filter");
        }

        jniPresenter.setFilter("*n");

        if (jniPresenter.testStart("efwehfjewhfkjewhjkfhewkjfhjewn") != 1) {
            Log.d("Error Test", "regex1");
        }

        if (jniPresenter.testStart("efwehfjewhfkjewhjkfhnewkjfhjew") != 0) {
            Log.d("Error Test", "regex2");
        }

        jniPresenter.setFilter("*\\*h*");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*\\*h");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 0) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*jew");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*hje");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 0) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("efw*");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("fwe*");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 0) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("?fwe*");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*hje?");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*jew?");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 0) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*hf?je*");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("*hf?je*je?");
        if (jniPresenter.testStart("efwehfjewhfkjew*hjkfhnewkjfhjew") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("te*");
        if (jniPresenter.testStart("testest") != 1) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("es*");
        if (jniPresenter.testStart("testest") != 0) {
            Log.d("Error Test", "regex3");
        }

        jniPresenter.setFilter("test");
        if (jniPresenter.testStart("testest") != 0) {
            Log.d("Error Test", "regex3");
        }
    }

    // Test methods
    // ---------------------------------------------------------------------------------------------
}
