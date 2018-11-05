package com.model2.mvc.view.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

	public class ListPurchaseAction extends Action {
		
		public String execute(	HttpServletRequest request,
				HttpServletResponse response) throws Exception {
    
		Search search=new Search();
		
		HttpSession session = request.getSession();
		
		User user = (User)session.getAttribute("user");

		System.out.println("user : " +user);
		
		String buyerId = user.getUserId();
		
		System.out.println("buyer : "+user);
		System.out.println("buyerId " +buyerId);
		
		
		
		int currentPage=1;
		if(request.getParameter("currentPage") != null)
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));

		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
	
		search.setPageSize(pageSize);
		

		PurchaseService service=new PurchaseServiceImpl();
		
		Map<String,Object> map=service.getPurchaseList(search, buyerId);
		
		
		Page resultPage	= 
				new Page(	currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("ListPurchaseAction ::"+resultPage);
	
	
		
		UserService userService = new UserServiceImpl();
		//userVO = userService.getUser(buyerId);
		System.out.println("user : "+user);
		
		request.setAttribute("list", map.get("list"));
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("search", search);
		request.setAttribute("map", map);
	
		
	
		return  "forward:/purchase/listPurchase.jsp";
	}
}