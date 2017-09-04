package jdbc.step1.simple.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.OracleInfo;

/*
 * CREATE TABLE customer(
   name varchar2(20) primary key,
   age number(3),
   height number(3),
   weight number(3),
   gender char(5));
 *  
 */

public class SimplecustomerDAOTest {
	
	//////////////////// 공통적인 메소드 //////////////////////
	private Connection connection() throws SQLException{
		Connection conn = DriverManager.getConnection(OracleInfo.URL, OracleInfo.USER, OracleInfo.PASS);
		System.out.println("DataBase Connection......");
		return conn;
	}
	
	private void closeAll(PreparedStatement ps , Connection conn) throws SQLException{
		if(ps !=null)ps.close();
		if(conn !=null)conn.close();
	}
	//OverLoading argument 만 다른것, 하는일은 같은데
	private void closeAll(ResultSet rs, PreparedStatement ps , Connection conn) throws SQLException{
		if(rs!=null)
			rs.close();
		closeAll(ps, conn);
		
	}
	
	//Business Logic..........
	//1. Customer를 DB에 추가하는 로직
	/*public void insertCustomer(String name, int age, int height, int weight, String gender) throws  SQLException {
			
		
		 * 1. DB연결... Connection 리턴
		 * 2. PreparedStatement 생성... 이때 쿼리문 pre-compile
		 * 3. 바인딩..
		 * 4. 쿼리문 실행.. executeUpdate()
		 * 5. 자원을 닫는다... close() - 여는순서 반대로 닫기.
		
		Connection conn = null; 
		PreparedStatement ps = null;
		//1.....고정적
		conn=connection();
		//2.....가변적
		String sql1 ="insert into customer values(?,?,?,?,?)";
		ps = conn.prepareStatement(sql1);
		//3.....가변적
		ps.setString(1, name);
		ps.setInt(2, age);
		ps.setInt(3, height);
		ps.setInt(4, weight);
		ps.setString(5, gender);
		ps.executeUpdate();
		//4.....가변적
		System.out.println("insertCustomer 완료하였습니다.");
		//5.....고정적
		closeAll(ps ,conn);
		
		
	}*/
	
	
	//2. Customer를 DB에 삭제하는 로직
	/*public void deleteCustomer(String name) throws SQLException {
		
		 *//** 1. DB연결... Connection 리턴
		 * 2. PreparedStatement 생성... 이때 쿼리문 pre-compile
		 * 3. 바인딩..
		 * 4. 쿼리문 실행.. executeUpdate()
		 * 5. 자원을 닫는다... close() - 여는순서 반대로 닫기.*//*
		
		Connection conn = null; 
		PreparedStatement ps = null;
		
		conn=connetcion(); 
		
		
		String sql2 = "delete from customer where name = ?";
	    ps = conn.prepareStatement(sql2);
		ps.setString(1, name);
		ps.executeUpdate();
		System.out.println("deletetCustomer 완룐하였습니다.");
		
		closeAll(ps ,conn);
		
		
		
		
	}*/
	
	/*public void updateCustomer(String name, int age, int height, int weight, String gender) throws SQLException{
		//pk 가 아닌 나머지 모든 컬러들을 overwrite 시키는것
		Connection conn = null; 
		PreparedStatement ps = null;
		
		conn = connection(); 
		
		String sql3 ="update customer set age= ?, height=?, weight=?, gender=? where name = ?";
		
		ps = conn.prepareStatement(sql3);
		
		ps.setInt(1, age);
		ps.setInt(2, height);
		ps.setInt(3, weight);
		ps.setString(4, gender);
		ps.setString(5, name);
		
		ps.executeUpdate();
		System.out.println("updateCUstomer 완료");
		
		closeAll(ps, conn);
					
		
		
	}*/
	
	public void printAllCustomer() throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = connection();
		
		String sql4 = "select * from customer";
		ps = conn.prepareStatement(sql4);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString(1)+"\t"+rs.getInt(2)+"\t"+rs.getInt(3)
			+"\t"+rs.getInt(4)+"\t"+rs.getString(5));
					
		}
		//OverLoading argument 만 다른것, 하는일은 같은데
		closeAll(rs, ps, conn);
	
	}
	
	public void printCustomer(String name) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = connection();
		
		String sql5 = "select * from customer where name = ? ";
		ps = conn.prepareStatement(sql5);
		ps.setString(1, name);
		rs = ps.executeQuery();
		
		if(rs.next()) 
			System.out.println(name+"\t"+rs.getInt(2)+"\t"+rs.getInt(3)
			+"\t"+rs.getInt(4)+"\t"+rs.getString(5));
		
		closeAll(rs, ps, conn);
		
		
	}
	
	public static void main(String[] args) throws  SQLException {
		System.out.println("JDBC SImple DAO Test....");
		 SimplecustomerDAOTest dao = new  SimplecustomerDAOTest();
		//dao.insertCustomer("효리", 20, 160, 45, "w");
		//dao.deleteCustomer("아이유");
		//dao.updateCustomer("아이유", 20, 160, 55, "M");
		 dao.printAllCustomer();
		 dao.printCustomer("효리");
	}//main
	
	static {
		System.out.println("Driver Loading Start....");
		try {
			Class.forName(OracleInfo.DRIVER);
			System.out.println("Driver Loading Success....");
		} catch (ClassNotFoundException e) {
				System.out.println("Driver Loading Fail.....");
		}
		
		
	}

}
