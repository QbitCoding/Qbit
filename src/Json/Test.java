package Json;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		new Test().testJSONToObject();

	}
	 public void testJSONToObject() throws Exception{
	        String json = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";  
	        JSONObject jsonObject = JSONObject.fromObject( json ); 
//	        System.out.println(jsonObject);
	        Object bean = JSONObject.toBean( jsonObject ); 
//	        assertEquals( jsonObject.get( "name" ), PropertyUtils.getProperty( bean, "name" ) );  
//	        assertEquals( jsonObject.get( "bool" ), PropertyUtils.getProperty( bean, "bool" ) );  
//	        assertEquals( jsonObject.get( "int" ), PropertyUtils.getProperty( bean, "int" ) );  
//	        assertEquals( jsonObject.get( "double" ), PropertyUtils.getProperty( bean, "double" ) );  
//	        assertEquals( jsonObject.get( "func" ), PropertyUtils.getProperty( bean, "func" ) );  
	        System.out.println( jsonObject.get( "name" ));
//	        System.out.println(PropertyUtils.getProperty(bean, "bool"));
//	        System.out.println(PropertyUtils.getProperty(bean, "int"));
//	        System.out.println(PropertyUtils.getProperty(bean, "double"));
//	        System.out.println(PropertyUtils.getProperty(bean, "func"));
//	        System.out.println(PropertyUtils.getProperty(bean, "array"));
	         
	        List arrayList = (List)JSONArray.toCollection(jsonObject.getJSONArray("array"));
	        for(Object object : arrayList){
	            System.out.println(object);
	        }
	         
	    }

}
