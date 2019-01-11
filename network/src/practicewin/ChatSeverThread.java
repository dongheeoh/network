package practicewin;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.io.PrintWriter;

import java.net.Socket;

import java.nio.charset.StandardCharsets;

import java.util.List;

public class ChatSeverThread extends Thread {
	private String nickname = null;
	private Socket socket = null;
	//ChatServer에 있는 writePool 리스트를 사용하기 위함
	List<PrintWriter> listWriters = null;
	
	//1.생성자 생성(socket과 listWriters)정보 얻기위함
	public ChatSeverThread(Socket socket, List<PrintWriter> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		try {
			BufferedReader buffereedReader =new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

			while (true) {
				String request = buffereedReader.readLine();
				if (request == null) {
					consoleLog("클라이언트로부터 연결 끊김");
					doQuit(printWriter);
					break;
				}

				String[] tokens = request.split(":");
				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], printWriter);
				}
				else if ("message".equals(tokens[0])) {
					if("개새끼".equals(tokens[1]) || "시발".equals(tokens[1]) || "좃까".equals(tokens[1]))
					{
						doMessage("바르말 고운말 사용합시다!");
					}else {
					doMessage(tokens[1]);
					}
				}
				else if ("quit".equals(tokens[0])) {
					doQuit(printWriter);
				}
			}
		}catch (IOException e) {
			consoleLog(this.nickname + "님이 채팅방을 나갔습니다.");
		}

	}

	private void doQuit(PrintWriter writer) {
		removeWriter(writer);//나가면 list에서 삭제 해줘야함
		String data = this.nickname + "님이 퇴장했습니다.";
		broadcast(data); //퇴장했다는 표시 띄움
	}

	private void removeWriter(PrintWriter writer) {
		synchronized (listWriters) { //동기화시켜줘야함 -> arraylist는 자동동기화가 없으므로
			listWriters.remove(writer); 
		}
	}

	private void doMessage(String data) {
		broadcast(this.nickname + ":" + data); //메세지를  브로드캐스트로 넘겨줌
	}

	private void doJoin(String nickname, PrintWriter writer) {
		this.nickname = nickname;
		String data = nickname + "님이 입장하였습니다.";
		broadcast(data);
		// writer pool에 저장
		addWriter(writer);
	}
	private void addWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}
	private void broadcast(String data) { //저장되있는곳 쭉보여줌
		synchronized (listWriters) {
			for (PrintWriter writer : listWriters) {
				writer.println(data);
				writer.flush();
			}
		}
	}
	private void consoleLog(String log) {

		System.out.println(log);
	}
}
