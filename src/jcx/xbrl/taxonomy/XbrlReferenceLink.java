package jcx.xbrl.taxonomy;

import jcx.xbrl.exception.*;
import java.util.*;
import org.w3c.dom.*;

class XbrlReferenceLink
{

    public XbrlReferenceLink()
    {
        hSchemaTable = null;
        hLocatorTable = new Hashtable();
    }

    protected void load(Element element)
        throws TaxonomyFormatErrorException
    {
        Hashtable hashtable = new Hashtable();
        Hashtable hashtable1 = new Hashtable();
        Vector vector = new Vector();
        eReferenceLink = element;
        if(eReferenceLink != null)
        {
            for(Node node = eReferenceLink.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    if(element1.getAttribute("xlink:type").compareTo("resource") == 0)
                    {
                        XbrlReferenceResource xbrlreferenceresource = new XbrlReferenceResource(element1);
                        hashtable.put(element1.getAttribute("xlink:label"), xbrlreferenceresource);
                    } else
                    if(element1.getAttribute("xlink:type").compareTo("arc") == 0)
                    {
                        XlinkArc xlinkarc = new XlinkArc(element1);
                        vector.add(xlinkarc);
                    } else
                    if(element1.getAttribute("xlink:type").compareTo("locator") == 0)
                    {
                        XlinkLocator xlinklocator = new XlinkLocator(jcx.xbrl.xml.getElementName(element1.getAttribute("xlink:href"), "#"), element1.getAttribute("xlink:label"), element1);
                        hLocatorTable.put(xlinklocator.strID, xlinklocator);
                        hashtable1.put(element1.getAttribute("xlink:label"), xlinklocator);
                    }
                }

        }
        for(int i = 0; i < vector.size(); i++)
        {
            XlinkArc xlinkarc1 = (XlinkArc)vector.get(i);
            XlinkLocator xlinklocator1 = (XlinkLocator)hashtable1.get(xlinkarc1.eData.getAttribute("xlink:from"));
            xlinklocator1.add(xlinkarc1);
            XlinkResource xlinkresource = (XlinkResource)hashtable.get(xlinkarc1.eData.getAttribute("xlink:to"));
            xlinkarc1.add(xlinkresource);
        }

    }

    protected XbrlReferenceResource getReferenceByID(String s)
        throws Exception
    {
        XbrlReferenceResource xbrlreferenceresource = null;
        XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
        if(xlinklocator != null)
        {
            if(xlinklocator.hTable.size() > 0)
            {
                XlinkArc xlinkarc = (XlinkArc)xlinklocator.hTable.get(0);
                if(xlinkarc != null && xlinkarc.objToResource.size() > 0)
                    xbrlreferenceresource = (XbrlReferenceResource)xlinkarc.objToResource.get(0);
            }
        } else
        {
            throw new Exception("Element id not found..." + s);
        }
        return xbrlreferenceresource;
    }

    protected Element getCollection(Element element)
    {
        Element element1 = null;
        Object obj = null;
        for(Enumeration enumeration = hLocatorTable.keys(); enumeration.hasMoreElements();)
        {
            String s = (String)enumeration.nextElement();
            XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
            if(xlinklocator != null)
            {
                XlinkArc xlinkarc = (XlinkArc)xlinklocator.hTable.get(0);
                if(xlinkarc != null)
                {
                    XlinkResource xlinkresource = (XlinkResource)xlinkarc.objToResource.get(0);
                    element1 = element.getOwnerDocument().createElement("resource");
                    element1.setAttribute("id", s);
                    for(Node node = xlinkresource.eData.getFirstChild(); node != null; node = node.getNextSibling())
                        if(node.getNodeType() == 1)
                        {
                            Element element2 = (Element)node;
                            element1.setAttribute(element2.getTagName(), jcx.xbrl.xml.getTextStringInChild(element2));
                        }

                    element.appendChild(element1);
                }
            }
        }

        return element1;
    }

    protected Element eReferenceLink;
    protected Hashtable hSchemaTable;
    Hashtable hLocatorTable;
}
