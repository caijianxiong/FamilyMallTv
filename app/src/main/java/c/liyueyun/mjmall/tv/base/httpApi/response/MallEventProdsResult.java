package c.liyueyun.mjmall.tv.base.httpApi.response;

import java.util.List;

/**
 * Created by songjie on 2019-03-27
 */
public class MallEventProdsResult {
    private String id;  // 活动id,
    private String bg;  // url,									//活动背景图地址
    private List<MallRowItem> items;   // [									//商品数组

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public List<MallRowItem> getItems() {
        return items;
    }

    public void setItems(List<MallRowItem> items) {
        this.items = items;
    }
}
