package jdbc.step2.test;
/*
 * member라는 테이블을 생성
 * CREATE TABLE member(
 * 			id varchar2(20),
 * 			name varchar2(20),
 * 			age number(3));
 * ::
 * 오라클에서 문자에 해당하는 데이타 타입은
 * 1) varchar2
 * 2) char
 * 
 * varchar2 는 가변길이 문자열(String)로 최대크기는 400byte
 * 
 * char는 고정길이 문자열
 * ::
 * DML (명시적 commit 필요) | DDL (auto commit)
 * 
 * insert into member values('r', 'w', 0);
 * delete from member;
 * delete from member where id = '111';
 * update member set age = 40 where id = '111';
 * -------------------------------------------------
 * select * from member;
 * select name, age from member; === 권장, 부분만 출력
 * select name, age from member where id ='777';
 * -------------------------------------------------
 * 
 * create table member( id varchar2(20)); === auto commit.
 * DROP TABLE test PURGE; 
 * -------------------------------------------------
 * 
 * 1.test 라는 테이블을 생성
 * 	ssn number(5)
 *  name varchar2(30)
 *  address varchar2(50)
 *  
 *  2. date를 입력 (3줄)
 *  3. 특정한 데이타 하나를 삭제
 *  4. 두 개의 데이타중 하나의 주소와 이름을 동시에 수정
 *  5. select로 표시함
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
		System.out.println("드라이버 로딩 성공....");
		//2 DBConnection
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		System.out.println("DB연결 성공...");
		
	
	/*
	 * Query 문을 수행 ( executeQuery() | executeUpdate() )
	 * - Statement를 생성
	 * -sql문을 작성 -> 실행
	 * 
	 */
	
		Statement st = conn.createStatement();
		System.out.println("Statement Creating......");
		
		/*String query1 = "insert into member values('kosta', '아이유', '24')";
		
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
