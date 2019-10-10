package c.liyueyun.mjmall.tv.base.httpApi.response;

/**
 * Created by songjie on 2019-03-28
 */
public class MallCell {
    private String id;  // 'item1',
    private String type;    // prod,					//商品
    private String img; // url,					//图片地址
    private int cols;    // 3,					//所占列数
    private String title;   // '标题',				//商品名
    private String price;   // '19.8',				//现价
    private String oriPrice;    // '42'				//原价

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }
}
