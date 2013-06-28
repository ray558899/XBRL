package jcx.xbrl.taxonomy;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;


public class XbrlSchema{

    protected Element eDefinition;
    protected Hashtable hSchemaTable;
    protected Hashtable hSchemaTableByName;
    protected Hashtable hSchemaFileTable;
    protected String strSchemaFilePath;
    protected String PREFIX;
    protected String URI;
	Vector PREFIXS=new Vector();
	Vector URIS=new Vector();
	protected Vector hv=new Vector();

    public XbrlSchema()
    {
        hSchemaTable = null;
        hSchemaTableByName = null;
        hSchemaFileTable = null;
        strSchemaFilePath = null;
        PREFIX = null;
        URI = null;
        hSchemaTable = new Hashtable();
        hSchemaTableByName = new Hashtable();
        hSchemaFileTable = new Hashtable();
    }
/**
*  取得 所有 Schema 物件的清單
*
* @return  Vector,內含所有的 ID,形態為 String
*
*/

    public Vector getSchemaList()
    {
        return hv;
    }

/**
*  取得 所有 Schema 物件的Hashtable
*
* @return  Hashtable
*
*/

    public Hashtable getSchemaTable()
    {
        return hSchemaTable;
    }

    protected int load(String s, Element element, Vector vector)
    {
        boolean flag = false;
        int k = 0;
        eDefinition = element;
		String PREFIX=null;
        if(hSchemaFileTable.get(element.getAttribute("targetNamespace")) == null)
        {
            if(element.getAttribute("targetNamespace").indexOf("entry") == -1) {
                URI = element.getAttribute("targetNamespace");
                NamedNodeMap namednodemap = element.getAttributes();
                for(int i = 0; i < namednodemap.getLength() && !flag; i++)
                {
                    Attr attr = (Attr)namednodemap.item(i);
                    if(attr.getNodeValue().equals(URI) && !attr.getNodeName().equals("targetNamespace"))
                    {
                        PREFIX = jcx.xbrl.xml.getElementName(attr.getNodeName(), ":");
						if(this.PREFIX==null) this.PREFIX=PREFIX;
						PREFIXS.addElement(PREFIX);
						URIS.addElement(URI);
//			System.out.println("Load Schema " + strSchemaFilePath+"  PREFIX="+PREFIX);
                        flag = true;
                    }
                }

            }

            for(Node node = eDefinition.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    String s1 = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");
                    if(s1.equals("element"))
                    {
                        XbrlSchemaElement xbrlschemaelement = new XbrlSchemaElement(element1);
//if(element1.getAttribute("id").indexOf("OtherSupplementalInformation")!=-1) System.err.println("----- PREFIX="+PREFIX +" id="+element1.getAttribute("id")+" "+element1);
						xbrlschemaelement.PREFIX=PREFIX;
                        hSchemaTable.put(element1.getAttribute("id"), xbrlschemaelement);
						hv.addElement(element1.getAttribute("id"));
                        hSchemaTableByName.put(element1.getAttribute("name"), xbrlschemaelement);
                        k++;
                    } else {
						if(s1.equalsIgnoreCase("import") && strSchemaFilePath != null && !element1.getAttribute("schemaLocation").startsWith("http"))
						{
//							String s2 = s+ element1.getAttribute("schemaLocation");
							String s2 = strSchemaFilePath+"/"+element1.getAttribute("schemaLocation");

//			System.out.println("Load Schema " + strSchemaFilePath+"  "+element1.getAttribute("schemaLocation"));
							File f=new File(s2);
							if(f.exists()){
								try{
									Document document = jcx.xbrl.xml.load(s2);
									Element element2 = document.getDocumentElement();
									load(s2, element2, vector);
								}catch(Exception exception){
									System.out.println(exception.getMessage());
								}
							} else {
								System.out.println("-- File not found ! ---");
								System.out.println("-- Path="+ strSchemaFilePath);
								System.out.println("-- File="+ element1.getAttribute("schemaLocation"));
								System.out.println("---------- end --------\r\n");
							}
						}
					}
                }

            hSchemaFileTable.put(element.getAttribute("targetNamespace"), element);

//            System.out.println("Load Schema " + element.getAttribute("targetNamespace"));

            NodeList nodelist = element.getElementsByTagName("link:linkbaseRef");
            for(int j = 0; j < nodelist.getLength(); j++)
            {
                Element element3 = (Element)nodelist.item(j);
                String s3 = getPath1(s,element3.getAttribute("xlink:href"));
//System.err.println("--s3="+s3);
                vector.add(s3);
            }

        } else
        {
            System.out.println("Schema is existed..." + element.getAttribute("targetNamespace"));
        }
        return k;
    }
    protected String getPath0(String s)
    {
        String s1 = null;
        if(s != null)
        {
            int i = 0;
            int k = -1;
            while(i < s.length() && i != -1) 
            {
                int j = s.indexOf(47, i);
                if(j != -1)
                {
                    if(j != s.length() - 1)
                        k = j;
                    i = j + 1;
                } else
                {
                    i = -1;
                }
            }
            if(k == -1)
                s1 = "/";
            else
                s1 = s.substring(0, k + 1);
        }
        return s1;
    }
    protected String getPath1(String s, String s1)
    {
        String s2 = null;
        s2 = s.replace('\\', '/');
        if(!s2.substring(s2.length() - 1, s2.length()).equals("/"))
        {
            int k = s2.lastIndexOf(47);
            s2 = s2.substring(0, k + 1);
        }
        for(int i = 0; i < s1.length() && i != -1;)
        {
            int j = s1.indexOf(47, i);
            if(j != -1)
            {
                String s3 = s1.substring(i, j);
                if(!s3.equals("."))
                    if(s3.equals(".."))
                    {
                        s2 = getPath0(s2);
                    } else
                    {
                        s2 = s2 + s3;
                        s2 = s2 + "/";
                    }
                i = j + 1;
            } else
            {
                String s4 = s1.substring(i, s1.length());
                s2 = s2 + s4;
                i = -1;
            }
        }

        return s2;
    }

/**
*  根據ID取得 XbrlSchemaElement 物件
*
* @param   ID .
* @return  回傳 XbrlSchemaElement 物件
*
*/

