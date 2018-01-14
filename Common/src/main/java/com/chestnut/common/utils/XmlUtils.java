package com.chestnut.common.utils;

import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import rx.Observable;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/1/10 17:14
 *     desc  :  XML解析工具类
 *     thanks To:
 *          1.  [Android解析XML的三种方式]     http://blog.csdn.net/d_shadow/article/details/55253586
 *          2.  [Android几种解析XML方式的比较]       http://blog.csdn.net/isee361820238/article/details/52371342
 *          3.  [android xml 解析 修改]     http://blog.csdn.net/i_lovefish/article/details/39476051
 *          4.  [android 对xml文件的pull解析，生成xml ，对xml文件的增删]        http://blog.csdn.net/jamsm/article/details/52205800
 *     dependent on:
 *     update log:
 * </pre>
 */
public class XmlUtils {

    /**
     * DOM解析
     *  把文档中的所有元素，按照其出现的层次关系，解析成一个个Node对象(节点)。
     *  缺点是消耗大量的内存。
     * @param xmlFilePath   文件
     * @return  Document
     */
    public static Document loadWithDom(String xmlFilePath) {
        try {
            File file = new File(xmlFilePath);
            if (!file.exists()) {
                throw new RuntimeException("not find file:" + xmlFilePath);
            }
            else {
                InputStream inputStream = new FileInputStream(file);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                FileUtils.closeIO(inputStream);
                return document;
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
    }

    public static Observable<Document> loadWithDomRx(String xmlFilePath) {
        return Observable.just(loadWithDom(xmlFilePath));
    }

    /**
     * 保存修改后的Doc
     *  http://blog.csdn.net/franksun1991/article/details/41869521
     * @param doc   doc
     * @param saveXmlFilePath   路径
     * @return  是否成功
     */
    public static boolean saveXmlWithDom(Document doc,String saveXmlFilePath) {
        if (doc==null || saveXmlFilePath==null || saveXmlFilePath.isEmpty())
            return false;
        try {
            //将内存中的Dom保存到文件
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            //设置输出的xml的格式，utf-8
            transformer.setOutputProperty("encoding", "utf-8");
            DOMSource source = new DOMSource(doc);
            //打开输出流
            File file = new File(saveXmlFilePath);
            if (!file.exists())
                Log.i("XmlUtils","saveXmlWithDom,createNewFile:"+file.createNewFile());
            OutputStream outputStream = new FileOutputStream(file);
            //xml的存放位置
            StreamResult src = new StreamResult(outputStream);
            transformer.transform(source, src);
            FileUtils.closeIO(outputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Observable<Boolean> saveXmlWithDomRx(Document doc,String saveXmlFilePath) {
        return Observable.just(saveXmlWithDom(doc, saveXmlFilePath));
    }

    /**
     * 测试SAX解析
     * @param xmlFilePath 文件
     */
    private static void loadWithSax(String xmlFilePath) {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        SAXParser saxParser;
        try {
            File file = new File(xmlFilePath);
            InputStream inputStream = new FileInputStream(file);//得到输入流
            saxParser = factory.newSAXParser();//创建解析器
            saxParser.parse(inputStream,new DefaultHandler(){//开始解析
                //文档开始标记
                @Override
                public void startDocument() throws SAXException {
                    super.startDocument();
                    Log.i("loadWithSax","startDocument");
                }
                //文档结束标记
                @Override
                public void endDocument() throws SAXException {
                    super.endDocument();
                    FileUtils.closeIO(inputStream);
                    Log.i("loadWithSax","endDocument");
                }
                //解析到标签
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri, localName, qName, attributes);
                    Log.i("loadWithSax","startElement"+",uri:"+uri+",localName:"+localName+",qName:"+qName);
                    if (attributes!=null) {
                        for (int i = 0; i < attributes.getLength(); i++) {
                            Log.i("loadWithSax",attributes.getLocalName(i)+","+attributes.getValue(i)+","+attributes.getType(i));
                        }
                    }
                }
                //标签解析结束
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    super.endElement(uri, localName, qName);
                    Log.i("loadWithSax","endElement"+",uri:"+uri+",localName:"+localName+",qName:"+qName);
                }
                /**
                 *  文本
                 *  该方法中的ch把所解析的xml的所有数据都保存进来，且ch初始化为2K数据。 start是一个节点">"的位置。length就是">"到下一个"<"的长度。
                 *  <namesList>
                 *      <name>michael</name>
                 *  </namesList>
                 *  执行namesList节点时，因为没有文本，
                 *  不会执行到该方法。
                 */
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    super.characters(ch, start, length);
                    Log.i("loadWithSax","characters"+",start:"+start+",length:"+length);
                    for (int i = 0; i < ch.length; i++) {
                        Log.i("loadWithSax","char:"+ch[i]+",ASCII:"+(int)ch[i]);
                    }
                }
                //警告回调
                @Override
                public void warning(SAXParseException e) throws SAXException {
                    super.warning(e);
                    Log.i("loadWithSax","warning"+","+e.getMessage());
                }
                //错误回调
                @Override
                public void error(SAXParseException e) throws SAXException {
                    super.error(e);
                    Log.i("loadWithSax","error1"+","+e.getMessage());
                }
            });
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            Log.i("loadWithSax","error2"+","+e.getMessage());
        }
    }

    private static void saveWithSax(String saveFilePath) {
//        // 创建文件对象
//        File fileText = new File(saveFilePath);
//        // 向文件写入对象写入信息
//        FileWriter stringWriter;
//        try {
//            stringWriter = new FileWriter(fileText);
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlSerializer xmlSerializer = factory.newSerializer();
//            xmlSerializer.setOutput(stringWriter);
//
//            //制造假数据：
//            ArrayList<Person> personArrayList = new ArrayList<>();
//            Person person1 = new Person("zhangsan",21);
//            person1.id=1;
//            person1.key="33";
//            person1.type="type";
//            Person person2 = new Person("lisi",12);
//            Person person3 = new Person("wangwu",23);
//            personArrayList.add(person1);
//            personArrayList.add(person2);
//            personArrayList.add(person3);
//
//            //star document
//            xmlSerializer.startDocument("utf-8", true);
//            xmlSerializer.text("\n");
//            xmlSerializer.startTag(null, "persons");
//
//            for(Person person:personArrayList){
//                //star tag
//                xmlSerializer.text("\n");
//                xmlSerializer.text("\t");
//                xmlSerializer.startTag(null, "person");
//                //添加参数
//                if (person.id!=-1) {
//                    xmlSerializer.attribute(null,"id",String.valueOf(person.id));
//                }
//                if (person.key!=null) {
//                    xmlSerializer.attribute(null,"key",person.key);
//                }
//                if (person.type!=null) {
//                    xmlSerializer.attribute(null,"type",person.type);
//                }
//                //添加内容：name
//                xmlSerializer.text("\n");
//                xmlSerializer.text("\t");
//                xmlSerializer.text("\t");
//                xmlSerializer.startTag(null, "name");
//                xmlSerializer.text(person.name);
//                xmlSerializer.endTag(null, "name");
//                //添加内容：age
//                xmlSerializer.text("\n");
//                xmlSerializer.text("\t");
//                xmlSerializer.text("\t");
//                xmlSerializer.startTag(null, "age");
//                xmlSerializer.text(String.valueOf(person.age));
//                xmlSerializer.endTag(null, "age");
//                //end tag
//                xmlSerializer.text("\n");
//                xmlSerializer.text("\t");
//                xmlSerializer.endTag(null, "person");
//            }
//
//            //end document
//            xmlSerializer.text("\n");
//            xmlSerializer.endTag(null, "persons");
//            xmlSerializer.endDocument();
//
//            //close
//            FileUtils.closeIO(stringWriter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 测试PULL解析
     * @param xmlFilePath 文件
     */
    private static void loadWithPull(String xmlFilePath) {
        File file = new File(xmlFilePath);
        try {
            InputStream inputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();//得到Pull解析器
            parser.setInput(inputStream, "UTF-8");//设置下输入流的编码
            int eventType = parser.getEventType();//得到第一个事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {//如果事件类型不是文档结束的话则不断处理事件
                switch (eventType) {
                    case (XmlPullParser.START_DOCUMENT)://如果是文档开始事件
                        Log.i("loadWithPull","START_DOCUMENT");
                        break;
                    case (XmlPullParser.START_TAG)://如果遇到标签开始
                        String tagName = parser.getName();// 获得解析器当前元素的名称
                        Log.i("loadWithPull","START_TAG:"+tagName);
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            Log.i("loadWithPull","attr:"+parser.getAttributeName(i)+":"+parser.getAttributeValue(i));
                        }
                        if (!parser.isEmptyElementTag()) {
                            Log.i("loadWithPull","content:"+parser.getText());
                        }
                        break;
                    case (XmlPullParser.END_TAG)://如果遇到标签结束
                        Log.i("loadWithPull","END_TAG:"+parser.getName());
                        break;
                }
                eventType=parser.next();//进入下一个事件处理
            }
            Log.i("loadWithPull","END_DOCUMENT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






