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
	
	//�������� ����...
	
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
	
	//////////////// �����Ͻ� ���� /////////////////////
	
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
			if(isExists(conn,cust.getSsn())) throw new DuplicateSSNException("�̹� �׷���� �־��!!");
			
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
			if(!isExists(conn,ssn)) throw new RecordNotFoundException("�׷���� �����!");
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
	 * Ư�� ���� ������ �ֽ��� ������ �����ϴ� ���
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
	 * Ư���� ���� ��� ������ �����ϴ� ���
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
				// �����ڷ� ���� ��Ű�ų� (  �����ٰ� , getportfolio(ssn));  )
 			}
			//�̷��� sector������ �ְų�
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
	 * ���� � �ֽ��� ?
	 * � ������ �ִ°� ?
	 * 0���� ũ�� ----> update
	 * 0�̸� -----> insert into
	 */
	public void buyShares(String ssn, String symbol, int quantity) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			//������ �����ִ� �ֽ����� �ε��ֽ����� �ε�
			String query="SELECT * FROM shares WHERE ssn=? AND symbol=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ssn);
			ps.setString(2, symbol);
			rs = ps.executeQuery();
			if(rs.next()) {// ������ ��� �ֽ��� �̹� ������ �ִ�
				
				//int q = rs.getInt(1); // ���� �������� q newQuantity = quantity+q;
			 
				
				//�ֽ��� ������ ������
				String query2 = "UPDATE shares SET quantity = quantity + ? WHERE ssn=? AND symbol=?";
				ps = conn.prepareStatement(query2);
				ps.setInt(1, quantity);
				ps.setString(2, ssn);
				ps.setString(3, symbol);
				System.out.println(ps.executeUpdate()+" �ֽ������� ������Ʈ �Ǿ����ϴ�. ");
				
			}else {	//�ƿ� �ش� ������ �ֽ��� ����.			
				
			//ó�� ��� �ֽ��϶�
			String query3="INSERT INTO shares VALUES(?,?,?)";
			ps = conn.prepareStatement(query3);
			ps.setString(1, ssn);
			ps.setString(2, symbol);
			ps.setInt(3, quantity);
			System.out.println(ps.executeUpdate()+" ù �ֽı��� �Դϴ�.");
						
			}
			
			
		}finally {
			
			closeAll(rs, ps, conn);
		}
	}
	
	/*
	 * ���� � �ֽ��� � �Ȱų�?
	 * 100���� ������ �ִ�.
	 * 1) 100�� �� �Ĵ� ��� ----> delete
	 * 2) 50�� �Ĵ� ��� ----> update
	 * 3) 110�� �Ĵ� ���----> ...exception
	 * ::
	 * �����ҷ�����
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
				int q = rs.getInt(1);// ���� ���� �ֽ�
				int newQuantity =q-quantity;
				if(q==quantity) {//delete
					String query1 = "DELETE FROM shares WHERE ssn = ? AND symbol = ?";
					ps = conn.prepareStatement(query1);
					ps.setString(1, ssn);
					ps.setString(2, symbol);
					System.out.println(ps.executeUpdate()+" �ֽ��� ���� �� �Ⱦҽ��ϴ�.. delete");
				}else if(q>quantity) { // update
					String query2 = "UPDATE shares SET quantity= quantity-? WHERE ssn=? AND symbol = ?";
					ps = conn.prepareStatement(query2);
					ps.setInt(1, quantity);
					ps.setString(2, ssn);
					ps.setString(3, symbol);
					System.out.println(ps.executeUpdate()+"�ֽ��� �κ������� �Ⱦҽ��ϴ�... update");
				}else {
					throw new RecordNotFoundException("�Ȱ��� �ϴ� �ֽ��� �� ���ƿ�!");
					
				}
				
			}else {
				throw new RecordNotFoundException("�Ȱ��� �ϴ� �ֽ��� �����!");
			}
			
		}finally {
			
			closeAll(rs, ps, conn);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Database db = new Database("127.0.0.1");
		
		//�߰��ϴ� ����
		//db.addCustomer(new CustomerRec("000-000","kosta","�Ǳ�"));
			
		
		//�����ϴ� ����
		db.deleteCustomer("000-000");
		
		//�����ϴ� ����
		//db.updateCustomer(new CustomerRec("222-222", "abc", "Busan"));
		
		//�ֽ������� �ҷ����� ���� by ssn
		//System.out.println(db.getPortfolio("222-222"));
		
		//�������� �ҷ����� ���� by ssn
		//System.out.println(db.getCustomer("222-222"));
		
		//��� ���� �ҷ����� ����
		//System.out.println(db.getAllCustomers());
		
		//��� �ֽ������� �ҷ����� ����
		//System.out.println(db.getAllStocks());
		
		//�ֽ� ������ �ҷ����� ���� by symbol
		//db.getStockPrice("JDK");
		
		//�ֽ� �춧 ����
		//db.buyShares("000-000", "PMLtd", 10000);
		
		//�ֽ� �ȶ� ����
		//db.sellShares("111-112", "PMLtd", 1000);
		
		
		
	}
	
}









