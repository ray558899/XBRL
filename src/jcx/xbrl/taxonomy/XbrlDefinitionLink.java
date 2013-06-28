package jcx.xbrl.taxonomy;

import jcx.xbrl.data.XbrlTree;
import java.io.PrintStream;
import java.util.*;
import org.w3c.dom.*;


class XbrlDefinitionLink
{

    public XbrlDefinitionLink()
    {
        hSchemaTable = null;
        hLinkbaseTable = new Vector();
        hLocatorTable = new Hashtable();
    }

    protected void scan(Element element)
    {
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        Vector vector2 = new Vector();
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            if(node.getNodeType() == 1)
            {
                Element element1 = (Element)node;
                if(element1.getAttribute("xlink:type").equals("locator"))  {
                    XlinkLocator xlinklocator = new XlinkLocator(jcx.xbrl.xml.getElementName(element1.getAttribute("xlink:href"), "#"), element1.getAttribute("xlink:label"), element1);
                    vector2.add(xlinklocator);
                } else if(element1.getAttribute("xlink:type").equals("arc"))  {
                    XlinkArc xlinkarc = new XlinkArc(element1);
                    if(element1.getAttribute("xlink:arcrole").equals("http://xbrl.org/int/dim/arcrole/dimension-default")){
                        vector1.add(xlinkarc);
					} else {
                        vector.add(xlinkarc);
					}

                }
            }

        hLinkbaseTable.add(element);
        for(int k = 0; k < vector2.size(); k++)
        {
            XlinkLocator xlinklocator1 = (XlinkLocator)vector2.get(k);
            for(int i = 0; i < vector.size(); i++)
            {
                XlinkArc xlinkarc1 = (XlinkArc)vector.get(i);
                if(xlinkarc1.eData.getAttribute("xlink:from").equals(xlinklocator1.strLabel))
                    xlinklocator1.add(xlinkarc1);
                if(xlinkarc1.eData.getAttribute("xlink:to").equals(xlinklocator1.strLabel))
                {
                    xlinklocator1.objFromArc = xlinkarc1;
                    xlinkarc1.add(xlinklocator1);
                }
            }

        }

        for(int l = 0; l < vector2.size(); l++)
        {
            XlinkLocator xlinklocator2 = (XlinkLocator)vector2.get(l);
            if(xlinklocator2.objFromArc == null)
            {
                XlinkLocator xlinklocator3 = (XlinkLocator)hLocatorTable.get(xlinklocator2.strID);
                if(xlinklocator3 != null)
                {
                    for(int j = 0; j < xlinklocator2.hTable.size(); j++)
                    {
                        XlinkArc xlinkarc2 = (XlinkArc)xlinklocator2.hTable.get(j);
                        xlinklocator3.add(xlinkarc2);
                    }

                } else
                {
                    hLocatorTable.put(xlinklocator2.strID, xlinklocator2);
                }
            } else
            {
                hLocatorTable.put(xlinklocator2.strID, xlinklocator2);
            }
        }

    }

    protected void createNodeFromLocator(XbrlDefinitionTree xbrldefinitiontree, XlinkLocator xlinklocator, XlinkLocator xlinklocator1, XlinkArc xlinkarc)
    {
        XbrlSchemaElement xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(xlinklocator.strID);
        if(xbrlschemaelement != null) {
            XbrlDefinitionTreeNode xbrldefinitiontreenode;
            if(xlinklocator1 == null)  {
                xbrldefinitiontreenode = xbrldefinitiontree.appendChild(null, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, 0.0D);
            } else {
                XbrlDefinitionTreeNode xbrldefinitiontreenode1 = xbrldefinitiontree.appendChild(xlinklocator1.strID, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, xlinkarc.getOrder());
                xbrldefinitiontreenode1.arcrole = xlinkarc.getArcRole();
            }
            for(int i = 0; i < xlinklocator.hTable.size(); i++) {
                XlinkArc xlinkarc1 = (XlinkArc)xlinklocator.hTable.get(i);
                if(xlinkarc1.objToResource.size() > 0)
                {
                    XlinkLocator xlinklocator2 = (XlinkLocator)xlinkarc1.objToResource.get(0);
                    if(xlinklocator2 != null)
                        createNodeFromLocator(xbrldefinitiontree, xlinklocator2, xlinklocator, xlinkarc1);
                }
            }

        } else{
            XbrlDefinitionTreeNode xbrldefinitiontreenode;
            xbrldefinitiontreenode = xbrldefinitiontree.appendChild(null, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, 0.0D);
            System.out.println("Can't find schema for locator: " + xlinklocator.strID);
        }
    }

    protected void createTree(String tree_id)
    {
        hTreeTable = new Vector();
        for(Enumeration enumeration = hLocatorTable.keys(); enumeration.hasMoreElements();)
        {
            String s = (String)enumeration.nextElement();
            XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
            if(xlinklocator.objFromArc == null)
            {
//                System.out.println("Create definition tree..." + s + "  " + xlinklocator.hTable.size());
                XbrlDefinitionTree xbrldefinitiontree = new XbrlDefinitionTree(tree_id);
                createNodeFromLocator(xbrldefinitiontree, xlinklocator, null, null);
                xbrldefinitiontree.calculateLevel(xbrldefinitiontree.getRootNode(), 0);
                hTreeTable.add(xbrldefinitiontree);
            }
        }

    }

    protected XbrlDefinitionTree[] getSchemaTree()
    {
        XbrlDefinitionTree[] t = (XbrlDefinitionTree[])hTreeTable.toArray(new XbrlDefinitionTree[0]);
        return t;
    }

    protected Document exportToXML()
    {
        Document document = jcx.xbrl.xml.getNewDocument();
        Element element = document.createElement("definition");
        document.appendChild(element);
        for(int i = 0; i < hTreeTable.size(); i++)
        {
            XbrlDefinitionTree xbrldefinitiontree = (XbrlDefinitionTree)hTreeTable.get(i);
            xbrldefinitiontree.exportToXML(element);
        }

        return document;
    }

    protected Vector hLinkbaseTable;
    protected Hashtable hSchemaTable;
    protected Hashtable hLocatorTable;
    protected Vector hTreeTable;
}
