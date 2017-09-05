package book.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import book.exception.DuplicateIsbnException;
import book.exception.InvalidInputException;
import book.exception.RecoreNotFoundException;
import book.vo.Book;
import config.OracleInfo;


public class BookDAO {
	//싱글톤
	private static BookDAO dao = new BookDAO();
	
	private BookDAO(){
		try {
			Class.forName(OracleInfo.DRIVER);
			System.out.println("Driver Loading....Ok");
		}catch(ClassNotFoundException e) {
			System.out.println("Drvier Loading....Fail");
		}
		
	}
	
	//싱글톤 private으로 인해 우회하는 메소드
	
	public static BookDAO getInstance() {
		return dao;
	}
	
	//공통로직
	private Connection getConnect() throws SQLException{
		Connection conn = DriverManager.getConnection(OracleInfo.URL,OracleInfo.USER,OracleInfo.PASS);
		System.out.println("DB Connection...");
		return conn;
	}
	
	private void closeAll(PreparedStatement ps, Connection conn)throws SQLException{
		if(ps!=null)ps.close();
		if(conn!=null)conn.close();
	}
	private void closeAll(ResultSet rs, PreparedStatement ps, Connection conn)throws SQLException{
		if(rs!=null)rs.close();
		closeAll(ps, conn);
	}
	//book.exception.
	//DuplicateIsbnException
	//RecordNotFoundException
	//InvalidInputException
	
	
	//비지니스 로직
	
	public int isExists(String isbn)throws SQLException{
		int count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn= getConnect();
			String query="SELECT COUNT(-1) FROM book WHERE isbn = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, isbn);
			rs= ps.executeQuery();
			if(rs.next()) return rs.getInt(1);
			else return count;
		}finally {
			closeAll(rs, ps, conn);
								
		}
	}
	
	public void registerBook(Book vo)throws SQLException,DuplicateIsbnException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			String query="INSTER INTO book VALUSE(?,?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, vo.getIsbn());
			ps.setString(2, vo.getTitle());
			ps.setString(3, vo.getWriter());
			ps.setString(4, vo.getPublisher());
			ps.setInt(5, vo.getPrice());
		 
			System.out.println(ps.executeUpdate()+"REGISTER :: "+vo.getTitle());
			}finally {
				closeAll(ps, conn);
			}
		
	}//
	
	public void deleteBook(String isbn)throws SQLException,RecoreNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnect();
			String query="DELETE FROM book WHERE isbn = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, isbn);
			System.out.println(ps.executeUpdate()+"DELETE isbn :: "+isbn);
		}finally {
			closeAll(ps, conn);
					
			
		}
	}//
	
	public Book findByIsbn(String isbn)throws SQLException, RecoreNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Book book;
		try {
			conn = getConnect();
			String query="SELECT * FROM book WHERE isbn=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, isbn);
			rs = ps.executeQuery();
			if(rs.next()) {
				book = new Book(isbn,
						rs.getString("title"),
						rs.getString("writer"),
						rs.getString("publisher"),
						rs.getInt("price"));
			}else {
				throw new RecoreNotFoundException("그런책 ㄴㄴㄴ");
				
			}
			
		}finally {
				closeAll(rs, ps, conn);
			}
			return book;
			
			
		
		
	}//
	
	public Book  findByTitle(String title,String isbn)throws SQLException, RecoreNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Book book= null;
		try {
			conn = getConnect();
			String query = "SELECT * FROM book WHERE title =? AND isbn=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, title);
			ps.setString(2, isbn);
			rs= ps.executeQuery();
			if(rs.next()) {
				book = new Book(isbn,
						rs.getString("title"), 
						rs.getString("author"), 
						rs.getString("publisher"), 
						rs.getInt("price"));
			}else {
				throw new RecoreNotFoundException("그런책 없다!");
			}
			
		}finally {
			closeAll(rs,ps, conn);
		}
		return book;
	}//
	
	public ArrayList<Book>  findByWriter(String writer)throws SQLException, RecoreNotFoundException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Book> list = new ArrayList<Book>();
		
		try {
			conn = getConnect();
			String query="SELECT * FROM book WHERE author=?";
			
			ps=conn.prepareStatement(query);
			ps.setString(1, writer);
			while(rs.next()) {
				list.add(new Book
						(rs.getString("isbn"),
						 rs.getString("title"),
						 rs.getString("author"),
						 rs.getString("publisher"),
						 rs.getInt("price")));	
					
			} if (list.isEmpty())
				System.out.println("리스트가 비었습니다.");
	    	}finally {
				closeAll(rs, ps, conn);
			}
			
		return list;
		
	}//
	
	public ArrayList<Book>  findByPrice(int min, int max)throws SQLException, InvalidInputException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Book> list = new ArrayList<Book>();
		if(min<=0 || max <=min)throw new InvalidInputException();{
		}
		try {
			conn = getConnect();
			String query = "SELECT * FROM book WHERE price>=? AND price<=?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, min);
			ps.setInt(2, max);
			rs= ps.executeQuery();
			while(rs.next()) {
				list.add(new Book(
								rs.getString("isbn"),
								rs.getString("title"),
								rs.getString("author"),
								rs.getString("publisher"),
								rs.getInt("price")));
			}
		
			
					
		}finally {
			closeAll(rs,ps, conn);
		}
		return list;
	}//
	
	
	public void  discountBook(int per, String publisher)throws SQLException, InvalidInputException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnect();
			String query = "UPDATE book SET price =(price *  (1-?/100) WHERE publisher =?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, per);
			ps.setString(2, publisher);
			System.out.println(ps.executeUpdate()+" ROW UPDATE OK!!");
			
		}finally {
			closeAll(ps, conn);
		}
		
	}//
	
	
	public void  printAllBookInfo()throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnect();
			String query = "SELECT * FROM book";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println(
								rs.getString("isbn")+"\t"+
								rs.getString("title")+"\t"+
								rs.getString("author")+"\t"+
								rs.getString("publisher")+"\t"+
								rs.getInt("price"));
			}
			
		}finally {
			closeAll(rs,ps, conn);
		}
		
	}//
}
















