package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	public static final int PORT = 5000;
	public static final int BUFFER_SIZE = 1024;
	public static String message="";
	public static void main(String[] args) {
		DatagramSocket socket = null;
		Date d=new Date();
		
		
		try {
			// 1.소켓생성
			socket = new DatagramSocket(PORT);
			
			//시간생성
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String date=transFormat.format(d);
			while (true) {
				
				
				// 2.데이터수신
				DatagramPacket receiverPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receiverPacket);

				
				message = date;
				System.out.println("[server] time now :" + message);
				
				
				// 3.데이터전송
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receiverPacket.getAddress(),
						receiverPacket.getPort());
				socket.send(sendPacket);
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}

}
