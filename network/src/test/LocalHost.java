package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddres= InetAddress.getLocalHost();
			
			String hostname =inetAddres.getHostName();
			String hostAdress=inetAddres.getHostAddress();
			System.out.println(hostname+" : "+hostAdress);
			
			byte[] addresses=inetAddres.getAddress();
			for(byte address:addresses) {
				System.out.print(address&0x000000ff);
				System.out.print(".");
			}
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}

	}

}
