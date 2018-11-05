package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;


public class ProductDAO {
	
	public ProductDAO() {		
	}
	
	public void insertProduct(Product product) throws Exception { 
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO PRODUCT VALUES"
				+ " (seq_product_prod_no.nextval,?,?,TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'YYYYMMDD'),?,?,sysdate)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate());
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
		
	}
	
	public Product findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT*FROM PRODUCT where prod_no = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);
		
		ResultSet rs = stmt.executeQuery();
		
		Product product = null;
		
		while(rs.next()) {
		product = new Product();
		product.setFileName(rs.getString("IMAGE_FILE"));
		product.setManuDate(rs.getString("MANUFACTURE_DAY"));
		product.setPrice(rs.getInt("PRICE"));
		product.setProdDetail(rs.getString("PROD_DETAIL"));
		product.setProdName(rs.getString("PROD_NAME"));
		product.setProdNo(rs.getInt("PROD_NO"));
		product.setRegDate(rs.getDate("REG_DATE"));
		
		}
		con.close();
		
		return product;
	}
	public Map<String, Object> getProductList(Search search) throws Exception {
		
		Map<String , Object> map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT ";
		
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " where PROD_NO LIKE '%" + search.getSearchKeyword()
						+ "%'";
			} else if (search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("") ) {
				sql += " where PROD_NAME LIKE '%" + search.getSearchKeyword()
						+ "%'";
			} else if (search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("") ) {
				sql += " where PRICE LIKE '%" + search.getSearchKeyword()
				+ "%'";
			}
		}
		sql += " ORDER BY prod_name";
		
		System.out.println("ProuductDAO::Original SQL :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProuductDAO :: totalCount  :: " + totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);
		
		List<Product> list = new ArrayList<Product>();
		
			
			while(rs.next()){
				Product pro = new Product();
				
				pro.setFileName(rs.getString("IMAGE_FILE"));
				pro.setManuDate(rs.getString("MANUFACTURE_DAY"));
				pro.setPrice(rs.getInt("PRICE"));
				pro.setProdDetail(rs.getString("PROD_DETAIL"));
				pro.setProdName(rs.getString("PROD_NAME"));
				pro.setProdNo(rs.getInt("PROD_NO"));
				pro.setRegDate(rs.getDate("REG_DATE"));

				list.add(pro);
			}
			
			//==> totalCount 정보 저장
			map.put("totalCount", new Integer(totalCount));
			//==> currentPage 의 게시물 정보 갖는 List 저장
			map.put("list", list);

			rs.close();
			pStmt.close();
			con.close();

			return map;
			
			}
	
			public void updateProduct(Product product) throws Exception {
				
			Connection con = DBUtil.getConnection();
			
			String sql = "update PRODUCT set prod_name=?, prod_detail=?, MANUFACTURE_DAY=?,PRICE=?,IMAGE_FILE=? where PROD_NO=?";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, product.getProdName());
			stmt.setString(2, product.getProdDetail());
			stmt.setString(3, product.getManuDate());
			stmt.setInt(4, product.getPrice());
			stmt.setString(5, product.getFileName());
			stmt.setInt(6, product.getProdNo());
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
				
				System.out.println("UserDAO :: make SQL :: "+ sql);	
				
				return sql;
			}
		}