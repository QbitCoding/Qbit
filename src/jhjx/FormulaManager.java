package jhjx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pnbclient.alias.StoredProcedureService;
import pnbclient.helper.StringHelper;

import com.yuanqi.iproject.util.Calculation;
import com.yuanqi.iproject.util.RelationBuilderKI;
import com.yuanqi.iproject.util.RunStringReg;
import com.yuanqi.iproject.util.TransTools;

public class FormulaManager {
	private pnbclient.alias.SQLCommandService srv = null;

	public FormulaManager() {
		try {
			srv = new pnbclient.alias.SQLCommandService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 添加参数信息
	 */
	public Boolean addFormula(String formula, String formulacn) {
		boolean b = false;
		try {
			StoredProcedureService sp = new StoredProcedureService();
			sp.addParameter(java.util.UUID.randomUUID().toString());
			sp.addParameter(formula);
			sp.addParameter(formulacn);
			sp.execute("jh_adverser_personal_formula_add");
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * 获取公式
	 * 
	 * @param tableid 报表id
	 */
	public List getFormula(int tableid) {
		List list = null;
		try {
			srv.addParameter(tableid);
			list = srv.getListMap("jh_formula_get");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 获取计算公式列
	 */
	public List getAdversePersonalFormulaColumn() {
		List list = null;
		try {
			list = srv.getListMap("fd_adverse_personal_formula_column");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 获取计算公式
	 */
	public List getAdversePersonalFormula() {
		List list = null;
		try {
			list = srv.getListMap("fd_adverse_personal_formula");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 获取计算公式参数
	 */
	public List<Map> getAdversePersonalFormulaMap(String deptid) {
		List<Map> list = null;
		try {
			srv.addParameter(deptid);
			list = srv.getListMap("fd_adverse_personal_formula_param");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 行计算
	 * 
	 * @param id 行id
	 * 
	 * @param deptid 部门id
	 * 
	 * @return boolean
	 */
	public boolean sumAdversePersonalFormula(String id, String deptid) {
		boolean b = false;
		try {
			List<Map> formulaList = getAdversePersonalFormula();
			String formula = formulaList.get(0).get("formula").toString();
			// srv.addParameter(id);
			// srv.addParameter(deptid);
			List<Map> list = getAdversePersonalFormulaMap(deptid);
			Map<String, String> listMap = new HashMap<String, String>();
			for (Map map : list) {
				String m = (String.valueOf(map.get("column_name")));
				String n = (String.valueOf(map.get("amount")));
				listMap.put(m, n);
			}
			String result = RunStringReg.cacComplex(Calculation.RunParament(
					listMap, formula));
			srv.addParameter(result);
			srv.addParameter(id);
			srv.execute("fd_adverse_personal_formula_sum");
			b = true;
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * 获取计算公式
	 */
	public List getBusinessDepositFormula() {
		List list = null;
		try {
			list = srv.getListMap("fd_business_deposit_formula_get");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 添加引入列
	 * 
	 * @param tableid 列id
	 */
	public boolean addFormulaRelationColumn(String columnids, String tableid) {
		boolean b = false;
		if (StringHelper.isEmpty(tableid)) {
			return b;
		}
		try {
			deleteFormulaRelationColumn(tableid);
			if (!columnids.equals("-1")) {
				String[] columns = columnids.split(",");
				StoredProcedureService sp = new StoredProcedureService();
				for (int i = 0; i < columns.length; i++) {
					sp.addParameter(tableid);
					sp.addParameter(columns[i]);
					sp.execute("jh_formula_relation_column_add");
				}
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/*
	 * 删除引入列
	 * 
	 * @param tableid 列id
	 */
	public boolean deleteFormulaRelationColumn(String tableid) {
		boolean b = false;
		try {
			srv.addParameter("tableid", tableid);
			srv.execute("jh_formula_relation_column_delete");
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// 修改公式排序
	public String modifyFormulaSort(HashMap map) {
		String result = null;
		try {
			StoredProcedureService sp = new StoredProcedureService();
			sp.addParameter(String.valueOf(map.get("tableid")));
			sp.addParameter(String.valueOf(map.get("paramid")));
			sp.addParameter(String.valueOf(map.get("paramsort")));
			sp.addParameter(String.valueOf(map.get("exeperson")));
			List list = sp.getListMap("jh_formula_modify_sort");
			if (list != null) {
				HashMap<String, String> resultmap = (HashMap<String, String>) list
						.get(0);
				result = resultmap.get("flag").toLowerCase();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List getAllFormula() {
		List list = null;
		try {
			list = srv.getListMap("jh_all_formula_get");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 修改公式排序
	public boolean updateFormula(String columnid, String columnname, int flag) {
		boolean b = false;
		try {
			StoredProcedureService sp = new StoredProcedureService();
			sp.addParameter(columnid);
			sp.addParameter(columnname);
			sp.addParameter(flag);
			List list = sp.getListMap("jh_formula_test");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// public static void main(String[] args) {
	// FormulaManager fm = new FormulaManager();
	// List list = fm.getAllFormula();
	// int count = 0;
	// if(list != null){
	// for(int i=0;i<list.size();i++){
	// // 参数
	// List<String> formulaParameters = new ArrayList<String>();
	// HashMap map =(HashMap) list.get(i);
	// String columnid = String.valueOf(map.get("columnid"));
	// String formla = String.valueOf(map.get("formula"));
	// Pattern p2 = Pattern.compile("Acolumn\\d*\\s");
	// Matcher m2 = p2.matcher(formla);
	// while (m2.find()) {
	// String cols = m2.group().trim();
	// if (formulaParameters.indexOf(cols) == -1) {
	// formulaParameters.add(cols);
	// formla = formla.replaceAll(cols, "");
	// }
	// }
	// System.out.println(formulaParameters);
	// if(formulaParameters.size() != 0){
	// for(int j=0;j<formulaParameters.size();j++){
	// String col = formulaParameters.get(j).replace("A", "");
	// fm.updateFormula(columnid,col,3);
	// }
	// }
	//
	// }
	// System.out.println("end");
	// }
	// }

	/**
	 * 通过列名获得对应公式
	 * 
	 * @param columnid
	 * @return
	 */
	public String getFormula(String columnid) {
		List<Map<String, String>> list = null;
		try {
			srv.addParameter(columnid);
			list = (List<Map<String, String>>) srv
					.getListMap("jh_columnformula_get");
			return list.get(0).get("formula_cn");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List getFormulaAndColumn(String tableid) {
		List list = null;
		try {
			srv.addParameter(tableid);
			list = srv.getListMap("jh_formulaANDcolumn");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public Map getAllFormulaAndCol(){
		List list = null;
		try {
			list = srv.getListMap("jh_allformula");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TransTools.listToMap(list, "name", "formula");
	}
	private static RelationBuilderKI<String,String> powerRelationBuilder;
	private static Pattern columnamepattern;
	private RelationBuilderKI<String,String> getPowerRelationBuilder() {
		if (powerRelationBuilder != null)
			return powerRelationBuilder;
		Map map = getAllFormulaAndCol();
		powerRelationBuilder 
		=new RelationBuilderKI.PowerRelationBuilder<String>();
//		= new RelationBuilderKI.CachedRelationBuilder<String>(new RelationBuilderKI.PowerRelationBuilder<String>());
		columnamepattern = Pattern.compile("[_a-zA-Z]+\\d+");
		for (Object name : map.keySet()) {
			Matcher ma = columnamepattern.matcher(map.get(name).toString());
			while (ma.find()) {
				String s=ma.group();
				if(s.startsWith("A")){
					powerRelationBuilder.add(s.substring(1, s.length()), s);
				}
				powerRelationBuilder.add(s, name.toString());
			}
		}
//		//以下为测试代码
//		Collection<String> list0=powerRelationBuilder.getAllChildren();
//		Collection<String> set0=new HashSet<String>(list0);
//		Collection<String> errlist=new ArrayList<String>();
//		for(String name0:list0){
////			System.out.println(name0);
//			Matcher ma = columnamepattern.matcher(map.get(name0).toString());
//			set0.remove(name0);
//			while (ma.find()) {
//				if(set0.contains(ma.group())) {
//					errlist.add(name0);
//					System.out.println(name0);
//				}
//			}
//		}
//		System.out.println(errlist.size());
//		//测试代码结束
		return powerRelationBuilder;
	}
	/**
	 * 得到所有排序过的需要计算的columnname
	 * @return
	 */
	public Collection<String> getColumnOrderby(){
		return getPowerRelationBuilder().getAllChildren();
	}
	/**
	 * 输入那些column被修改,得到那些column需要被计算,collection已经排序
	 * @param editcolumn
	 * @return
	 */
	public Collection<String> getColumnOrderby(Collection<String> editcolumn){
		return getPowerRelationBuilder().getAllChildren((String[]) editcolumn.toArray());
	}
	/**
	 * 
	 * @param columname
	 */
	public void delFormula(String columname){
		getPowerRelationBuilder().removeChild(columname);
	}
	public void newFormula(String columname,String formula){
		delFormula(columname);
		Matcher m = columnamepattern.matcher(formula);
		while(m.find()){
			powerRelationBuilder.add(m.group(), columname);
		}
	}
	private Log log =LogFactory.getLog("Qbit");
	public static void main(String[] args) {
		Collection<String> list=new FormulaManager().getPowerRelationBuilder().getAllChildren();
		System.out.println(list.size());
		System.out.println(list);
	}
}
