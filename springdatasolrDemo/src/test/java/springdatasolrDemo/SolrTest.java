package springdatasolrDemo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itcast.demo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class SolrTest {
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Test
	public void add() {
		List<TbItem> list=new ArrayList();
		
		for(int i=0;i<100;i++){
			TbItem item=new TbItem();
			item.setId(i+1L);
			item.setBrand("华为");
			item.setCategory("手机");
			item.setGoodsId(1L);
			item.setSeller("华为2号专卖店");
			item.setTitle("华为Mate"+i);
			item.setPrice(new BigDecimal(2000+i));	
			list.add(item);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	//根据主键进行查询和删除
	@Test
	public void findById() {
		//传入 ID 和 返回值类型
		TbItem item = solrTemplate.getById("1", TbItem.class);
		//输出结果：华为Mate9
		System.out.println(item.getTitle());
	}
	
	@Test
	public void deleteById() {
		//会自动转化为 Long 类型
		solrTemplate.deleteById("1");
		solrTemplate.commit();
	}
	
	@Test
	public void findByPage() {
		//创建查询对象(属性：条件)
		Query query = new SimpleQuery("*:*");
		//开始值 与 每页记录数
		query.setOffset(20);
		query.setRows(20);
		//调用 Solr 服务器进行查询
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("总记录数："+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}	
	//显示记录数据
	private void showList(List<TbItem> list){		
		for(TbItem item:list){
			System.out.println(item.getTitle() +item.getPrice());
		}		
	}
	
	@Test
	public void testPageQueryMutil(){	
		Query query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_title").contains("2");
		criteria=criteria.and("item_title").contains("5");		
		query.addCriteria(criteria);
		//query.setOffset(20);//开始索引（默认0）
		//query.setRows(20);//每页记录数(默认10)
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("总记录数："+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}
	
	@Test
	public void testDeleteAll(){
		Query query=new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
	
}
