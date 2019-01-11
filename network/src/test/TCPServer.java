package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class TCPServer {
	private static final int PORT=5000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket=null;
		
		try {
			// 1. 서버소켓 생성
			serverSocket=new ServerSocket();
			// 2. bindin : (Socket에 SocketAddress(IPAddress+ port)를 바인딩한다.
			InetAddress inetAddress=InetAddress.getLocalHost();
			String localhostAddress=inetAddress.getHostAddress();
			
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT));
			System.out.println("[server] binding "+localhostAddress+" : "+PORT);
			
			
			//3. accept(클라이언트로 부터 연결 요청을 기다린다.)
			Socket socket= serverSocket.accept(); //blocking
			
			//remoteSocketAddress얻기
			InetSocketAddress inetRemoteSocketAddress= (InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteHostAddress=inetRemoteSocketAddress.getAddress().getHostAddress();
			int remotePort=inetRemoteSocketAddress.getPort();
			
		
			System.out.println("[server] connected by client["+remoteHostAddress+" : "+remotePort+"]");
			
			try {
			//4.IOStream 받아오기
			InputStream is=socket.getInputStream();
			OutputStream os=socket.getOutputStream();
			
			while(true) {
			//5.데이터 읽기
				byte[] buffer=new byte[256];
				int readByteCount= is.read(buffer); //blocking
				if(readByteCount==-1) {
					//정상종료 : remotSocket close()
					//메소드를 통해서 정상적으로 소켓을 닫은 경우
					System.out.println("[server] closed by client");
					break;
				}
			
				String data= new String(buffer,0,readByteCount,"UTF-8");
				System.out.println("[server] received :"+data); //echo bufferdRead는 "\n"떨어져있음
			
				//6. 데이터쓰기
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				os.write(data.getBytes("UTF-8"));
			}
			}catch(SocketException e) {
				System.out.println("[server] abnomal closed by client");
			}
			catch(IOException e) {
				e.printStackTrace();
			}finally {
				//7. 자원정리(소켓 닫기)
				try {
					if(socket!=null && socket.isClosed()==false) {
						socket.close();
					}
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(serverSocket!=null && serverSocket.isClosed()==false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
