package rumba_main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    		   
    		   int howmanyusers = usercounter;
        	   socketConn.close();
	           Runnable connection = new Runnable() {
	               @Override
	               public void run() {
	            	   	DatagramSocket socket = null;
	            	   	DatagramPacket inPacket = null; //recieving packet
	              		DatagramPacket outPacket = null; //sending packet
	              		byte[] inBuf, outBuf;
	              		String chosen_dir="", oneuser;	          
	              		StringBuilder sbuild = null;
	              		int BUFFERSIZE=50000;
	            	   
	                   try {
	                	System.out.println("\nRunning...\n");
	       				socket = new DatagramSocket(PORTS_CONN[howmanyusers-1]);
	           			
	           			while(true) {
	           				inBuf = new byte[BUFFERSIZE];
	           				inPacket = new DatagramPacket(inBuf, inBuf.length); //odebranie wyboru do switcha
	           				socket.receive(inPacket); 
	           				String ch = new String(inPacket.getData(), 0, inPacket.getLength());
	           				int choice = Integer.valueOf(ch);
	           				
	           				switch(choice) {
	           				case 1:

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
	        						if(loginsAll.get(i).toString().equals(chosen_user)) {
	        							System.out.println(i);//identyfikuje wybranego uzytkownika
	        							chosen_dir=directoriesAll.get(i).toString();
	        						}

	        					}


	        					System.out.println(chosen_dir);		
	           					
	           					StringBuilder sb = new StringBuilder("\n");
	           					String dirname = chosen_dir; //dir wybranego usera pozniej
	           					System.out.println(dirname);
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
	           					int X=0;
	           					for(int i=0; i<fl.length; i++) {
	           						if(fl[i].getName()==filename) {
	           							X=(int)fl[i].length();
	           							outBuf=new byte[X];
	           						}
	           					}
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
	           						
	           						System.out.println("ERROR!");
	           						sb.append("ERROR!");
	           						outBuf = (sb.toString()).getBytes();
	           						outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
	           						socket.send(outPacket);
	           						
	           					}
	           					else{
	           						
	           						try {
	           							
	           							//File Send Process, Independent
	           							File ff = new File(fl[index].getAbsolutePath());
	           							int size_of_segment=60000;
	           							byte[] segment = new byte[size_of_segment];
	           			
	           							for(int i=0;i<20;i++) {
	           							sb = new StringBuilder();
	           				            DataInputStream input = new DataInputStream( new FileInputStream( ff ) );
	           					        try {
	           					        	byte[] DATASIZE = sb.toString().getBytes();
	           					        	int DATASIZElenght=DATASIZE.length;
	           					        	int size_of_data=0;
	           					        	for(int j=0;i<DATASIZElenght;i++) {
	           					        		size_of_data=size_of_data+DATASIZE[j];
	           					        	}
	           					        	while(size_of_data<size_of_segment) {
	           					                    sb.append( Integer.toBinaryString( input.readByte() ) );
	           					                    
	           					                }
	           					        	segment = (sb.toString()).getBytes();
	           					        	outPacket = new DatagramPacket(segment, 0, segment.length, source_address_C, source_port_C);
	           					        	socket.send(outPacket); 
	           					        } 
	           					        catch( EOFException eof ) {
	           					        }
	           					        catch( IOException e ) {
	           					        	e.printStackTrace();
	           					        }
	           							 
	           							}
	           							
	           							
	           						}
	           						catch(IOException ioe) {
	           							
	           							System.out.println(ioe);
	           							
	           						}
	           					}
	           				break;
	           				case 2:
	           					
	           					outBuf =String.valueOf(usersAll.size()).getBytes();
	           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
	           					socket.send(outPacket);
	           					
	           					for(int i=0;i<usersAll.size();i++) {
	           						outBuf =String.valueOf(usersAll.size()).getBytes();
		           					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address_C, source_port_C);
		           					socket.send(outPacket);
	           					}
	           					
	           					
	           				break;
	           				case 3:
	           					for(int i=0;i<usersAll.size();i++) {
	           						if(usersAll.get(i).toString().contains(source_address_C.toString())) //usuwanie obecnego uzytkownika 
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
