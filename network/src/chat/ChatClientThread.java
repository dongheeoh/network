package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClientThread extends Thread {
	BufferedReader br = null;
	private Socket socket;

	public ChatClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			while (true) {
				// 7. 데이터 읽기(수신)
				String data;
				data = br.readLine();
				if (data == null) {
					System.out.println("closed by server");
					break;
				}
				// 8. 콘솔 출력
				System.out.println(">>"+data);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("종료되었습니다.");
			e.printStackTrace();
		}

	}

}
