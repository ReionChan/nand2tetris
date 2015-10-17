package org.reion;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Xml 工具类
 * 
 * @author Reion
 * 
 */
public final class XmlUtils {

	/**
	 * 私有构造器.
	 */
	private XmlUtils() {
	}

	/**
	 * 获取Document对象
	 * 
	 * @return
	 */
	public static Document getDocument() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = builder.newDocument();
		return doc;
	}

	/**
	 * 将DOM对象写入XML文件
	 * 
	 * @param xmlFile
	 *            xml文件
	 * @param rootNode
	 *            根元素
	 */
	public static void writeXml(final File xmlFile, final Node rootNode) {
		TransformerFactory tff = TransformerFactory.newInstance();
		try {
			Transformer tf = tff.newTransformer();
			FileOutputStream fos = new FileOutputStream(xmlFile);
			tf.transform(new DOMSource(rootNode), new StreamResult(fos));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
