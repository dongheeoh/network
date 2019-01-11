package practicewin;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class ChatClientThread extends Thread {
	Socket socket = null;
	TextArea textArea;

	ChatClientThread(Socket socket, TextArea textArea) {
		this.socket = socket;
		this.textArea = textArea;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			while (true) {
				String msg = br.readLine();
				textArea.append(msg);
				textArea.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
