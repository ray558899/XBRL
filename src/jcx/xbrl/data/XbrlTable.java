package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.*;


public class XbrlTable{

    protected Element eRoot;
    protected Vector objFieldList;
    protected Vector objRowList;
    protected XbrlTaxonomy objTaxonomy;

    public XbrlTable(XbrlTaxonomy xbrltaxonomy, Element element)
    {
        eRoot = null;
        objFieldList = null;
        objRowList = null;
        objTaxonomy = null;
        objTaxonomy = xbrltaxonomy;
        eRoot = element;
        objFieldList = new Vector();
        objRowList = new Vector();
    }

    public void createTable(Element element, Hashtable hashtable, String s, String s1, XbrlContextList xbrlcontextlist, String s2, String s3, 
            String s4, boolean flag)  throws Exception
    {
        NodeList nodelist = element.getElementsByTagName(s3);
        if(nodelist.getLength() > 0)
        {
            Element element1 = (Element)nodelist.item(0);
            getColumnMember(element1, flag);
        }
        nodelist = element.getElementsByTagName(s4);
        if(nodelist.getLength() > 0)
        {
            Element element2 = (Element)nodelist.item(0);
            getRowMember(element2, flag);
        }
        fillData(hashtable, s, s1, xbrlcontextlist, s2);
    }

    public void getRowMember(Element element, boolean flag)
    {
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            if(node.getNodeType() == 1)
            {
                Element element1 = (Element)node;
                objRowList.add(element1.getTagName());
                if(flag)
                    getRowMember(element1, flag);
            }

    }

    public void getColumnMember(Element element, boolean flag)
    {
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            if(node.getNodeType() == 1)
            {
                Element element1 = (Element)node;
                objFieldList.add(element1.getTagName());
                if(flag)
                    getColumnMember(element1, flag);
            }

    }

    public void fillData(Hashtable hashtable, String s, String s1, XbrlContextList xbrlcontextlist, String s2) throws Exception
    {
        Object obj = null;
        Object obj1 = null;
        Element element4 = eRoot.getOwnerDocument().createElement("head");
        eRoot.appendChild(element4);
        Element element = eRoot.getOwnerDocument().createElement("caption");
        element4.appendChild(element);
        for(int j = 0; j < objFieldList.size(); j++)
        {
            String s4 = (String)objFieldList.get(j);
            Element element1 = eRoot.getOwnerDocument().createElement(s4);
            element4.appendChild(element1);
            jcx.xbrl.xml.setTextStringInChild(element1, objTaxonomy.getLabelByID(s4, objTaxonomy.getDefaultLanguage()));
        }

        for(int i = 0; i < objRowList.size(); i++)
        {
            String s3 = (String)objRowList.get(i);
            Element element5 = eRoot.getOwnerDocument().createElement(s3);
            eRoot.appendChild(element5);
            Element element2 = eRoot.getOwnerDocument().createElement("caption");
            element5.appendChild(element2);
            jcx.xbrl.xml.setTextStringInChild(element2, objTaxonomy.getLabelByID(s3, objTaxonomy.getDefaultLanguage()));
            for(int k = 0; k < objFieldList.size(); k++)
            {
                String s5 = (String)objFieldList.get(k);
                Element element3 = eRoot.getOwnerDocument().createElement(s5);
                element5.appendChild(element3);
                XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(s3);
                if(xbrlelementpool != null)
                {
                    XbrlContext xbrlcontext;
                    if(xbrlelementpool.objSchema.getPeriodType().equals("duration"))
                        xbrlcontext = xbrlcontextlist.containsDimensionMemberDurationContext(s2, objTaxonomy.getNameByID(s5), s, s1);
                    else
                        xbrlcontext = xbrlcontextlist.containsDimensionMemberInstantContext(s2, objTaxonomy.getNameByID(s5), s1);

                    XbrlElement XbrlElement = xbrlelementpool.get(xbrlcontext);

                    if(XbrlElement != null){
						jcx.xbrl.xml.setTextStringInChild(element3, XbrlElement.getValue());
					}
                }
            }

        }

    }

}
