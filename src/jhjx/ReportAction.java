package jhjx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import pnbclient.helper.StringHelper;

import com.powernt.info.UserInfo;
import com.powernt.logic.UserManager;
import com.powernt.tools.ValidateUser;
import com.yaunqi.iproject.count.DynamicEngine;
import com.yuanqi.iproject.logic.FormulaManager;
import com.yuanqi.iproject.logic.OrganizeManager;
import com.yuanqi.iproject.logic.ReportManager;
import com.yuanqi.iproject.util.WebTools;

public class ReportAction extends DispatchAction {
	public ActionForward resulteditui(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String orgnamesearch = request.getParameter("orgnamesearch");
		String date = request.getParameter("date");
		String tableid = request.getParameter("tableid");
		request.setAttribute("tableid", tableid);
		request.setAttribute("date", date);
		request.setAttribute("type", request.getParameter("type"));
		return mapping.findForward("ResultEdit");
	}

	public ActionForward resultedit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		// if (!ValidateUser.validateOperation(request, response, userInfo,
		// "10164")) {
		// return null;
		// }
		// // 写日志
		// UserManager dm = new UserManager();
		// dm.addUserLog(userInfo.getUserid(), 1, "10164", "open",
		// "打开报表导入导出页面");
		String value = request.getParameter("value");
		String columnid = request.getParameter("columnid").toString();
		String orgid = request.getParameter("orgid").toString();
		String date = request.getParameter("date");
		String formula = new FormulaManager().getFormula(columnid);
		if (formula == null)
			formula = "无公式";
		Double val = new ReportManager()
				.getResultCorrect(orgid, columnid, date);
		Map responseKM = new HashMap();
		responseKM.put("formula", formula);
		responseKM.put("value", val.toString());
		WebTools.writeJson(response, responseKM);
		return null;
	}

	/**
	 * 打开报表导入页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportImport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		if (!ValidateUser.validateOperation(request, response, userInfo,
				"10164")) {
			return null;
		}
		// 写日志
		UserManager dm = new UserManager();
		dm.addUserLog(userInfo.getUserid(), 1, "10164", "open", "打开报表导入导出页面");
		request.setAttribute("tableid", request.getParameter("tableid"));
		request.setAttribute("code", "10164");
		return mapping.findForward("ReportImport");
	}

	/**
	 * 报表树
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getReportTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		List list = null;
		try {
			StringBuffer filtersql = new StringBuffer();
			filtersql.append(" 1=1 ");
			// 添加查看权限
			filtersql
					.append(" and  m.menuid IN(SELECT ri.resourceid FROM fd_rolerights ri LEFT JOIN fd_userroles ro ON ro.roleid=ri.roleid WHERE ri.operationcode='Open' AND ro.userid='"
							+ userInfo.getUserid() + "')");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("filtersql", filtersql.toString());
			list = new ReportManager().getReportTree(map);
			if (list != null) {
				WebTools.writeJson(response, list);
			} else {
				WebTools.writeText(response, "1");
			}
		} catch (Exception e) {
			WebTools.writeText(response, "0");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 打开报表页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code)) {
			return null;
		}
		// 写日志
		UserManager dm = new UserManager();
		String tableid = request.getParameter("tableid");
		// 默认不显示部门类型
		int isshowtype = 2;
		// 默认不显示部门主管
		int isshowleader = 2;
		String reportName = new ReportManager().getReportName(tableid);
		List<HashMap<String, Object>> list = new ReportManager()
				.getReportDetailInfo(tableid);
		if (list != null) {
			HashMap<String, Object> map = (HashMap<String, Object>) list.get(0);
			isshowtype = Integer
					.parseInt(String.valueOf(map.get("isshowtype")));
			isshowleader = Integer.parseInt(String.valueOf(map
					.get("isshowleader")));
		}
		dm.addUserLog(userInfo.getUserid(), 1, code, "open", "打开报表页面");
		request.setAttribute("tableid", tableid);
		request.setAttribute("code", code);
		request.setAttribute("tablename", reportName);
		request.setAttribute("isshowtype", isshowtype);
		request.setAttribute("isshowleader", isshowleader);
		return mapping.findForward("list");
	}

	/**
	 * 报表下拉框
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getReportSelect(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		List list = new ReportManager().getReportList();
		String data = "";
		if (list != null) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("{\"list\":[");
			int s = list.size();
			for (int i = 0; i < s; i++) {
				Map map = (Map) list.get(i);
				buffer.append("{\"key\":\"" + map.get("name")
						+ "\",\"value\":\"" + map.get("tablename") + "\"}");
				if (i != s - 1) {
					buffer.append(",");
				}
			}
			buffer.append("]}");
			data = buffer.toString();
		} else {
			data = "{\"list\":[]}";
		}
		WebTools.writeText(response, data);
		return null;
	}

	/**
	 * 修改报表数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editReportData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code)) {
			return null;
		}
		String columns = request.getParameter("columns");
		String values = request.getParameter("values");
		int tableid = Integer.parseInt(request.getParameter("tableid"));
		String departmentid = request.getParameter("departmentid");
		String date = request.getParameter("date");
		if (StringHelper.isEmpty(columns)) {
			return null;
		} else if (StringHelper.isEmpty(values)) {
			return null;
		} else if (StringHelper.isEmpty(departmentid)) {
			return null;
		} else if (StringHelper.isEmpty(date)) {
			WebTools.writeText(response, 3);
			return null;
		}
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("columns", columns);
			map.put("values", values);
			map.put("tableid", tableid);
			map.put("departmentid", departmentid);
			map.put("date", date);
			map.put("flag", 1);  // 1:部门报表  2:员工报表
			map.put("op", 2);
			// 公式
			List formulas = new FormulaManager().getFormula(new Integer(map
					.get("tableid").toString()));
			boolean b = new ReportManager().relationCol2(map, formulas);
			// boolean b = new ReportManager().getRelationCol2(map);
			if (b) {
				WebTools.writeText(response, 1);
			} else {
				WebTools.writeText(response, 2);
			}
		} catch (Exception e) {
			WebTools.writeText(response, 2);
			e.printStackTrace();
		}
		return null;
	}

	Collection<String> orderlist;

	public ActionForward updateReportData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		orderlist = new FormulaManager().getColumnOrderby();
		log.debug("orderlist:" + orderlist);
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String userid = userInfo.getUserid();
		String date = request.getParameter("date");
		HashMap<String, Object> filterMap = new HashMap<String, Object>();
		StringBuffer filtersql = new StringBuffer();
		// 添加权限
		filtersql
				.append(" o.orgID in(SELECT power_orgid FROM jh_power_view WHERE empid='"
						+ userid + "')");
		filterMap.put("filtersql", filtersql.toString());
		// 部门
		List<HashMap<String, Object>> departmentids = new OrganizeManager()
				.getOrganizeIds(filterMap);
		ReportManager rm = new ReportManager();
		Map formulaKM = new FormulaManager().getAllFormulaAndCol();
		all = departmentids.size() * orderlist.size();
		counter = 0;
		log.debug("ALL:" + all);
		Map<String, Map> paremeterKM = new HashMap<String, Map>();
		for (int j = 0; j < departmentids.size(); j++) {
			HashMap<String, Object> idmap = (HashMap<String, Object>) departmentids
					.get(j);
			String deptid = (String) idmap.get("id");
			paremeterKM.put(deptid, rm.getParameters(deptid, date));
		}
		for (int i = 0; i < orderlist.size(); i++) {
			String name = ((List<String>) orderlist).get(i);
			if (name.startsWith("A"))
				continue;
			for (String id : paremeterKM.keySet()) {
					count(name, paremeterKM.get(id), formulaKM.get(name)
							.toString(),id);
				System.out.println(counter++ + "/" + all);
			}
		}
		return null;
	}

	private int all;
	private int counter;
	private static Log log = LogFactory.getLog("Qbit");
	Map<String, Object> cacheKM = new HashMap<String, Object>();
	Pattern p;
	Map<String, Double> sumKM = new HashMap<String, Double>();

	private double count(String name, Map paremeterKM, String formula,String id)
			throws Exception {
		if (p == null)
			p = Pattern.compile("_?[a-zA-Z]+\\d+");
		Object o = cacheKM.get(name);
		paremeterKM.putAll(sumKM);
		StringBuilder sb = new StringBuilder();
		if (o == null) {
			sb.append("public class K" + name + "{\n");
			sb.append("    public Double calculate(java.util.Map paremeterKM) {\n");
			Matcher m = p.matcher(formula);
			Set<String> has = new HashSet<String>();
			while (m.find()) {
				String column = m.group();
				if (has.contains(column))
					continue;
				has.add(column);
				sb.append("double " + column
						+ "=Double.valueOf(paremeterKM.get(\"" + column
						+ "\").toString());\n");
				// sb.append("System.out.println(\"message:\"+" + column +
				// ");\n");
			}
			sb.append("return " + formula + ";\n");
			sb.append("}\n}");
			DynamicEngine de = DynamicEngine.getInstance();
			o = de.javaCodeToObject("K" + name, sb.toString());
			cacheKM.put(name, o);
		}
		Method mothed = o.getClass().getMethod("calculate", Map.class);
		Object obj = null;
		try {
			obj = mothed.invoke(o, paremeterKM);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			log.debug("MAP:"+paremeterKM);
			log.warn("反射异常", e.getTargetException());
			log.debug("SRC:"+sb);
			log.debug("name:"+name);
			log.debug("FORMULA:"+formula);
			log.debug("DEPT:"+id);
			throw new RuntimeException();
		}
		Double result = Double.valueOf(obj.toString());
		paremeterKM.put(name, result);
		if (sumKM.get("A" + name) == null)
			sumKM.put("A" + name, 0.0);
		sumKM.put("A" + name, result + sumKM.get("A" + name));
		if (result == Double.NaN) {
			log.debug("name:" + name + " result:" + result + " formula:"
					+ formula+" dept:"+id);
			log.debug(paremeterKM);
		}
		return result;
	}

	/**
	 * 同步部门报表数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateReportData2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("currentNum", 0);
		request.getSession().setAttribute("allNum", 0);
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String userid = userInfo.getUserid();
		ReportManager rm = new ReportManager();
		int tableid = Integer.parseInt(String.valueOf(request
				.getParameter("tableid")));
		String date = request.getParameter("date");
		try {
			HashMap<String, Object> colMap = new HashMap<String, Object>();
			colMap.put("tableid", tableid);
			colMap.put("date", date);
			HashMap<String, Object> filterMap = new HashMap<String, Object>();
			StringBuffer filtersql = new StringBuffer();
			// 添加权限
			filtersql
					.append(" o.orgID in(SELECT power_orgid FROM jh_power_view WHERE empid='"
							+ userid + "')");
			filterMap.put("filtersql", filtersql.toString());
			// 部门
			List<HashMap<String, Object>> departmentids = new OrganizeManager()
					.getOrganizeIds(filterMap);

			if (departmentids != null) {
				// 记录进度条的总值
				request.getSession().setAttribute("allNum",
						departmentids.size());
				for (int j = 0; j < departmentids.size(); j++) {
					HashMap<String, Object> idmap = (HashMap<String, Object>) departmentids
							.get(j);
					String deptid = (String) idmap.get("id");
					colMap.put("deptid", deptid);
					colMap.put("flag", 1);
					// 公式
					List<HashMap<String, Object>> baseData = new ReportManager()
							.getReportFormulaAndValue(colMap);
					if (baseData != null) {
						HashMap<String, Object> rMap = new HashMap<String, Object>();
						List flist = new ArrayList();
						for (int i = 0; i < baseData.size(); i++) {
							HashMap<String, Object> monthMap = (HashMap<String, Object>) baseData
									.get(i);
							HashMap<String, String> fmap = new HashMap<String, String>();
							fmap.put("columnname",
									String.valueOf(monthMap.get("colname")));
							fmap.put("formula",
									String.valueOf(monthMap.get("formula")));
							flist.add(fmap);

						}
						rMap.put("tableid", tableid);
						rMap.put("departmentid", deptid);
						rMap.put("date", date);
						rMap.put("flag", 1);
						rMap.put("op", 1);
						rm.relationCol2(rMap, flist);
					}
					// 记录进度条的当前值
					request.getSession().setAttribute("currentNum", j + 1);
				}
				WebTools.writeText(response, "1");
			}
		} catch (Exception e) {
			WebTools.writeText(response, "2");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 显示部门报表数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String type = request.getParameter("type");
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String table_id = request.getParameter("table_id");
		String orgnamesearch = request.getParameter("orgnamesearch");
		String datesearch = request.getParameter("datesearch");
		StringBuffer filtersql = new StringBuffer();
		filtersql.append(" 1=1 ");
		if (!StringHelper.isEmpty(table_id)) {
			filtersql.append(" and r.table_id=" + Integer.parseInt(table_id));
		}
		if (!StringHelper.isEmpty(orgnamesearch)) {
			filtersql.append(" and o.orgname like '%" + orgnamesearch + "%' ");
		}
		if (!StringHelper.isEmpty(datesearch)) {
			filtersql.append(" and  r.months='" + datesearch + "'");
		} else {
			filtersql.append(" and r.months='" + df.format(new Date()) + "'");
		}
		// 添加查看权限
		filtersql
				.append(" and  o.orgID in(SELECT power_orgid FROM jh_power_view WHERE empid='"
						+ userInfo.getUserid() + "')");
		PageAction pageAction = new PageAction();
		try {
			pageAction.getDeptReportPageList(ReportManager.class, "getReport",
					filtersql.toString(), request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 导出部门报表数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward exportDepartmentReportData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code)) {
			return null;
		}
		String type = request.getParameter("type");
		if ("excel".equals(type)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			String table_id = request.getParameter("tableid");
			String orgnamesearch = request.getParameter("orgnamesearch");
			String datesearch = request.getParameter("date");
			StringBuffer filtersql = new StringBuffer();
			filtersql.append(" 1=1 ");
			if (!StringHelper.isEmpty(table_id)) {
				filtersql.append(" and r.table_id="
						+ Integer.parseInt(table_id));
			}
			if (!StringHelper.isEmpty(orgnamesearch)) {
				filtersql.append(" and o.orgname like '%" + orgnamesearch
						+ "%' ");
			}
			if (!StringHelper.isEmpty(datesearch)) {
				filtersql.append(" and  r.months='" + datesearch + "'");
			} else {
				filtersql.append(" and r.months='" + df.format(new Date())
						+ "'");
			}
			PageAction pageAction = new PageAction();
			try {
				List<HashMap<String, Object>> list = new ReportManager()
						.getReportDetailInfo(table_id);
				List contentlist = pageAction.getReportExcel(
						ReportManager.class, "getReport", filtersql.toString(),
						request, response);
				List headlist = new ReportManager()
						.getReportExportHead(table_id);
				String reportName = new ReportManager().getReportName(table_id);
				new ExportData().exportDeptExcel(reportName, headlist,
						contentlist, list, response);
				return null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 部门报表月结
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward beginDeptReportMonthly(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code)) {
			return null;
		}
		String tableid = request.getParameter("tableid");
		String deptid = request.getParameter("deptid");
		String date = request.getParameter("date");
		if (StringHelper.isEmpty(tableid)) {
			WebTools.writeText(response, "2");
		} else if (StringHelper.isEmpty(deptid)) {
			WebTools.writeText(response, "2");
		} else {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tableid", tableid);
				map.put("deptid", deptid);
				map.put("date", date);
				boolean b = new ReportManager().beginDeptReportMonthly(map);
				if (b) {
					WebTools.writeText(response, "1");
				} else {
					WebTools.writeText(response, "2");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 报表是否显示部门类型
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward isdShowOrganizeType(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		int tableid = Integer.parseInt(String.valueOf(request
				.getParameter("tableid")));
		int typeid = Integer.parseInt(String.valueOf(request
				.getParameter("typeid")));
		if (tableid == 0) {
			WebTools.writeText(response, "2");
		} else if (typeid == 0) {
			WebTools.writeText(response, "2");
		} else {
			try {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("tableid", tableid);
				map.put("typeid", typeid);
				boolean b = new ReportManager().isdShowOrganizeType(map);
				if (b) {
					WebTools.writeText(response, "1");
				} else {
					WebTools.writeText(response, "2");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 报表是否显示部门主管
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward isdShowOrganizeLeader(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		int tableid = Integer.parseInt(String.valueOf(request
				.getParameter("tableid")));
		int typeid = Integer.parseInt(String.valueOf(request
				.getParameter("typeid")));
		if (tableid == 0) {
			WebTools.writeText(response, "2");
		} else if (typeid == 0) {
			WebTools.writeText(response, "2");
		} else {
			try {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("tableid", tableid);
				map.put("typeid", typeid);
				boolean b = new ReportManager().isdShowOrganizeLeader(map);
				if (b) {
					WebTools.writeText(response, "1");
				} else {
					WebTools.writeText(response, "2");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public ActionForward editresult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		// if (!ValidateUser.validateOperation(request, response, userInfo,
		// "10164")) {
		// return null;
		// }
		// // 写日志
		// UserManager dm = new UserManager();
		// dm.addUserLog(userInfo.getUserid(), 1, "10164", "open",
		// "打开报表导入导出页面");
		String value = request.getParameter("val");
		String columnid = request.getParameter("columnid").toString();
		String orgid = request.getParameter("orgid").toString();
		String date = request.getParameter("date");
		Map map = new HashMap();
		boolean b = new ReportManager().setResultCorrect(orgid, columnid, date,
				value);
		WebTools.writeText(response, b ? 1 : 0);
		return null;
	}

	public static void main(String[] args) {
		float f = 0.00f;
		System.out.println(23.97 * 0.06);
	}

	public List<String> calculateOrder() {
		List<String> list = new ArrayList<String>();
		Set<String> set = new HashSet<String>();

		return list;
	}
}
