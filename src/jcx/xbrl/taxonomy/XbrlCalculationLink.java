package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import java.io.PrintStream;
import java.util.*;
import org.w3c.dom.*;


class XbrlCalculationLink
{

    public XbrlCalculationLink()
    {
        hSchemaTable = null;
        hLocatorTable = new Hashtable();
    }

    protected void scan(Element element)
    {
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            if(node.getNodeType() == 1)
            {
                Element element1 = (Element)node;
                if(element1.getAttribute("xlink:type").equals("locator"))
                {
                    XlinkLocator xlinklocator = new XlinkLocator(jcx.xbrl.xml.getElementName(element1.getAttribute("xlink:href"), "#"), element1.getAttribute("xlink:label"), element1);
                    vector1.add(xlinklocator);
                } else
                if(element1.getAttribute("xlink:type").equals("arc"))
                {
                    XlinkArc xlinkarc = new XlinkArc(element1);
                    vector.add(xlinkarc);
                }
            }

        for(int k = 0; k < vector1.size(); k++)
        {
            XlinkLocator xlinklocator2 = (XlinkLocator)vector1.get(k);
            for(int i = 0; i < vector.size(); i++)
            {
                XlinkArc xlinkarc1 = (XlinkArc)vector.get(i);
                if(xlinkarc1.eData.getAttribute("xlink:from").equals(xlinklocator2.strLabel))
                    xlinklocator2.add(xlinkarc1);
                else
                if(xlinkarc1.eData.getAttribute("xlink:to").equals(xlinklocator2.strLabel))
                {
                    xlinklocator2.objFromArc = xlinkarc1;
                    xlinkarc1.add(xlinklocator2);
                }
            }

        }

        for(int l = 0; l < vector1.size(); l++)
        {
            XlinkLocator xlinklocator3 = (XlinkLocator)vector1.get(l);
            if(xlinklocator3.objFromArc == null)
            {
                XlinkLocator xlinklocator4 = (XlinkLocator)hLocatorTable.get(xlinklocator3.strID);
                if(xlinklocator4 != null)
                {
                    for(int j = 0; j < xlinklocator3.hTable.size(); j++)
                    {
                        XlinkArc xlinkarc2 = (XlinkArc)xlinklocator3.hTable.get(j);
                        XlinkLocator xlinklocator1 = (XlinkLocator)xlinkarc2.getFirstResource();
                        if(xlinklocator4.getLocator(xlinklocator1.strID) == null)
                            xlinklocator4.add(xlinkarc2);
                    }

                } else
                {
                    hLocatorTable.put(xlinklocator3.strID, xlinklocator3);
                }
            } else
            {
                hLocatorTable.put(xlinklocator3.strID, xlinklocator3);
            }
        }

    }

    protected void createNodeFromLocator(XbrlCalculationTree xbrlcalculationtree, XlinkLocator xlinklocator, XlinkLocator xlinklocator1, XlinkArc xlinkarc)
    {
        XbrlSchemaElement xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(xlinklocator.strID);
        if(xbrlschemaelement != null)
        {
            XbrlCalculationTreeNode xbrlcalculationtreenode;
            if(xlinklocator1 == null)
            {
                xbrlcalculationtreenode = xbrlcalculationtree.appendChild(null, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, 0.0D);
            } else{
                XbrlCalculationTreeNode xbrlcalculationtreenode1 = xbrlcalculationtree.appendChild(xlinklocator1.strID, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, xlinkarc.getOrder());
                xbrlcalculationtreenode1.arcrole = xlinkarc.getArcRole();
                xbrlcalculationtreenode1.iWeight = new java.math.BigDecimal(xlinkarc.eData.getAttribute("weight"));
            }
            for(int i = 0; i < xlinklocator.hTable.size(); i++)
            {
                XlinkArc xlinkarc1 = (XlinkArc)xlinklocator.hTable.get(i);
                if(xlinkarc1.objToResource.size() > 0)
                {
                    XlinkLocator xlinklocator2 = (XlinkLocator)xlinkarc1.objToResource.get(0);
                    if(xlinklocator2 != null)
                        createNodeFromLocator(xbrlcalculationtree, xlinklocator2, xlinklocator, xlinkarc1);
                }
            }

        } else {
            xbrlcalculationtree.appendChild(null, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, 0.0D);
            System.out.println("schema locator not found " + xlinklocator.strID);
        }
    }

    protected void createTree()
    {
        hTreeTable = new Vector();
        for(Enumeration enumeration = hLocatorTable.keys(); enumeration.hasMoreElements();)
        {
            String s = (String)enumeration.nextElement();
            XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
            if(xlinklocator.objFromArc == null)
            {
//                System.err.println("Create calculation tree..." + s);
                XbrlCalculationTree xbrlcalculationtree = new XbrlCalculationTree();
                createNodeFromLocator(xbrlcalculationtree, xlinklocator, null, null);
                xbrlcalculationtree.calculateLevel(xbrlcalculationtree.getRootNode(), 0);

//                System.err.println("---- xbrlcalculationtree=" + xbrlcalculationtree);
//					Document doc= jcx.xbrl.xml.getNewDocument();
//					Element e1=doc.createElement("response1");
//					xbrlcalculationtree.printXML(e1);
//		System.err.println(jcx.xbrl.xml.print(e1));

				hTreeTable.add(xbrlcalculationtree);
            }
        }

    }

    protected Vector getSchemaTree()
    {
        return hTreeTable;
    }

    protected Document exportToXML()
    {
        Document document = jcx.xbrl.xml.getNewDocument();
        Element element = document.createElement("definition");
        document.appendChild(element);
        for(int i = 0; i < hTreeTable.size(); i++)
        {
            XbrlCalculationTree xbrlcalculationtree = (XbrlCalculationTree)hTreeTable.get(i);
            xbrlcalculationtree.exportToXML(element);
        }

        return document;
    }

    protected Hashtable hSchemaTable;
    Hashtable hLocatorTable;
    Vector hTreeTable;
}
