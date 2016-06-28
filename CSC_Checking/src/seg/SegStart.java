package seg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 对正确的语料进行分词，输出结果
 */
public class SegStart {
    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        String testText = null;
        String standardText = null;
        Set<String> excludes = new HashSet<>();
        for(String arg : args){
            System.out.println("命令行参数："+arg);
            if(arg.endsWith(".jar")){
                continue;
            }
            if(arg.startsWith("-testText=")){
                testText=arg.replace("-testText=", "").trim();
                System.out.println("testText："+testText);
                continue;
            }
            if(arg.startsWith("-standardText=")){
                standardText=arg.replace("-standardText=", "").trim();
                System.out.println("standardText："+standardText);
                continue;
            }
            excludes.add(arg);
        }
        List<Class> classes = new ArrayList<>();
        if(args.length>0 && new File(args[0]).exists()){
            classes.addAll(processJar(new File(args[0]), excludes));
        }else{
            classes.addAll(processDir(excludes));
        }
        Collections.reverse(classes);
        int i=1;
        for(Class clazz : classes){
            System.out.println((i++)+"："+clazz.getSimpleName());
        }
        List<SaveResult> list = new ArrayList<>();
        for(Class clazz : classes){
            Seg eval = (Seg)clazz.newInstance();
            if(testText!=null) {
                eval.setTestText(testText);
                eval.setStandardText(standardText);
            }
            System.out.println("先预热："+((WordSegmenter)eval).seg("今天下雨，中华人民共和国，结合成分子"));
            list.addAll(eval.run());
        }
    }

    private static List<Class> processJar(File jarFile, Set<String> excludes) throws IOException, ClassNotFoundException {
        List<Class> list = new ArrayList<>();
        JarFile jarfile = new JarFile(jarFile);        
        Enumeration files = jarfile.entries();
        o:while(files.hasMoreElements()){
            JarEntry entry = (JarEntry)files.nextElement();
            for(String exclude : excludes){
                if(entry.getName().contains(exclude)){
                    continue o;
                }
            }
            if(entry.getName().startsWith("seg")
                    && entry.getName().endsWith(".class")){
                String cls = entry.getName().replaceAll("/", ".");
                cls = cls.replaceAll(".class","");
                Class clazz = Class.forName(cls);
                if(Seg.class.isAssignableFrom(clazz)){
                    list.add(clazz);  
                }
            }
        }
        return list;
    }

    private static List<Class> processDir(Set<String> excludes) throws ClassNotFoundException {
        List<Class> list = new ArrayList<>();
        URL url = SegStart.class.getClassLoader().getResource("seg/hello.java");
        File dir = new File(url.getFile().replace("hello.java", ""),"impl");
        o:for(File file : dir.listFiles()){
            String cls = file.getPath();
            if(cls.endsWith(".java")){
                continue ;
            }
            for(String exclude : excludes){
                if(cls.contains(exclude)){
                    continue o;
                }
            }
            int index = cls.indexOf("seg\\");
            if(index == -1){
                index = cls.indexOf("seg/");
            }
            cls = cls.substring(index);
            cls = cls.replaceAll("\\\\", "\\.");
            cls = cls.replaceAll("/", "\\.");
            cls = cls.replaceAll(".class","");
            Class clazz = Class.forName(cls);
            if(Seg.class.isAssignableFrom(clazz)){
                list.add(clazz);
            }
        }
        return list;
    }
}