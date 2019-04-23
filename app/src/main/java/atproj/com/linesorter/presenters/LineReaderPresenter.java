package atproj.com.linesorter.presenters;

import atproj.com.linesorter.tasks.FileLineReader;
import atproj.com.linesorter.views.LineReaderView;

/**
 * Created by andre on 17-Apr-19.
 */

public class LineReaderPresenter {

    private LineReaderView view;
    private String filePath;
    private int chunkOfLines;

    private int position;

    public LineReaderPresenter(LineReaderView view, String filePath, int chunkOfLines) {
        this.view = view;
        this.filePath = filePath;
        this.chunkOfLines = chunkOfLines;
    }

    public void startRead() {
        position = 0;
        read();
    }

    public void readNext() {
        read();
    }

    private void read() {
        FileLineReader fileLineReader = new FileLineReader(new FileLineReader.LineReaderListener() {
            @Override
            public void onComplete(String lines, int size) {
                position += size;

                if (size < chunkOfLines)
                    view.onLineReadComplete();

                view.onLineReadProcess(lines);
            }

            @Override
            public void onError() {
                view.showError();
            }
        });
        fileLineReader.execute(filePath, String.valueOf(position), String.valueOf(chunkOfLines));
    }
}
