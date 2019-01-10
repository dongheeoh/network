package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class ChatSeverThread extends Thread {

	private String nickname; //닉네임
	private Socket socket;
	List<Writer> listWriters; //공유객체를 위한 참조변수
	BufferedReader br=null;
	PrintWriter pw=null;
	
	public ChatSeverThread(Socket socket, List<Writer> listWriters) {
		this.socket=socket;
		this.listWriters=listWriters;
	}
	//서버에서 여러 클라이언트를 접속 시키기위해 Thread의 run메소드 사용
	@Override
	public void run() {
		
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		ChatServer.log("connected by client[" + inetRemoteSocketAddress.getAddress().getHostAddress() + ":" + inetRemoteSocketAddress.getPort() + "]");
		
		try {
			//4. IOStream 생성(받아오기)
			 br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			 pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			while(true) {
				//5. 데이터 읽기(수신)
				String data = br.readLine();
				if(data == null) {
					ChatServer.log("closed by client");
					doQuit(pw);
					break;
				}
				ChatServer.log("received:" + data);
				
				//6. 데이터 쓰기(전송)
				//pw.println(data);
				
				//6.프로토콜 분석 설계
				String[] tokens=data.split(":");
				
				if("join".equals(tokens[0])) {
					doJoin(tokens[1],pw);
				}else if("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])) {
					doQuit(pw);
				}else {
					ChatServer.log("에러:알수없는 요청("+tokens[0]+")");
				}
				
			}
		} catch(SocketException e) {
			ChatServer.log("abnormal closed by client");
		} catch (IOException e) {
			ChatServer.log("error:" + e);
		} finally {
			try {
				//7. 자원정리(소켓 닫기)
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch(IOException e) {
				ChatServer.log("error:" + e);
			}
		}
	}
	
	private void doJoin(String nickName,Writer writer) {
		this.nickname=nickName;
		
		String data=nickName+"님이 참여하였습니다.";
		broadcast(data);
		
		//writer pool에 저장
		addWriter(writer);
		//ack응답
		pw.println("join:ok");
		pw.flush();
	}
	private void doMessage(String message) {
		broadcast(message);
		pw.flush();
		
	}
	private void doQuit(Writer writer) {
		removeWriter(writer);
		
		String data=nickname+"님이 퇴장 하였습니다.";
		broadcast(data);
	}
	//list에 추가 시키기위함
	private void addWriter(Writer writer) {
		synchronized(listWriters) {
			listWriters.add(writer);
		}
	}
	private void removeWriter(Writer writer) {
		synchronized(listWriters) {
			listWriters.remove(writer);
		}
	}
	private void broadcast(String data) {
		//리스트로 메세지를 보낼수 있도록 브로드캐스팅을 만들어준다
		
		synchronized(listWriters) {
			for(Writer writer:listWriters) {
				//printWrite 객체를 사용하여 다른 스레드의 IOstream사용
				PrintWriter printWriter=(PrintWriter)writer;
				printWriter.println(data);
				printWriter.flush();//pw에서 auto를true해주었기에 안써도 상관x
			}
		}
	}
	

}
