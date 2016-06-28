package seg;

public class SaveResult implements Comparable{

    private String analyzer;
    private float segSpeed;
    private int totalLineCount;
    private int perfectLineCount;
    private int wrongLineCount;
    private int totalCharCount;
    private int perfectCharCount;
    private int wrongCharCount;

    public String getAnalyzer() {
        return analyzer;
    }
    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }
    public float getSegSpeed() {
        return segSpeed;
    }
    public void setSegSpeed(float segSpeed) {
        this.segSpeed = segSpeed;
    }
    public float getLinePerfectRate(){
        return (int)(perfectLineCount/(float)totalLineCount*10000)/(float)100;
    }
    public float getLineWrongRate(){
        return (int)(wrongLineCount/(float)totalLineCount*10000)/(float)100;
    }
    public float getCharPerfectRate(){
        return (int)(perfectCharCount/(float)totalCharCount*10000)/(float)100;
    }
    public float getCharWrongRate(){
        return (int)(wrongCharCount/(float)totalCharCount*10000)/(float)100;
    }
    public int getTotalLineCount() {
        return totalLineCount;
    }
    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }
    public int getPerfectLineCount() {
        return perfectLineCount;
    }
    public void setPerfectLineCount(int perfectLineCount) {
        this.perfectLineCount = perfectLineCount;
    }
    public int getWrongLineCount() {
        return wrongLineCount;
    }
    public void setWrongLineCount(int wrongLineCount) {
        this.wrongLineCount = wrongLineCount;
    }
    public int getTotalCharCount() {
        return totalCharCount;
    }
    public void setTotalCharCount(int totalCharCount) {
        this.totalCharCount = totalCharCount;
    }
    public int getPerfectCharCount() {
        return perfectCharCount;
    }
    public void setPerfectCharCount(int perfectCharCount) {
        this.perfectCharCount = perfectCharCount;
    }
    public int getWrongCharCount() {
        return wrongCharCount;
    }
    public void setWrongCharCount(int wrongCharCount) {
        this.wrongCharCount = wrongCharCount;
    }
    @Override
    public String toString(){
        if(perfectCharCount==0){
            //只评估速度
            return analyzer+"："
                    +"\n"
                    +"    分词速度："+segSpeed+" 字符/毫秒";
        }
        return analyzer+"："
                +"\n"
                +"    分词速度："+segSpeed+" 字符/毫秒"
                +"\n"
                +"    行数完美率："+getLinePerfectRate()+"%"
                +"  行数错误率："+getLineWrongRate()+"%"
                +"  总的行数："+totalLineCount
                +"  完美行数："+perfectLineCount
                +"  错误行数："+wrongLineCount
                +"\n"
                +"    字数完美率："+getCharPerfectRate()+"%"
                +" 字数错误率："+getCharWrongRate()+"%"
                +" 总的字数："+totalCharCount
                +" 完美字数："+perfectCharCount
                +" 错误字数："+wrongCharCount;
    }
    @Override
    public int compareTo(Object o) {
        SaveResult other = (SaveResult)o;
        if(other.getLinePerfectRate() - getLinePerfectRate() > 0){
            return 1;
        }
        if(other.getLinePerfectRate() - getLinePerfectRate() < 0){
            return -1;
        }
        return 0;
    }
    public static void main(String[] args){
        SaveResult r = new SaveResult();
        r.setAnalyzer("test");
        r.setSegSpeed(100);
        r.setTotalCharCount(28374428);
        r.setTotalLineCount(2533688);
        r.setPerfectCharCount(7152898);
        r.setWrongCharCount(21221530);
        r.setPerfectLineCount(868440);
        r.setWrongLineCount(1665248);
        System.out.println(r);
    }
}
