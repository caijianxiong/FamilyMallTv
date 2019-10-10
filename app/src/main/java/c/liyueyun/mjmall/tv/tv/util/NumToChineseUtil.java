package c.liyueyun.mjmall.tv.tv.util;

public class NumToChineseUtil {

    private static String num="0123456789";
    private static String[] nums={"零","一","二","三","四","五","六","七","八","九"};
    public static String transform(String msg){//语音识别的纯数字
        String content="";
        if (msg==null||msg.equals("")){
            return content;
        }
        char[] chars=msg.toCharArray();
        for (int i=0;i<chars.length;i++){
            if (num.contains(chars[i]+"")){
                if (chars[i]=='0'){
                    content=content+nums[0];
                }else if (chars[i]=='1'){
                    content=content+nums[1];
                }else if (chars[i]=='2'){
                    content=content+nums[2];
                }else if (chars[i]=='3'){
                    content=content+nums[3];
                }else if (chars[i]=='4'){
                    content=content+nums[4];
                }else if (chars[i]=='5'){
                    content=content+nums[5];
                }else if (chars[i]=='6'){
                    content=content+nums[6];
                }else if (chars[i]=='7'){
                    content=content+nums[7];
                }else if (chars[i]=='8'){
                    content=content+nums[8];
                }else if (chars[i]=='9'){
                    content=content+nums[9];
                }
            }else {
                content=content+chars[i];
            }
        }

        return content;
    }

}
