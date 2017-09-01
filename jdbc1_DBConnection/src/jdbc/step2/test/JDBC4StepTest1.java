package jdbc.step2.test;
/*
 * member��� ���̺��� ����
 * CREATE TABLE member(
 * 			id varchar2(20),
 * 			name varchar2(20),
 * 			age number(3));
 * ::
 * ����Ŭ���� ���ڿ� �ش��ϴ� ����Ÿ Ÿ����
 * 1) varchar2
 * 2) char
 * 
 * varchar2 �� �������� ���ڿ�(String)�� �ִ�ũ��� 400byte
 * 
 * char�� �������� ���ڿ�
 * ::
 * DML (������ commit �ʿ�) | DDL (auto commit)
 * 
 * insert into member values('r', 'w', 0);
 * delete from member;
 * delete from member where id = '111';
 * update member set age = 40 where id = '111';
 * -------------------------------------------------
 * select * from member;
 * select name, age from member; === ����, �κи� ���
 * select name, age from member where id ='777';
 * -------------------------------------------------
 * 
 * create table member( id varchar2(20)); === auto commit.
 * DROP TABLE test PURGE; 
 * -------------------------------------------------
 * 
 * 1.test ��� ���̺��� ����
 * 	ssn number(5)
 *  name varchar2(30)
 *  address varchar2(50)
 *  
 *  2. date�� �Է� (3��)
 *  3. Ư���� ����Ÿ �ϳ��� ����
 *  4. �� ���� ����Ÿ�� �ϳ��� �ּҿ� �̸��� ���ÿ� ����
 *  5. select�� ǥ����
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC4StepTest1 {
	public static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	public static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	public static final String USER = "hr";
	public static final String PASS = "hr";
	
	JDBC4StepTest1() throws ClassNotFoundException, SQLException{
		
		//1 DriverLoading
		Class.forName(DRIVER_NAME);
		System.out.println("����̹� �ε� ����....");
		//2 DBConnection
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		System.out.println("DB���� ����...");
		
	
	/*
	 * Query ���� ���� ( executeQuery() | executeUpdate() )
	 * - Statement�� ����
	 * -sql���� �ۼ� -> ����
	 * 
	 */
	
		Statement st = conn.createStatement();
		System.out.println("Statement Creating......");
		
		/*String query1 = "insert into member values('kosta', '������', '24')";
		
		int i = st.executeUpdate(query1);
		System.out.println(i);*/
		
		/*String query2 = "delete from member where age = 24";
			System.out.println(st.executeUpdate(query2)+"Row Delete!!");*/
		
		String query3 = "select * from member";
		ResultSet rs = st.executeQuery(query3);
		
		while(rs.next()) {
			System.out.println(rs.getString("id")+ "\t "
								+rs.getString("name")+"\t"
								+rs.getInt("age"));
		}
		
		System.out.println("========================================");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new JDBC4StepTest1();

	}

}