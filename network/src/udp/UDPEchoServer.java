package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
	public static final int PORT=5000;
	public static final int BUFFER_SIZE=1024;
	public static void main(String[] args) {
		DatagramSocket socket=null;
		
		try {
			//1. 소켓생성
			socket = new DatagramSocket(PORT);
			while (true) {
				// 2.데이터수신
				DatagramPacket receiverPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receiverPacket);

				byte[] data = receiverPacket.getData();
				int length = receiverPacket.getLength();
				String message = new String(data, 0, length, "UTF-8");
				System.out.println("[server] received : " + message);
				
				
				//3.데이터전송
				byte[] sendData=message.getBytes("UTF-8");
				DatagramPacket sendPacket=new DatagramPacket(sendData, sendData.length,receiverPacket.getAddress(),receiverPacket.getPort());
				socket.send(sendPacket);
						
			}
			 
		} catch (SocketException e) {
			e.printStackTrace();
			
		}catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(socket!=null && socket.isClosed()!=false) {
				socket.close();
			}
			
		}
		
		

	}

}
