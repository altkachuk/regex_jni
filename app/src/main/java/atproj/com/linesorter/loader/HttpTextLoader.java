package atproj.com.linesorter.loader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

import atproj.com.linesorter.jni.JNILinesorter;
import atproj.com.linesorter.jni.NativeCallListener;

/**
 * Created by andre on 04-Apr-19.
 */

public class HttpTextLoader implements TextLoader{

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    private volatile static TextLoaderListener listener;
    private static ConcurrentLinkedQueue<Action> waitActions;

    public HttpTextLoader() {
        waitActions = new ConcurrentLinkedQueue<>();
    }

    public void setListener(TextLoaderListener listener) {
        this.listener = listener;
        checkQueue();
    }

    public void removeListener() {
        this.listener = null;
    }

    @Override
    public void load(String url) {
        GetAsyncTask getAsyncTask = new GetAsyncTask();
        getAsyncTask.execute(url);
    }

    private static class GetAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];

            StringBuilder stringBuilder = new StringBuilder();

            byte data[] = new byte[1024];

            String tail = "";
            int count = 0;

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

                while ((count = input.read(data)) != -1) {
                    String string = new String(data, 0, count, StandardCharsets.US_ASCII);
                    String[] lines = parseLines(string);

                    if (lines.length == 1) {
                        tail = lines[0];
                    } else if (lines.length > 1) {
                        int i = 0;
                        for (; i < lines.length - 1; i++) {
                            String l = lines[i];
                            if (i == 0) {
                                l = tail + l;
                            }
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append('\n');
                            }
                            stringBuilder.append(l);
                        }
                        tail = lines[i];
                        onProcess(stringBuilder.toString());
                        stringBuilder.delete(0, stringBuilder.length());
                    }
                }

                if (tail.length() > 0) {
                    onProcess(tail);
                }

                input.close();
            }
            catch(IOException e){
                Log.e(HttpTextLoader.class.getSimpleName(), e.getMessage(), e);
                onError(e.getMessage());
                return null;
            } catch (Exception e) {
                Log.e(HttpTextLoader.class.getSimpleName(), e.getMessage(), e);
                onError(e.getMessage());
                return null;
            }

            onComplete();
            return null;
        }

        private String[] parseLines(String src) {
            return src.split("\\n");
        }

        private void onProcess(String line) {
            if (listener == null) {
                Log.d(JNILinesorter.class.getSimpleName(), "listener = null");
                waitActions.offer(new Action("onProcess", line));
                return;
            }
            if (waitActions.size() > 0) {
                waitActions.offer(new Action("onProcess", line));
                checkQueue();
            } else {
                listener.onProcess(line);
            }
        }

        private void onComplete() {
            if (listener == null) {
                Log.d(JNILinesorter.class.getSimpleName(), "listener = null");
                waitActions.offer(new Action("onComplete", null));
                return;
            }
            if (waitActions.size() > 0) {
                waitActions.offer(new Action("onComplete", null));
                checkQueue();
            } else {
                listener.onComplete();
            }
        }

        private void onError(String message) {
            if (listener == null) {
                Log.d(JNILinesorter.class.getSimpleName(), "listener = null");
                waitActions.offer(new Action("onError", message));
                return;
            }
            if (waitActions.size() > 0) {
                waitActions.offer(new Action("onError", message));
                checkQueue();
            } else {
                listener.onError(message);
            }
        }
    }

    private static void checkQueue() {
        if (listener != null) {
            while (!waitActions.isEmpty() && listener != null) {
                Action action = waitActions.poll();
                switch (action.name) {
                    case "onProcess":
                        listener.onProcess(action.getValue());
                        break;
                    case "onComplete":
                        listener.onComplete();
                        break;
                    case "onError":
                        listener.onError(action.getValue());
                        break;
                }
            }
        }
    }

    private static class Action {

        private String name, value;

        public Action(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
