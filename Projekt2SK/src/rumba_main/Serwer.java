//Sends all types of files within UDP Packe size limit

package rumba_main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Serwer {
	
	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		DatagramPacket inPacket = null; //recieving packet
		DatagramPacket outPacket = null; //sending packet
		byte[] inBuf, outBuf;
		String msg;
		final int PORT = 50000;
		
		try {
			
			socket = new DatagramSocket(PORT);
			
			while(true) {
				
				System.out.println("\nRunning...\n");
				
				inBuf = new byte[100];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				
				int source_port = inPacket.getPort();
				InetAddress source_address = inPacket.getAddress();
				msg = new String(inPacket.getData(), 0, inPacket.getLength());
				System.out.println("Client: "+source_address + ":" + source_port);
				
				String dirname="/media/";
				File f1=new File(dirname);
				File fl[]=f1.listFiles();
				
				StringBuilder sb = new StringBuilder("\n");
				int c = 0;
				
				for(int i=0; i<fl.length; i++) {
					
					if(fl[i].canRead())
						c++;
				}
				
				sb.append(c+" files found.\n\n");
				
				for(int i=0; i<fl.length; i++) {
					
					sb.append(fl[i].getName()+" "+fl[i].length()+ " Bytes\n");
					
				}
				
				sb.append("\nEnter file name for download: ");
				outBuf = (sb.toString()).getBytes();
				outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
				socket.send(outPacket);
				
				inBuf = new byte[100];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				String filename = new String(inPacket.getData(), 0, inPacket.getLength());
				
				System.out.println("Requested file: "+filename);
				
				boolean flis = false;
				int index = -1;
				sb = new StringBuilder("");
				for(int i=0; i<fl.length; i++) {
					
					if(((fl[i].getName()).toString()).equalsIgnoreCase(filename)){
						
						index=i;
						flis=true;
						
					}
					
				}
				
				if(!flis){
					
					System.out.println("ERROR");
					sb.append("ERROR");
					outBuf = (sb.toString()).getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
					socket.send(outPacket);
					
				}
				else{
					
					try {
						
						//File Send Process, Independent
						File ff = new File(fl[index].getAbsolutePath());
						FileReader fr = new FileReader(ff);
						BufferedReader brf = new BufferedReader(fr);
						String s = null;
						sb = new StringBuilder();
						
						while((s = brf.readLine())!=null) {
							
							sb.append(s);
							
						}
						
						if(brf.readLine()==null) {
							
							System.out.println("File Read Successful. Closing Socket.");
							
						}
						
						outBuf = new byte[100000];
						outBuf = (sb.toString()).getBytes();
						outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
						socket.send(outPacket);
						
					}
					catch(IOException ioe) {
						
						System.out.println(ioe);
						
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println("ERROR\n");
		}
	}
}
