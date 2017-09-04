package jdbc.step2.mvc.dao;
/*
 * DAO 객체란?
 *  - DB Access 하는 로직을 따로 모아놓은 클래스이다.
 *  	DataAccessObject 를 의미하며 약자로 DAO라 불린다.
 *  ::
 *  DAO안의 비지니스 로직은 크게 두 부류로 나뉜다.
 *  1_ 모든 메소드 마다 고정적으로 들어가는 기능 -> 고정적인 기능
 *  	- DB연결 작업 | CLOSE
 *  2_ 모든 메소드마다 조금씩 달라지는 기능 -> 가변적인 기능
 *  	- Query문 작성 | 바인딩 과정 | Query 수행
 *  ::
 *  
 *  싱글톤 패턴으로 작성
 *  DAO 객체는 단 한개만 생성
 *  이 안의 메소드를 각각의 요청이 있을때마다 뽑아다 쓸 수 있도록 한다.
 *  1) private static으로 객체를 하나를 그 안에서 생성
 *  2) private() 생성자로 다른 곳에서 생성을 막아둔다
 *  3) public static MemberDAO getInstance(){}
 *  
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import config.OracleInfo;
import jdbc.step2.mvc.vo.Member;

public class MemberDAO {
	private static MemberDAO dao = new MemberDAO();
	
	private MemberDAO() {
		System.out.println("SingleTone Pattern... Creating");
	}

	public static MemberDAO getInstance() {
		return dao;
	}
////////////////////공통적인 메소드 //////////////////////
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
	
///////////////////비지니스 로직 ///////////////////
	public void addMember(Member member) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		String sql= "insert into member values(?,?,?,?)";
		ps = conn.prepareStatement(sql);
		ps.setString(1, member.getId());
		ps.setString(2, member.getName());
		ps.setString(3, member.getPassword());
		ps.setString(4, member.getAddress());


		System.out.println(ps.executeUpdate()+" row ADD() :: "+member.getName());
		
		closeAll(ps, conn);
		
	}
	
	
	
	public void removeMember(String id) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		String sql2 = "delete from member where name =?	";
		ps = conn.prepareStatement(sql2);
		
		ps.setString(1, id);
		
		System.out.println(ps.executeUpdate()+" row remove() :: "+id);
		
		closeAll(ps, conn);
	}
	
	
	
	public void updateMember(Member member) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		String sql= "update member set address =?, name =? where id =";
		ps = conn.prepareStatement(sql);
		
		ps.setString(1, member.getAddress());
		ps.setString(2, member.getName());
		ps.setString(3, member.getId());


		System.out.println(ps.executeUpdate()+" row update() :: "+member.getId());
		
		closeAll(ps, conn);
		
	}
	
	public Member getMember(String id) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Member member = null;
		
		conn = connection();
		
		String query = "SELECT * FROM member WHERE id=?";
		ps = conn.prepareStatement(query);
		ps.setString(1, id);
		
		rs = ps.executeQuery();
		if(rs.next()) {
			member = new Member(id, 
								rs.getString("name"), 					
								rs.getString("password"), 
								rs.getString("address"));
		}
		return member;
	}
	
	public ArrayList<Member> getAllMember() throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Member> list = new ArrayList<Member>();
		
		conn = connection();
		
		String query = "SELECT * FROM member";
		ps = conn.prepareStatement(query);		
		
		rs = ps.executeQuery();
		while(rs.next()) {
			list.add(new Member(rs.getString("id"), 
								rs.getString("name"), 					
								rs.getString("password"), 
								rs.getString("address")));
		}
		return list;
		
	}
	
	
}
