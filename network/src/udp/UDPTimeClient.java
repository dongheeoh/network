package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UDPTimeClient {
	private static final String SERVER_IP="218.39.221.66" ;
	
	public static void main(String[] args) {
	
		DatagramSocket socket=null;
		try {
			//소켓 생성
			socket=new DatagramSocket();
			
			
				//메세지 전송
				byte[] data=UDPTimeServer.message.getBytes("UTF-8");
				DatagramPacket sendPacket=new DatagramPacket(data, data.length,new InetSocketAddress(SERVER_IP,UDPEchoServer.PORT));
				socket.send(sendPacket);
		
		//5.메세지 수신
		DatagramPacket receivePacket=new DatagramPacket(new byte[UDPEchoServer.BUFFER_SIZE], UDPEchoServer.BUFFER_SIZE);
		socket.receive(receivePacket);
		
		String message = new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
		System.out.println("<<"+message);
			
		
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			//자원정리
			if(socket!=null && socket.isClosed()==false) {
				socket.close();
			}
		}

	}

}
