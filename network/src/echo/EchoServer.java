package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	// PORT번호 설정
	private static final int PORT = 5001;

	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		try {
		//1.서버소켓 생성
		serverSocket=new ServerSocket();
		//2.binding (SocketAddress(IPAddress+Port)를 바인딩
		InetAddress inetAddress=InetAddress.getLocalHost();
		String localhostAddress=inetAddress.getHostAddress();
		
		serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
		System.out.println("[server] binding"+localhostAddress+":"+PORT);
		
		//3.accept(클라이언트로 부터 연결 요청을 기다린다.)
		Socket socket=serverSocket.accept();
		
		//remoteSocketAddress 얻기
		InetSocketAddress inetRemoteSocketAddress=(InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress=inetRemoteSocketAddress.getAddress().getHostAddress();
		int remotePort=inetRemoteSocketAddress.getPort();
		
		System.out.println("[server] connected by client["+remoteHostAddress+" : "+remotePort+"]");
		
		try {
			//4.IOstream 받아오기
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			//InputStream is=socket.getInputStream();
			//OutputStream os=socket.getOutputStream();
			
			while(true) {
				//5.데이터 읽기
				
				String data=br.readLine();//blocking
				if(data==null) {
					
					System.out.println("[server] closed by client");
					break;
				}
				

				System.out.println(">>"+data);
				
				//6.데이터쓰기
				//os.write(data.getBytes("UTF-8"));
				pw.println(data);
			}
			
		}catch(SocketException e) {
			System.out.println("[server] abnomal closed by client");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			//자원정리
			try {
				if(socket!=null && socket.isClosed()==false) {
					socket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(serverSocket!=null && serverSocket.isClosed()==false) {
					serverSocket.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