	public XbrlSchemaElement getSchemaElement(String ID)
    {
        XbrlSchemaElement xbrlschemaelement = null;
        xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(ID);
        return xbrlschemaelement;
    }
/**
*  根據ID取得 XbrlSchemaElement 的名稱
*
* @param   id .
* @return  回傳 XbrlSchemaElement 的名稱
*
*/
    public String getNameByID(String id)
    {
        String s1 = null;
        XbrlSchemaElement xbrlschemaelement = null;
        xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(id);
        if(xbrlschemaelement != null) s1 = xbrlschemaelement.getName();
        return s1;
    }

/**
*  根據名稱取得 XbrlSchemaElement 的ID
*
* @param   name 名稱 .
* @return  回傳 XbrlSchemaElement 的id
*
*/
    public String getIDByName(String name)
    {
//System.err.println("--"+hSchemaTableByName);
        String s1 = null;
        XbrlSchemaElement xbrlschemaelement = null;
        xbrlschemaelement = (XbrlSchemaElement)hSchemaTableByName.get(name);
        if(xbrlschemaelement != null) s1 = xbrlschemaelement.getID();
        return s1;
    }

    protected Enumeration getElementIDs()
    {
        return hSchemaTable.keys();
    }

    protected void print()
    {
        XbrlSchemaElement xbrlschemaelement;
        for(Enumeration enumeration = hSchemaTable.keys(); enumeration.hasMoreElements(); System.out.println(xbrlschemaelement.getID() + "," + xbrlschemaelement.getType() + "," + xbrlschemaelement.getParentType()))
        {
            String s = (String)enumeration.nextElement();
            xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(s);
        }

    }

    protected void exportXML(Element element)
    {
        XbrlSchemaElement xbrlschemaelement;
        Element element1;
        for(Enumeration enumeration = hSchemaTable.keys(); enumeration.hasMoreElements(); element1.setAttribute("parentType", xbrlschemaelement.getParentType()))
        {
            String s = (String)enumeration.nextElement();
            element1 = element.getOwnerDocument().createElement(s);
            element.appendChild(element1);
            xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(s);
            element1.setAttribute("type", xbrlschemaelement.getType());
        }

    }
}
