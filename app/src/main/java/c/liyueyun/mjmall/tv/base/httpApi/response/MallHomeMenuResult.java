package c.liyueyun.mjmall.tv.base.httpApi.response;

import java.util.List;

/**
 * Created by songjie on 2019-03-27
 */
public class MallHomeMenuResult {
    private String logo;
    private String bg;
    private List<MenuItem> menus;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public List<MenuItem> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuItem> menus) {
        this.menus = menus;
    }

    public class MenuItem{
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
