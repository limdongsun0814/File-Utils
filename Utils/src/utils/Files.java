package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Files {
	private String path; // 현재의 경로
	private List<File> FileList; //현재 경로의 파일 리스트
	private Node tree;// 이후 남은 경로의 파일 리스트
	private String str;
	

//	tree, FileList, str
	public Files(String path){
		this.path=path;
		getFileList();
		strUpdate();
		fileSynchronization();
	}
	
	public Node getNode(int index) throws FileNotFoundException {
		return tree.getChild(index);
	}
	public String toString() {
		return str;
	}
	public void fileSynchronization() {
		tree = new Node(path,this);
		dfs(tree,path);
		
	}
	
	
	private String strUpdate() {
		str = String.format("path: %s \n, FileList: %s \n, Node: %s \n", path,FileList,tree);
		return str;
	}
	
	private void dfs(Node node,String path) {
		File f = new File(path);
		if(f.list()!=null)
		for(String fileName:f.list()) {
			String newFileName = fileName;//path+"\\"+fileName;
			Node newNode=node.insertChild(newFileName,node,this);
			if(new File(path+"\\"+newFileName).isDirectory())
			dfs(newNode,path+"\\"+newFileName);
		}
	}
	
	private List<File> getFileList() {
		File f = new File(path);
		System.out.println(Arrays.toString(f.list()));
		FileList=new ArrayList<>();
		if(f.list()!=null)
		for(String fileName : f.list()) {
			String newFileName = path+"\\"+fileName;
			FileList.add(new File(newFileName));
		}
		return FileList;
	}
	public static void main(String[] args) {
		System.out.println("test");
	}

	
	public class Node{
		private String node;
		private List<Node> child;
		private Node parent;
		private Files files;
		Node(String node,Files files){
			this.node=node;
			this.files=files;
		}
		Node(String node,Node parent,Files files){
			this.node=node;
			this.parent=parent;
			this.files=files;
		}
		
		public void remove() {
			String path = getPath();
			System.out.println(path);
			if(child!=null)
			for(Node ch:child) {
				ch.remove(path);
			}
			File f = new File(path);
			f.delete();
			files.getFileList();
			if(child!=null) {
				files.fileSynchronization();
				child=null;
			}
			node=null;
			files.strUpdate();
		}
		public void remove(String path) {
			String newPath = path+"\\"+node;
			if(child!=null)
			for(Node ch:child) {
				ch.remove(newPath);
			}
			File f = new File(newPath);
			f.delete();
			node=null;
			child=null;
		}
		
		
		public String getNode() {
			return node;
		}
		public Node getChild(int index) throws FileNotFoundException {
			if(child==null) 
				throw new FileNotFoundException();
			return child.get(index);
		}

		public Node insertChild(String data,Node parent,Files files) {
			if(child == null)
				child = new ArrayList<>();
			Node newNode = new Node(data,parent,files);
			child.add(newNode);
			return newNode;
		}
		
		public String toString() {
			if(child==null)
				return node;
			return String.format("{node: %s / child: %s}", node,child.toString());
		}
		private String getPath() {
			if(parent==null)
				return getNode();
			return parent.getPath()+"\\"+node;
		}
	}
}
