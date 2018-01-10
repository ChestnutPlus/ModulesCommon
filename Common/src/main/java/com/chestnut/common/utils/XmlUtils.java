package com.chestnut.common.utils;

import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
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
                return documentBuilder.parse(inputStream);
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Observable<Boolean> saveXmlWithDomRx(Document doc,String saveXmlFilePath) {
        return Observable.just(saveXmlWithDom(doc, saveXmlFilePath));
    }


    public static void loadWithPull(String xmlFilePath) {
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






