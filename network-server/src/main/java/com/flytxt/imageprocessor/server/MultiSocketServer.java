package com.flytxt.imageprocessor.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.flytxt.imageprocessor.request.RequestHandler;
import com.flytxt.imageprocessor.request.RequestProcessor;

public class MultiSocketServer implements Runnable{
	
	private ServerSocket serverSocket = null;
	private ExecutorService executor = null;
	int port = 9081;
	private RequestProcessor requestProcessor = null;
	
	
	public void startServer(String strategy) throws IOException, InterruptedException{		
		serverSocket = new ServerSocket(port);	
		executor = Executors.newFixedThreadPool(6);
				
		requestProcessor = new  RequestHandler();		
		
		new Thread(this).start();
	}

	private void invalidStrategy() {
		/**
		 * Please provide the Run Time Parameter strategy 
		 */
		System.out.println("\n \t Please provide the Run Time strategy");	
		System.out.println("*****************************************************************");
		System.out.println("*	init   : For initalize the network			*");
		System.out.println("*	load   : For load the saved network 			*");
		System.out.println("*	loadT  : For load the saved network and Train  again	*");
		System.out.println("*****************************************************************");
		
		
	}

	@Override
	public void run() {	
		System.out.println("Server listening  on port " + port);
		while(true){
			Socket socket;
			try {
				socket = serverSocket.accept();
				executor.execute(new Worker(socket,requestProcessor));
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}		
	}
	
	public static void main(String[] args) {
		try {
		
		new MultiSocketServer().startServer(null);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
