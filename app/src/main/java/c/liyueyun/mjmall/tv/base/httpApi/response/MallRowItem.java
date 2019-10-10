package c.liyueyun.mjmall.tv.base.httpApi.response;

import java.util.List;

/**
 * Created by songjie on 2019-03-28
 */
public class MallRowItem {
    private int row;    // 1,								//所在行数
    private int high;   // 400,
    private List<MallCell> cells;   // [

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public List<MallCell> getCells() {
        return cells;
    }

    public void setCells(List<MallCell> cells) {
        this.cells = cells;
    }

}
