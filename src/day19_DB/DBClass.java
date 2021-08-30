package day19_DB;

import java.sql.Connection; //sql connection을 가져옴.
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBClass {
	private String url;
	private String id;
	private String pwd;
	private Connection con;
	//오라클의 기능을 자바에서 사용하기 위함. 무조건 처음 실행시켜줌.
	public DBClass() {
		try {
			// 자바에서 오라클에 연결할 수 있도록 도와주는 라이브러리를 등록하는 것.
			Class.forName("oracle.jdbc.driver.OracleDriver");//드라이브 로드
			url = "jdbc:oracle:thin:@localhost:1521:xe";//오라클이 사용하는 포트넘버
			id = "seohee"; //Oracle 접속할 때 Id, Pw
			pwd = "789";
			//연결된 객체 얻어오기.
			con = DriverManager.getConnection(url,id,pwd); //오라클에 대한 정보.
			System.out.println(con);
		} catch (Exception e) {
			e.printStackTrace();
			//처음 드라이브를 로드하고 try ~catch로 묶은 뒤에 실행했을 때 오류는 해당 드라이버가 없기 때문.
			//ojdbc 연결. (package> properties> java Build path> library> class path> add external> ojdbc)
		}

	}
	/*
	 * 1. 드라이브 로드(오라클 기능 사용)
	 * 2. 연결된 객체를 얻어온다.
	 * 3. 연결된 객체를 이용해서 명령어(쿼리문)을 전송할 수 있는 전송 객체를 얻어온다.
	 * 4. 전송 객체를 이용해서 데이터베이스에 전송 후 결과를 얻어온다. 
	 * 5. 얻어온 결과는 int또는 ResultSet으로 받는다.
	 */

	public ArrayList<StudentDTO> getUsers() {
		ArrayList<StudentDTO> list= new ArrayList<StudentDTO>();
		String sql = "select * from newst"; //db로 전송됨. sql쿼리문 모든 데이터 보기.
		try {
			PreparedStatement ps = con.prepareStatement(sql);//(3.)전송객체를 얻어온 것. 연결된 객체(con)를 이용해서 명령어(select~)를 실행할 수 있는 실행객체(ps)로 받아옴.
			ResultSet rs = ps.executeQuery();//ps명령어 실행. ps와 con, con과 db 연결된 것.
			//ResultSet 자료형은 Itorator와 비슷하다고 생각할 수 있음. 
			while(rs.next()) {
				StudentDTO dto = new StudentDTO();
				dto.setStNum(rs.getString("id")); //sql에서 컬럼명 작성. id, name, age
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));
				list.add(dto); //저장된 데이터가 계속 있을 수 있기 때문에. 

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list; //실행 하면 sql에 있는 데이터가 잘 나옴.
	}
	
	public int saveData(String stNum, String name, int age) {
		//insert into newst values('111', '홍길동', 20);
		String sql = 
				"insert into newst values('"+stNum+"', '"+name+"',"+age+")";
		int result = 0;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			//ResultSet rs = ps.executeQuery(); //이렇게 써도 문제는 업지만 보통은 Update를 많이 씀.
			//저장 성공시 1을 반환, 실패시 catch이동이나 0을 반환
			result = ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace(); //문제가 뭔지 알 수 있기 때문에 필요함.
		}
		return result;
	}
	

	public int saveData02(String stNum, String name, int age) {
		//insert into newst values('111', '홍길동', 20); 데이터 베이스에선 이렇게 씀.
		String sql = "insert into newst values(?, ?, ?)";
		int result = 0;
		try {
			PreparedStatement ps = con.prepareStatement(sql);//역시 전송객체를 가져옴.
			ps.setString(1, stNum);
			ps.setString(2, name);
			ps.setInt(3, age);
			//ResultSet rs = ps.executeQuery(); resultset은 데이터를 통으로 가져올 때 사용함.
			//저장 성공시 1을 반환, 실패시 carch이동이나 0을 반환
			result = ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace(); //문제가 뭔지 알 수 있기 때문에 필요함.
		}
		return result;
	}
	public int delete(String userNum) {
		int result = 0;
		//delete from newst where id = 'userNum';
		String sql = "delete from newst where id = '"+userNum+"'";  //방법1
		//String sql = "delete from newst where id = ?";			//방법2
		try {
			PreparedStatement ps = con.prepareStatement(sql);//실행객체 불러오기
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public int modify(String stNum, String name, int age) {
		int result = 0;
		//update newst set name = '홍길동', age = 20 where id = 'test';
		String sql = "update newst set name=?, age=? where id =? ";
	

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, stNum);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}	
