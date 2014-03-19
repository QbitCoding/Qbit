package jhjx;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import formulaK.FormulaKI;

public class ReportManager {
	private pnbclient.alias.SQLCommandService srv = null;
	private static Log log = LogFactory.getLog("Qbit");
	public ReportManager() {
		try {
			srv = new pnbclient.alias.SQLCommandService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getReportSelect() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<option value=''>----请选择----</option>");
		List list = null;
		try {
			list = srv.getListMap("fd_report_select");
			if (null != list) {
				for (int index = 0; index < list.size(); index++) {
					HashMap map = (HashMap) list.get(index);
					buffer.append("<option value='" + map.get("tablename").toString() + "'>" + map.get("name").toString() + "</option>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String getReportColumnSelect(String name) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<option value=''>----请选择----</option>");
		List list = null;
		try {
			srv.addParameter("tablename", name);
			list = srv.getListMap("fd_report_column_select");
			if (null != list) {
				for (int index = 0; index < list.size(); index++) {
					HashMap map = (HashMap) list.get(index);
					buffer.append("<option value='" + map.get("column_name").toString() + "'>" + map.get("column_comment").toString() + "</option>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public List getReportColumnList(String name) {
		List list = null;
		try {
			srv.addParameter("tablename", name);
			list = srv.getListMap("fd_report_column_select");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取报表名称
	 * 
	 * @param tableid
	 * @return
	 */
	public String getReportName(String tableid) {
		if (StringHelper.isEmpty(tableid)) {
			return null;
		}
		String name = null;
		try {
			srv.addParameter(tableid);
			List<HashMap<String, String>> list = srv.getListMap("fd_report_name");
			if (list != null) {
				HashMap<String, String> map = (HashMap<String, String>) list.get(0);
				name = map.get("report_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 每月1号2:00定时生成本月数据
	 */
	public boolean createReportMonthData() {
		boolean b = false;
		StoredProcedureService sp = new StoredProcedureService();
		try {
			sp.getListMap("jh_report_data_init");
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public List getReportList() {
		List list = null;
		try {
			list = srv.getListMap("fd_report_select");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List getReport(String filterSQL, PageActionInInfo in, PageActionOutInfo out) {
		List list = null;
		try {
			srv.addParameter("filtersql", filterSQL);
			list = srv.getListMap("jh_report", in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 更新报表数据
	 * 
	 * @param map
	 *            接受页面传递过来的列名和列值
	 * @param formulas
	 *            公式
	 * @param parameters
	 *            参数值
	 * @return
	 */
	public boolean editReportData(HashMap<String, Object> map, List formulas, List parameters) {
		boolean b = false;
		log.debug("editReportData");
		try {
			HashMap<String, Object> reportMap = new HashMap<String, Object>();
			String[] columns = (String[]) ((String) map.get("columns")).split(",");
			String[] values = (String[]) ((String) map.get("values")).split(",");
			for (int i = 0; i < columns.length; i++) {
				reportMap.put(columns[i], values[i]);
			}
			// 报表id
			int tableid = (Integer) map.get("tableid");
			StoredProcedureService sp = new StoredProcedureService();
			// 部门id
			String departmentid = (String) map.get("departmentid");
			// 根据报表id、部门id、时间获取关联列名称和值
			List<HashMap<String, Object>> rlist = getRelationColumnInfo(tableid, departmentid, (String) map.get("date"),1);
			if (rlist != null) {
				for (int d = 0; d < rlist.size(); d++) {
					HashMap<String, Object> rmap = (HashMap<String, Object>) rlist.get(d);
					reportMap.put((String) rmap.get("column_name"), rmap.get("column_value"));
				}
			}
			HashMap<String, String> paramMap = new HashMap<String, String>();
			for (int i = 0; i < parameters.size(); i++) {
				HashMap temp = (HashMap) parameters.get(i);
				String name = temp.get("columnname").toString();
				String value = temp.get("parameter").toString();
				paramMap.put(name, value);
			}

			// 如果有公式，则进行动态计算
			System.out.println("counting!");
			if (formulas != null) {
//				for (int i = 0; i < formulas.size(); i++) {
//					log.debug("--------------------------------");
//					log.debug("--------------------------------");
//					log.debug("--------------------------------");
//					new CountAction().count((HashMap) formulas.get(i), reportMap, paramMap);
//				}
					qbitcount(formulas,reportMap,paramMap);	
			}

			Set<String> set = reportMap.keySet();
			for (String key : set) {
				if (!StringHelper.isEmpty(key)) {
					sp.addParameter(key);
					sp.addParameter(String.valueOf(reportMap.get(key)));
					sp.addParameter(departmentid);
					sp.addParameter((String) map.get("date"));
					sp.addParameter("1");
					sp.execute("fd_report_edit");

				}
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private void qbitcount(List formulas, HashMap<String, Object> reportMap,
			HashMap<String, String> paramMap) {
		Logger logger = Logger.getLogger("Qbit");
		logger.info("paramMap"+paramMap);
		logger.info("###############");
		for (int i = 0; i < formulas.size(); i++) {
			Map<String, String> mm = (HashMap) formulas.get(i);
			logger.debug("columnname"+mm.get("columnname"));
			logger.debug("formula"+mm.get("formula"));
			try{
				String value=FormulaKI.FormulaFactory.getInstance("SimpleFormulaK").
						getFormula(mm.get("formula"), paramMap).getValue();
				logger.info(value);
				reportMap.put(mm.get("columnname"), value);
				paramMap.put(mm.get("columnname"), value);
				logger.info(mm.get("columnname")+":"+ value);
			}catch(Exception e){
				logger.warn("ERROR", e);
			}
			logger.info("------------------------------------");
		}
	}

	/**
	 * 获取关联列的名称和值
	 * 
	 * @param tableid
	 *            报表id
	 * @param tid
	 * @param date
	 *            日期
	 * @param flag
	 *            标记字段，等于1时tid表示部门id，等于2时tid表示员工id
	 * @return
	 */
	public List<HashMap<String, Object>> getRelationColumnInfo(int tableid, String tid, String date,int flag) {
		List<HashMap<String, Object>> list = null;
		try {
			StoredProcedureService sp = new StoredProcedureService();
			sp.addParameter(tableid);
			sp.addParameter(tid);
			sp.addParameter(date);
			sp.addParameter(flag);
			list = sp.getListMap("jh_parameter_relation_column_info");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 月结
	 * @param map
	 * @return
	 */
	public boolean beginDeptReportMonthly(HashMap<String, String> map) {
		boolean b = false;
		try {
			StoredProcedureService sp = new StoredProcedureService();
			sp.addParameter((String) map.get("tableid"));
			sp.addParameter((String) map.get("deptid"));
			sp.addParameter((String) map.get("date"));
			sp.execute("jh_dept_report_monthly");
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 点击“同步数据”按钮时同步报表数据
	 * @param map
	 * @param formulas
	 * @param request
	 * @return
	 */
	public boolean updateReportData(HashMap<String, Object> map,List<HashMap<String,Object>> formulas,HttpServletRequest request) {
		boolean b = false;
		try {
			HashMap<String,Object> colMap = new HashMap<String,Object>();
			HashMap<String,Object> monthMap = new HashMap<String,Object>();
			String columns = "";
			String values = "";
			int tableid = (Integer) map.get("tableid");
			String date = (String) map.get("date");
			colMap.put("tableid", tableid);
			colMap.put("date", date);
			// 部门
			List<HashMap<String,Object>> departmentids = new OrganizeManager().getOrganizeIds();
			
			if (departmentids != null) {
				System.out.println("allNum:"+departmentids.size());
				// 记录进度条的总值
				request.getSession().setAttribute("allNum",departmentids.size());
				for (int j = 0; j < departmentids.size(); j++) {
					HashMap<String,Object> idmap = (HashMap<String,Object>) departmentids.get(j);
					String deptid = (String) idmap.get("id");
					colMap.put("deptid", deptid);
					// 列名和值
					List columnValue = getReportColumnValue(colMap);
					HashMap<String, String> columnsMap = new HashMap<String, String>();
					for (int i = 0; i < columnValue.size(); i++) {
						HashMap temp = (HashMap) columnValue.get(i);
						columns += temp.get("column_name").toString() + ",";
						values += temp.get("column_value").toString() + ",";
					}
					monthMap.put("columns",columns);
					monthMap.put("values",values);
					monthMap.put("tableid",tableid);
					monthMap.put("departmentid",deptid);
					monthMap.put("date",date);
					relationCol2(monthMap,formulas);
					System.out.println("currentNum:"+(j+1));
					// 记录进度条的当前值
					request.getSession().setAttribute("currentNum",j+1);
				}
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 获取报表列名和值
	 * @param map
	 * @return
	 */
	public List getReportColumnValue(HashMap<String,Object> map) {
		List list = null;
		try {
			srv.addParameter((Integer)map.get("tableid"));
			srv.addParameter((String)map.get("deptid"));
			srv.addParameter((String)map.get("date"));
			list = srv.getListMap("jh_report_column_and_value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean relationCol2(HashMap map, List formulas) {
		log.debug("relationCol2");
		boolean b = false;
		try {
			// 参数值
			List parameters = new ParameterManager().getParameterValue((Integer) map.get("tableid"), (String) map.get("departmentid"), "1");
			new ReportManager().editReportData(map, formulas, parameters);
			String[] columns = (String[]) ((String) map.get("columns")).split(",");
			if (columns[0] != null && columns[0] != "") {
				for (int i = 0; i < columns.length; i++) {
					map.put("colname", columns[i]);
					getRelationCol2(map);
				}
			}

			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 递归查询关联列
	 * 
	 * @param map
	 * @return
	 */
	public boolean getRelationCol2(HashMap<String, Object> map) {
		log.debug("getRelationCal2");
		boolean b = false;
		try {
			StoredProcedureService sp = new StoredProcedureService();
			sp.addParameter((String) map.get("colname"));
			sp.addParameter((Integer) map.get("tableid"));
			sp.addParameter((String) map.get("departmentid"));
			sp.addParameter((String) map.get("date"));
			sp.addParameter(1);   // 1表示部门报表

			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) sp.getListMap("jh_report_relation_column_and_value");
System.out.println("list:"+list);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					HashMap<String, Object> rmap = list.get(i);
					List<HashMap<String, Object>> formulasList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> fmap = new HashMap<String, Object>();
					fmap.put("formula", rmap.get("formula"));
					fmap.put("columnname", rmap.get("colname"));
					formulasList.add(fmap);
					// 参数值
					List parameters = new ParameterManager().getParameterValue((Integer) map.get("tableid"), (String) map.get("departmentid"), "1");
					new ReportManager().editReportData(rmap, formulasList, parameters);

					getRelationCol2(rmap);
				}
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public static void main(String[] args) {
		/*
		 * HashMap map = new HashMap(); map.put("columns", "column23");
		 * map.put("tableid", 64); map.put("departmentid", "3");
		 * List<HashMap<String,Object>> list = new
		 * ReportManager().getRelationCol(map); HashMap<String,Object> map2
		 * =(HashMap<String,Object>)list.get(0); String[] columns = (String[])
		 * ((String) map2.get("columns")).split(","); String[] values =
		 * (String[]) ((String) map2.get("values")).split(",");
		 * System.out.println(map2.toString()); System.out.println("tableid:"
		 * +map2.get("tableid") + "  ,columns:"+columns.length +
		 * "  ,values:"+values.length);
		 */
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("colname", "column23");
		map3.put("tableid", 1);
		map3.put("departmentid", "3");
		map3.put("date", "2014-01");
		new ReportManager().getRelationCol2(map3);
	}

	public List getReportTree() {
		List list = null;
		try {
			list = srv.getListMap("fd_report_tree");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 部门报表导出文件头部信息
	 * @param tableid 报表id
	 * @return
	 */
	public List getReportExportHead(String tableid) {
		if (StringHelper.isEmpty(tableid)) {
			return null;
		}
		List list = null;
		try {
			srv.addParameter("tableid", tableid);
			list = srv.getListMap("jh_department_report_export_head");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public Map getParameters(String deptid, String date) {
		List list=null;
		Map map=null;
		try{
			srv.addParameter(deptid);
			srv.addParameter(date);
			list=srv.getListMap("jh_parameter4calculate");
			log.debug("DEPT:"+deptid);
			log.debug("list:"+list);
			log.debug("--------------");
			map= TransTools.listToMap(list, "name", "value");
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	private static Log log=LogFactory.getLog("Qbit");
}
