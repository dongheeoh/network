package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {

	private static final String SERVER_IP="218.39.221.66";
	private static final int SERVER_PORT=5000;
	
	public static void main(String[] args) {
		Socket socket=null;

		try {
			//1. 소켓생성(Client Socket)
			socket=new Socket();
			
			//1-1 socket buffer size확인
			int receiveBufferSize=socket.getReceiveBufferSize();
			int sendBufferSize=socket.getSendBufferSize();
			
			System.out.println(receiveBufferSize+" : "+sendBufferSize);
			
			//1-2 socket buffer size 변경
			socket.setReceiveBufferSize(1024*10);
			socket.setSendBufferSize(1024*10);
			
			 receiveBufferSize=socket.getReceiveBufferSize();
			 sendBufferSize=socket.getSendBufferSize();
			
			//1-3. SO_NODELAY ( Nagle알고리즘을 off) 딜레이 없이 꺼내겠다 / 안전하게 전송x 속도만
			 socket.setTcpNoDelay(true);
			 
			//1-4. SO_TIMEOUT
			socket.setSoTimeout(1);
			 
			System.out.println(receiveBufferSize+" : "+sendBufferSize);
			
			//2. 서버연결 - 바로accept
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client] connected");
			
			//3.IOStream 받아오기
			InputStream is=socket.getInputStream();
			OutputStream os=socket.getOutputStream();
			
			//4.쓰기
			String data="Hello world\n";
			os.write(data.getBytes("UTF-8"));
			
			//5.읽기
			byte[] buffer=new byte[255];
			int readByteCount= is.read(buffer); //Blocking
			if(readByteCount==-1) {
				System.out.println("[client] closed by server");
				return;
			}
			
			data=new String(buffer,0,readByteCount,"UTF-8");
			System.out.println("[client] recieved :"+data);
			
			
			
		}catch(SocketTimeoutException e){
			System.out.println("[client] Time Out");
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket!=null && socket.isClosed()==false) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
