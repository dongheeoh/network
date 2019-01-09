package echo;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	// PORT번호 설정
	private static final int PORT = 5001;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2. Binding
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			log("binding " + localhostAddress + ":" + PORT);
			
			while (true) {
				// 3. accept
				Socket socket = serverSocket.accept();
				Thread thread = new EchoSeverReceiveThread(socket);
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
