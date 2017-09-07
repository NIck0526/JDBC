package broker.three.shares;
/*
 * 한 칸짜리 빈 깡통
 * ::
 * 나중에 서버쪽에서 반환객체와 상태코드르 여기에 집어넣어줄것.
 */
import java.util.ArrayList;

public class Result extends ArrayList{
	
	private int status = -1;  //0은 정상, 성공 | 음수는 비저상, 실패
	
	public Result() {
		super(1); //한칸짜리 ArrayList
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	

}
