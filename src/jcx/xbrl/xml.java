package jcx.xbrl;
import java.net.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class xml {

    static public void save(String s, Document document)
    {
        try
        {
            BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(s));
            String s1 = print(document);
            bufferedwriter.write(s1, 0, s1.length());
            bufferedwriter.flush();
            bufferedwriter.close();
//            System.out.println("Saving..." + s);
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
    }

    static public void save(String s, Element element)
    {
        save(s, "Big5", element);
    }

    static public void save(String s, String s1, Element element)
    {
        try
        {
            BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(s));
            String s2 = "<?xml version=\"1.0\" encoding=\"" + s1 + "\"?>\r\n";
            s2 = s2 + print(element);
            bufferedwriter.write(s2, 0, s2.length());
            bufferedwriter.flush();
            bufferedwriter.close();
            System.out.println("Saving..." + s);
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
    }

   static  public void save(OutputStream outputstream, String s, Element element)
    {
        try
        {
            String s1 = "<?xml version=\"1.0\" encoding=\"" + s + "\"?>\r\n";
            s1 = s1 + print(element);
            outputstream.write(s1.getBytes(), 0, s1.getBytes().length);
            outputstream.flush();
            outputstream.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
    }
	static public byte[] getBytes(String s) throws IOException{
		FileInputStream fin=new FileInputStream(s);
		int c=0;
		byte b[]=new byte[4096];
		ByteArrayOutputStream ba=new ByteArrayOutputStream();
		while((c=fin.read(b))!=-1){
			ba.write(b,0,c);
		}
		fin.close();
		return ba.toByteArray();

	}
    static public Document load(String s)
        throws FileNotFoundException
    {
        Document document = null;
        try {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            document = documentbuilder.parse(s);
//			byte bb[]=getBytes(s);
//            document = documentbuilder.parse(new ByteArrayInputStream(bb));

//            System.out.println("Loading..." + s);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            throw filenotfoundexception;
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
        return document;
    }

    static public Document parse(String s)
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
//            System.out.println("Loading..." + s);
            document = documentbuilder.parse(new InputSource(new ByteArrayInputStream(s.getBytes())));
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
        return document;
    }

    static public Document parse(InputStream inputstream)
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            document = documentbuilder.parse(inputstream);
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
        return document;
    }
/*
    static public Document parse(ByteArrayInputStream bytearrayinputstream)
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            document = documentbuilder.parse(new InputSource(bytearrayinputstream));
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
        return document;
    }
*/
    static public Document getNewDocument()
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            document = documentbuilder.newDocument();
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.err);
        }
        return document;
    }

    static public String getTextStringInChild(Element element)
    {
        String s = null;
        if(element != null && element.hasChildNodes())
        {
            Node node = element.getFirstChild();
            if(node.getNodeType() == 3)
            {
                Text text = (Text)node;
                s = text.getNodeValue();
            } else
            {
                s = print(element);
            }
        }
        return s;
    }

    static public Text setTextStringInChild(Element element, String s)
    {
        Text text = null;
        if(element != null)
            if(element.hasChildNodes())
            {
                Node node = element.getFirstChild();
                if(node.getNodeType() == 3)
                {
                    text = (Text)node;
                    text.setNodeValue(s);
                }
            } else
            {
                text = element.getOwnerDocument().createTextNode(s);
                element.appendChild(text);
            }
        return text;
    }

    static public Element getMainElement(Element element, String s)
    {
        Element element1 = null;
        Object obj = null;
        boolean flag = false;
        if(element != null)
        {
            for(Node node = element.getFirstChild(); node != null && !flag; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element2 = (Element)node;
                    if(element2.getTagName().compareTo(s) == 0)
                    {
                        flag = true;
                        element1 = element2;
                    }
                }

        }
        return element1;
    }

    static public Element getDataByID(Element element, String s)
    {
        Element element1 = null;
        boolean flag = false;
        if(element != null)
        {
            for(Node node = element.getFirstChild(); node != null && !flag; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element2 = (Element)node;
                    if(s.compareTo(element2.getAttribute("id")) == 0)
                    {
                        flag = true;
                        element1 = element2;
                    }
                }

        }
        return element1;
    }

    static public Element getChildByName(Element element, String s)
    {
        Element element2 = null;
        boolean flag = false;
        if(element != null)
        {
            for(Node node = element.getFirstChild(); node != null && !flag; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    if(element1.getTagName().compareTo(s) == 0)
                    {
                        flag = true;
                        element2 = element1;
                    }
                }

        }
        return element2;
    }

    static public Element getFirstElement(Node node)
    {
        Element element = null;
        boolean flag = false;
        if(node != null)
        {
            for(Node node1 = node.getFirstChild(); node1 != null && !flag; node1 = node1.getNextSibling())
                if(node1.getNodeType() == 1)
                {
                    element = (Element)node1;
                    flag = true;
                }

        }
        return element;
    }

    static public Element getDataByValue(Element element, String s, String s1)
    {
        return getChildByValue(element, s, s1);
    }

   static  public Element getChildByValue(Element element, String s, String s1)
    {
        Element element1 = null;
        boolean flag = false;
        if(element != null)
        {
            for(Node node = element.getFirstChild(); node != null && !flag; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element2 = (Element)node;
                    if(s1.compareTo(element2.getAttribute(s)) == 0)
                    {
                        flag = true;
                        element1 = element2;
                    }
                }

        }
        return element1;
    }

    static public Element getChildBy2Value(Element element, String s, String s1, String s2, String s3)
    {
        Element element1 = null;
        boolean flag = false;
        if(element != null)
        {
            for(Node node = element.getFirstChild(); node != null && !flag; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element2 = (Element)node;
                    if(element2.getAttribute(s).compareTo(s1) == 0 && element2.getAttribute(s2).compareTo(s3) == 0)
                    {
                        flag = true;
                        element1 = element2;
                    }
                }

        }
        return element1;
    }

    static public void cloneElement(Node node, Node node1)
    {
        short word0 = node.getNodeType();
        switch(word0) {
        case 1: // '\001'
            Element element = (Element)node1;
            Attr aattr[] = sortAttributes(node.getAttributes());
            for(int i = 0; i < aattr.length; i++)
            {
                Attr attr = aattr[i];
                element.setAttribute(attr.getNodeName(), attr.getNodeValue());
            }

            NodeList nodelist = node.getChildNodes();
            for(int j1 = 0; j1 < nodelist.getLength(); j1++){
				Node n0=nodelist.item(j1);

				Node node2=node1.getOwnerDocument().createElement(n0.getNodeName());
				node1.appendChild(node2);

				cloneElement(n0,node2);
			}


            // fall through

        case 3: // '\003'
        default:
            return;
        }
    }

   static  public String getNameSpace(String s, String s1)
    {
        String s2 = null;
        int i = s.indexOf(s1);
        if(i != -1)
            s2 = s.substring(0, i);
        return s2;
    }

    static public String getElementName(String s, String s1)
    {
        String s2 = null;
        int i = s.indexOf(s1);
        if(i != -1)
            s2 = s.substring(i + 1);
        else
            s2 = s;
        return s2;
    }

    static public String getNameSpace(String s)
    {
        String s1 = null;
        int i = s.indexOf(":");
        if(i != -1)
            s1 = s.substring(0, i);
        return s1;
    }

    static public String getElementName(String s)
    {
        String s1 = null;
        int i = s.indexOf(":");
        if(i != -1)
            s1 = s.substring(i + 1);
        else
            s1 = s;
        return s1;
    }


    static public String print(Node node){
		return print1(node,0);
	}
    static public String print1(Node node,int level){
		String tab1="";
		for(int i=0;i<level;i++) tab1+="\t";
        boolean flag = false;
        boolean flag1 = true;
        StringBuffer stringbuffer = new StringBuffer();
        if(node == null)
        {
            stringbuffer.append("<html/>");
            return stringbuffer.toString();
        }
        short word0 = node.getNodeType();
//System.err.println("--type="+word0+" ->"+node.CDATA_SECTION_NODE);
        switch(word0)
        {
        case 2: // '\002'
        case 6: // '\006'
        case 8: // '\b'
            if(flag) {
                stringbuffer.append(normalize(node.getNodeValue()));
            } else {
	            stringbuffer.append(tab1);
                stringbuffer.append("<!-- ");
                stringbuffer.append(node.getNodeValue());
                stringbuffer.append("-->\r\n");
            }
            break;
        default:
            break;

        case 9: // '\t'
            stringbuffer.append("<?xml version=\"1.0\" encoding=\"Big5\"?>\r\n");
            NodeList nodelist = node.getChildNodes();
            for(int j1 = 0; j1 < nodelist.getLength(); j1++)
                stringbuffer.append(print1(nodelist.item(j1),level+1));

            break;

        case 1: // '\001'
            stringbuffer.append(tab1);
            stringbuffer.append('<');
            stringbuffer.append(node.getNodeName());
            Attr aattr[] = sortAttributes(node.getAttributes());
            for(int i = 0; i < aattr.length; i++)
            {
                Attr attr = aattr[i];
                stringbuffer.append(' ');
                stringbuffer.append(attr.getNodeName());
                stringbuffer.append("=\"");
                stringbuffer.append(normalize(attr.getNodeValue()));
                stringbuffer.append("\"");
            }

            NodeList nodelist2 = node.getChildNodes();
            if(nodelist2 == null)
                break;
            int l = nodelist2.getLength();
            if(l > 0)
            {
				boolean only_text_child=false;
				if(l==1){
					if(nodelist2.item(0).getNodeType()==3){
						only_text_child=true;
					}
				}

				if(only_text_child){
					stringbuffer.append(">");
					for(int j = 0; j < l; j++){
						stringbuffer.append(print1(nodelist2.item(j),level+1));
					}

					stringbuffer.append("</");
					stringbuffer.append(node.getNodeName());
					stringbuffer.append(">\r\n");
				} else {
					stringbuffer.append(">\r\n");
					for(int j = 0; j < l; j++){
						stringbuffer.append(print1(nodelist2.item(j),level+1));
					}

		            stringbuffer.append(""+tab1);
					stringbuffer.append("</");
					stringbuffer.append(node.getNodeName());
					stringbuffer.append(">\r\n");
				}
                break;
            }
            if(flag1)
            {
                if(node.getNodeName().toUpperCase().compareTo("SCRIPT") != 0)
                {
                    stringbuffer.append("/>");
                } else
                {
                    stringbuffer.append('>');
                    stringbuffer.append("</");
                    stringbuffer.append(node.getNodeName());
                    stringbuffer.append(">\r\n");
                }
            } else
            {
                stringbuffer.append("/>\r\n");
            }
            break;

        case 3: // '\003'
            stringbuffer.append(node.getNodeValue());
            break;

        case 5: // '\005'
            if(flag)
            {
                NodeList nodelist1 = node.getChildNodes();
                if(nodelist1 == null)
                    break;
                int i1 = nodelist1.getLength();
                for(int k = 0; k < i1; k++)
                    stringbuffer.append(print1(nodelist1.item(k),level+1));

            } else {
                stringbuffer.append('&');
                stringbuffer.append(node.getNodeName());
                stringbuffer.append(';');
            }
            break;

        case 4: // '\004'
            if(flag)
            {
                stringbuffer.append(normalize(node.getNodeValue()));
            } else
            {
                stringbuffer.append("<![CDATA[");
                stringbuffer.append(node.getNodeValue());
                stringbuffer.append("]]>\r\n");
            }
            break;

        case 7: // '\007'
            stringbuffer.append(tab1);
            stringbuffer.append("<?");
            stringbuffer.append(node.getNodeName());
            String s = node.getNodeValue();
            if(s != null && s.length() > 0)
            {
                stringbuffer.append(' ');
                stringbuffer.append(s);
            }
            stringbuffer.append("?>\r\n");
            break;
        }
        return stringbuffer.toString();
    }

    static public Attr[] sortAttributes(NamedNodeMap namednodemap)
    {
        int i = namednodemap == null ? 0 : namednodemap.getLength();
        Attr aattr[] = new Attr[i];
        for(int j = 0; j < i; j++)
            aattr[j] = (Attr)namednodemap.item(j);

        return aattr;
    }

    static protected String normalize(String s)
    {
        boolean flag = false;
        StringBuffer stringbuffer = new StringBuffer();
        int i = s == null ? 0 : s.length();
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            switch(c)
            {
            case 60: // '<'
                stringbuffer.append("&lt;");
                break;

            case 62: // '>'
                stringbuffer.append("&gt;");
                break;

            case 38: // '&'
                stringbuffer.append("&amp;");
                break;

            case 34: // '"'
                stringbuffer.append("&quot;");
                break;

            case 39: // '\''
                stringbuffer.append("'");
                break;

            case 10: // '\n'
            case 13: // '\r'
                if(flag)
                {
                    stringbuffer.append("&#");
                    stringbuffer.append(Integer.toString(c));
                    stringbuffer.append(';');
                }
                break;

            default:
                stringbuffer.append(c);
                break;
            }
        }

        return stringbuffer.toString();
    }

}