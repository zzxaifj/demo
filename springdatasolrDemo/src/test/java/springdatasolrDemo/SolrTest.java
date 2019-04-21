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
			item.setBrand("��Ϊ");
			item.setCategory("�ֻ�");
			item.setGoodsId(1L);
			item.setSeller("��Ϊ2��ר����");
			item.setTitle("��ΪMate"+i);
			item.setPrice(new BigDecimal(2000+i));	
			list.add(item);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	//�����������в�ѯ��ɾ��
	@Test
	public void findById() {
		//���� ID �� ����ֵ����
		TbItem item = solrTemplate.getById("1", TbItem.class);
		//����������ΪMate9
		System.out.println(item.getTitle());
	}
	
	@Test
	public void deleteById() {
		//���Զ�ת��Ϊ Long ����
		solrTemplate.deleteById("1");
		solrTemplate.commit();
	}
	
	@Test
	public void findByPage() {
		//������ѯ����(���ԣ�����)
		Query query = new SimpleQuery("*:*");
		//��ʼֵ �� ÿҳ��¼��
		query.setOffset(20);
		query.setRows(20);
		//���� Solr ���������в�ѯ
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("�ܼ�¼����"+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}	
	//��ʾ��¼����
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
		//query.setOffset(20);//��ʼ������Ĭ��0��
		//query.setRows(20);//ÿҳ��¼��(Ĭ��10)
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("�ܼ�¼����"+page.getTotalElements());
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
