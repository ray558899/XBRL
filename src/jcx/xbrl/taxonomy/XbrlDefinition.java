package jcx.xbrl.taxonomy;

import jcx.xbrl.exception.TaxonomyFormatErrorException;
import jcx.xbrl.exception.XbrlRoleNotSupportException;
import java.io.PrintStream;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


class XbrlDefinition
{
	Vector hv=new Vector();
    public XbrlDefinition()
    {
        hDefinitionTable = null;
        hDefinitionTable = new Hashtable();
    }

    protected Vector getDefinitionList()
    {
        return hv;
    }
    protected XbrlDefinitionLink getDefinitionLink(String s)
    {
        XbrlDefinitionLink xbrldefinitionlink = null;
        xbrldefinitionlink = (XbrlDefinitionLink)hDefinitionTable.get(s);
        return xbrldefinitionlink;
    }

    protected int load(Hashtable hashtable, Element element)
        throws TaxonomyFormatErrorException, NullPointerException
    {
        int i = 0;
        if(element != null)
        {
            Element element1 = element;
            String s = element1.getAttribute("xlink:role");
            XbrlDefinitionLink xbrldefinitionlink = getDefinitionLink(s);
            if(xbrldefinitionlink == null) {
                xbrldefinitionlink = new XbrlDefinitionLink();
                xbrldefinitionlink.hSchemaTable = hashtable;
                hDefinitionTable.put(s, xbrldefinitionlink);
				hv.addElement(s);
                xbrldefinitionlink.scan(element1);
            } else {
                xbrldefinitionlink.scan(element1);
            }
            i++;
            String s1 = element1.getAttribute("xlink:title");
            if(s1.compareTo("") == 0) s1 = element1.getAttribute("xlink:role");
//            System.out.println("Definition load "  + s);
        } else {
            throw new NullPointerException("Null definition link.");
        }
        return i;
    }

    protected void createTree(String s) {
        XbrlDefinitionLink xbrldefinitionlink = getDefinitionLink(s);
//        System.out.println("Create definition tree for role: " + s);
        xbrldefinitionlink.createTree(s);
    }

    protected void createTree()
    {
        String s;
        for(Enumeration enumeration = hDefinitionTable.keys(); enumeration.hasMoreElements(); createTree(s))
            s = (String)enumeration.nextElement();

    }

    protected XbrlDefinitionTree[] getDefinitionTree(String s)
        throws XbrlRoleNotSupportException
    {
        XbrlDefinitionTree xbrldefinitiontree[] = null;
        XbrlDefinitionLink xbrldefinitionlink = getDefinitionLink(s);
        if(xbrldefinitionlink != null) {
            xbrldefinitiontree = xbrldefinitionlink.getSchemaTree();
//            if(xbrldefinitionlink.hTreeTable.size() > 0){
//                xbrldefinitiontree = (XbrlDefinitionTree)xbrldefinitionlink.hTreeTable.get(0);
//			}
        } else {
            throw new XbrlRoleNotSupportException("Unsupport definition role (" + s+")");
        }

        return xbrldefinitiontree;
    }
/*
    protected XbrlDefinitionTree[] getDefinitionTrees()
        throws XbrlRoleNotSupportException
    {
        XbrlDefinitionTree[] xbrldefinitiontrees = null;
		Vector v1=new Vector();
		for(Enumeration e = hDefinitionTable.keys() ; e.hasMoreElements() ;) {
				Object key=e.nextElement();
				XbrlDefinitionLink xbrldefinitionlink = getDefinitionLink(""+key);
				if(xbrldefinitionlink.hTreeTable.size() > 0){
					XbrlDefinitionTree xbrldefinitiontree = (XbrlDefinitionTree)xbrldefinitionlink.hTreeTable.get(0);
					v1.addElement(xbrldefinitiontree);
				}
		}
        return (XbrlDefinitionTree[])v1.toArray(new XbrlDefinitionTree[0]);
    }
*/
    protected Hashtable hDefinitionTable;
}
