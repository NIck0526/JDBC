package jdbc.step2.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC4StepTest2 {
	public static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	public static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	public static final String USER = "hr";
	public static final String PASS = "hr";
	
	JDBC4StepTest2() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER_NAME);
		System.out.println("Drvier Loading...!");
		
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		System.out.println("Data Base Connecting..!!");
		
		Statement st = conn.createStatement();
		System.out.println("Statement Creating....!!");
		
		/*String query1 = "insert into member values('oracle', '오라클', 50)";
		String query2 = "insert into member values('Jdk', '제임스', 60)";
		
		System.out.println(st.executeUpdate(query1)+"추가 되었당.");
		System.out.println(st.executeUpdate(query2)+"추가 되었당.");*/
		
		/*String query3 = "delete from member where age = 60";
		System.out.println(st.executeUpdate(query3)+"Row Delete!!");*/
		
		String  query4 = "update member set name = '제우스', id = 'pp' where age = 50 ";
		System.out.println(st.executeUpdate(query4)+"");
		
		/*String query5 = "select name, age from member";
		ResultSet rs = st.executeQuery(query5);
		
		while(rs.next()) {
			System.out.println(rs.getString("name")+"\t"
								+rs.getInt("age"));*/
		}
		

	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new JDBC4StepTest2();

	}

}
