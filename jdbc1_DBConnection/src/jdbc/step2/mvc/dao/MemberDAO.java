package jdbc.step2.mvc.dao;
/*
 * DAO ��ü��?
 *  - DB Access �ϴ� ������ ���� ��Ƴ��� Ŭ�����̴�.
 *  	DataAccessObject �� �ǹ��ϸ� ���ڷ� DAO�� �Ҹ���.
 *  ::
 *  DAO���� �����Ͻ� ������ ũ�� �� �η��� ������.
 *  1_ ��� �޼ҵ� ���� ���������� ���� ��� -> �������� ���
 *  	- DB���� �۾� | CLOSE
 *  2_ ��� �޼ҵ帶�� ���ݾ� �޶����� ��� -> �������� ���
 *  	- Query�� �ۼ� | ���ε� ���� | Query ����
 *  ::
 *  
 *  �̱��� �������� �ۼ�
 *  DAO ��ü�� �� �Ѱ��� ����
 *  �� ���� �޼ҵ带 ������ ��û�� ���������� �̾ƴ� �� �� �ֵ��� �Ѵ�.
 *  1) private static���� ��ü�� �ϳ��� �� �ȿ��� ����
 *  2) private() �����ڷ� �ٸ� ������ ������ ���Ƶд�
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
////////////////////�������� �޼ҵ� //////////////////////
	private Connection connection() throws SQLException{
		Connection conn = DriverManager.getConnection(OracleInfo.URL, OracleInfo.USER, OracleInfo.PASS);
		System.out.println("DataBase Connection......");
		return conn;
	}

	private void closeAll(PreparedStatement ps , Connection conn) throws SQLException{
		if(ps !=null)ps.close();
		if(conn !=null)conn.close();
	}
	//OverLoading argument �� �ٸ���, �ϴ����� ������
	private void closeAll(ResultSet rs, PreparedStatement ps , Connection conn) throws SQLException{
		if(rs!=null)
			rs.close();
		closeAll(ps, conn);

	}
	
///////////////////�����Ͻ� ���� ///////////////////
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
