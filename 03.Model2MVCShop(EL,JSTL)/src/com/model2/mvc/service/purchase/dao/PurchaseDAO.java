package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;

import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class PurchaseDAO {

	public Purchase findPurchase(int tranNo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select*from TRANSACTION where TRAN_NO = ? ";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);
		
		ResultSet rs = stmt.executeQuery();
		
		Purchase purchase = null;
		
		
		while(rs.next()) {
			
		purchase = new Purchase();	
		Product purchaseProd = new Product();
		purchaseProd.setProdNo(rs.getInt("PROD_NO"));
		
		User buyer = new User();
		buyer.setUserId(rs.getString("BUYER_ID"));
		
		
		purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
		purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
		purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
		purchase.setDivyAddr(rs.getString("DEMAILADDR"));
		
		System.out.println("리시버주소 : " +purchase.getDivyAddr()); 
		purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
		purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
		purchase.setOrderDate(rs.getDate("ORDER_DATA"));
		purchase.setDivyDate(rs.getString("DLVY_DATE"));
		purchase.setTranNo(rs.getInt("TRAN_NO"));
		purchase.setBuyer(buyer);
		purchase.setPurchaseProd(purchaseProd);
		
	
		
	}
		System.out.println("확인용 :" +purchase);
		con.close();
		return purchase;
	}
	public Purchase findPurchase2(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM users u, product p, transaction t WHERE t.prod_no=? AND u.user_id=t.buyer_id AND p.prod_no=t.prod_no";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);
		
		ResultSet rs = stmt.executeQuery();
					
			Purchase purchase = null;
			while (rs.next()) {
				purchase = new Purchase();
				
				Product product = new Product();
				product.setProdNo(rs.getInt("PROD_NO"));
				product.setProdName(rs.getString("PROD_NAME"));
				product.setProdDetail(rs.getString("PROD_DETAIL"));
				product.setManuDate(rs.getString("MANUFACTURE_DAY"));
				product.setPrice(rs.getInt("PRICE"));
				product.setFileName(rs.getString("IMAGE_FILE"));
				product.setRegDate(rs.getDate("REG_DATE"));
				purchase.setPurchaseProd(product);
				
			
				User user = new User();
				user.setUserId(rs.getString("USER_ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setRole(rs.getString("ROLE"));
				user.setSsn(rs.getString("SSN"));
				user.setPhone(rs.getString("CELL_PHONE"));
				user.setAddr(rs.getString("ADDR"));
				user.setEmail(rs.getString("EMAIL"));
				user.setRegDate(rs.getDate("REG_DATE"));
				purchase.setBuyer(user);
				
				purchase.setTranNo(rs.getInt("TRAN_NO"));
				purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
				purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
				purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
				purchase.setDivyAddr(rs.getString("DEMAILADDR"));
				purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
				purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
				purchase.setOrderDate(rs.getDate("ORDER_DATA"));
				Date date = rs.getDate("DLVY_DATE");
				
				if (date!=null)
					purchase.setDivyDate(date.toString());
				else
					purchase.setDivyDate("");
			}
			
			con.close();
		
		return purchase;
	}
	
	
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		
		Map<String, Object> map= new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		// 원래 쿼리 
		String sql = "SELECT*FROM transaction";
		
		if(buyerId != null) {
			sql += " WHERE buyer_id ='"+buyerId+"'";
		}
			sql += " ORDER BY prod_no";
		
		System.out.println("PurchaseDAO :: Original SQL ::"+sql);
		
		//==>TotalCount GET
		
		int totalCount =this.getTotalCount(sql);
		System.out.println("토탈카운트 :"+totalCount);
		
		//==> CurrentPage 게시물만 받도록 쿼리 다시 구성
		sql = makeCurrentPageSql(sql, search);
		
			PreparedStatement pStmt = 
				con.prepareStatement(	sql );
		
				ResultSet rs = pStmt.executeQuery();
				
				System.out.println(search);
				
	
			
		
			ProductService proService = new ProductServiceImpl();
			List<Purchase> list = new ArrayList<Purchase>();
			
			while(rs.next()) {
					Purchase purchase = new Purchase();
					User buyer = new User();
					buyer.setUserId(rs.getString("BUYER_ID"));
					System.out.println("buyer : " + buyer);
					
					purchase.setBuyer(buyer);
					purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
					purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
					purchase.setPurchaseProd(proService.getProduct(rs.getInt("PROD_NO")));
					purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
					purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));

					purchase.setDivyDate(rs.getString("DLVY_DATE"));
					purchase.setTranNo(rs.getInt("TRAN_NO"));
					
					list.add(purchase);
					//if (!rs.next())
					//	break;
				}
			
			
				map.put("totalCount", new Integer(totalCount));
	
				map.put("list",list);
					
				rs.close();
				pStmt.close();
				con.close();

				return map;
		}
	
	
	public Map<String, Object> getSaleList(Search search) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select*from TRANSACTION";
		
		int totalCount = this.getTotalCount(sql);
		
		PreparedStatement stmt = 
				con.prepareStatement(	sql );
															
			ResultSet rs = stmt.executeQuery();

		
			
			List<Purchase> list = new ArrayList<Purchase>();
		
				while(rs.next()) {
			
					Purchase purchase = new Purchase();
					
					purchase.setPaymentOption(rs.getString("PAYMENT_OTPION"));
					purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
					purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
					purchase.setDivyAddr(rs.getString("DEMAIL_ADDR"));
					purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
					purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
					purchase.setOrderDate(rs.getDate("ORDER_DATE"));
					purchase.setDivyDate(rs.getString("DLVY_DATE"));
					purchase.setTranNo(rs.getInt("TRAN_NO"));
					
					list.add(purchase);
					if (!rs.next())
						break;
				}
				map.put("totalCount", new Integer(totalCount));
				
				map.put("list",list);
				
				
				return map;
		}
	
		public void insertPurchase(Purchase purchase) throws Exception {
			
			
			
			Connection con = DBUtil.getConnection();
			
			String sql = "insert into TRANSACTION values (seq_product_prod_no.nextval,?,?,?,?,?,?,?,?,sysdate,?)";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			
			
			stmt.setInt(1, purchase.getPurchaseProd().getProdNo());
			stmt.setString(2, purchase.getBuyer().getUserId());
			stmt.setString(3, purchase.getPaymentOption());
			stmt.setString(4, purchase.getReceiverName());
			stmt.setString(5, purchase.getReceiverPhone());
			stmt.setString(6, purchase.getDivyAddr());
			stmt.setString(7,  purchase.getDivyRequest());
			stmt.setString(8, purchase.getTranCode());
			stmt.setString(9, purchase.getDivyDate());
			
			stmt.executeUpdate();
		}
		public void updatePurchase(Purchase purchase) throws Exception {
			System.out.println("날아왔어요");
			System.out.println("두번쨰확인 :::" +purchase);
			
			Connection con = DBUtil.getConnection();
		
			
			String sql =  "UPDATE TRANSACTION  set"
					+ " payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?,dlvy_date=?"
					+ " where tran_no=?";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			
			System.out.println("쿼리준비완료");
			
			System.out.println("sql :"+sql);
			
			
	
			
			stmt.setString(1, purchase.getPaymentOption());
			stmt.setString(2, purchase.getReceiverName());
			stmt.setString(3,  purchase.getReceiverPhone());
			stmt.setString(4, purchase.getDivyAddr());
			stmt.setString(5,  purchase.getDivyRequest());
			stmt.setString(6,  purchase.getDivyDate());
			stmt.setInt(7, purchase.getTranNo());
		
			System.out.println("sql 2번쨰 확인 :" +sql);
			
			stmt.executeUpdate();
		
	
			
			
			
			
			System.out.println("dao 확인 : "+sql);
			
			
			con.close();
		}
		public void updateTranCode(Purchase purchase) throws Exception{
			
			Connection con = DBUtil.getConnection();
			String sql = "UPDATE TRANSACTION set TRAN_STATUS_CODE=? where PROD_NO=?";
					
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, purchase.getTranCode());
			stmt.setInt(2, purchase.getPurchaseProd().getProdNo());
			
			stmt.executeUpdate();
			con.close();
		}
		private int getTotalCount(String sql) throws Exception {
			
			sql = "SELECT COUNT(*) "+
			          "FROM ( " +sql+ ") countTable";
			
			Connection con = DBUtil.getConnection();
			PreparedStatement pStmt = con.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			
			int totalCount = 0;
			if( rs.next() ){
				totalCount = rs.getInt(1);
			}
			
			pStmt.close();
			con.close();
			rs.close();
			
			return totalCount;
		}
		// 게시판 currentPage Row 만  return 
		private String makeCurrentPageSql(String sql , Search search){
			sql = 	"SELECT * "+ 
						"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
										" 	FROM (	"+sql+" ) inner_table "+
										"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
						"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
			
			System.out.println("PurchaseDAO :: make SQL :: "+ sql);	
			
			return sql;
		}
	}
