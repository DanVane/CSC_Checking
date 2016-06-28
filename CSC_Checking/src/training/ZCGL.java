/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package training;

/**
 * 字词概率表
 *
 * @author Administrator
 */
public class ZCGL {

    String word;//用来表示字词
    double probability;//用来表示该字词在语料库中独立出现的概率

    public String getWord() {
        return word;
    }//读取表中字词

    public double getProbability() {
        return probability;

    }//读取表中该字词独立出现的概率
}
