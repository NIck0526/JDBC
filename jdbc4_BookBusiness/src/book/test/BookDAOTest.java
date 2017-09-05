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
		
		//책 등록
	  /* try{
		   dao.registerBook(new Book("7G7", "노르웨이의 숲", "하루키", "도우", 29000));
	   }catch(DuplicateIsbnException e ) {
		   System.out.println(e.getMessage());
	   }*/
		
		//책 제거
		/*try{
			dao.deleteBook("7G7");
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//책 찾기 :: isbn
		/*try {
			System.out.println(dao.findByIsbn("1A1"));
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//책 찾기 :: title
		/*try {
			System.out.println(dao.findByTitle("스프링", "5E5"));
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//책 찾기 :: author
		/*try {
			System.out.println(dao.findByWriter("나가타"));
		}catch(RecoreNotFoundException e) {
			System.out.println(e.getMessage());
		}*/
		
		//책 찾기 :: price
		System.out.println(dao.findByPrice(10000, 28000));
		
		//책 할인 하기
		dao.discountBook(10, "한빛");

		
	}
	
	static {
		try {
			Class.forName(OracleInfo.DRIVER);
			System.out.println("드라이버 로딩 성공...");
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패...");
		}
	}//static

}
