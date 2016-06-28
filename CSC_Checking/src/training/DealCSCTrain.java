/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

/**
 *处理训练语料，生成正确的语料
 * @author Administrator
 */
public class DealCSCTrain {

    private SAXReader reader;
    private Document document;

    public DealCSCTrain(String str) throws Exception {
        reader = new SAXReader();
        document = reader.read(new File(str));
    }

    public static void main(String[] args) throws Exception {
        String path = "data/CSC_train.xml";
        DealCSCTrain test = new DealCSCTrain(path);
        test.getInfo();
    }

    public void getInfo() {
        BufferedWriter writer =null;
        File dest = new File("data/tuner.txt");  
        try {
            writer = new BufferedWriter(new FileWriter(dest));

        } catch (Exception e3) {
            e3.printStackTrace();
        }
        int i = 0;
        Element root = document.getRootElement();
        QName qname1 = new QName("ESSAY");
        QName qname2 = new QName("TEXT");
        QName qname3 = new QName("PASSAGE");
        QName qname4 = new QName("MISTAKE");
        QName qname5 = new QName("WRONG");
        QName qname6 = new QName("CORRECTION");
        List<Element> listESSAY = root.elements(qname1);
        System.out.println(listESSAY.size());
        for (Element e : listESSAY) {
            Element text = e.element(qname2);
            List<Element> listPASSAGE = text.elements(qname3);
            List<Element> listMISTAKE = e.elements(qname4);
            String oldid = null;
            String str = null;
            for (Element e1 : listMISTAKE) {
                String mid = e1.attributeValue("id");
                if (oldid == null) {
                        oldid = mid;
                } else if (mid.equals(oldid)) {

                } else if (str != null) {
                    oldid = mid;
                    try {

                        writer.write(str);
                        writer.write('\n');

                        writer.flush();
                    } catch (Exception ee) {

                    }
                }
                Element wrong = e1.element(qname5);
                Element correction = e1.element(qname6);

                for (Element e2 : listPASSAGE) {
                    if (mid.equals(e2.attributeValue("id"))) {
                        e2.setText(e2.getText().replace(wrong.getText(), correction.getText()));
                        str = e2.getText();                      
                    }
                }

            }

        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DealCSCTrain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
