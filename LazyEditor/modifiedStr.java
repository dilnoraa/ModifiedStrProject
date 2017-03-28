package LazyEditor;



public class modifiedStr {
	
	private int flag; // flag degisken 0 ise bu stringte uygun ![...](...) veya [....](...)  bulunmamaktadir
	private String sentence; // yaniltici parantezlerden arindirilimis yeni string 
	
	
    public modifiedStr(){
    	flag=0;
    	sentence="";
    }
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	

}
