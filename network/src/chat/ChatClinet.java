package chat;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClinet {

	private static final String SERVER_IP="218.39.221.66";
	private static final int SERVER_PORT=5001;
//	private static final String SERVER_IP="218.39.221.67";
//	private static final int SERVER_PORT=5000;
	public static void main(String[] args) {
		Scanner scanner = null;
		Socket socket = null;
		PrintWriter pw=null;
		

		
		try {
			//1. Scanner 생성(표준입력 연결)
			scanner = new Scanner(System.in);
			
			//2. 소켓 생성
			socket = new Socket();

			//3. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			//4. IOStream 생성(받아오기)

			 pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			 //5.join프로토콜
			 System.out.print("닉네임>>");
			 String nickname=scanner.nextLine();
			 pw.println("join:"+nickname);
			 pw.flush();
			 
			 
			 
			while(true) {
				//6.ChatClientReceiveThread시작
				 Thread thread = new ChatClientThread(socket);
				 thread.start();
				
				String input = scanner.nextLine();
				
				if("quit".contentEquals(input)) {
					pw.println("quit");
					pw.flush();
					break;
				}else {
					//message처리
					pw.println("message:"+nickname+":"+input);
				}
				
				//6. 데이터 쓰기(전송)
				
				
				
			}
		} catch (IOException e) {
			log("error:" + e);
		} finally {
			try {
				if(scanner != null) {
					scanner.close();
				}
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				log("error:" + e);
			}
		}
	}

	private static void log(String log) {
		System.out.println("[client] " + log);
	}


}
