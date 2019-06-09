package atproj.com.linesorter.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import atproj.com.linesorter.R;
import atproj.com.linesorter.ui.fragments.LinesFragment;

public class LinesActivity extends AppCompatActivity implements LinesFragment.OnFragmentListener {

    private static final String EXTRA_URL = "extraUrl";

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, LinesActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);

        String url = getIntent().getStringExtra(EXTRA_URL);
        startLinesFragment(url);
    }

    private void startLinesFragment(String url) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment linesFragment = manager.findFragmentById(R.id.fragment_container);
        if (linesFragment == null) {
            linesFragment = LinesFragment.newInstance(url);
            manager.beginTransaction()
                    .add(R.id.fragment_container, linesFragment)
                    .commit();
        }
    }

    @Override
    public void onLinesFragmentLoadError() {

    }
}
