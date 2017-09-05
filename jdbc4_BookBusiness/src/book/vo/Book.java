package book.vo;
/*
 * Book ���̺� ���� ������ ��� �ִ� VOŬ����
 */
public class Book {
	private String isbn;
	private String title;
	private String Writer;   /// �÷���� �ٸ�
	private String publisher;
	private int price;
	
	
	public Book(String isbn, String title, String writer, String publisher, int price) {
		super();
		this.isbn = isbn;
		this.title = title;
		Writer = writer;
		this.publisher = publisher;
		this.price = price;
	}
	
	
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return Writer;
	}
	public void setWriter(String writer) {
		Writer = writer;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	
	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", Writer=" + Writer + ", publisher=" + publisher
				+ ", price=" + price + "]";
	}
	
	
}
