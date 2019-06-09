package atproj.com.linesorter.utils;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by andre on 17-Apr-19.
 */

public class AppFileUtil {

    static public String getPath(Context context, String name) {
        return getFile(context, name).getAbsolutePath();
    }

    static public File getFile(Context context, String name) {
        String dataDir = context.getApplicationInfo().dataDir;
        File file = new File(dataDir, name);
        return file;
    }

    static public boolean addLineInFile(Context context, String name, String line) {
        File file = getFile(context, name);
        try (FileOutputStream output = new FileOutputStream(file);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));) {
            if (!file.exists())
                file.createNewFile();

            writer.write(line);
            writer.newLine();

            writer.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
