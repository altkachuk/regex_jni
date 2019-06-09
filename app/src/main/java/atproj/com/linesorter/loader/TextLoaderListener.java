package atproj.com.linesorter.loader;

public interface TextLoaderListener {

    void onProcess(String lines);

    void onComplete();

    void onError(String message);

}
