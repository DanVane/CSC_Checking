/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import training.ZCTX;

/**
 * 差错和纠错
 *
 * @author Administrator
 */
public class Main {

    Map<String, Double> zc;
    Map<String, ZCTX> zcdouble;
    double r0 = -12;
    Map<String, String> sp;
    Map<String, String> ss;

    public void initList() {
        zc = new HashMap<String, Double>();
        zcdouble = new HashMap<String, ZCTX>();
        sp = new HashMap<String, String>();
        ss = new HashMap<String, String>();
    }

    public void readZCTable() {
        String line = null;
        try {
            FileInputStream file = new FileInputStream("temp/zcgl.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            line = reader.readLine();
            while (line != null) {
                String[] strs = line.split("\t");//按空格划分词组
                zc.put(strs[0], Double.parseDouble(strs[1]));
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream file = new FileInputStream("temp/zctx.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            line = reader.readLine();
            while (line != null) {
                String[] strs = line.split("\t");//按空格划分词组
                ZCTX zctx = new ZCTX(strs[0], strs[1], Double.parseDouble(strs[2]));
                zcdouble.put(strs[0] + strs[1], zctx);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void segSentence() {
        boolean b1;
        boolean b2;
        double k;
        //读入测试文件，逐行分词，计算概率，纠错
        try {
            FileInputStream file = new FileInputStream("temp/CSC_testfenci2.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            String pid = null;
            String segStr;
            while (line != null && line.length() > 0) {
//                System.out.println(line);
                if (pid == null) {
                    pid = line.substring(1, 27);
                    line = line.substring(27);
                } else {
                    pid = line.substring(0, 26);
                    line = line.substring(26);
                }
                pid = pid.replace(" ", "");
                pid=pid.trim();
                String[] strs = line.split(" ");//按空格划分词组
                for (int i = 1; i < strs.length - 1; i++) {//计算信息判断是否出错
                    b1 = false;
                    b2 = false;
                    String te1 = strs[i - 1];
                    String te2 = strs[i];
                    if (zcdouble.containsKey(te1 + te2)) {//有i-1和i，有i和i+1
                        k = zcdouble.get(te1 + te2).getProbability();
                        if (k < 1.028E-5) {
                            String te3 = strs[i + 1];
                            if (zcdouble.containsKey(te2 + te3)) {
                                k = zcdouble.get(te2 + te3).getProbability();
                                if (k < 1.028E-5) {
                                    String s = findCorrect(line, te2);
                                    if (!s.equals(te2)) {
//                                        System.out.println(te2 + "的替换方案是" + s);
                                        line = saveResult(pid, te2, strs, i, s);
                                    }
                                }
                            } else {//有i-1和i，没有i和i+1
                                k = zcdouble.get(te1 + te2).getProbability();
                                if (k < 1.028E-5) {
                                    String s = findCorrect(line, te2);
                                    if (!s.equals(te2)) {
//                                        System.out.println(te2 + "的替换方案是" + s);
                                        line = saveResult(pid, te2, strs, i, s);
                                    }
                                }
                            }
                        }
                    } else {//没有i-1和i
                        String te3 = strs[i + 1];
                        if (zcdouble.containsKey(te2 + te3)) {//没有i-1和i,有i和i+1
                            k = zcdouble.get(te2 + te3).getProbability();
                            if (k < 1.028E-5) {
                                String s = findCorrect(line, te2);
                                if (!s.equals(te2)) {
//                                    System.out.println(te2 + "的替换方案是" + s);
                                    line = saveResult(pid, te2, strs, i, s);
                                }
                            }
                        } else//没有i-1和i，没有i和i+1，不处理
                        if (zc.containsKey(te2)) {
                            if (Math.log(zc.get(te2)) > -8) {//单独出现频率高，不处理

                            } else {
                                String s = findCorrect(line, te2);
                                if (!s.equals(te2)) {
//                                    System.out.println(te2 + "的替换方案是" + s);
                                    line = saveResult(pid, te2, strs, i, s);
                                }
                            }
                        } else {//未出现，判断出错
                            String s = findCorrect(line, te2);
                            if (!s.equals(te2)) {
//                                System.out.println(te2 + "的替换方案是" + s);
                                saveResult(pid, te2, strs, i, s);
                            }

                        }
                    }

                }
                saveResult(pid, null, null, 0, null);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //计算
    public double calculateI(String str1, String str2) {
        double i = 0.1;
        if (zc.containsKey(str1)) {
            i += zc.get(str1);
        }
        if (zc.containsKey(str2)) {
            i += zc.get(str2);
        }
        if (zc.containsKey(str1 + str2)) {
            i += zc.get(str1 + str2) * 10;
        }
        if (zcdouble.containsKey(str1 + str2)) {
            i += zcdouble.get(str1 + str2).getProbability() * 10;
        }
        return i;
    }

    public String findCorrect(String str, String term) {
        //从sp和ss中寻找替换方案
        double k = 0;
        double tempK = calculateK(str);
        String s;
        String finalS = term;
        String tempStr = str;
        String s1 = sp.get(term);

        if (s1 != null) {
            s1 = s1.replace("\t", "");
            for (int i = 0; i < s1.length(); i++) {
                s = s1.substring(i, i + 1);
                tempStr.replace(term, s);
                tempK = calculateK(tempStr);
                if (tempK > k) {
                    finalS = s;
                }
            }
        }

        String s2 = ss.get(term);
        if (s2 != null) {
            s2 = s2.replace("\t", "");
            for (int i = 0; i < s2.length(); i++) {
                s = s2.substring(i, i + 1);
                tempStr.replace(term, s);
                tempK = calculateK(tempStr);
                if (tempK > k) {
                    finalS = s;
                    k = tempK;
                }
            }
        }
        return finalS;
    }

    public double calculateK(String str) {
        //信息之和
        double k = 0.0;
        List<Term> terms = NlpAnalysis.parse(str);
        for (int i = 0; i < terms.size() - 1; i++) {
            k += calculateI(terms.get(i).getName(), terms.get(i + 1).getName());
        }
        return k;
    }

    public void readSimilarPron() {
        //读入同音文件
        try {
            FileInputStream file = new FileInputStream("data/CharacterSet_SimilarPronunciation.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            line = reader.readLine();
            do {
                sp.put(line.substring(0, 1), line.substring(1));
                line = reader.readLine();
            } while (line != null && line.length() > 0);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readSimilarSharp() {
        //读入同形文件
        try {
            FileInputStream file = new FileInputStream("data/CharacterSet_SimilarShape.txt");
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            do {
                ss.put(line.substring(0, 1), line.substring(2));
                line = reader.readLine();
            } while (line != null && line.length() > 0);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.initList();
        m.initResult();
        m.readZCTable();

        m.readSimilarPron();
        m.readSimilarSharp();
        m.segSentence();
    }

    String oldPid = null;

    private String saveResult(String pid, String te2, String[] strs, int i, String s1) {
        String line = "";
        String s = pid.replace(" ", "");
        s=s.trim();
        s = s.substring(5,s.length()-3);
        s=s.trim();
         if(s.contains(")")){
             s = s.substring(0,s.length()-1);
        }
        if (oldPid == null) {
            oldPid = s;
        } else if (oldPid.equals(s)) {//在当前行后加
            int k = 0;
            for (int j = 0; j < i; j++) {
                line +=strs[j];
                k += strs[j].length();
            }
            try {
                if(k==0&&s1==null){
                }else
                writer.write(", "+(k+1)+", "+s1);
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            line +=s1;
            if(strs!=null){
                 for(int j=i+1;j<strs.length;j++){
                line += strs[j];
            }
            }
           
            return line;
        }
        
       int k = 0;
            for (int j = 0; j < i; j++) {
                line +=strs[j];
                k += strs[j].length();
            }
            try {
                writer.write('\n');
                if(k==0&&s1==null){
                     writer.write(s+", 0");
                }else
                writer.write(s+", "+(k+1)+", "+s1);
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            line +=s1;
            if(strs!=null){
                for(int j=i+1;j<strs.length;j++){
                line += strs[j];
            }
            }
            
            oldPid = s;
            return line;
    }

    Writer writer = null;

    private void initResult() {
        File dest = new File("temp/result.txt");
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));

        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

}
