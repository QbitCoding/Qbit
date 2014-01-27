package jhjx;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import pnbclient.helper.StringHelper;

import com.powernt.form.Mapform;
import com.powernt.info.UserInfo;
import com.powernt.logic.UserManager;
import com.powernt.tools.ValidateUser;
import com.yaunqi.iproject.count.CountAction;
import com.yuanqi.iproject.logic.CommonCodeManager;
import com.yuanqi.iproject.logic.ParameterManager;
import com.yuanqi.iproject.util.WebTools;

public class ParameterAction extends DispatchAction {

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
		dm.addUserLog(userInfo.getUserid(), 1, code, "open", "打开企业存款参数管理页面");
		request.setAttribute("tableid", request.getParameter("tableid"));
		request.setAttribute("code", code);
		return mapping.findForward("list");
	}

	// 获取表格头数据
	public ActionForward getParameterHead(ActionMapping mapping,
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
		String table_id = request.getParameter("table_id");
		List list = new ParameterManager().getParameterHead(table_id);
		if (list != null) {
			WebTools.writeJson(response, list);
		} else {
			WebTools.writeText(response, "装载表格头部发生异常");
		}
		return null;
	}

	// 获取参数列表
	public ActionForward getParameterList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String action = request.getParameter("op");
		String table_id = request.getParameter("table_id");
		action = "getlist";
		if ("getlist".equals(action)) {
			StringBuffer filtersql = new StringBuffer();
			String namesearch = request.getParameter("namesearch");
			String parametersearch = request.getParameter("parametersearch");
			String codesearch = request.getParameter("codesearch");
			filtersql.append(" 1=1 ");
			if (!StringHelper.isEmpty(table_id)) {
				filtersql.append(" and p1.table_id="
						+ Integer.parseInt(table_id));
			}
			if (!StringHelper.isEmpty(namesearch)) {
				filtersql.append(" and p1.name like '%" + namesearch + "%' ");
			}
			if (!StringHelper.isEmpty(parametersearch)) {
				filtersql.append(" and p1.parameter like '%" + parametersearch
						+ "%' ");
			}
			if (!StringHelper.isEmpty(codesearch)) {
				filtersql.append(" and p1.code like '%" + codesearch + "%' ");
			}
			PageAction pageAction = new PageAction();
			try {
				pageAction.getTreePageList(ParameterManager.class,
						"getParameterList", filtersql.toString(), request,
						response);
			} catch (Exception ex) {
			}

		}
		return null;
	}

	/*
	 * 获取计算公式列按钮
	 */
	public ActionForward getParameterFormulaList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code,
				"Open")) {
			return null;
		}
		String tableid = request.getParameter("table_id");
		List list = new ParameterManager().getParameterFormulaList(tableid);
		if (list != null) {
			WebTools.writeJson(response, list);
		} else {
			WebTools.writeJson(response, null);
		}
		return null;
	}

	/*
	 * 新增参数公式
	 */
	public ActionForward addParameterFormula2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code,
				"New")) {
			return null;
		}
		ParameterManager pm = new ParameterManager();
		String tableid = request.getParameter("tableid");
		String columnid = request.getParameter("columnid");
		String formula = request.getParameter("formula");
		String formulacn = request.getParameter("formulacn");
		if (StringHelper.isEmpty(tableid)) {

		} else if (StringHelper.isEmpty(columnid)) {

		} else if (StringHelper.isEmpty(formula)) {
			WebTools.writeText(response, 4);
		} else if (StringHelper.isEmpty(formulacn)) {
			WebTools.writeText(response, 5);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("columnid", columnid);
		map.put("tableid", tableid);
		map.put("formula", formula);
		map.put("formulacn", formulacn);
		
		try{
			boolean b = pm.addParameterFormula(map);
			new UserManager().addUserLog(userInfo.getUserid(), 1, code, "New",
					"新增公式" + (b ? "成功" : "失败"));
			if (b) {
				WebTools.writeText(response, 1);
			} else {
				WebTools.writeText(response, 2);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	/*
	 * 获取新增参数公式jsp页面
	 */
	public ActionForward addParameterFormula(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code,
				"New")) {
			return null;
		}
		Mapform mapform = (Mapform) form;
		ParameterManager pm = new ParameterManager();
		String tableid = request.getParameter("tableid");
		String columnid = request.getParameter("columnid");
		
		List list1 = pm.getParameterButton2(tableid, "1");
		List list2 = pm.getParameterButton2(tableid, "2");
		List list3 = pm.getParameterButton2(tableid, "3");
		List list4 = pm.getParameterButton2(tableid, "4");
		List list = pm.getParameterButton2(tableid, "5");
		String formulaColumns = pm.getParameterFormulaColumns(columnid);
		request.setAttribute("paramButtons1", list1);
		request.setAttribute("paramButtons2", list2);
		request.setAttribute("paramButtons3", list3);
		request.setAttribute("paramButtons4", list4);
		request.setAttribute("paramButtons", list);
		request.setAttribute("formulaColumns", formulaColumns);
		request.setAttribute("op", "new");
		request.setAttribute("code", code);
		request.setAttribute("columnid", columnid);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("tableid", tableid);
		map.put("columnid", columnid);
		mapform.setMap(map);
		request.setAttribute("action",
				"parameter.do?action=addParameterFormula&code=" + code);
		return mapping.findForward("addFormula");
	}

	/**
	 *  修改公式
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editParameterFormula(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code,
				"Modify")) {
			return null;
		}
		Mapform mapform = (Mapform) form;
		ParameterManager pm = new ParameterManager();
		String id = request.getParameter("id");
		String tableid = request.getParameter("tableid");
		String action = request.getParameter("op");
		String columnid = request.getParameter("columnid");
		if ("edit".equals(action)) {
			
			HashMap<String,Object> map = (HashMap<String,Object>) mapform.getMap();
			boolean b = pm.editParameterFormula(map);
			HashMap<String,String> fmap = pm.getParameterFormulaDetail((String) map.get("id"));
			String columnname = (String) fmap.get("column_name");
			if(!StringHelper.isEmpty(columnname)){
				new CountAction().removeCacheData(columnname);
			}
			new UserManager().addUserLog(userInfo.getUserid(), 1, code, "New",
					"修改公式" + (b ? "成功" : "失败"));
			if (b) {
				ValidateUser.showDialog(response, "修改成功", true, true, true);
				return null;
			} else {
				request.setAttribute("res", "修改失败");
			}
		}
		try {
			List list1 = pm.getParameterButton2(tableid, "1");
			List list2 = pm.getParameterButton2(tableid, "2");
			List list3 = pm.getParameterButton2(tableid, "3");
			List list4 = pm.getParameterButton2(tableid, "4");
			List list = pm .getParameterButton2(tableid, "5");
			request.setAttribute("paramButtons1", list1);
			request.setAttribute("paramButtons2", list2);
			request.setAttribute("paramButtons3", list3);
			request.setAttribute("paramButtons4", list4);
			request.setAttribute("paramButtons", list);
			request.setAttribute("op", "edit");
			request.setAttribute("code", code);
			request.setAttribute("columnid", columnid);
			HashMap<String, String> map = pm.getParameterFormulaDetail(id);
			map.put("tableid", tableid);
			map.put("id", id);
			map.put("columnid", columnid);
			request.setAttribute("formula", (String) map.get("formula"));
			request.setAttribute("formulacn", (String) map.get("formulacn"));
			mapform.setMap(map);
			request.setAttribute("action",
					"parameter.do?action=editParameterFormula&code=" + code);
		} catch (Exception e) {
			WebTools.writeText(response, "获取公式信息时发生异常");
			e.printStackTrace();
		}

		return mapping.findForward("addFormula");
	}

	// 参数树
	public ActionForward getParameterTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableid = request.getParameter("tableid");
		List list = new ParameterManager().getParameterTree(tableid);
		if (list != null) {
			WebTools.writeJson(response, list);
		} else {
			WebTools.writeText(response, "获取参数树发生异常");
		}
		return null;
	}

	// 获取选中树的节点数据
	public ActionForward getParameterCheckedNodes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableid = request.getParameter("tableid");
		List list = new ParameterManager().getParameterCheckedNodes(tableid);
		if (list != null) {
			WebTools.writeJson(response, list);
		} else {
			WebTools.writeJson(response, null);
		}
		return null;
	}

	// 参数下拉框
	public ActionForward getParameterSelect(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableid = request.getParameter("tableid");
		List list = new ParameterManager().getParameterSelect(tableid);
		if (list != null) {
			WebTools.writeJson(response, list);
		} else {
			WebTools.writeText(response, "获取参数树发生异常");
		}
		return null;
	}

	// 参数下拉框(树形)
	public ActionForward getParameterSelectTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableid = request.getParameter("tableid");
		List list = new ParameterManager().getParameterSelectTree(tableid);
		if (list != null) {
			WebTools.writeJson(response, list);
		} else {
			WebTools.writeText(response, "获取参数树发生异常");
		}
		return null;
	}

	/*
	 * 删除
	 */
	public ActionForward deleteParameterFormula(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code,
				"Delete")) {
			return null;
		}
		String id = request.getParameter("id");
		String columnid = request.getParameter("columnid");
		String result = null;
		if (!StringHelper.isEmpty(id)) {
			try {
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("formulaid", id);
				map.put("columnid", columnid);
				new ParameterManager().deleteParameterFormula(map);
				// 1：删除成功
				result = "1";
			} catch (Exception e) {
				// 2：删除失败
				result = "2";
				e.printStackTrace();
			}
		}
		WebTools.writeText(response, result);
		return null;
	}
	
	/**
	 * 删除参数
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteParameter(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo, code,
				"Delete")) {
			return null;
		}
		String id = request.getParameter("id");
		String result = "2";
		ParameterManager pm = new ParameterManager();
		if (!StringHelper.isEmpty(id)) {
			try {
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("columnid", id);
				if(!pm.isDeleteParameter(map)){
					// 3：不能删除，有公式引用该列
					result = "3";
				}else if(!pm.isHashChildrenParameter(map)){
					// 4：不能删除，有子参数
					result = "4";
				}else{
					boolean b = pm.deleteParameter(map);
					if(b){
						// 1：删除成功
						result = "1";
					}else{
						// 2：删除失败
						result = "2";
					};
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		WebTools.writeText(response, result);
		return null;
	}

	/**
	 * 参数上移
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyParameterUpRow(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		String result = null;
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String code = request.getParameter("code");
		if (!ValidateUser.validateOperationString(request, response, userInfo,
				code, "Modify")) {
			result = "3";
			WebTools.writeText(response, result);
			return null;
		}

		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", request.getParameter("id"));
			map.put("previd", request.getParameter("previd"));
			boolean b = new ParameterManager().modifyParameterUpRow(map);
			new UserManager().addUserLog(userInfo.getUserid(), 1, code,
					"Modify", "修改参数上移" + (b ? "成功" : "失败"));
			if (b) {
				// 1：操作成功
				result = "1";
			} else {
				// 2：操作失败
				result = "2";
			}
		} catch (Exception e) {
			result = "2";
			e.printStackTrace();
		}
		WebTools.writeText(response, result);
		return null;
	}

	/**
	 * 添加参数
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addParameter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String menucode = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo,
				menucode, "New")) {
			return null;
		}
		String op = request.getParameter("op");
		int tableid = Integer.parseInt(String.valueOf(request.getParameter("tableid")));
		ParameterManager pm = new ParameterManager();
		if ("new".equals(op)) {
			Mapform mapform = (Mapform) form;
			HashMap map = (HashMap) mapform.getMap();
			map.put("tableid", tableid);
			String code = (String) map.get("code");
			CommonCodeManager ccm = new CommonCodeManager();
			HashMap<String,Object> ccmMap = new HashMap<String,Object>();
			ccmMap.put("tableid", tableid);
			ccmMap.put("selfid", null);
			ccmMap.put("code", code);
			ccmMap.put("flag", 1);
			if (ccm.isExistCode(ccmMap)) {
				boolean b = pm.addParameter(map);
				new UserManager().addUserLog(userInfo.getUserid(), 1, menucode,
						"Modify", "添加参数" + (b ? "成功" : "失败"));
				if (b) {
					ValidateUser.showDialog(response, "添加成功", true, true, true);
					return null;
				} else {
					request.setAttribute("res", "新增失败");
				}
			} else {
				request.setAttribute("res", "编码已经存在，请重新填写");
			}
		}
		request.setAttribute("tableid", tableid);
		request.setAttribute("op", "new");
		request.setAttribute("action", "parameter.do?action=addParameter&code="
				+ menucode);
		return mapping.findForward("addParameter");
	}

	/**
	 * 编辑参数
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editParameter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 检查session
		HttpSession session = ValidateUser.checkSession(request, response);
		if (session == null) {
			return null;
		}
		UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
		String menucode = request.getParameter("code");
		if (!ValidateUser.validateOperation(request, response, userInfo,
				menucode, "Modify")) {
			return null;
		}
		String op = request.getParameter("op");
		String id = request.getParameter("id");
		int tableid = Integer.parseInt(String.valueOf(request.getParameter("tableid")));
		Mapform mapform = (Mapform) form;
		ParameterManager pm = new ParameterManager();
		CommonCodeManager ccm = new CommonCodeManager();
		HashMap map = (HashMap) mapform.getMap();
		if ("edit".equals(op)) {
			String code = (String) map.get("code");
			String paramid = (String) map.get("id");
			HashMap<String,Object> ccmMap = new HashMap<String,Object>();
			ccmMap.put("tableid", tableid);
			ccmMap.put("selfid", paramid);
			ccmMap.put("code", code);
			ccmMap.put("flag", 1);
			// 判断code是否唯一
			if (ccm.isExistCode(ccmMap)) {
				System.out.println("map:"+map);
				boolean b = pm.modifyParameter(map);
				new UserManager().addUserLog(userInfo.getUserid(), 1, menucode,
						"Modify", "修改参数信息" + (b ? "成功" : "失败"));
				if (b) {
					ValidateUser.showDialog(response, "修改成功", true, true, true);
					return null;
				} else {
					request.setAttribute("res", "修改失败");
				}
			} else {
				mapform.setMap(map);
				request.setAttribute("res", "编码已经存在，请重新填写");
			}
		} else {
			HashMap detailmap = pm.getParameter(id);
			mapform.setMap(detailmap);
		}
		request.setAttribute("op", "edit");
		request.setAttribute("tableid", tableid);
		request.setAttribute("code", menucode);
		request.setAttribute("action", "parameter.do?action=editParameter");
		return mapping.findForward("addParameter");
	}
}
