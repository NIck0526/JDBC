package broker.three.client;
/*
 *  클라이언트 측 통신을 담당하는 대표주자
 *  ::
 *  2tier에서 Database 자리에  그대로 들어있어야 한다.
 *  즉, 이전에 Database의 메소드 선언부가 그대로 있어야 한다.
 *  (Broker의 소스코드 수정을 하지 않기 위해서)
 *  ::
 *  역할
 *  1. 소켓을 생성해서 서버와의 연결
 *  2. 소켓으로부터 스트림 뽑아내고 Command를 던지고 | 받아야 한다.
 *  3. 메소드 안에서의 구현
 *  	1) 도시락 싸다.
 *  	2) 서버쪽으로 던진다.
 *  	----------------------	
 *  	3) 다시 jury 가 던진 도시락 받는다
 *  	4) 도시락 까본다 ( Data UnPack || getter)
 *   
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import broker.three.exception.DuplicateSSNException;
import broker.three.exception.InvalidTransactionException;
import broker.three.exception.RecordNotFoundException;
import broker.three.shares.Command;
import broker.three.shares.Result;
import broker.three.vo.CustomerRec;
import broker.three.vo.StockRec;

public class Protocol {

	//필드 선언
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Command cmd;
	
	private static final int MIDDLE_PORT = 60000;
	
	public Protocol(String serverIp) throws Exception {
		s = new Socket(serverIp, MIDDLE_PORT);
		System.out.println("client socket creating....");
		
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());

	}
	
	//////////////// 공통적인 기능 ///////////////////////////
	
	public void writeCommand(Command cmd) {
		try {
			oos.writeObject(cmd);
			System.out.println("client writeCommand().....end");
		}catch(IOException e) { //IOException :: Stream 관련 exception Write, Read
			System.out.println("protocol :: client writeCommand()....."+e);
		}
	}
	
	public int getResponse() {  //readObject() + getResult() --> Status Code
		try {
			cmd=(Command)ois.readObject();	
			System.out.println("client readCommand.....end");
		}catch(Exception e) {	
			System.out.println("client getResponse()....."+e);
		}
		int status = cmd.getResults().getStatus();
		return status;
	}
	
	////////////////////// Database와 동일한 메소드가 선언 //////////////////////
	public void buyShares(String ssn, String symbol, int quantity) throws SQLException{

		// 1. 도시락 싼다
		cmd = new Command(Command.BUYSHARES);
		String[ ] str = { ssn, symbol,String.valueOf(quantity)};  //String.valueOf(quantity) :: int 를 String 으로 만듬.
		cmd.setArgs(str);
		
		// 2. 도시락 던진다 
		
		writeCommand(cmd);
		/////////////////////////////////////////////////////////////
		
		// 3. 도시락 받는다
		getResponse();
					
		
	}
	
	public void sellShares(String ssn, String symbol, int quantity) throws SQLException, RecordNotFoundException, InvalidTransactionException{
		
		cmd = new Command(Command.SELLSHARES);
		String[ ] str = { ssn, symbol,String.valueOf(quantity)};  //String.valueOf(quantity) :: int 를 String 으로 만듬.
		cmd.setArgs(str);
		
		// 2. 도시락 던진다 
		
		writeCommand(cmd);
		/////////////////////////////////////////////////////////////
		
		// 3. 도시락 받는다
		int status=getResponse();
		if(status==-1) throw new RecordNotFoundException("팔려는 주식이 없어여....sellShares() ");
		else if(status==-3) throw new InvalidTransactionException("팔려는 주식이 넘 많아여...sellShares");
		
	}
	
	public ArrayList<StockRec> getAllStocks(){
		
		ArrayList<StockRec> list = new ArrayList<StockRec>();
		
		cmd = new Command(Command.GETALLSSTOCK);

		writeCommand(cmd);
		///////////////////////////////////////////////////
		
		int status=getResponse(); //도시락을 받았다.
		
		list = (ArrayList)cmd.getResults().get(0); // ArrayList 칸이 1칸이라서 인자값이 0
		
			
		return list;
	}
	
	public float getStockPrice(String symbol) {
		
		float temp = 0.0f;
		
		cmd = new Command(Command.GETSTOCKPRICE);
		String[] str = {symbol};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		getResponse();
		
		temp = (float)cmd.getResults().get(0);
		
		return temp;
		
	}
	
	public ArrayList<CustomerRec> getAllCustomers(){
		
		
		ArrayList<CustomerRec> list = new ArrayList<CustomerRec>();
		
		cmd = new Command(Command.GETALLCUSTOMERS);
		
		writeCommand(cmd);
		
		getResponse();
		
		list =(ArrayList)cmd.getResults().get(0);
		
		 
		return list;
	}
	
	
	public CustomerRec getCustomer(String ssn) {
		
		CustomerRec cr = null;
		
		cmd = new Command(Command.GETCUSTOMER);
		
		String [] str = {ssn};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		getResponse();
		
		cr = (CustomerRec)cmd.getResults().get(0);
		
		return cr;
		
	}
	
	public void addCustomer(CustomerRec cr) throws DuplicateSSNException{
		
		cmd = new Command(Command.ADDCUSTOMER);
		
		String [] str = {cr.getSsn(), cr.getName(), cr.getAddress()}; // String으로 넣어야함 new CustomerRec 이거는 타입이 CustomerRec일때만
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status = getResponse();
		
		if(status==-2) throw new DuplicateSSNException("이미 추가된 고객입니다... addCustomer()");
			
	}
	
	
	public void deleteCustomer(String ssn) throws RecordNotFoundException{
		
		cmd = new Command(Command.DELETECUSTOMER);
		
		String [] str = {ssn};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status = getResponse();
		
		if(status==-1) throw new RecordNotFoundException("존재하지 않는 고객입니다...deleteCUstomer() ");
		
	}
	
	public void updateCustomer(CustomerRec cr) throws RecordNotFoundException{
		
		cmd = new Command(Command.UPDATECUSTOMER);
		
		String [] str = {cr.getSsn(), cr.getName(), cr.getAddress()};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status = getResponse();
		
		if(status==-1) throw new RecordNotFoundException("존재하지 않는 고객입니다...deleteCUstomer() ");		
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
