package atproj.com.linesorter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import atproj.com.linesorter.R;

/**
 * Created by andre on 17-Apr-19.
 */

public class LineArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> stringList;
    private List<Boolean> checkedList;

    private OnChangeSelectedListener listener;

    public LineArrayAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        stringList = new ArrayList<>();
        checkedList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            stringList.add(values[i]);
            checkedList.add(false);
        }
    }

    public LineArrayAdapter(Context context) {
        super(context, -1);
        this.context = context;
        stringList = new ArrayList<>();
        checkedList = new ArrayList<>();
    }

    public void setOnChangeSelectedListener(OnChangeSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_lines, parent, false);

        TextView textView = rowView.findViewById(R.id.text_view);
        textView.setText(stringList.get(position));

        CheckBox check = rowView.findViewById(R.id.check);
        check.setChecked(checkedList.get(position));
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkedList.set(position, b);
                listener.onChange(b, position);
            }
        });

        return rowView;
    }

    @Override
    public void add(String object) {
        stringList.add(object);
        checkedList.add(false);
        super.add(object);
    }

    @Override
    public String getItem(int position) {
        return stringList.get(position);
    }

    public interface OnChangeSelectedListener {
        void onChange(boolean isSelected, int position);
    }
}
