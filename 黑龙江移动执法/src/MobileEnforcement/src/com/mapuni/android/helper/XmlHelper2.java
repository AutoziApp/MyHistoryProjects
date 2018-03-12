package com.mapuni.android.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHelper2 {
    private static XmlHelper2 util;

    public static XmlHelper2 getInstance() {

        if (util == null) {
            util = new XmlHelper2();

        }
        return util;

    }

    private XmlHelper2() {
        super();
    }

    public <T> List<T> getList(Class<T> c, String str, String matchesNode) {

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
        List<T> list = getList(c, in, matchesNode);
        return list;

    }

    public <T> T getObject(Class<T> c, String str, String matchesNode) {
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        T obj = getObject(c, in, matchesNode);
        return obj;

    }

    public <T> List<T> getList(Class<T> c, InputStream in, String matchesNode) {
        XMLListHandle<T> handle = new XMLListHandle<T>(c, in, matchesNode);
        List<T> list = handle.getList();
        return list;

    }

    public <T> T getObject(Class<T> c, InputStream in, String matchesNode) {
        XMLListHandle<T> handle = new XMLListHandle<T>(c, in, matchesNode);
        T obj = (T) handle.getObject();
        return obj;

    }

    private class XMLListHandle<T> extends DefaultHandler {
        private T obj;
        private Stack<String> stack = new Stack<String>();
        private Class<T> c = null;
        private Field[] fields = null;
        private List<T> list = new ArrayList<T>();
        private String matchesNode;

        public XMLListHandle(Class<T> c, InputStream in, String matchesNode) {
            this.matchesNode = matchesNode;
            this.c = c;
            this.fields = c.getDeclaredFields();
            try {
                this.obj =  c.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = spf.newSAXParser();
                InputSource is = new InputSource(new InputStreamReader(in,
                        "UTF-8"));
                saxParser.parse(is, this);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void startElement(String namespaceURI, String localName,
                String qName, Attributes attr) throws SAXException {
            stack.push(qName);
            try {
                if (qName.equalsIgnoreCase(matchesNode)) {
                    this.obj =  c.newInstance();
                }

            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }

            for (int i = 0; i < attr.getLength(); i++) {
                for (Field field : fields) {
                    String aQName = attr.getQName(i);
                    String aLName = attr.getLocalName(i);
                    String fName = field.getName();
                    if (fName.equalsIgnoreCase(aQName)
                            || fName.equalsIgnoreCase(aLName)) {
                        try {
                            field.setAccessible(true);
                            if (obj != null) {
                                field.set(obj, attr.getValue(aQName));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        @Override
        public void characters(char[] ch, int start, int end)
                throws SAXException {
            String tag = stack.peek();
            for (Field field : fields) {
                if (tag.equalsIgnoreCase(field.getName())) {
                    String string = new String(ch, start, end);
                    try {
                        field.setAccessible(true);
                        if (obj != null) {
                            field.set(obj, string);
                        }

                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            String tag = stack.peek();
            if (tag.equalsIgnoreCase(matchesNode) && obj != null) {
                list.add(obj);
            }
            stack.pop();

        }

        public List<T> getList() {
            return list;
        }

        public T getObject() {
            return obj;

        }

    }

}
