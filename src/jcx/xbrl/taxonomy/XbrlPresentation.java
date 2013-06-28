package jcx.xbrl.taxonomy;

import jcx.xbrl.data.XbrlTree;
import jcx.xbrl.exception.*;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


class XbrlPresentation
{

	Vector hv=new Vector();
    public XbrlPresentation()
    {
        hPresentationTable = null;
        hPresentationTable = new Hashtable();
    }

    protected int load(Hashtable hashtable, Element element)
        throws TaxonomyFormatErrorException
    {
        int i = 0;
        Element element1 = element;
        String s = element1.getAttribute("xlink:role");
//System.err.println("--- add "+s);
        XbrlPresentationLink xbrlpresentationlink = (XbrlPresentationLink)hPresentationTable.get(s);
        if(xbrlpresentationlink == null)
        {
            xbrlpresentationlink = new XbrlPresentationLink();
            xbrlpresentationlink.hSchemaTable = hashtable;
            hPresentationTable.put(s, xbrlpresentationlink);
			hv.addElement(s);
            xbrlpresentationlink.scan(element1);
        } else
        {
            xbrlpresentationlink.scan(element1);
        }
        i++;
        String s1 = element1.getAttribute("xlink:title");
        if(s1.compareTo("") == 0)  s1 = element1.getAttribute("xlink:role");
        return i;
    }

    protected void createTree(String s)
    {
        XbrlPresentationLink xbrlpresentationlink = getPresentationLink(s);

		xbrlpresentationlink.createTree();
    }

    protected void createTree()
    {
        String s;
        for(Enumeration enumeration = hPresentationTable.keys(); enumeration.hasMoreElements(); createTree(s))
            s = (String)enumeration.nextElement();

    }

    protected Vector getPresentationList() {
		return hv;
//        return hPresentationTable.keys();
    }

    protected XbrlPresentationLink getPresentationLink(String s)
    {
        XbrlPresentationLink xbrlpresentationlink = null;
        xbrlpresentationlink = (XbrlPresentationLink)hPresentationTable.get(s);
        return xbrlpresentationlink;
    }

    protected XbrlPresentationTree[] getPresentationTree(String s)
        throws XbrlRoleNotSupportException
    {
        XbrlPresentationTree xbrlpresentationtree[] = null;
        XbrlPresentationLink xbrlpresentationlink = getPresentationLink(s);
        if(xbrlpresentationlink != null)
            xbrlpresentationtree = xbrlpresentationlink.getSchemaTree();
        else
            throw new XbrlRoleNotSupportException(s);
        return xbrlpresentationtree;
    }
    protected Document exportToXML(String s)
        throws XbrlRoleNotSupportException
    {
        Document document = null;
        XbrlPresentationLink xbrlpresentationlink = getPresentationLink(s);
        if(xbrlpresentationlink != null)
            document = xbrlpresentationlink.exportToXML();
        else
            throw new XbrlRoleNotSupportException(s);
        return document;
    }

/*
    public void print()
    {
        XbrlPresentationTree xbrlpresentationtree;
        for(Enumeration enumeration = hPresentationTable.keys(); enumeration.hasMoreElements(); xbrlpresentationtree.print(xbrlpresentationtree.getRootNode()))
        {
            XbrlPresentationLink xbrlpresentationlink = (XbrlPresentationLink)hPresentationTable.get(enumeration.nextElement());
            xbrlpresentationtree = xbrlpresentationlink.getSchemaTree();
        }

    }
*/

    protected Hashtable hPresentationTable;
}
