package tv.cloudwalker.cloudwalkerworld.module;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cognoscis on 12/1/18.
 */

public class RecommendationRow {

    @SerializedName("rowHeader")
    private String rowHeader;
    @SerializedName("rowItems")
    private ArrayList<MovieTile> rowItems;

    public RecommendationRow(String rowHeader, ArrayList<MovieTile> rowItems) {
        this.rowHeader = rowHeader;
        this.rowItems = rowItems;
    }

    public String getRowHeader() {
        return rowHeader;
    }

    public void setRowHeader(String rowHeader) {
        this.rowHeader = rowHeader;
    }

    public ArrayList<MovieTile> getRowItems() {
        return rowItems;
    }

    public void setRowItems(ArrayList<MovieTile> rowItems) {
        this.rowItems = rowItems;
    }
}
