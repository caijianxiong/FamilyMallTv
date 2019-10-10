package c.liyueyun.mjmall.tv.base.httpApi.response;

import java.util.List;

/**
 * Created by songjie on 003 1/3.
 */

public class UIMangerResult {
    private SrcItem logo;
    private SrcItem notice;
    private HomeData home;
    private List<SrcItem> about;
    private Emall emall;

    public SrcItem getLogo() {
        return logo;
    }

    public void setLogo(SrcItem logo) {
        this.logo = logo;
    }

    public SrcItem getNotice() {
        return notice;
    }

    public void setNotice(SrcItem notice) {
        this.notice = notice;
    }

    public HomeData getHome() {
        return home;
    }

    public void setHome(HomeData home) {
        this.home = home;
    }

    public List<SrcItem> getAbout() {
        return about;
    }

    public void setAbout(List<SrcItem> about) {
        this.about = about;
    }

    public Emall getEmall() {
        return emall;
    }

    public void setEmall(Emall emall) {
        this.emall = emall;
    }

    /**
     * 主界面数据
     */
    public class HomeData{
        private List<SrcItem> unconnected;
        private List<ConnectItem> connected;

        public List<SrcItem> getUnconnected() {
            return unconnected;
        }

        public void setUnconnected(List<SrcItem> unconnected) {
            this.unconnected = unconnected;
        }

        public List<ConnectItem> getConnected() {
            return connected;
        }

        public void setConnected(List<ConnectItem> connected) {
            this.connected = connected;
        }
    }

    /**
     * 支持点击跳转的ITEM
     */
    public static class SrcItem {
        String text;
        String src;
        String link;
        String linkType;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }
    }

    /**
     * home单个item,包涵了各个平台
     */
    public class ConnectItem {
        SrcItem all;
        SrcItem ios;
        SrcItem android;
        SrcItem pc;
        SrcItem wx;

        public SrcItem getAll() {
            return all;
        }

        public void setAll(SrcItem all) {
            this.all = all;
        }

        public SrcItem getIos() {
            return ios;
        }

        public void setIos(SrcItem ios) {
            this.ios = ios;
        }

        public SrcItem getAndroid() {
            return android;
        }

        public void setAndroid(SrcItem android) {
            this.android = android;
        }

        public SrcItem getPc() {
            return pc;
        }

        public void setPc(SrcItem pc) {
            this.pc = pc;
        }

        public SrcItem getWx() {
            return wx;
        }

        public void setWx(SrcItem wx) {
            this.wx = wx;
        }
    }

    /**
     * 商城
     */
    public class Emall{
        List<EmallPage> pages;

        public List<EmallPage> getPages() {
            return pages;
        }

        public void setPages(List<EmallPage> pages) {
            this.pages = pages;
        }

        public class EmallPage {
            List<EmallPageItem> list;

            public List<EmallPageItem> getList() {
                return list;
            }

            public void setList(List<EmallPageItem> list) {
                this.list = list;
            }

            public class EmallPageItem{
                private String type;
                private int row;
                private int col;
                private int rowspan;
                private int colspan;
                private String merged;
                private String itemId;
                private String src;
                private String link;
                private String linkType;
                private String title1;
                private String title2;
                private boolean enableLinkAction;
                private String linkActionSrc;
                private String linkActionUrl;
                private String linkActionScanType;
                private String linkUrlTitle1;
                private String linkUrlTitle2;
                private String linkTitle1;
                private String linkTitle2;
                private boolean enableContact; //是否支持联系人
                private String contactText; //联系文字，默认"联系"
                private String contactAvatarSrc;//客服头像
                private String contactImageSrc;//联系详情图片
                private boolean enableCall; //是否支持通话
                private String callNumbers;//通话号码

                public boolean isEnableContact() {
                    return enableContact;
                }

                public void setEnableContact(boolean enableContact) {
                    this.enableContact = enableContact;
                }

                public String getContactText() {
                    return contactText;
                }

                public void setContactText(String contactText) {
                    this.contactText = contactText;
                }

                public String getContactAvatarSrc() {
                    return contactAvatarSrc;
                }

                public void setContactAvatarSrc(String contactAvatarSrc) {
                    this.contactAvatarSrc = contactAvatarSrc;
                }

                public String getContactImageSrc() {
                    return contactImageSrc;
                }

                public void setContactImageSrc(String contactImageSrc) {
                    this.contactImageSrc = contactImageSrc;
                }

                public boolean isEnableCall() {
                    return enableCall;
                }

                public void setEnableCall(boolean enableCall) {
                    this.enableCall = enableCall;
                }

                public String getCallNumbers() {
                    return callNumbers;
                }

                public void setCallNumbers(String callNumbers) {
                    this.callNumbers = callNumbers;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getRow() {
                    return row;
                }

                public void setRow(int row) {
                    this.row = row;
                }

                public int getCol() {
                    return col;
                }

                public void setCol(int col) {
                    this.col = col;
                }

                public int getRowspan() {
                    return rowspan;
                }

                public void setRowspan(int rowspan) {
                    this.rowspan = rowspan;
                }

                public int getColspan() {
                    return colspan;
                }

                public void setColspan(int colspan) {
                    this.colspan = colspan;
                }

                public String getMerged() {
                    return merged;
                }

                public void setMerged(String merged) {
                    this.merged = merged;
                }

                public String getItemId() {
                    return itemId;
                }

                public void setItemId(String itemId) {
                    this.itemId = itemId;
                }

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getLinkType() {
                    return linkType;
                }

                public void setLinkType(String linkType) {
                    this.linkType = linkType;
                }

                public String getTitle1() {
                    return title1;
                }

                public void setTitle1(String title1) {
                    this.title1 = title1;
                }

                public String getTitle2() {
                    return title2;
                }

                public void setTitle2(String title2) {
                    this.title2 = title2;
                }

                public boolean isEnableLinkAction() {
                    return enableLinkAction;
                }

                public void setEnableLinkAction(boolean enableLinkAction) {
                    this.enableLinkAction = enableLinkAction;
                }

                public String getLinkActionSrc() {
                    return linkActionSrc;
                }

                public void setLinkActionSrc(String linkActionSrc) {
                    this.linkActionSrc = linkActionSrc;
                }

                public String getLinkActionUrl() {
                    return linkActionUrl;
                }

                public void setLinkActionUrl(String linkActionUrl) {
                    this.linkActionUrl = linkActionUrl;
                }

                public String getLinkActionScanType() {
                    return linkActionScanType;
                }

                public void setLinkActionScanType(String linkActionScanType) {
                    this.linkActionScanType = linkActionScanType;
                }

                public String getLinkUrlTitle1() {
                    return linkUrlTitle1;
                }

                public void setLinkUrlTitle1(String linkUrlTitle1) {
                    this.linkUrlTitle1 = linkUrlTitle1;
                }

                public String getLinkUrlTitle2() {
                    return linkUrlTitle2;
                }

                public void setLinkUrlTitle2(String linkUrlTitle2) {
                    this.linkUrlTitle2 = linkUrlTitle2;
                }

                public String getLinkTitle1() {
                    return linkTitle1;
                }

                public void setLinkTitle1(String linkTitle1) {
                    this.linkTitle1 = linkTitle1;
                }

                public String getLinkTitle2() {
                    return linkTitle2;
                }

                public void setLinkTitle2(String linkTitle2) {
                    this.linkTitle2 = linkTitle2;
                }
            }
        }
    }
}
