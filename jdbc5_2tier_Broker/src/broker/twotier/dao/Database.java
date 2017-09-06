package broker.twotier.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import broker.twotier.exception.DuplicateSSNException;
import broker.twotier.exception.RecordNotFoundException;
import broker.twotier.vo.CustomerRec;
import broker.twotier.vo.SharesRec;
import broker.twotier.vo.StockRec;
import config.OracleInfo;

public class Database {

	public Database(String server) throws ClassNotFoundException{
		Class.forName(OracleInfo.DRIVER);
		System.out.println("Driver Loading...... ok!!");
		
	}
	
	//공통적인 로직...
	
	public Connection getConnect() throws SQLException{
		
		Connection conn = DriverManager.getConnection(OracleInfo.URL,OracleInfo.USER,OracleInfo.PASS);
		System.out.println("DBconnecting..... ok");
		return conn;
	}
	
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException{
		if(ps!=null)ps.close();
		if(conn!=null)conn.close();
		
	}
	
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException{
		if(rs!=null)rs.close();
		closeAll(ps, conn);
		
	}
	
	//////////////// 비지니스 로직 /////////////////////
	
	public boolean isExists(Connection conn, String ssn) throws SQLException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	
			
			String query = "SELECT ssn FROM customer WHERE ssn =? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			rs = ps.executeQuery();
			
	
		return rs.next();
		
	}
	
	public void addCustomer(CustomerRec cust) throws SQLException, DuplicateSSNException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
		
			conn = getConnect();
			if(isExists(conn,cust.getSsn())) throw new DuplicateSSNException("이미 그런사람 있어요!!");
			
			String query ="INSERT INTO customer VALUES(?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, cust.getSsn());
			ps.setString(2, cust.getName());
			ps.setString(3, cust.getAddress());
			System.out.println(ps.executeUpdate()+" Add Customer ssn:: "+cust.getSsn());
		}finally {
			closeAll(ps, conn);
		}
		
	}
	
	public void deleteCustomer(String ssn) throws SQLException, RecordNotFoundException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			if(!isExists(conn,ssn)) throw new RecordNotFoundException("그런사람 없어요!");
			String query="DELETE FROM customer WHERE ssn = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			
			System.out.println(ps.executeUpdate()+" Delete Customer ssn :: "+ssn);
								
			
		}finally {
			closeAll(ps, conn);
		}
		
	}
	
	public void updateCustomer(CustomerRec cust) throws SQLException, RecordNotFoundException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			if(!isExists(conn,cust.getSsn())) throw new RecordNotFoundException();
			String query="UPDATE customer SET address =?, cust_name=? WHERE ssn=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, cust.getAddress());
			ps.setString(2, cust.getName());
			ps.setString(3, cust.getSsn());
			System.out.println(ps.executeUpdate()+" Update Customer ssn ::"+cust.getSsn());
			
		}finally {
			closeAll(ps, conn);
		}
		
	}
	
	/*
	 * 특정 고객이 보유한 주식의 정보를 리턴하는 기능
	 */
	public Vector<SharesRec> getPortfolio(String ssn) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<SharesRec> v = new Vector<SharesRec>();
		
		try {
			conn = getConnect();
			String query="SELECT * FROM shares WHERE  ssn=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			rs = ps.executeQuery();
			while(rs.next()) {
				v.add(new SharesRec(ssn, 
						rs.getString("symbol"), 
						rs.getInt("quantity")));
						
			}
			
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return v;
	}
	
	
	/*
	 * 특정한 고객의 모든 정보를 리턴하는 기능
	 */
	
	public CustomerRec getCustomer(String ssn) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerRec cr = null;
		
		
		try {
			conn = getConnect();
			String query="SELECT * FROM customer WHERE ssn=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			rs = ps.executeQuery();
			if(rs.next()) {
				   cr= new CustomerRec(ssn,     
						rs.getString("cust_name"),
						rs.getString("address"));
				// 생성자로 주입 시키거나 (  위에다가 , getportfolio(ssn));  )
 			}
			//이렇게 sector식으로 넣거나
			cr.setPortfolio(getPortfolio(ssn));
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return cr;
	}
	
	public ArrayList<CustomerRec> getAllCustomers() throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null; 	
		ResultSet rs = null;
		ArrayList<CustomerRec> list = new ArrayList<CustomerRec>();
		try {
			conn = getConnect();
			String query="select * from customer";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new CustomerRec(
						rs.getString("ssn"), 
						rs.getString("cust_name"), 
						rs.getString("address"), 
						getPortfolio(rs.getString("ssn"))));
			}
			
		}finally {
			closeAll(rs, ps, conn); 
		}
		
		
		return list;
		
	}
	
	
	public ArrayList<StockRec> getAllStocks() throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<StockRec> list = new ArrayList<StockRec>();
		
		try {
			conn = getConnect();
			String query="SELECT * FROM stock";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new StockRec(rs.getString("symbol"), 
						  rs.getFloat("price")));
			}
			
		}finally {
			closeAll(rs, ps, conn); 
		}
		
		
		return list;
		
	}



	public float getStockPrice(String symbol) throws SQLException, RecordNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		float price = 0.0f;
		
		try {
			conn = getConnect();
			String query="SELECT price FROM stock WHERE symbol = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, symbol);
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println(symbol+rs.getFloat("price"));
			}
			
			
		}finally {
			
			closeAll(rs, ps, conn);
		}
		return price;
	}
	
	/*
	 * 누가 어떤 주식을 ?
	 * 몇개 가지고 있는가 ?
	 * 0보다 크면 ----> update
	 * 0이면 -----> insert into
	 */
	public void buyShares(String ssn, String symbol, int quantity) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			//개인이 갖고있는 주식정보 로딩주식정보 로딩
			String query="SELECT * FROM shares WHERE ssn=? AND symbol=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			ps.setString(2, symbol);
			rs = ps.executeQuery();
			if(rs.next()) {// 기존에 몇개의 주식을 이미 가지고 있다
				
				//int q = rs.getInt(1); // 현재 보유수량 q newQuantity = quantity+q;
			 
				
				//주식을 가지고 있을때
				String query2 = "UPDATE shares SET quantity = quantity + ? WHERE ssn=? AND symbol=?";
				ps = conn.prepareStatement(query2);
				ps.setInt(1, quantity);
				ps.setString(2, ssn);
				ps.setString(3, symbol);
				System.out.println(ps.executeUpdate()+" 주식정보가 업데이트 되었습니다. ");
				
			}else {	//아예 해당 종목의 주식이 없다.			
				
			//처음 사는 주식일때
			String query3="INSERT INTO shares VALUES(?,?,?)";
			ps = conn.prepareStatement(query3);
			ps.setString(1, ssn);
			ps.setString(2, symbol);
			ps.setInt(3, quantity);
			System.out.println(ps.executeUpdate()+" 첫 주식구매 입니다.");
						
			}
			
			
		}finally {
			
			closeAll(rs, ps, conn);
		}
	}
	
	/*
	 * 누가 어떤 주식을 몇개 팔거냥?
	 * 100개를 가지고 있다.
	 * 1) 100개 다 파는 경우 ----> delete
	 * 2) 50개 파는 경우 ----> update
	 * 3) 110개 파는 경우----> ...exception
	 * ::
	 * 수량불러오기
	 */

	public void sellShares(String ssn, String symbol, int quantity) throws SQLException,RecordNotFoundException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			String query= "SELECT quantity FROM shares WHERE ssn = ? AND symbol = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			ps.setString(2, symbol);
			rs = ps.executeQuery();
			if(rs.next()) {
				int q = rs.getInt(1);// 현재 보유 주식
				int newQuantity =q-quantity;
				if(q==quantity) {//delete
					String query1 = "DELETE FROM shares WHERE ssn = ? AND symbol = ?";
					ps = conn.prepareStatement(query1);
					ps.setString(1, ssn);
					ps.setString(2, symbol);
					System.out.println(ps.executeUpdate()+" 주식을 몽땅 다 팔았습니다.. delete");
				}else if(q>quantity) { // update
					String query2 = "UPDATE shares SET quantity= quantity-? WHERE ssn=? AND symbol = ?";
					ps = conn.prepareStatement(query2);
					ps.setInt(1, quantity);
					ps.setString(2, ssn);
					ps.setString(3, symbol);
					System.out.println(ps.executeUpdate()+"주식을 부분적으로 팔았습니다... update");
				}else {
					throw new RecordNotFoundException("팔고자 하는 주식이 넘 많아요!");
					
				}
				
			}else {
				throw new RecordNotFoundException("팔고자 하는 주식이 없어요!");
			}
			
		}finally {
			
			closeAll(rs, ps, conn);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Database db = new Database("127.0.0.1");
		
		//추가하는 로직
		//db.addCustomer(new CustomerRec("000-000","kosta","판교"));
			
		
		//삭제하는 로직
		db.deleteCustomer("000-000");
		
		//변경하는 로직
		//db.updateCustomer(new CustomerRec("222-222", "abc", "Busan"));
		
		//주식정보를 불러오는 로직 by ssn
		//System.out.println(db.getPortfolio("222-222"));
		
		//고객정보를 불러오는 로직 by ssn
		//System.out.println(db.getCustomer("222-222"));
		
		//모든 고객을 불러오는 로직
		//System.out.println(db.getAllCustomers());
		
		//모든 주식정보를 불러오는 로직
		//System.out.println(db.getAllStocks());
		
		//주식 가격을 불러오는 로직 by symbol
		//db.getStockPrice("JDK");
		
		//주식 살때 로직
		//db.buyShares("000-000", "PMLtd", 10000);
		
		//주식 팔때 로직
		//db.sellShares("111-112", "PMLtd", 1000);
		
		
		
	}
	
}









