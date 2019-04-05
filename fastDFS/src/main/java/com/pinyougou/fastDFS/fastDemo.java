package com.pinyougou.fastDFS;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class fastDemo {
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		ClientGlobal.init("E:\\learnWorkspaces\\demo\\fastDFS\\src\\main\\resources\\fdfs_client.conf");
		//
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		String[] upload_file = storageClient.upload_file("D:\\tools\\FastDFS\\aa.jpg", "jpg", null);
		for(String file:upload_file) {
			System.out.println(file);
		}
	}
}
