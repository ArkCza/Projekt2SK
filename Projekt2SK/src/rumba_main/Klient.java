package rumba_main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Klient {
	
	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		DatagramPacket inPacket = null; //recieving packet
		DatagramPacket outPacket = null; //sending packet
		byte[] inBuf, outBuf;
		String msg = null;
		final int PORT = 50000;
		Scanner src = new Scanner(System.in);
		
		try {
			
			InetAddress address = InetAddress.getByName("127.0.0.1");
			socket = new DatagramSocket();
			
			msg="";
			outBuf=msg.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address, PORT);
			socket.send(outPacket);
			
			inBuf = new byte[65535];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			
			String data = new String(inPacket.getData(), 0, inPacket.getLength());
			//Printing file list
			System.out.println(data);
			
			//Send file name
			String filename = src.nextLine();
			outBuf = filename.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address, PORT);
			socket.send(outPacket);
			
			//Receive file
			inBuf = new byte[100000];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			
			data=new String(inPacket.getData(), 0, inPacket.getLength());
			if(data.endsWith("ERROR")) {
				System.out.println("File does not exist");
				socket.close();
			}
			else {
				try {
					BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
					pw.write(data);
					//Force write buffer to file
					pw.close();
					
					System.out.println("FIle writing successful, closing socket");
					socket.close();
				}
				catch(IOException ioe) {
					System.out.println("File error\n");
					socket.close();
				}
			}
		}
		catch(Exception e) {
			System.out.println("Network error, please try again.\n");
		}
	}
}
