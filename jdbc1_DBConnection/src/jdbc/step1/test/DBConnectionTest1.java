package jdbc.step1.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * 1. ����̹� �ε�
 * 		: DB���� ������ ������ �ִ� ����̹� Ŭ������
 * 		  �ش� Ŭ���̾�Ʈ�� �޸𸮿� �ε���Ų��
 * 		�� �������̴� ���� �ܰ��� DB������ �̷������ �ʴ´�.
 * 
 * 2. DB����
 * 		: jdbc4 �ܰ��� ���� �߿��� ����
 * 		 DB�� ������ �����Ǹ�, Connection ��ü�� �����Ǿ� ����.
 */
public class DBConnectionTest1 {
	DBConnectionTest1(){
		
		try {
			//1
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("����̹� �ε� ����....");
			
			//2				����Ʈ�� 			�ּ�		��Ʈ		����
			String url ="jdbc:oracle:thin:@127.0.0.1:1521:xe";
			String user = "hr";
			String password = "hr";
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println("DB���� ����...");
		
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����...");
		}catch (SQLException e) {
			System.out.println("DB���� ����...");
		}
		
	}

	public static void main(String[] args) {
		new DBConnectionTest1();
		

	}

}
