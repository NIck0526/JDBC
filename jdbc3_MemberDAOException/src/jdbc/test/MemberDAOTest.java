package jdbc.test;

import config.OracleInfo;
import jdbc.dao.MemberDAO;
import jdbc.exception.DuplicateIdException;
import jdbc.exception.RecordNotFoundException;
import jdbc.vo.Member;
public class MemberDAOTest {

	public static void main(String[] args) throws Exception{
		MemberDAO dao = MemberDAO.getInstance();//이부분이 변경..
		try {
			dao.addMember(new Member("kosta1", "아이유", "1234", "신사동"));
		}catch(DuplicateIdException e) {
			System.out.println(e.getMessage());
		}
		try {
			dao.removeMember("hahash");
		}catch(RecordNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//dao.updateMember(new Member("kosta", "아이유", "4321", "여의도"));
		//System.out.println(dao.getAllMember());
	}//main
	
	static {
		try {
			Class.forName(OracleInfo.DRIVER);
			System.out.println("드라이버 로딩 성공...");
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패...");
		}
	}//static
}//class
