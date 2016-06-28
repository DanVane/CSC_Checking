/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package training;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class CreateTables {



    int num = 0;//总字词串数
    Map<String, Integer> map = new HashMap<String, Integer>();

    //读入分词文件
    public void readSegZCGL() {
        try {
            FileInputStream file = new FileInputStream("temp/result-text-NlpAnalysis.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                String[] strs = line.split(" ");//按空格划分词组
                for (String str : strs) {
                    if (str.length() <= 0) {
                        continue;
                    }
                    num++;
                    if (map.containsKey(str)) {//已经在map中
                        map.put(str, map.get(str) + 1);
                    } else {
                        map.put(str, 1);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File dest = new File("temp/zcgl.txt");
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = entry.getKey().toString();
                Integer value = Integer.parseInt(entry.getValue().toString());
                double d = (double) value / num;
                String str = key + '\t' + d+'\t'+value+'\t'+Math.log(d);
                writer.write(str);
                writer.write('\n');
                writer.flush();
            }

        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    Map<String, Integer> map2 = new HashMap<String, Integer>();

    //读入分词文件
    public void readSegZCTX() {
        String oldStr = null;
        try {
            FileInputStream file = new FileInputStream("temp/result-text-NlpAnalysis.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                String[] strs = line.split(" ");//按空格划分词组
                for (String str : strs) {
                    num++;
                    if (str.length() <= 0) {
                        continue;
                    }
                    if(oldStr ==null){
                        oldStr = str;
                        continue;
                    }
                    String temp=str;
                    str = oldStr+'\t'+str;
                    oldStr = temp;
                    if (map2.containsKey(str)) {//已经在map中
                        map2.put(str, map2.get(str) + 1);
                    } else {
                        map2.put(str, 1);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File dest = new File("temp/zctx.txt");
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));
            Iterator it = map2.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = entry.getKey().toString();
                Integer value = Integer.parseInt(entry.getValue().toString());
                 double d = (double) value / num;
                String str = key + '\t' + d+'\t'+value+'\t'+Math.log(d);
                writer.write(str);
                writer.write('\n');
                writer.flush();
            }

        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CreateTables c = new CreateTables();
        c.readSegZCGL();
        c.readSegZCTX();
        
    }
}
