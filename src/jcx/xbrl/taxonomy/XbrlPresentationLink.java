package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import java.io.PrintStream;
import java.util.*;
import org.w3c.dom.*;


class XbrlPresentationLink
{

    public XbrlPresentationLink()
    {
        hSchemaTable = null;
        hLocatorTable = new Hashtable();
    }

    protected void scan(Element element)
    {
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()){
            if(node.getNodeType() == 1)
            {
                Element element1 = (Element)node;
				String type=element1.getAttribute("xlink:type");
                if(type.equals("locator"))
                {
                    XlinkLocator xlinklocator = new XlinkLocator(jcx.xbrl.xml.getElementName(element1.getAttribute("xlink:href"), "#"), element1.getAttribute("xlink:label"), element1);
//				System.err.println("type="+type+" "+element1.getAttribute("xlink:label"));
                    vector1.add(xlinklocator);
                } else if(type.equals("arc"))
                {
                    XlinkArc xlinkarc = new XlinkArc(element1);
                    vector.add(xlinkarc);
                }
            }
		}

 
        for(int k = 0; k < vector1.size(); k++)
        {
            XlinkLocator xlinklocator2 = (XlinkLocator)vector1.get(k);
            for(int i = 0; i < vector.size(); i++)
            {
                XlinkArc xlinkarc1 = (XlinkArc)vector.get(i);
                if(xlinkarc1.eData.getAttribute("xlink:from").equals(xlinklocator2.strLabel)){
                    xlinklocator2.add(xlinkarc1);
                } else if(xlinkarc1.eData.getAttribute("xlink:to").equals(xlinklocator2.strLabel)) {
                    xlinklocator2.objFromArc = xlinkarc1;
                    xlinkarc1.add(xlinklocator2);
                }
            }

        }

        for(int l = 0; l < vector1.size(); l++) {
            XlinkLocator xlinklocator3 = (XlinkLocator)vector1.get(l);
            if(xlinklocator3.objFromArc == null) {
                XlinkLocator xlinklocator4 = (XlinkLocator)hLocatorTable.get(xlinklocator3.strID);
                if(xlinklocator4 != null) {
                    for(int j = 0; j < xlinklocator3.hTable.size(); j++)
                    {
                        XlinkArc xlinkarc2 = (XlinkArc)xlinklocator3.hTable.get(j);
                        XlinkLocator xlinklocator1 = (XlinkLocator)xlinkarc2.getFirstResource();
                        if(xlinklocator4.getLocator(xlinklocator1.strID) == null)
                            xlinklocator4.add(xlinkarc2);
                    }

                } else {
//					if(hLocatorTable.get(xlinklocator3.strID)!=null) System.err.println("--- dup1 "+xlinklocator3.strID);
                    hLocatorTable.put(xlinklocator3.strID, xlinklocator3);
                }
            } else {
//				if(hLocatorTable.get(xlinklocator3.strID)!=null) System.err.println("--- dup2 "+xlinklocator3.strID);
                hLocatorTable.put(xlinklocator3.strID, xlinklocator3);
            }
        }
//	System.err.println("-- v1="+vector1.size()+"  v2="+vector.size()+" "+hLocatorTable.size());

    }

    protected void createNodeFromLocator(XbrlPresentationTree xbrlpresentationtree, XlinkLocator xlinklocator, XlinkLocator xlinklocator1, XlinkArc xlinkarc,int level)   {
        XbrlSchemaElement xbrlschemaelement = (XbrlSchemaElement)hSchemaTable.get(xlinklocator.strID);
//		System.err.println("lv="+level+" "+xlinklocator.strID+" "+xlinklocator.hTable.size());
        if(xbrlschemaelement != null)
        {
            XbrlPresentationTreeNode xbrlpresentationtreenode;
            if(xlinklocator1 == null)
            {
                xbrlpresentationtreenode = xbrlpresentationtree.appendChild(null, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, 0.0D);
            } else {
                XbrlPresentationTreeNode xbrlpresentationtreenode1 = xbrlpresentationtree.appendChild(xlinklocator1.strID, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, xlinkarc.getOrder());
                if(xbrlpresentationtreenode1 != null && xlinkarc.eData.hasAttribute("preferredLabel")){
                    xbrlpresentationtreenode1.preferredLabel = xlinkarc.eData.getAttribute("preferredLabel");
				}
            }
            for(int i = 0; i < xlinklocator.hTable.size(); i++)  {
                XlinkArc xlinkarc1 = (XlinkArc)xlinklocator.hTable.get(i);
                if(xlinkarc1.objToResource.size() > 0)
                {
                    XlinkLocator xlinklocator2 = (XlinkLocator)xlinkarc1.objToResource.get(0);
                    if(xlinklocator2 != null){
                        createNodeFromLocator(xbrlpresentationtree, xlinklocator2, xlinklocator, xlinkarc1,level+1);
					}
                }
            }

        } else {
            XbrlPresentationTreeNode xbrlpresentationtreenode;
            xbrlpresentationtreenode = xbrlpresentationtree.appendChild(null, xlinklocator.strID, xlinklocator.eData, xbrlschemaelement, 0.0D);
            System.out.println("---- Scheme not found ! " + xlinklocator.strID);
        }
    }

    public void createTree()
    {
        hTreeTable = new Vector();
        for(Enumeration enumeration = hLocatorTable.keys(); enumeration.hasMoreElements();)
        {
            String s = (String)enumeration.nextElement();
            XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
            if(xlinklocator.objFromArc == null)  {
//System.err.println(" t="+s+" xlinklocator.objFromArc="+xlinklocator.objFromArc);
					XbrlPresentationTree xbrlpresentationtree = new XbrlPresentationTree();
					createNodeFromLocator(xbrlpresentationtree, xlinklocator, null, null,0);
					xbrlpresentationtree.calculateLevel(((XbrlTree) (xbrlpresentationtree)).getNode(), 0);

					hTreeTable.add(xbrlpresentationtree);
//					break;

            }
        }

    }

    protected XbrlPresentationTree[] getSchemaTree()
    {
        XbrlPresentationTree[] xbrlpresentationtree = (XbrlPresentationTree[])hTreeTable.toArray(new XbrlPresentationTree[0]);
        return xbrlpresentationtree;
    }

    protected Document exportToXML()
    {
        Document document = jcx.xbrl.xml.getNewDocument();
        Element element = document.createElement("report");
        document.appendChild(element);
        for(int i = 0; i < hTreeTable.size(); i++)
        {
            XbrlPresentationTree xbrlpresentationtree = (XbrlPresentationTree)hTreeTable.get(i);
            xbrlpresentationtree.exportToXML(element);
        }

        return document;
    }

    protected String strID;
    protected String strName;
    protected Hashtable hSchemaTable;
    Hashtable hLocatorTable;
    Vector hTreeTable;
}
