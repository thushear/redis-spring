package net.shopin.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-21
 * Time: 上午9:45
 */
public class SolrUtil {

    public static SolrServer server = null;

    public static EmbeddedSolrServer eServer;

    static {


        setUp();
    }





    public static void setUp(){
        System.setProperty("solr.solr.home","D:\\solr-4.8.0\\example\\solr");

        CoreContainer.Initializer  initializer =  new CoreContainer.Initializer();
        try {
            /*CoreContainer coreContainer = initializer.initialize();*/
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


    }


    public static void testAddIndex(){
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id","ABC");
        doc.setField("text","京东商城");
        try {
            eServer.add(doc);
            eServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public static void addIndex(){
        try {



            CommonsHttpSolrServer solrServer = new CommonsHttpSolrServer("http://localhost:8393/solr");
            solrServer.setSoTimeout(1000);
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField( "id", "id1", 1.0f );
            doc1.addField( "name", "doc1", 1.0f );
            doc1.addField( "price", 10 );
            SolrInputDocument doc2 = new SolrInputDocument();
            doc2.addField( "id", "id2", 1.0f );
            doc2.addField( "name", "doc2", 1.0f );
            doc2.addField( "price", 20 );
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
            docs.add( doc1 );
            docs.add( doc2 );
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



    public static void main(String[] args){

        System.out.println("solrj  start  ");
        setUp();
        testAddIndex();
        /*addIndex();*/



    }








}
