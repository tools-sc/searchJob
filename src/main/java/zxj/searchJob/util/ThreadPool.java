package zxj.searchJob.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import zxj.searchJob.Controller;

public class ThreadPool {

	public static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	
	public static ThreadPoolExecutor workPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Controller.threadNum);
	
}
