package com.flytxt.imageprocessor.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.flytxt.imageprocessor.request.RequestHandler;
import com.flytxt.imageprocessor.request.RequestProcessor;

public class Server extends Thread {
	
	ServerSocket serverSocket = null;
	RequestProcessor invoker = null;
	
	
	public void startServer() throws IOException, InterruptedException{		
		serverSocket = new ServerSocket(9082);
		System.out.println("Server : "+serverSocket.getInetAddress() +"  Listening on Port 9082" );
		invoker = new RequestHandler();
		
		this.start();
	}
	
	/**
	 * waiting  for connection 
	 */
	@Override
	public void run() {
		boolean running = true;
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out =null;
		
		while(running){
			System.out.println( "waiting for connection" );
			try {
				
				socket = serverSocket.accept();
				 // Get input and output streams
				in =new DataInputStream(socket.getInputStream());  
	            out = new DataOutputStream( socket.getOutputStream() );
   
	          
	            String line = in.readUTF();
	           	                               
	            if("exit".equalsIgnoreCase(line)){
	               	running =false;
	               	out.writeUTF( "Stop server initiated ");
	               	out.flush();
	            }else{
	            	String result = invoker.predict(line);
	 	            out.writeUTF(result);
	 	            out.flush();
	            }           
				
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}finally{
				 try {
					in.close();
					out.close();
			         socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		         
			}
				
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		try {
			new Server().startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
