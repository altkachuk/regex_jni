package atproj.com.linesorter.tasks;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andre on 04-Apr-19.
 */

public class TextLoader extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    private TextLoaderListener listener;

    public TextLoader(TextLoaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];
        String filePath = params[1];
        try {
            URL myUrl = new URL(stringUrl);
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(1024);

            connection.connect();

            InputStream input = new BufferedInputStream(myUrl.openStream());
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[1024];

            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "complete";
    }


    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if (result == null)
            listener.onError();
        else
            listener.onComplete();
    }

    public interface TextLoaderListener {
        void onComplete();
        void onError();
    }
}
