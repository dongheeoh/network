package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private static final int PORT = 5001;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		//PrintWriter에 사용할 List생성
		List<Writer> listWriters=new ArrayList<Writer>();
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			
			//2. Binding
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			log("연결기다림 " + localhostAddress + ":" + PORT);
			
			while (true) {
				// 3. accept
				Socket socket = serverSocket.accept();
				Thread thread = new ChatSeverThread(socket,listWriters);
				thread.start();
			}
			
			
		} catch (IOException e) {
			log("error:" + e);
		} finally {
			try {
				if( serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				log("error:" + e);
			}
		}
	}
	
	 static void log(String log) {
		System.out.println("[server"+Thread.currentThread().getId()+"] " + log);
	}

}
