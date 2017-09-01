package jdbc.step3.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.OracleInfo;

/*
 * primary key : unique + not null
 * 
 create table member(
 	id varchar2(20) primary key,
  	name varchar2(30) not null,
  	pass varchar2(30) not null,
  	tel varchar2(30) not null );
  
  1) �÷��� �߰� : alter table member add(address varchar2(10));
  2) �÷��� �Ӽ����� : alter table member modify(address varchar2(50));
  3) �÷��� �̸��� ���� : alter table member rename column pass to password;
  4) �÷����� ���� : alter table member drop column tel;
 */
public class JDBC4StepTest1 {

	
	JDBC4StepTest1() throws ClassNotFoundException, SQLException{
		Class.forName(OracleInfo.DRIVER);
		System.out.println("Drvier Loading...!");
		
		Connection conn = DriverManager.getConnection(OracleInfo.URL, OracleInfo.USER, OracleInfo.PASS);
		System.out.println("Data Base Connecting..!!");
		
		System.out.println("==============Prepared Statement ��� ==============");
		
	
		
		System.out.println("3.PreparedStatement... success!!!!!");
		//insert
		/*String sql1 = "insert into member values(?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql1);*/
		
		/*ps.setString(1, "222");
		ps.setString(2, "222");
		ps.setString(3, "222");
		ps.setString(4, "222");*/
		
		//delete
		/*String sql2 = "delete from member where name = ?";
		PreparedStatement ps = conn.prepareStatement(sql2);
		ps.setString(1, "222");
		System.out.println(ps.executeUpdate()+"�����Ϸ�");*/
		
		//update
		/*String sql3 = "update member set address =? where name=?";
		PreparedStatement ps = conn.prepareStatement(sql3);
		ps.setString(1, "333");
		ps.setString(2, "111");
		System.out.println(ps.executeUpdate()+"�����Ϸ�");*/
		
		//select
		String sql4 = "select name, address from member";
		PreparedStatement ps = conn.prepareStatement(sql4);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString("name")+"\t"
								+rs.getInt("address"));
		}
		
		
		
		/*System.out.println(ps.executeUpdate()+"row Insert okokok!!!ok!ok!ok!ok!okokok!!!!");*/
		//�� ���� �ݴ�� �ݾ��ش�.
		/*
		 * insert
		 * delete
		 * update
		 * select
		 */
		ps.close();
		conn.close();
		
		
	}// ������
	
		
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
		new JDBC4StepTest1 ();
	}

}
