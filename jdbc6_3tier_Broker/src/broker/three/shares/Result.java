package broker.three.shares;
/*
 * �� ĭ¥�� �� ����
 * ::
 * ���߿� �����ʿ��� ��ȯ��ü�� �����ڵ帣 ���⿡ ����־��ٰ�.
 */
import java.util.ArrayList;

public class Result extends ArrayList{
	
	private int status = -1;  //0�� ����, ���� | ������ ������, ����
	
	public Result() {
		super(1); //��ĭ¥�� ArrayList
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	

}
