package atproj.com.linesorter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionContext {

    public SessionContext() {
        items = new ArrayList<>();
        selectedItems = new HashMap<>();
    }

    private List<String> items;
    private HashMap<Integer, String> selectedItems;

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public HashMap<Integer, String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(HashMap<Integer, String> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void clear() {
        items.clear();
        selectedItems.clear();
    }
}
