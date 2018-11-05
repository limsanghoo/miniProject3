package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


public class UpdateTranCodeByProdAction extends Action {
	
	public String execute(HttpServletRequest request, 
																HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("prodNo : "+prodNo);
		
		String tranCode = request.getParameter("tranCode");
		System.out.println("trancode : "+tranCode);
		
		PurchaseService pService = new PurchaseServiceImpl();
		Purchase purchase = pService.getPurchase2(prodNo);
		
		System.out.println("trancode2 : " +tranCode);
		System.out.println("purchase : "+purchase);
		
		purchase.setTranCode(tranCode);
		request.setAttribute("vo",purchase);
		
		System.out.println("purchaseVO : " +purchase);
		pService.updateTranCode(purchase);
		
		return "forward:/updateTranCode.do";
		
		
	}

}
