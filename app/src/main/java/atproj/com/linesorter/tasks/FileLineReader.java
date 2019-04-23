package atproj.com.linesorter.tasks;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Created by andre on 16-Apr-19.
 */

public class FileLineReader extends AsyncTask<String, Void, String> {

    private LineReaderListener listener;
    private int size;

    public FileLineReader(LineReaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String filePath = strings[0];
        int position = Integer.parseInt(strings[1]);
        int length = Integer.parseInt(strings[2]);

        String result = "";
        size = 0;

        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            LineNumberReader lineReader = new LineNumberReader(fileReader);

            lineReader.setLineNumber(position);

            StringBuilder stringBuilder = new StringBuilder();

            String line;
            int i = 0;
            while ((line = lineReader.readLine()) != null && i < length) {
                if (stringBuilder.length() > 0)
                    stringBuilder.append('\n');
                stringBuilder.append(line);
                i++;
            }
            size = i;

            lineReader.close();

            result = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return result;
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if (result == null)
            listener.onError();
        else
            listener.onComplete(result, size);
    }

    public interface LineReaderListener {
        void onComplete(String lines, int size);
        void onError();
    }
}
