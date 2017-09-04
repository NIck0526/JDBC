package jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import config.OracleInfo;
import jdbc.exception.DuplicateIdException;
import jdbc.exception.RecordNotFoundException;
import jdbc.vo.Member;
/*
 * CRUD �������� 
 * ����Ÿ�� ��� �ְ�/����/�����ϱ� ���� �ݵ�� ���� �����ؾ� �ϴ�
 * ����� �ִ�.
 * PK(�⺻Ű)�� �ش��ϴ� ����Ÿ�� �ִ����� ���θ� �ݵ�� ������ �˾ƾ� �Ѵ�.
 * -----------------------------------------------------------
 * addXxx() : ��� �߰��ϰ��� �ϴ� ����Ÿ �����ϸ� - DuplicateIdException
 * deleteXxx() : ��� �����ϰ��� �ϴ� ����Ÿ ������ - RecordNotFoundException
 * updateXxx() : ���  �����ϰ��� �ϴ� ����Ÿ ������ - RecordNotFoundException
 */
public class MemberDAO {
	private static MemberDAO dao = new MemberDAO();
	
	private MemberDAO() {
		System.out.println("SingleTone Pattern...Creating DAO");
	}
	
	public static MemberDAO getInstance() {
		return dao;		
	}
	/////////// �������� �޼ҵ� ///////////////////////////
	private Connection connection() throws SQLException{
		Connection conn=DriverManager.getConnection(OracleInfo.URL, "hr", "hr");
		System.out.println("DataBase Connection.........");
		return conn;
	}

	private void closeAll(PreparedStatement ps, Connection conn) throws SQLException{
		if(ps != null) ps.close();
		if(conn !=null) conn.close();
	}
	private void closeAll(ResultSet rs,PreparedStatement ps, Connection conn) throws SQLException{
		if(rs !=null) rs.close();
		closeAll(ps, conn);
	}
	
	////////////////// �����Ͻ� ���� ////////////////////////////
	public boolean idExists(String id, Connection conn)throws SQLException{
		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT id FROM member WHERE id=?";
		ps = conn.prepareStatement(query);
		ps.setString(1, id);
		
		rs = ps.executeQuery();		
		return rs.next() ;
		
	}
	public void addMember(Member member)throws SQLException, DuplicateIdException{
		Connection conn = null;
		PreparedStatement ps = null;		
		try {
			conn = connection();		
			if(!idExists(member.getId(),conn)) {
				String query = "INSERT INTO member VALUES(?,?,?,?)";
		
				ps = conn.prepareStatement(query);
				ps.setString(1, member.getId());
				ps.setString(2, member.getName());
				ps.setString(3, member.getPassword());
				ps.setString(4, member.getAddress());
		
				System.out.println(ps.executeUpdate()+" row ADD() :: "+member.getName());
		}else {
			throw new DuplicateIdException("�׷� ��� �̹� �־��");
		}
		}finally {
			closeAll(ps, conn);
		}
	}
	public void removeMember(String id)throws SQLException,RecordNotFoundException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = connection();
			if(idExists(id, conn)) {//�����Ϸ��� ����� �ִٸ�
				String query = "DELETE FROM member WHERE id=?";
				ps = conn.prepareStatement(query);
				ps.setString(1, id);
		
				System.out.println(ps.executeUpdate()+" row DELETE() ");
			}else { //�����Ϸ��� ����� ���ٸ�
				throw new RecordNotFoundException("�����Ϸ��� ����� �����");
			}
		}finally{
		 closeAll(ps, conn);
		}
	}
	public void updateMember(Member member)throws SQLException,RecordNotFoundException{
		/*
		 * �����Ϸ��� member�� id�� ������ id�� ���� �����
		 * ��� ������(�������� ������) ��������� ��ź�� �߻����Ѽ�
		 * �ش��ϴ� member�� �������� �ʵ��� �����ϰ� ��� �ؾ��Ѵ�.
		 */
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = connection();
			if(idExists(member.getId(), conn)) {
			String query = "UPDATE member SET name=? , password=? , address=? WHERE id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, member.getName());
			ps.setString(2, member.getPassword());
			ps.setString(3, member.getAddress());
			ps.setString(4, member.getId());
		
			System.out.println(ps.executeUpdate()+" row UPDATE() ");
			}else {
				throw new RecordNotFoundException("�����Ϸ��� ����� �����");
			}
		}finally {
			closeAll(ps, conn);	
		}
	}
	public  Member getMember(String id)throws SQLException{
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
	
	public  ArrayList<Member> getAllMember()throws SQLException{
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


























