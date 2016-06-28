/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package training;

/**
 * 字词二元同现概率表
 *
 * @author Administrator
 */
public class ZCTX {

    String word;//相邻两字词中的前一个 
    String nextword;//用来表示相邻的第2个字词
    double probability;//用来表示该相邻两字词在语料库中同时出现的概率 

    public ZCTX(String word, String nextword, double probability) {
        this.word = word;
        this.nextword = nextword;
        this.probability = probability;
    }
    
    

    public String getWord() {
        return word;
    }//读取前一个字词

    public String getNextword() {
        return nextword;
    }//读取后一个字词

    public double getProbability() {
        return probability;//读取相邻字词出现概率
    }
}
