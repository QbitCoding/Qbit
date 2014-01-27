package folderUtils;

import java.io.File;
import java.util.Map;

public class Cleaner {
	private Cleaner(Map<String,Object> args){
		super();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static Cleaner getinstance(Map<String,Object> args){
		return new Cleaner(args);
	}
	private boolean compareFolder(File root){
		
	}
}
