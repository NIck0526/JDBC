package book.test;

import java.sql.SQLException;

import book.dao.BookDAO;
import book.exception.DuplicateIsbnException;
import book.exception.InvalidInputException;
import book.exception.RecoreNotFoundException;
import book.vo.Book;
import config.OracleInfo;

public class BookDAOTest {

	public static void main(String[] args) throws Exception {
		BookDAO dao = BookDAO.getInstance();
		
		//System.out.println(dao.isExists("1A1"));
		
		//å ���
	  /* try{
		   dao.registerBook(new Book("7G7", "�븣������ ��", "�Ϸ�Ű", "����", 29000));
	   }catch(DuplicateIsbnException e ) {
		   System.out.println(e.getMessage());
	   }*/
		
		//å ����
		/*try{
			dao.deleteBook("7G7");
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//å ã�� :: isbn
		/*try {
			System.out.println(dao.findByIsbn("1A1"));
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//å ã�� :: title
		/*try {
			System.out.println(dao.findByTitle("������", "5E5"));
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//å ã�� :: author
		/*try {
			System.out.println(dao.findByWriter("����Ÿ"));
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//å ã�� :: price
		System.out.println(dao.findByPrice(10000, 28000));
		
		//å ���� �ϱ�
		dao.discountBook(10, "�Ѻ�");

		
	}
	
	static {
		try {
			Class.forName(OracleInfo.DRIVER);
			System.out.println("����̹� �ε� ����...");
		}catch(ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����...");
		}
	}//static

}
