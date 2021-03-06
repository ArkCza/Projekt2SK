package rumba_threaded;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.util.ArrayList;

public class Klient {
	public static void main(String[] args) {
		DatagramSocket socket = null;
		DatagramPacket inPacket = null; //recieving packet
		DatagramPacket outPacket = null; //sending packet
		byte[] inBuf, outBuf;
		String msg = null;
		String login = null;
		String oneuser = null;
		String ch,rec,chosen_user,chosen_address,katalog;
		final int PORT = 50000;
		final int[] PORTS_CONN= {50001,50002,50003,50004,50005,50006,50007,50008,50009};
		List<String> users = null;
		List<String> logins = new ArrayList<String>();
		
		Scanner src = new Scanner(System.in);
		StringBuilder sbuild;
		int choice,rozm,chosen_port=0;
		//while(true) {
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1"); 
			socket = new DatagramSocket();
			socket.setBroadcast(true); //-------------------b-cast set
			msg="";
			outBuf=msg.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wys�anie prosby o polaczenie
			socket.send(outPacket);
			socket.setSoTimeout(1000);
			
			System.out.println("Choose your login:");
			login=src.nextLine();
			outBuf=login.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wys�anie loginu
			socket.send(outPacket);
			
			System.out.println("Choose a directory you wish to share:");
			katalog=src.nextLine();
			outBuf=katalog.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wys�anie katalogu
			socket.send(outPacket);
			
			inBuf = new byte[10000];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			String whichport = new String(inPacket.getData(), 0, inPacket.getLength()); //odebranie na ktory port wysylac
			int whichportchoice = Integer.valueOf(whichport);
			int NEWPORT=PORTS_CONN[whichportchoice];
			System.out.println("Connecting to "+NEWPORT);
			
			
			while(true) {
				System.out.println("MENU:");
				System.out.println("1 - File download");
				System.out.println("2 - Show user list");
				System.out.println("3 - Log out and exit");
				
				//wysy�am wybor dla switcha do serwera:
				System.out.println("Choose from menu:");
				ch=src.nextLine();
				
				choice=Integer.parseInt(ch);
				outBuf=ch.getBytes();
				outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,NEWPORT); //wys�anie wyboru
				socket.send(outPacket);
				switch(choice) {
				case 1:
					
					//WSTAWKA DLA WIELU UZYTKOWNIKOW!!!!!!!!
					///////////////////
					/*inBuf = new byte[1000];
					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie rozmiaru
					socket.receive(inPacket);
					rec = new String(inPacket.getData(), 0, inPacket.getLength());
					System.out.println(rec);
					rozm=Integer.parseInt(rec);
					System.out.println(rozm);
					System.out.println("Lista uzytkownikow:\n");*/
					
					//for(int i=0;i<rozm;i++) {
					inBuf = new byte[50000];
					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie listy userow
					socket.receive(inPacket);
					String receivedlist = new String(inPacket.getData(), 0, inPacket.getLength());
					System.out.println(receivedlist);
					
					System.out.println("Wybierz uzytkownika (nick na liscie): \n");
					chosen_user=src.nextLine();
					outBuf=chosen_user.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,NEWPORT); //wys�anie wyboru uzytkownika
					socket.send(outPacket);
					
					/*inBuf = new byte[1000];
					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie adresu 
					socket.receive(inPacket);
					chosen_address = new String(inPacket.getData(), 0, inPacket.getLength());*/
					
					/*inBuf = new byte[1000];
					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie portu
					socket.receive(inPacket);
					String chosen_p = new String(inPacket.getData(), 0, inPacket.getLength());
					chosen_port = Integer.valueOf(chosen_p);*/
					////////////////////////////////////////////
					
					
					inBuf = new byte[10000];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					String data = new String(inPacket.getData(), 0, inPacket.getLength()); //LISTA PLIK�W PRZYCHODZI //DO KLIENTA
					//Printing file list
					System.out.println(data);
					
					
					//Send file name and download the specified files
					String filename = src.nextLine();
					outBuf = filename.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length,address,NEWPORT);
					socket.send(outPacket);
					
					inBuf = new byte[10];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					String recei =new String(inPacket.getData(), 0, inPacket.getLength());
					
					if(recei.endsWith("ERROR!")) {
						System.out.println("File does not exist");
						socket.close();
					}
					else {
						int howmany=Integer.valueOf(recei);
						BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
						String receivingString = null;
						try {
							for(int i=0;i<howmany;i++) {
								System.out.println(i);
								inBuf = new byte[500];
								System.out.println(i);
								inPacket = new DatagramPacket(inBuf, inBuf.length);
								System.out.println(i);
								socket.receive(inPacket);
								System.out.println(i);
								receivingString = new String(inPacket.getData(), 0, inPacket.getLength());
								System.out.println(i);
								pw.write(receivingString,500*i,500); //CO� TU NIE GRA
								System.out.println(i);
							}
							pw.close();
							System.out.println("File writing successful");
							//socket.close();
						}
						catch(IOException ioe) {
							System.out.println("File error\n");
							socket.close();
						}
						
						
							
					}
				break;
				case 2:
					//System.out.println("Nic nie robie :) \n");
					
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
					System.out.println("Lista uzytkownikow: ");
					System.out.println(sb.toString());
					/*
					ByteArrayInputStream bais = new ByteArrayInputStream(inPacket.getData());
					DataInputStream in = new DataInputStream(bais);
					
					while (in.available() > 0) {
						
						String element = in.readUTF();
						System.out.println(element);
						
					}*/
							
					
					
					
					
					
					//logins = new ArrayList<String>(inPacket.getData(), 0, inPacket.getLength());
					//System.out.println("Uzytkownik: "+logins.get(0));
					
					/*
					katalog=src.nextLine();
					outBuf=katalog.getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,PORT); //wys�anie katalogu
					socket.send(outPacket);
					*/
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