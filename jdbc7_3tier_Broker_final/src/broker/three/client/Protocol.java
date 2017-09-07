package broker.three.client;
/*
 *  Ŭ���̾�Ʈ �� ����� ����ϴ� ��ǥ����
 *  ::
 *  2tier���� Database �ڸ���  �״�� ����־�� �Ѵ�.
 *  ��, ������ Database�� �޼ҵ� ����ΰ� �״�� �־�� �Ѵ�.
 *  (Broker�� �ҽ��ڵ� ������ ���� �ʱ� ���ؼ�)
 *  ::
 *  ����
 *  1. ������ �����ؼ� �������� ����
 *  2. �������κ��� ��Ʈ�� �̾Ƴ��� Command�� ������ | �޾ƾ� �Ѵ�.
 *  3. �޼ҵ� �ȿ����� ����
 *  	1) ���ö� �δ�.
 *  	2) ���������� ������.
 *  	----------------------	
 *  	3) �ٽ� jury �� ���� ���ö� �޴´�
 *  	4) ���ö� ��� ( Data UnPack || getter)
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

	//�ʵ� ����
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
	
	//////////////// �������� ��� ///////////////////////////
	
	public void writeCommand(Command cmd) {
		try {
			oos.writeObject(cmd);
			System.out.println("client writeCommand().....end");
		}catch(IOException e) { //IOException :: Stream ���� exception Write, Read
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
	
	////////////////////// Database�� ������ �޼ҵ尡 ���� //////////////////////
	public void buyShares(String ssn, String symbol, int quantity) throws SQLException{

		// 1. ���ö� �Ѵ�
		cmd = new Command(Command.BUYSHARES);
		String[ ] str = { ssn, symbol,String.valueOf(quantity)};  //String.valueOf(quantity) :: int �� String ���� ����.
		cmd.setArgs(str);
		
		// 2. ���ö� ������ 
		
		writeCommand(cmd);
		/////////////////////////////////////////////////////////////
		
		// 3. ���ö� �޴´�
		getResponse();
					
		
	}
	
	public void sellShares(String ssn, String symbol, int quantity) throws SQLException, RecordNotFoundException, InvalidTransactionException{
		
		cmd = new Command(Command.SELLSHARES);
		String[ ] str = { ssn, symbol,String.valueOf(quantity)};  //String.valueOf(quantity) :: int �� String ���� ����.
		cmd.setArgs(str);
		
		// 2. ���ö� ������ 
		
		writeCommand(cmd);
		/////////////////////////////////////////////////////////////
		
		// 3. ���ö� �޴´�
		int status=getResponse();
		if(status==-1) throw new RecordNotFoundException("�ȷ��� �ֽ��� ���....sellShares() ");
		else if(status==-3) throw new InvalidTransactionException("�ȷ��� �ֽ��� �� ���ƿ�...sellShares");
		
	}
	
	public ArrayList<StockRec> getAllStocks(){
		
		ArrayList<StockRec> list = new ArrayList<StockRec>();
		
		cmd = new Command(Command.GETALLSSTOCK);

		writeCommand(cmd);
		///////////////////////////////////////////////////
		
		int status=getResponse(); //���ö��� �޾Ҵ�.
		
		list = (ArrayList)cmd.getResults().get(0); // ArrayList ĭ�� 1ĭ�̶� ���ڰ��� 0
		
			
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
		
		String [] str = {cr.getSsn(), cr.getName(), cr.getAddress()}; // String���� �־���� new CustomerRec �̰Ŵ� Ÿ���� CustomerRec�϶���
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status = getResponse();
		
		if(status==-2) throw new DuplicateSSNException("�̹� �߰��� ���Դϴ�... addCustomer()");
			
	}
	
	
	public void deleteCustomer(String ssn) throws RecordNotFoundException{
		
		cmd = new Command(Command.DELETECUSTOMER);
		
		String [] str = {ssn};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status = getResponse();
		
		if(status==-1) throw new RecordNotFoundException("�������� �ʴ� ���Դϴ�...deleteCUstomer() ");
		
	}
	
	public void updateCustomer(CustomerRec cr) throws RecordNotFoundException{
		
		cmd = new Command(Command.UPDATECUSTOMER);
		
		String [] str = {cr.getSsn(), cr.getName(), cr.getAddress()};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status = getResponse();
		
		if(status==-1) throw new RecordNotFoundException("�������� �ʴ� ���Դϴ�...deleteCUstomer() ");		
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
