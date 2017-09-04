package jdbc.test;

import config.OracleInfo;
import jdbc.dao.MemberDAO;
import jdbc.exception.DuplicateIdException;
import jdbc.exception.RecordNotFoundException;
import jdbc.vo.Member;
public class MemberDAOTest {

	public static void main(String[] args) throws Exception{
		MemberDAO dao = MemberDAO.getInstance();//�̺κ��� ����..
		try {
			dao.addMember(new Member("kosta1", "������", "1234", "�Ż絿"));
		}catch(DuplicateIdException e) {
			System.out.println(e.getMessage());
		}
		try {
			dao.removeMember("hahash");
		}catch(RecordNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//dao.updateMember(new Member("kosta", "������", "4321", "���ǵ�"));
		//System.out.println(dao.getAllMember());
	}//main
	
	static {
		try {
			Class.forName(OracleInfo.DRIVER);
			System.out.println("����̹� �ε� ����...");
		}catch(ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����...");
		}
	}//static
}//class
