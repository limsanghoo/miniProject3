package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;



public class AddPurchaseViewAction extends Action {
	
	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		int  prodNo=Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("prodNo: " +prodNo);
		
	
		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(prodNo);
		//PurchaseService service=new PurchaseServiceImpl();
		//PurchaseVO purchaseVO=service.getPurchase(prodNo);
		
		request.setAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";

	}
}
