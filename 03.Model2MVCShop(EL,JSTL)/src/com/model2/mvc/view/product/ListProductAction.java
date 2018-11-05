package com.model2.mvc.view.product;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class ListProductAction extends Action{

	public String execute(HttpServletRequest req,
												HttpServletResponse res) throws Exception {
		Search search = new Search();
		
		int currentPage=1;
		
		if(req.getParameter("currentPage") != null) {
			currentPage=Integer.parseInt(req.getParameter("currentPage"));
		}
			search.setCurrentPage(currentPage);
			search.setSearchCondition(req.getParameter("searchCondition"));
			search.setSearchKeyword(req.getParameter("searchKeyword"));
			
			
			
			int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
			int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		
			search.setPageSize(pageSize);
			
			ProductService productService=new ProductServiceImpl();
			Map<String , Object> map=productService.getProductList(search);
			
			Page resultPage	= 
						new Page(	currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
			System.out.println("ListProductAction ::"+resultPage);
			
			// Model °ú View ¿¬°á
			req.setAttribute("list", map.get("list"));
			req.setAttribute("resultPage", resultPage);
			req.setAttribute("search", search);
			
			return "forward:/product/listProduct.jsp";
	}

	
}
