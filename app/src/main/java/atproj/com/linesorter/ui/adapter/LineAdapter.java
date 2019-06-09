package atproj.com.linesorter.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atproj.com.linesorter.R;
import atproj.com.linesorter.ui.viewholder.LineViewHolder;

public class LineAdapter extends RecyclerView.Adapter<LineViewHolder> {

    private OnChangeSelectedItems listener;
    private List<String> items;
    private HashMap<Integer, String> selectedItems;

    public LineAdapter(HashMap<Integer, String> selectedItems, OnChangeSelectedItems listener) {
        super();
        if (selectedItems != null) {
            this.selectedItems = selectedItems;
        } else {
            this.selectedItems = new HashMap<>();
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_line, null);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {
        final String item = items.get(position);
        if (item != null) {
            holder.setItem(item, position, selectedItems.containsKey(position));
            holder.setOnChangeSelectedListener(new LineViewHolder.OnChangeSelectedListener() {
                @Override
                public void onChange(boolean isSelected, int position) {
                    if (isSelected) {
                        selectedItems.put(position, item);
                    } else {
                        selectedItems.remove(position);
                    }

                    listener.onChange(selectedItems.size());
                }
            });
        }
    }

    public HashMap<Integer, String> getSelectedItems() {
        return selectedItems;
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public String getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<String> rangeItems) {
        if (items == null) {
            items = new ArrayList<>();
        }

        if (rangeItems != null) {
            int pos = items.size();
            items.addAll(rangeItems);
            notifyItemRangeInserted(pos, items.size());
        }
    }

    public void addItem(String item) {
        if (items == null) {
            items = new ArrayList<>();
        }

        if (item != null) {
            int pos = items.size();
            items.add(item);
            notifyItemInserted(pos);
        }
    }

    public void clearSelectedItems() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void clear() {
        selectedItems.clear();
        if (items != null && items.size() > 0) {
            int oldSize = items.size();
            items.clear();
            notifyItemRangeRemoved(0, oldSize);
        }
    }

    public interface OnChangeSelectedItems {

        void onChange(int size);

    }
}
