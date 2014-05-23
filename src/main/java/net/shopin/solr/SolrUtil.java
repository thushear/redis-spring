package net.shopin.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-21
 * Time: 上午9:45
 */
public class SolrUtil {

    public static SolrServer server = null;

    public static EmbeddedSolrServer eServer;


    public static CommonsHttpSolrServer solrServer;

    static {


        setUp();
    }


    public static void setUp() {
        //commonshttpSolrServer
        try {
            solrServer = new CommonsHttpSolrServer("http://localhost:8983/solr");
            solrServer.setSoTimeout(1000);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        /*System.setProperty("solr.solr.home","D:\\solr-4.8.0\\example\\solr");

        CoreContainer.Initializer  initializer =  new CoreContainer.Initializer();
        try {
            *//*CoreContainer coreContainer = initializer.initialize();*//*
            File home = new File( "D:\\solr-4.8.0\\example\\solr" );
            File f = new File(home,"solr.xml");
            CoreContainer coreContainer = new CoreContainer();
            coreContainer.load("D:\\solr-4.8.0\\example\\solr",f);
            eServer = new EmbeddedSolrServer(coreContainer,"");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/

    }


    public static void testAddIndex() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", "ABC");
        doc.setField("text", "京东商城");
        try {
            eServer.add(doc);
            eServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public static void addIndex() {
        try {


            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField("id", "id1", 1.0f);
            doc1.addField("name", "doc1", 1.0f);
            doc1.addField("price", 10);
            doc1.addField("content", "中国人民站起来了好多年");
            SolrInputDocument doc2 = new SolrInputDocument();
            doc2.addField("id", "id2", 1.0f);
            doc2.addField("name", "doc2", 1.0f);
            doc2.addField("price", 20);
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
            docs.add(doc1);
            docs.add(doc2);
            solrServer.add(docs);
            solrServer.commit();
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public static void deleteIndex(String ids) {
        try {
            solrServer.deleteById(ids);
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public static void testQuery() {
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        query.addSortField("price", SolrQuery.ORDER.asc);
        try {
            QueryResponse response = solrServer.query(query);
            SolrDocumentList list = response.getResults();
            List<Item> beans = response.getBeans(Item.class);
            for (ListIterator iter = beans.listIterator(); iter.hasNext(); ) {
                Item item = (Item) iter.next();
                System.out.println(item);
            }

            /*for(int i = 0;i < list.size();i++){
                SolrDocument sd = list.get(i);
                String id = (String) sd.getFieldValue("id");
                String text = (String) sd.getFieldValue("text");
                System.out.println(id + ":" + text);
            }*/


        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    /**
     * 测试高亮
     */
    public static void testHighLight() {
        SolrQuery query = new SolrQuery();
        //query.set("q","*:*");
        query.setQuery("javaone");
        query.setHighlight(true).setHighlightSnippets(1);
        query.setParam("hl.fl", "content");
        //query.addHighlightField("content");
        //query.setHighlightSimplePre();
        try {
            QueryResponse response = solrServer.query(query);
            Iterator<SolrDocument> iter = response.getResults().iterator();
            while(iter.hasNext()){
                SolrDocument resultDoc = iter.next();
                List content = (List) resultDoc.getFieldValue("content");
                String id = (String) resultDoc.getFieldValue("id");
                System.out.println(id + ":" + content);
                if(response.getHighlighting().get(id)!=null){

                    List<String> highlightSnippets =
                            response.getHighlighting().get(id).get("content");
                    System.out.println(highlightSnippets);
                }
            }

        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public static void testAddBeans() {
        Item item = new Item();
        item.id = "one";
        item.name = "javaone";
        //item.price=100.0;
        item.content = "中国人民";
        try {
            solrServer.addBean(item);
            solrServer.commit();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public static void main(String[] args) {

        System.out.println("solrj  start  ");
        /*setUp();
        testAddIndex();*/
        /*addIndex();*/
        /*deleteIndex("id1");*/
        //testQuery();
        /*addIndex();*/
        /*testAddBeans();*/
        testHighLight();
    }


    public static class Item {

        @Field
        String id;

        @Field
        String name;

        @Field
        Float price;

        @Field
        String content;


        @Override
        public String toString() {
            return "Item{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", price='" + price + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }


}
