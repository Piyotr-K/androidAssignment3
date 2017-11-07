package ca.bcit.ass3.katz_kao;

/**
 * Created by Lel on 2017-11-07.
 */

public class Item {
    private String itemName;
    private String itemUnit;
    private int itemCount;

    public Item(String name, String unit, int count) {
        itemName = name;
        itemUnit = unit;
        itemCount = count;
    }

    public void setItemName(String name) {
        itemName = name;
    }

    public void setItemUnit(String unit) {
        itemUnit = unit;
    }

    public void setItemCount(int count) {
        itemCount = count;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public int getItemCount() {
        return itemCount;
    }
}
