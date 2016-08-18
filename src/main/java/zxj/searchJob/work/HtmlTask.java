package zxj.searchJob.work;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import zxj.searchJob.Controller;
import zxj.searchJob.util.ThreadPool;

public class HtmlTask implements Runnable{
	private String html;
	private static String filePath = "D:/jobdata/";

	public static List<Object> stream = Collections.synchronizedList(new ArrayList<>());
	public static Map<String,Integer> areaMap = new Hashtable<>();
	public static Map<String,Integer> moneyMap = new Hashtable<>();
	private final Object lock = new Object();
	
	public HtmlTask(String html) {
		this.html = html;
	}
	
	
	@Override
	public void run() {
		Document document = Jsoup.parse(html);
		Elements root = document.select(".el");
		for(Element el : root){
			Elements select = el.select(".t4");
			boolean isJob = !"".equals(select.text()) && !"薪资".equals(select.text());
			if(isJob){
				//获取所在区
				String area = getArea(el);
				//职位
				String job = getJob(el);
				//工资
				String money = getMoney(el);
				doWork(area, job, money);
			}
		}
		long completedTaskCount = ThreadPool.pool.getCompletedTaskCount() + 1;
		System.out.println("已完成的任务数量:" + completedTaskCount);
		synchronized (lock) {
			if(completedTaskCount == Controller.doNum){
				System.out.println("完成全部任务");
				System.out.println("正在保存数据文件");
				saveFile();
			}
		}
	}
	
	private void saveFile(){
		try {
			String streamPath = filePath + "stream.txt";
			BufferedWriter streamWrite = getWrite(streamPath);
			Iterator<Object> iterator = stream.iterator();
			while(iterator.hasNext()){
				Object next = iterator.next();
				streamWrite.write(next.toString());
				streamWrite.newLine();
				
			}
			streamWrite.close();
			System.out.println("流水文件保存完毕:" + streamPath);
			
			String areaPath = filePath + "area.txt";
			BufferedWriter areaWrite = getWrite(areaPath);
			Iterator<Entry<String, Integer>> areaIterator = areaMap.entrySet().iterator();
			while(areaIterator.hasNext()){
				Entry<String, Integer> entry = areaIterator.next();
				areaWrite.write(entry.getKey() + "   " + entry.getValue());
				areaWrite.newLine();
			}
			areaWrite.close();
			System.out.println("区域统计保存完毕:" + areaPath);
			
			String moneyPath = filePath + "money.txt";
			BufferedWriter moneyWrite = getWrite(moneyPath);
			Iterator<Entry<String, Integer>> moneyIterator = moneyMap.entrySet().iterator();
			while(moneyIterator.hasNext()){
				Entry<String, Integer> entry = moneyIterator.next();
				moneyWrite.write(entry.getKey() + "   " + entry.getValue());
				moneyWrite.newLine();
			}
			moneyWrite.close();
			System.out.println("工资统计保存完毕:" + moneyPath);
			System.out.println("程序运算完毕");
			long endTime = System.currentTimeMillis();
			long useTime = endTime - Controller.startTime;
			System.out.println("用时: " + (useTime / 1000) + " 秒");
			System.out.println("程序即将退出");
			System.exit(0);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedWriter getWrite(String path){
		try {
			File file = new File(path);
			BufferedWriter streamWrite = new BufferedWriter(new FileWriter(file,true));
			return streamWrite;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("流失败");
		}
	}
	
	private synchronized void doWork(String area,String job,String money){
		//流水
		stream.add(area + "   " + job + "   " + money);
		//区域
		Integer areaNum = areaMap.get(area);
		if(areaNum == null){
			areaMap.put(area, 1);
		}else{
			areaMap.put(area, areaNum + 1);
		}
		//工资
		Integer moneyNum = moneyMap.get(money);
		if(null == moneyNum){
			moneyMap.put(money, 1);
		}else{
			moneyMap.put(money,moneyNum + 1);
		}
	}
	
	/**
	 * 获取工资
	 * @param elements
	 * @return
	 */
	public String getMoney(Element elements){
		Elements select = elements.select(".t4");
		String text = select.text();
		return text;
	}
	
	/**
	 * 获取在哪个区
	 * @param elements
	 * @return
	 */
	private String getArea(Element elements){
		Elements select = elements.select(".t3");
		String text = select.text();
		String[] split = text.split("-");
		if(split.length == 2){
			return split[1];
		}else{
			return split[0];
		}
	}
	
	/**
	 * 获取职位
	 * @param elements
	 * @return
	 */
	private String getJob(Element elements){
		String text = elements.select("a").attr("title");
		if(text.contains("工程师") || text.contains("程序员") || text.contains("技术员") || text.contains("编程"))return "工程师";
		else if(text.contains("项目经理"))return "项目经理";
		else if(text.contains("产品经理"))return "产品经理";
		else if(text.contains("测试"))return "测试";
		else if(text.contains("总监"))return "总监";
		else if(text.contains("架构师"))return "架构师";
		else if(text.contains("主管"))return "技术主管";
		else if(text.contains("讲师"))return "讲师";
		else if(text.contains("实习") || text.contains("实训") || text.contains("学徒"))return "实习生";
		return text;
	}

}
