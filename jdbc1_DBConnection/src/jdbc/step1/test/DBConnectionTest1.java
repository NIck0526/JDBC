package jdbc.step1.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * 1. 드라이버 로딩
 * 		: DB서버 정보를 가지고 있는 드라이버 클래스를
 * 		  해당 클라이언트측 메모리에 로딩시킨다
 * 		이 과정없이는 다음 단계의 DB연결이 이루어지지 않는다.
 * 
 * 2. DB연결
 * 		: jdbc4 단계중 가장 중요한 과정
 * 		 DB에 연결이 성공되면, Connection 객체가 생성되어 리턴.
 */
public class DBConnectionTest1 {
	DBConnectionTest1(){
		
		try {
			//1
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로딩 성공....");
			
			//2				프로트콜 			주소		포트		네임
			String url ="jdbc:oracle:thin:@127.0.0.1:1521:xe";
			String user = "hr";
			String password = "hr";
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println("DB연결 성공...");
		
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패...");
		}catch (SQLException e) {
			System.out.println("DB연결 실패...");
		}
		
	}

	public static void main(String[] args) {
		new DBConnectionTest1();
		

	}

}
