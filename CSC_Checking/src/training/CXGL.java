/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package training;

/**
 * 词性概率表
 *
 * @author Administrator
 */
public class CXGL {

    int attrib;//用来表示词性
    long probability;//用来表示该词性在语料库中独立出现的概率

    public int getAttrib() {
        return attrib;

    }//读取表中词性

    public long probability() {
        return probability;
    }//读取该词性单独出现的概率1
}
