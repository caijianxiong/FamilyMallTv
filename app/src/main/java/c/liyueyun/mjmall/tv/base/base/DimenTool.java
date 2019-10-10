package c.liyueyun.mjmall.tv.base.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * Created by SongJie on 07/22 0022.
 */
public class DimenTool {

    public static void gen() {

        File file = new File("./app/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        DecimalFormat df=new DecimalFormat("###.00");
        StringBuilder sw360 = new StringBuilder();
        StringBuilder sw540 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    double num = Double.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

                    sw360.append(start).append(df.format(num * 0.667)).append(end).append("\n");
                    sw540.append(start).append(df.format(num * 1)).append(end).append("\n");
                    sw720.append(start).append(df.format(num * 1.333)).append(end).append("\n");
                } else {
                    sw360.append(tempString).append("\n");
                    sw540.append(tempString).append("\n");
                    sw720.append(tempString).append("\n");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  sw360 -->");
            System.out.println(sw360);
            System.out.println("<!--  sw540 -->");
            System.out.println(sw540);
            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);

            String sw360file = "./app/src/main/res/values-sw360dp/dimens.xml";
            String sw540file = "./app/src/main/res/values-sw540dp/dimens.xml";
            String sw720file = "./app/src/main/res/values-sw720dp/dimens.xml";
            writeFile(sw360file, sw360.toString());
            writeFile(sw540file, sw540.toString());
            writeFile(sw720file, sw720.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close();
    }

    public static void main(String[] args) {
        gen();
    }
}