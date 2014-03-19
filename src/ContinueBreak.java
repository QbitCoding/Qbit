import java.util.ArrayList;
import java.util.List;


public class ContinueBreak {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("true");
		list.add("false");
		list.add("null");
		list.add("default");
		for(int i=0;i<list.size();i++){
			System.out.println("---------");
			String s = list.get(i);
			switch(s){
			case "true":
				System.out.println("true");
			case "false":
				System.out.println("false");
			continue;
			case "null":
				System.out.println("null");
				break;
			default:System.out.println("default");
			}
			System.out.println("switchend");
		}
	}

}
