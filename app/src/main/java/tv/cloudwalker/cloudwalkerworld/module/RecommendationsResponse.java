package tv.cloudwalker.cloudwalkerworld.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cognoscis on 12/1/18.
 */

public class RecommendationsResponse {

    @SerializedName("rows")
    private RecommendationRow rows;

    public RecommendationsResponse(RecommendationRow rows) {
        this.rows = rows;
    }

    public RecommendationRow getRows() {
        return rows;
    }

    public void setRows(RecommendationRow rows) {
        this.rows = rows;
    }
}
