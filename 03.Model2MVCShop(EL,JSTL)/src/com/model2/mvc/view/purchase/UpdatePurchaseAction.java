package com.model2.mvc.view.purchase;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


public class UpdatePurchaseAction extends Action {

	public String execute(	HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Purchase purchase=new Purchase();
		
		int tranNo= Integer.parseInt(request.getParameter("tranNo"));
	
		
		
		
		purchase.setTranNo(tranNo);
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("divyDate"));
		
		System.out.println("¾÷µ«È®ÀÎ"+purchase);
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.updatePurchase(purchase);
		
		
		
		return "foward:/getPurchase.do?tranNo="+tranNo;
		}
	}

