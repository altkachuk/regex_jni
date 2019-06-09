package atproj.com.linesorter.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import atproj.com.linesorter.R;
import atproj.com.linesorter.ui.activities.LinesActivity;
import atproj.com.linesorter.ui.fragments.SetupFragment;

public class MainActivity extends AppCompatActivity implements SetupFragment.OnFragmentListener {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSetupFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int res = grantResults[i];

            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && res == PackageManager.PERMISSION_GRANTED) {
                startActivity(LinesActivity.newIntent(this, url));
            }
        }
    }

    private void requestPermisions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1);
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void startSetupFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment setuptFragment = manager.findFragmentById(R.id.fragment_container);
        if (setuptFragment == null) {
            setuptFragment = SetupFragment.newInstance();
            manager.beginTransaction()
                    .add(R.id.fragment_container, setuptFragment)
                    .commit();
        }
    }

    @Override
    public void onSetupFragmentLoad(String url) {
        if (checkPermissions()) {
            startActivity(LinesActivity.newIntent(this, url));
        } else {
            this.url = url;
            requestPermisions();
        }
    }
}
