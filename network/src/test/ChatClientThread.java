package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientThread extends Thread{
	private BufferedReader bufferedReader;
	private Socket socket;
	private ChatWindow chatWindow;
	
	public ChatClientThread(ChatWindow chatWindow, Socket socket) {
		this.socket = socket;
		this.chatWindow = chatWindow;
	}

	@Override
	public void run() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				while(true) {
					String data = bufferedReader.readLine();
					chatWindow.getTextArea().append(data);
					chatWindow.getTextArea().append("\n");				
				}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
