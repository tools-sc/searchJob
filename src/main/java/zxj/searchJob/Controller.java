package zxj.searchJob;

import java.net.URLEncoder;

import zxj.searchJob.util.ThreadPool;
import zxj.searchJob.work.RequestWork;

public class Controller {
	/**
	 * 查询多少页
	 */
	public static final int doNum = 2000;
	/**
	 * 开启多少个线程查询
	 */
	public static final int threadNum = 10;
	/**
	 * 要查找的职位
	 */
	private final static String job = "java";
	public final static String charset = "GBK";
	public static long startTime = System.currentTimeMillis();
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {

		String encoderJob = URLEncoder.encode(job, "utf-8");
        for(int i = 1 ; i <= doNum ; i ++){
        	String url = "http://search.51job.com/jobsearch/search_result.php?fromJs=1&jobarea=040000%2C00&district=000000&funtype=0000&industrytype=00&issuedate=9&providesalary=99&keyword="+encoderJob+"&keywordtype=2&curr_page="+i+"&lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&list_type=0&fromType=14&dibiaoid=0&confirmdate=9";
        	RequestWork work = new RequestWork(url);
        	ThreadPool.workPool.execute(work);
        }
    }
}
