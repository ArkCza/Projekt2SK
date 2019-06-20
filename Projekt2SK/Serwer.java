//Sends all types of files within UDP Packe size limit

package rumba_threaded;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.ArrayList;

public class Serwer {

   public static void main(String[] args) throws IOException {
	   
		
	   final int PORT = 50000;
	   final int[] PORTS_CONN= {50001,50002,50003,50004,50005,50006,50007,50008,50009};
       ExecutorService executorService = Executors.newFixedThreadPool(10);
       int usercounter=0;
       List<String> usersAll = new ArrayList<String>();
       List<String> directoriesAll = new ArrayList<String>();
       List<String> loginsAll = new ArrayList<String>();
       
       while (true){
    	   DatagramSocket socketConn = new DatagramSocket(PORT);
    	   DatagramPacket inPacketConn = null; //recieving packet
    	   DatagramPacket outPacketConn = null; //sending packet
    	   byte[] inBufConn, outBufConn;
    	   System.out.println("\nRunningAll...\n");
    	   inBufConn = new byte[5000];
    	   inPacketConn = new DatagramPacket(inBufConn, inBufConn.length); //odebranie pustego stringa
    	   socketConn.receive(inPacketConn);
    	   String ifconn = new String(inPacketConn.getData(), 0, inPacketConn.getLength());
    	   System.out.println("\nRequest: "+ifconn+"\n");
    	   //int usercounter=0;
    	   if(ifconn.equals("")) {
    		   usercounter+=1;
    		   System.out.println(usercounter);
    		   InetAddress source_address_C = inPacketConn.getAddress();
    		   int source_port_C = inPacketConn.getPort();
    		   
    		   inBufConn = new byte[5000];
        	   inPacketConn = new DatagramPacket(inBufConn, inBufConn.length); //odebranie loginu
        	   socketConn.receive(inPacketConn);
        	   String loginAll = new String(inPacketConn.getData(), 0, inPacketConn.getLength());
    		   loginsAll.add(loginAll);
        	   
        	   
        	   String useroneall=usercounter+" Client - "+loginAll+" : "+source_address_C+":"+source_port_C;
        	   usersAll.add(useroneall);
        	   System.out.println("User "+loginAll+" saved.\n");
        	   
        	   inBufConn = new byte[5000];
        	   inPacketConn = new DatagramPacket(inBufConn, inBufConn.length); //odebranie dira
        	   socketConn.receive(inPacketConn);
        	   String dirAll = new String(inPacketConn.getData(), 0, inPacketConn.getLength());
        	   directoriesAll.add(dirAll);
        	   System.out.println("Directory "+dirAll+" acknowledged.\n");
        	   
    		   outBufConn=Integer.toString(usercounter-1).getBytes();
    		   outPacketConn = new DatagramPacket(outBufConn, 0, outBufConn.length, source_address_C,source_port_C); //przekazanie na jakim porcie ma polaczyc sie user
    		   socketConn.send(outPacketConn);
    		   System.out.println("T");
    		   
    		   int howmanyusers = usercounter;
        	   socketConn.close();
	           Runnable connection = new Runnable() {
	               @Override
	               public void run() {
	            	    ServerSocket ssock = null;
	            	    Socket socketforfiles = null;
	            	   	DatagramSocket socket = null;
	            	   	DatagramPacket inPacket = null; //recieving packet
	              		DatagramPacket outPacket = null; //sending packet
	              		byte[] inBuf, outBuf;
	              		String msg,user,chosen_address="",chosen_dir="",login,oneuser,reponse="";
	              		int chosen_port=0,rozm;
	              		List<Integer> ports = new ArrayList<Integer>();
	              		List<String> logins = new ArrayList<String>();
	              		List<String> addresses = new ArrayList<String>();
	              		//List<String> users = new ArrayList<String>();
	              		//List<String> directories = new ArrayList<String>();
	              		
	              		StringBuilder sbuild = null;
	              		int BUFFERSIZE=50000;
	            	   
	                   try {
	                	System.out.println("\nRunning...\n");
	       				socket = new DatagramSocket(PORTS_CONN[howmanyusers-1]);
	       				
	           			//	inBuf = new byte[BUFFERSIZE];
	           			//	inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie pustego stringa
	           			//	socket.receive(inPacket);
	           			//	int source_port = inPacket.getPort();
	           			//	Integer sport = Integer.valueOf(source_port);
	           			//	System.out.println(sport);
	           			//	InetAddress source_address = inPacket.getAddress();
	           			//	System.out.println(source_address);
	           			//	addresses.add(source_address.toString());
	           			//	msg = new String(inPacket.getData(), 0, inPacket.getLength()); //przypisanie do zmiennej msg na serwerze - nawiazanie polaczenia
	           			
	           			
	           				/*inBuf = new byte[BUFFERSIZE];
	           				inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie loginu
	           				socket.receive(inPacket);
	           				InetAddress source_address = inPacket.getAddress();
	           				int source_port = inPacket.getPort();
		           			Integer sport = Integer.valueOf(source_port);
	           				login = new String(inPacket.getData(), 0, inPacket.getLength());
	           				logins.add(login);*/
	           				
	           				
	           			
	           			
	           			//for(int i=0;i<logins.size();i++) {
	           			//	user=howmanyusers+" Client - "+login+" : "+source_address_C+":"+source_port_C;
	           			//	//System.out.println(user); //wyswietlanie listy klientow
	           			//	users.add(user);
	           			//}
	           			
	           			
	           			
	           				/*inBuf = new byte[BUFFERSIZE];
	           				inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie katalogu
	           				socket.receive(inPacket);
	           				String catalog = new String(inPacket.getData(), 0, inPacket.getLength());
	           			//System.out.println("Zapisano udostepniany katalog: "+catalog);
	           				directoriesAll.add(catalog);
	           			
	           				System.out.println("Zapisano udostepniany katalog: "+directoriesAll.get(directoriesAll.size()-1));*/
	           			
	           			
	           			while(true) {
	           				inBuf = new byte[BUFFERSIZE];
	           				inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie wyboru do switcha
	           				socket.receive(inPacket); 
	           				String ch = new String(inPacket.getData(), 0, inPacket.getLength());
	           				int choice = Integer.valueOf(ch);
	           				
	           				switch(choice) {
	           				case 1:
	           					//WSTAWKA DLA WIELU UZYTKOWNIKOW 
	           					/*sbuild = new StringBuilder();
	        					rozm=users.size();
	        					sbuild.append(rozm);
	        					outBuf=(sbuild.toString()).getBytes();
	        					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address,source_port); //wys?anie rozmiaru listy
	        					socket.send(outPacket);*/

	        					sbuild = new StringBuilder();
	        					sbuild.append("List of users: \n");
	        					for(int i=0;i<usersAll.size();i++) {
	        						oneuser=(String)usersAll.get(i);
	        						sbuild.append(oneuser+"\n");
	        						
	        					}
	        					outBuf=(sbuild.toString()).getBytes();
        						outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C,source_port_C); //wys?anie uzytkownikow
        						socket.send(outPacket);
        						
	        					inBuf = new byte[BUFFERSIZE];
	        					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie wyboru uzytkownika
	        					socket.receive(inPacket);
	        					String chosen_user = new String(inPacket.getData(), 0, inPacket.getLength());
	        					
	        					for(int i=0;i<usersAll.size();i++) {
	        						if(loginsAll.get(i).toString().equals(chosen_user)) { //identyfikuje wybranego uzytkownika
	        							chosen_dir=directoriesAll.get(i).toString();
	        						}
	        					}

	        					//System.out.println(chosen_address);
	        					/*sbuild = new StringBuilder("");
	        					sbuild.append(chosen_port);
	        					
	        					outBuf=chosen_address.getBytes();
	        					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address,source_port); //wys?anie wybranego adresu
	        					socket.send(outPacket);
	        					
	        					outBuf=(sbuild.toString()).getBytes();
	        					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address,source_port); //wys?anie wybranego portu
	        					socket.send(outPacket);*/
	        					
	           					
	           					
	           					
	           					
	           					StringBuilder sb = new StringBuilder("\n");
	           					String dirname = chosen_dir; //dir wybranego usera pozniej
	           					File f1=new File(dirname);
	           					File fl[]=f1.listFiles();
	           					
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
	           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C); //WYSLANIE ZAPYTANIA O PLIKI DO POBRANIA - PO WYBRANIU UZYTKOWNIKA
	           					socket.send(outPacket);
	           					
	           					inBuf = new byte[10000];
	           					inPacket = new DatagramPacket(inBuf, inBuf.length);
	           					socket.receive(inPacket);
	           					String filename = new String(inPacket.getData(), 0, inPacket.getLength()); //PRZYCHODZI NAZWA PLIKU
	           					
	           					System.out.println("Requested file: "+filename);
	           					File chosenFile = null;
	           					for(int i=0; i<fl.length; i++) {
	           						if(fl[i].getName().equals(filename)) {
	           							chosenFile = fl[i];
	           						}
	           					}
	           					
	           					File ff = new File(chosenFile.getAbsolutePath());
       							int filesize = (int)ff.length();
       							int size_of_segment=500;
       							byte[] segment;
       							int howmanysegs=filesize/size_of_segment+1;
       							StringBuilder sending = new StringBuilder();
       							
	           					boolean flis = false;
	           					sb = new StringBuilder("");
	           					for(int i=0; i<fl.length; i++) {
	           						if(((fl[i].getName()).toString()).equalsIgnoreCase(filename)){
	           							flis=true;
	           						}
	           					}
	           					
	           					if(!flis){
	           						
	           						System.out.println("ERROR!");
	           						sending.append("ERROR!");
	           						outBuf = (sending.toString()).getBytes();
	           						outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
	           						socket.send(outPacket);
	           						
	           					}
	           					else{
	           						sending.append(howmanysegs);
	           						try {
	           							//File Send Process, Independent
	           							
	           							//mowimy klientowi ile segmentow ma odebrac
	           							outBuf = (Integer.toString(howmanysegs)).getBytes();
	    	           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
	    	           					socket.send(outPacket);

	    	           					
	           							//FileInputStream in = new FileInputStream(ff);
	           							//in.read(filedata);
	           							// fr = new FileReader(ff);
	           							//BufferedReader brf = new BufferedReader(fr);
	           							//String s = null;
	           							//sb = new StringBuilder();
	           							//while((s = brf.readLine())!=null) {
	           							//	sb.append(s);
	           							//}
	           							//if(brf.readLine()==null) {
	           							//	System.out.println("File Read Successful. Closing Socket.");
	           							//}
	    	           					DataInputStream input;
	           							for(int i=0;i<howmanysegs;i++) {
		           							sb = new StringBuilder("");
		           				            input = new DataInputStream( new FileInputStream( ff ) );
		           				            segment = new byte[500];
		           				         	try {
		           				         		sb.append(Integer.toBinaryString( input.read(segment,i*500,500)));
		           					        	segment = (sb.toString()).getBytes();
		           					        	outPacket = new DatagramPacket(segment, 0, segment.length, source_address_C, source_port_C);
		           					        	socket.send(outPacket);
		           					        	System.out.println("Sending..." +i);
		           					        } 
		           					        catch( EOFException eof ) {
		           					        }
		           					        catch( IOException e ) {
		           					        	e.printStackTrace();
		           					        }
		           					        //outBuf = new byte[100000];
		           							 
	           							}
	           							
	           							
	           						}
	           						catch(IOException ioe) {
	           							
	           							System.out.println(ioe);
	           							
	           						}
	           					}
	           				break;
	           				case 2:
	           					/*
	           					ByteArrayOutputStream baos = new ByteArrayOutputStream();
	           					DataOutputStream out = new DataOutputStream(baos);
	           					for (String element : logins) {
	           						
	           						out.writeUTF(element);
	           						
	           					}
	           					
	           					
	           					outBuf = baos.toByteArray();
	           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
	           					socket.send(outPacket);*/
	           					
	           					outBuf =String.valueOf(usersAll.size()).getBytes();
	           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
	           					socket.send(outPacket);
	           					
	           					for(int i=0;i<usersAll.size();i++) {
	           						outBuf =String.valueOf(usersAll.size()).getBytes();
		           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
		           					socket.send(outPacket);
	           					}
	           					
	           					
	           					/*
	           					inBuf = new byte[1000];
	           					inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie katalogu
	           					socket.receive(inPacket);
	           					String catalog = new String(inPacket.getData(), 0, inPacket.getLength());
	           					System.out.println("Zapisano udostepniany katalog: "+catalog);
	           					directories.add(catalog);
	           					
	           					*/
	           				break;
	           				case 3:
	           					for(int i=0;i<usersAll.size();i++) {
	           						if(usersAll.get(i).toString().contains(source_address_C.toString())) //usuwam obecnego uzytkownika 
	           							usersAll.remove(i);
	           					}
	           				break;
	           				}
	           			} 
	                   } catch (IOException e) {
	                       e.printStackTrace();
	                       System.out.println("ERROR!!!!\n");
	                   }
	
	               }
	           };
	           executorService.submit(connection);
       		}
    	   	else {
        	   socketConn.close();
    	   }
       }
   }
}