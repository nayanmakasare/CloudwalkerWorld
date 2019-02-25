package tv.cloudwalker.cloudwalkerworld.module;

/**
 * Created by cognoscis on 13/3/18.
 */

public class SourceInfo {

    private String sourceName;
    private int sourceId;
    private String category;
    private int source_icon_id;

    public SourceInfo(String sourceName, String category, int source_icon_id, int sourceId) {
        this.sourceName = sourceName;
        this.category = category;
        this.source_icon_id=source_icon_id;
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getSource_icon_id() {
        return source_icon_id;
    }

    public void setSource_icon_id(int source_icon_id) {
        this.source_icon_id = source_icon_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
}
