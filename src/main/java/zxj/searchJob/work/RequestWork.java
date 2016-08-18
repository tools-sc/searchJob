package zxj.searchJob.work;

import zxj.searchJob.Controller;
import zxj.searchJob.util.HttpUtil;
import zxj.searchJob.util.ThreadPool;

public class RequestWork implements Runnable{
	
	private String url;
	public RequestWork(String url) {
		this.url = url;
	}
	
	@Override
	public void run() {
		HttpUtil util = new HttpUtil(url, Controller.charset);
		String html = util.request();
		
        HtmlTask task = new HtmlTask(html);
        ThreadPool.pool.execute(task);
	}

}
