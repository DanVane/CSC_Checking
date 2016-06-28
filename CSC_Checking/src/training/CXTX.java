/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package training;

/**
 * 词性二元同现概率表
 *
 * @author Administrator
 */
public class CXTX {

    int attrib;
    int attribnext;//用来表示相邻两个词性
    long probability;//用来表示该相邻两个词性在语料库中同时出现的概率

    public int getAttrib() {
        return attrib;
    }//读取前一个词性

    public int getNextattrib() {
        return attribnext;

    }//读取后一个词性

    public long getProbability() {
        return probability;//读取两词性同时出现的概率
    }
}
