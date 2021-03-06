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
		String login = null;
		String ch, chosen_user, katalog;
		final int PORT = 50000;
		final int[] PORTS_CONN= {50001,50002,50003,50004,50005,50006,50007,50008,50009};
		
		Scanner src = new Scanner(System.in);
		int choice=0;

		try {
			
			InetAddress address = InetAddress.getByName("127.0.0.1"); 
			socket = new DatagramSocket();
			socket.setBroadcast(true); //-------------------b-cast set
			msg="";
			outBuf=msg.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wysĹ‚anie prosby o polaczenie
			socket.send(outPacket);
			socket.setSoTimeout(1000);
			
			System.out.println("Choose your login:");
			login=src.nextLine();
			outBuf=login.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wysĹ‚anie loginu
			socket.send(outPacket);
			
			System.out.println("Choose a directory you wish to share (for example on Windows C:\\\\Users\\\\ or on Linux /media/):");
			katalog=src.nextLine();
			outBuf=katalog.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wysĹ‚anie katalogu
			socket.send(outPacket);
			
			inBuf = new byte[10000];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			String whichport = new String(inPacket.getData(), 0, inPacket.getLength()); //odebranie na ktory port wysylac
			int whichportchoice = Integer.valueOf(whichport);
			int NEWPORT=PORTS_CONN[whichportchoice];
			System.out.println("Connecting to PORT "+NEWPORT+"\n");
			
			
			while(true) {
				System.out.println("MENU:");
				System.out.println("1 - File download");
				System.out.println("2 - Number of logged in users");
				System.out.println("3 - Log out and exit");
				
				//wysylanie wyboru dla switcha do serwera:
				System.out.println("Choose from menu:");
				ch=src.nextLine();
				
				choice=Integer.parseInt(ch);
				outBuf=ch.getBytes();
				outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,NEWPORT); //wysĹ‚anie wyboru
				socket.send(outPacket);
				switch(choice) {
				case 1:
					
					inBuf = new byte[50000];
					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie listy userow
					socket.receive(inPacket);
					String receivedlist = new String(inPacket.getData(), 0, inPacket.getLength());
					System.out.println(receivedlist);
					
					System.out.println("Choose a client name from the list:");
					chosen_user=src.nextLine();
					outBuf=chosen_user.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,NEWPORT); //wysĹ‚anie wyboru uzytkownika
					socket.send(outPacket);	
					
					inBuf = new byte[10000];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					String data = new String(inPacket.getData(), 0, inPacket.getLength()); //LISTA PLIKĂ“W PRZYCHODZI DO KLIENTA
					//Printing file list
					System.out.println(data);
					
					
					//Send file name and download the specified files
					String filename = src.nextLine();
					outBuf = filename.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length,address,NEWPORT);
					socket.send(outPacket);
					
					//Recieve number of packets
					inBuf = new byte[10000];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					String rcv = new String(inPacket.getData(), 0, inPacket.getLength());
					double no_of_pkt = Double.parseDouble(rcv);
					
					
					//Receive file
					FileOutputStream fos = new FileOutputStream(filename);
					StringBuilder sb1 = new StringBuilder();
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					
					for(double i=0; i <= no_of_pkt; i++) {
						
						byte [] b = new byte[1024];
						bos.write(b, 0, b.length);
						System.out.println("Packet: " + (i+1));
						inPacket = new DatagramPacket(b, b.length);
						socket.receive(inPacket);
						String file = new String(inPacket.getData(), 0, inPacket.getLength());
						sb1.append(file);
						Thread.sleep(10L);
						
					}
					
					data = sb1.toString();
					
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
							
							System.out.println("FIle writing successful");
							
						}
						catch(IOException ioe) {
							System.out.println("File error\n");
							socket.close();
						}
					}
				break;
				case 2:
					
					inBuf = new byte[10000];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					String sizee = new String(inPacket.getData(), 0, inPacket.getLength()); //odebranie rozmiaru listy
					int size = Integer.valueOf(sizee);
					String tmp;
					StringBuilder sb = new StringBuilder();
					
					for(int i=0;i<size;i++) {
						inBuf = new byte[60000];
						inPacket = new DatagramPacket(inBuf, inBuf.length); 
						socket.receive(inPacket);
						tmp = new String(inPacket.getData(), 0, inPacket.getLength());
						sb.append(tmp+"\n");
					}
					System.out.println("Number of logged in users: ");
					System.out.println(sb.toString());
					
					break;
				case 3:
					System.out.println("Logging out...");
					socket.close();
					System.exit(0);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Network error, please try again.\n");
		}
			
	}
}
