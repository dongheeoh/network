package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		
		try {
			while (true) {
				System.out.print(">");
				String line = scanner.nextLine();
				if("exit".equals(line)) {
					break;
				}
				InetAddress[] inetAddress = InetAddress.getAllByName(line);
				if (line != null) {
					for (InetAddress a : inetAddress) {
						System.out.println(a);
					}
				}
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
