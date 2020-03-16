package com.by.lizhiyoupin.app.common.utils.storage.sd;

import android.text.TextUtils;

import com.by.lizhiyoupin.app.common.log.LZLog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

public class Doms {
    public static final String TAG = Doms.class.getSimpleName();

    /**
     * DOM解析
     * 把文档中的所有元素，按照其出现的层次关系，解析成一个个Node对象(节点)。
     * 缺点是消耗大量的内存。
     *
     * @param xmlFilePath 文件
     * @return Document
     */
    public static Document loadWithDom(String xmlFilePath) {
        try {
            File file = new File(xmlFilePath);
            if (!file.exists()) {
                throw new RuntimeException("not find file:" + xmlFilePath);
            } else {
                InputStream inputStream = new FileInputStream(file);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return document;
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
    }

    public static Node getElementByNameAttribute(Element element, String name) {
        NodeList childNodes = element.getChildNodes();
        if (childNodes != null && childNodes.getLength() > 0) {
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                Node node = childNodes.item(i);
                if (node == null) {
                    continue;
                }
                NamedNodeMap attributes = node.getAttributes();
                if (attributes != null) {
                    Node nodeName = attributes.getNamedItem("name");
                    if (nodeName != null && TextUtils.equals(nodeName.getNodeValue(), name)) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 保存修改后的Doc
     * http://blog.csdn.net/franksun1991/article/details/41869521
     *
     * @param doc doc
     * @return 是否成功
     */
    public static boolean saveXmlWithDom(Document doc, File file) {
        if (doc == null) {
            return false;
        }
        OutputStream outputStream = null;
        try {
            //将内存中的Dom保存到文件
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            //设置输出的xml的格式，utf-8
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty("version", doc.getXmlVersion());
            DOMSource source = new DOMSource(doc);
            //打开输出流
            if (!file.exists()) {
                boolean success = file.createNewFile();
               LZLog.i(TAG, "saveXmlWithDom,createNewFile:" + success);
            }
            outputStream = new FileOutputStream(file);
            //xml的存放位置
            StreamResult src = new StreamResult(outputStream);
            transformer.transform(source, src);
            return true;
        } catch (Exception e) {
            LZLog.e(TAG, e.getMessage(), e);
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LZLog.e(TAG, e.getMessage(), e);
                }
            }
        }
    }
}
