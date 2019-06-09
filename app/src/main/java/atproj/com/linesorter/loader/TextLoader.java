package atproj.com.linesorter.loader;

public interface TextLoader {

    void setListener(TextLoaderListener listener);

    void removeListener();

    void load(String src);

}
