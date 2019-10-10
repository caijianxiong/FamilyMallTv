package c.liyueyun.mjmall.tv.base.httpApi.response;

import java.util.List;

/**
 * Created by songjie on 2019-03-27
 */
public class MallProdResult {
    private String id; //商品id
    private String title; // '标题',
    private String desc; // '产品信息'
    private List<Gallery> gallery;//画廊
    private String price;   // '19.8',					//现价
    private String oriPrice;    // '42',					//原价
    private String tel; // '400-820-2856',			//客服电话
    private String url; // '商品地址'

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(List<Gallery> gallery) {
        this.gallery = gallery;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public class Gallery{
        String url; // '图片地址',
        String type;    // 'image' video/mp4

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
