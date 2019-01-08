package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	
	private static final String SERVER_IP="218.39.221.66";
	private static final int SERVER_PORT=5001;
	
	public static void main(String[] args) {
		Socket socket=null;

		try {
			//1. 소켓생성(Client Socket)
			socket=new Socket();
			//키보드사용(표준입력 연결)
			Scanner scanner=new Scanner(System.in);
			
			//2. 서버연결 - 바로accept
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client] connected");
			
			//3.IOStream 받아오기
			//InputStream is=socket.getInputStream();
			//OutputStream os=socket.getOutputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
			
			
			
			
			while (true) {
				// 4.쓰기(전송)
				System.out.print(">>");
				String line=scanner.nextLine();
				if("quit".contentEquals(line)) {
					break;
				}
				pw.println(line);

				// 5.읽기(수신)
				String data = br.readLine();// blocking
				if (data== null) {
					System.out.println("[server] closed by client");
					break;
				}
				//콘솔출력
				System.out.println("<<"+data);

			}
			
			
		} catch (IOException e) {
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
