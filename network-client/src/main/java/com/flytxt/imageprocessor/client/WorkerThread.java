package com.flytxt.imageprocessor.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WorkerThread implements Runnable{
	
	private Socket socket = null;
	private DataOutputStream  out = null;
	private DataInputStream in = null;
	private String parentFolder ;
	private String imageLabel ;
	
	public WorkerThread(String parentFolder , String imageLabel){
		
			this.parentFolder =parentFolder;
			this.imageLabel = imageLabel;
			try {
				socket = new Socket("127.0.0.1", 9081);
				out = new DataOutputStream( socket.getOutputStream() );
				in = new DataInputStream(socket.getInputStream()); 
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void stop(){			
		try {				
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public String connect(String parentFolder){
		try {
			out.writeUTF(parentFolder);			
			out.flush();			
			return  in.readUTF(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public void run() {
		List<String> failedFiles = new ArrayList<>();
		try(DirectoryStream<Path> directoryStream =  Files.newDirectoryStream(
				Paths.get("F:/shiju/workspace/master/network/network-server/"+parentFolder))){
			
			for(Path path : directoryStream){				
				String line = this.connect(parentFolder);				
				if(!imageLabel.equals(line)){
					failedFiles.add(path.toString());
					System.out.println( " Invalid result   :  "+ path.toString());
				}
				if(path.toFile().exists()){
					path.toFile().delete();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println(this.connect("exit"));	
			stop();
		}
		
		System.out.println(" Total Failed Count for the category : "+imageLabel + " "+failedFiles.size());
		
	}

}
