package com.model2.mvc.service.purchase.impl;


import java.util.Map;

import com.model2.mvc.common.Search;

import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.dao.ProductDAO;

import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;


public class PurchaseServiceImpl implements PurchaseService {
	
	private PurchaseDAO dao;
	private ProductDAO prodDAO;
	
	
	public PurchaseServiceImpl() {
		dao = new PurchaseDAO();
	}
	
	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		
	dao.insertPurchase(purchase);
		
	}
	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return dao.findPurchase(tranNo);
	}
	@Override
	public Purchase getPurchase2(int ProdNo) throws Exception {
		// TODO Auto-generated method stub
		return dao.findPurchase2(ProdNo);
	}
	

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		dao.updatePurchase(purchase);
	
	}
	public void updateTranCode(Purchase purchase) throws Exception {
		dao.updateTranCode(purchase);
		
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPurchaseList(search, buyerId);
	}

	@Override
	public Map<String, Object> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	}

		

	


