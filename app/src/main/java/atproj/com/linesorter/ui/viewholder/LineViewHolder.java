package atproj.com.linesorter.ui.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import atproj.com.linesorter.R;
import atproj.com.linesorter.ui.adapter.LineAdapter;

public class LineViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout holderLayout;
    private TextView textView;

    private String item;
    private int position;
    private boolean isChecked = false;

    private OnChangeSelectedListener listener;

    public LineViewHolder(@NonNull View itemView) {
        super(itemView);

        holderLayout = itemView.findViewById(R.id.holder_layout);
        textView = itemView.findViewById(R.id.text_view);
    }

    public void setItem(String itm, int pos, boolean isChkd) {
        this.item = itm;
        this.position = pos;
        this.isChecked = isChkd;

        if (item != null) {
            textView.setText(item);
        }

        updateBackground();


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                listener.onChange(isChecked, position);
                updateBackground();
            }
        });
    }

    private void updateBackground() {
        if (isChecked) {
            holderLayout.setBackgroundResource(R.drawable.bg_selected_line_cell);
        } else {
            holderLayout.setBackgroundResource(R.drawable.bg_line_cell);
        }
    }

    public void setOnChangeSelectedListener(OnChangeSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnChangeSelectedListener {
        void onChange(boolean isSelected, int position);
    }
}
