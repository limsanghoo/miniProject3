package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;



public class AddPurchaseAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		String buyerId = request.getParameter("buyerId");
		System.out.println("buyerId : " + buyerId);
		int prodNo = Integer.parseInt(request.getParameter("prodNo") );
		System.out.println("prodNo : " +prodNo);
		
		User user = new User();
		
		UserService userService = new UserServiceImpl();
		user = userService.getUser(buyerId);
		
		ProductService productService = new ProductServiceImpl();
		
		Product product = new Product();
		product = productService.getProduct(prodNo);
		
		
		
		Purchase purchase=new Purchase();
		
		


		
		
		
		
	
		
		//System.out.println("purchaseVO : "+purchaseVO);
		//System.out.println("productVO : " +productVO);
		//System.out.println("============±¸ºÐÀÚ=================");
		
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		purchase.setTranCode("1");
		
		
		System.out.println("===========================");
		System.out.println("purchase : "+purchase);
		
		
		PurchaseService service=new PurchaseServiceImpl();
		service.addPurchase(purchase);
		request.setAttribute("product", product);
		request.setAttribute("user", user);
		request.setAttribute("purchase", purchase);
		
		//System.out.println("purchaseVO : "+purchaseVO);
		return "forward:/purchase/addPurchase.jsp";
	}
}