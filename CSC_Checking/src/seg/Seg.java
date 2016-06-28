package seg;

import edu.stanford.nlp.io.NullOutputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用评估逻辑
 */
public abstract class Seg {
    protected String testText = "data/tuner.txt";//待分词语料
    protected String standardText = "data/standard-text.txt";
    public abstract List<SaveResult> run() throws Exception;

    public void setTestText(String testText) {
        this.testText = testText;
    }

    public void setStandardText(String standardText) {
        this.standardText = standardText;
    }

    private static List<String> toText(List<SaveResult> list){
        List<String> result = new ArrayList<>();
        int i=1;
        for(SaveResult item : list){
            result.add("");
            result.add("    "+(i++)+"、"+item.toString());
        }
        for(String item : result){
            System.out.println(item);
        }
        return result;
    }

    protected float segFile(final String input, final String output, final Segmenter segmenter) throws Exception{
        //如果分词结果文件存放目录不存在，则创建
        if(!Files.exists(Paths.get(output).getParent())){
            Files.createDirectory(Paths.get(output).getParent());
        }
        float rate = 0;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input),"utf-8"));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(standardText==null?new NullOutputStream():new FileOutputStream(output),"utf-8"))){
            long size = Files.size(Paths.get(input));
            System.out.println("size:"+size);
            System.out.println("文件大小："+(float)size/1024/1024+" MB");
            int textLength=0;
            int progress=0;
            long start = System.currentTimeMillis();
            String line = null;
            while((line = reader.readLine()) != null){
                if("".equals(line.trim())){
                    writer.write("\n");
                    continue;
                }
                try{
                    writer.write(segmenter.seg(line));
                    writer.write("\n");
                }catch(Exception e){
                    System.out.println("分词失败："+line);
                    e.printStackTrace();
                }
            }
        }
        return rate;
    }

}
