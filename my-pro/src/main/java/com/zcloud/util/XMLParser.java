package com.zcloud.util;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLParser {

    public static <T> T getObjectFromXML(String xml,Class<T> clazz){
        T obj;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e1) {
            e1.printStackTrace();
            return null;
        }
        try {
            SAXReader reader = new SAXReader();
            Document doc;
            doc = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
//            doc = reader.read(responseString);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            Field field;
            for (Element element : list) {
                try {
                    field = clazz.getDeclaredField(element.getName());
                    field.setAccessible(true);
                    field.set(obj, element.getText());
                } catch (Exception e) {
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static Map<String, Object> getMapFromXML(String responseString) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            SAXReader reader = new SAXReader();
            Document doc;
            doc = reader.read(new ByteArrayInputStream(responseString.getBytes("UTF-8")));
//            doc = reader.read(responseString);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element element : list) {
                map.put(element.getName(), element.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String getXmlStringFromMap(Map<String, Object> map){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<xml>");
        for (String key : map.keySet()) {
            buffer.append("<").append(key).append(">")
            .append("<![CDATA[")
            .append(String.valueOf(map.get(key)))
            .append("]]>")
            .append("</").append(key).append(">");
        }
        buffer.append("</xml>");
        return buffer.toString();
    }
}
