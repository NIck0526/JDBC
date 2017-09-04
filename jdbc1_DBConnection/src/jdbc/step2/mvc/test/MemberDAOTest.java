package jdbc.step2.mvc.test;

import config.OracleInfo;
import jdbc.step2.mvc.dao.MemberDAO;
import jdbc.step2.mvc.vo.Member;

public class MemberDAOTest {

	public static void main(String[] args) throws Exception{
		MemberDAO dao = MemberDAO.getInstance();//�̺κ��� ����..
		dao.addMember(new Member("kosta1", "������", "1234", "�Ż絿"));
		//dao.removeMember("kosta1");
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
