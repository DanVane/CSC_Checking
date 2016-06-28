package seg.impl;

import java.util.*;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import seg.SaveResult;
import seg.Seg;
import seg.Segmenter;
import seg.WordSegmenter;

/**
 * Ansj分词器分词
 */
public class AnsjSeg extends Seg implements WordSegmenter{
    @Override
    public List<SaveResult> run() throws Exception {
        List<SaveResult> list = new ArrayList<>();
        list.add(run("NlpAnalysis"));
        
        return list;
    }
    private SaveResult run(final String analysis) throws Exception{
        // 对文本进行分词
        String analyzer = "Ansj "+analysis;
        switch(analysis){
            case "BaseAnalysis":
                    analyzer += " 基本分词";
                    break;
            case "ToAnalysis":
                    analyzer += " 精准分词";
                    break;
            case "NlpAnalysis":
                    try{
                        analyzer += " NLP分词";
                    }catch(Exception e){}
                    break;
            case "IndexAnalysis":
                    analyzer += " 面向索引的分词";
                    break;
        }
        String resultText = "temp/result-text-"+analysis+".txt";
        float rate = segFile(testText, resultText, new Segmenter(){
            @Override
            public String seg(String text) {
                StringBuilder result = new StringBuilder();
                try{
                    List<Term> terms = null;
                    switch(analysis){
                        case "BaseAnalysis":
                                terms = BaseAnalysis.parse(text);
                                break;
                        case "ToAnalysis":
                                terms = ToAnalysis.parse(text);
//                                 new NatureRecognition(terms).recognition(); 
                                break;
                        case "NlpAnalysis":
                                terms = NlpAnalysis.parse(text);
//                                   new NatureRecognition(terms).recognition(); 
                                break;
                        case "IndexAnalysis":
                                terms = IndexAnalysis.parse(text);
                                break;
                    }
                    for(Term term : terms){
                        result.append(term.getName()).append(" ");                    
                    }                    
                }catch(Exception e){
                    e.printStackTrace();
                }
                return result.toString();
            }
        });
        return null;
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();

        StringBuilder result = new StringBuilder();
        for(Term term : BaseAnalysis.parse(text)){
            result.append(term.getName()).append(" ");
        }
        map.put("BaseAnalysis", result.toString());

        result.setLength(0);
        for(Term term : ToAnalysis.parse(text)){
            result.append(term.getName()).append(" ");
        }
        map.put("ToAnalysis", result.toString());

        result.setLength(0);
        for(Term term : NlpAnalysis.parse(text)){
            result.append(term.getName()).append(" ");
        }
        map.put("NlpAnalysis", result.toString());

        result.setLength(0);
        for(Term term : IndexAnalysis.parse(text)){
            result.append(term.getName()).append(" ");
        }
        map.put("IndexAnalysis", result.toString());

        return map;
    }

    public static void main(String[] args) throws Exception{
        new AnsjSeg().run();
    }
}