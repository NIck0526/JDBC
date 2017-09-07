package broker.three.vo;

import java.io.Serializable;
import java.util.Vector;

/*
 * customer  ���̺� ���� ������ �����ϴ� VO Ŭ����
 * �׷����� �ұ��ϰ�, �ֽĽ��ڿ����� ���̱⿡ 
 * �ֽ��� ��� �� �� �ִ� ���� �Ǿ�� �Ѵ�.
 * ::
 * ���� ũ�� �� �η��� ���� �� ���� ���̴�.
 * 1) �ֽ��� �������� �ʴ� ��
 * 2) �ֽ��� ������ ��
 */
public class CustomerRec implements Serializable {
	private String ssn;
	private String name; // �÷���� �ٸ�
	private String address;
	
	private Vector<SharesRec> portfolio;

	
	//�ֽ��� �����ϰ� �ִ»��
	public CustomerRec( String ssn, String name, String address, Vector<SharesRec> portfolio) {
		super();
		this.ssn = ssn;
		this.name = name;
		this.address = address;
		this.portfolio = portfolio;
	}

	//�ֽ��� �����ϰ� ���� �ʴ»��
	public CustomerRec( String ssn, String name, String address) {
		this(ssn, name, address, null);
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn (String ssn) {
		this.ssn = ssn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Vector<SharesRec> getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Vector<SharesRec> portfolio) {
		this.portfolio = portfolio;
	}

	@Override
	public String toString() {
		return "Customer [ssn=" + ssn + ", name=" + name + ", address=" + address + ", portfolio=" + portfolio + "]";
	}
	
	
	
	
	
	
	
	
	
}
