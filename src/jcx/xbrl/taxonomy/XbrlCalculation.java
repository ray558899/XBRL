package jcx.xbrl.taxonomy;

import jcx.xbrl.exception.*;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


class XbrlCalculation
{

	Vector hv=new Vector();
    public XbrlCalculation()
    {
        hCalculationLinkTable = new Hashtable();
    }

    protected int load(Hashtable hashtable, Element element)
        throws TaxonomyFormatErrorException
    {
        int i = 0;
        Element element1 = element;
        String s = element1.getAttribute("xlink:role");
        XbrlCalculationLink xbrlcalculationlink = (XbrlCalculationLink)hCalculationLinkTable.get(s);
        if(xbrlcalculationlink == null) {
            xbrlcalculationlink = new XbrlCalculationLink();
            hCalculationLinkTable.put(s, xbrlcalculationlink);
			hv.addElement(s);
            xbrlcalculationlink.hSchemaTable = hashtable;
            xbrlcalculationlink.scan(element1);
        } else {
            xbrlcalculationlink.scan(element1);
        }
        i++;
        String s1 = element1.getAttribute("xlink:title");
        if(s1.equals(""))  s1 = element1.getAttribute("xlink:role");

		return i;
    }

    protected void createTree(String s)
    {
        XbrlCalculationLink xbrlcalculationlink = getCalculationLink(s);

		xbrlcalculationlink.createTree();
    }

    protected void createTree()
    {
        String s;
        for(Enumeration enumeration = hCalculationLinkTable.keys(); enumeration.hasMoreElements(); createTree(s))
            s = (String)enumeration.nextElement();

    }

    protected XbrlCalculationLink getCalculationLink(String s)
    {
        return (XbrlCalculationLink)hCalculationLinkTable.get(s);
    }
    protected XbrlCalculationTree[] getCalculationTree(String s)
        throws XbrlRoleNotSupportException
    {
        XbrlCalculationTree[] xbrlCalculationtree = null;
        XbrlCalculationLink xbrlCalculationlink = getCalculationLink(s);
        if(xbrlCalculationlink != null){
			Vector vv=xbrlCalculationlink.getSchemaTree();

            xbrlCalculationtree = (XbrlCalculationTree[])vv.toArray(new XbrlCalculationTree[0] );
        } else {
            throw new XbrlRoleNotSupportException(s);
		}
        return xbrlCalculationtree;
    }

    protected Vector getCalculationList()
    {
        return hv;
    }

    protected Document exportToXML(String s)
        throws XbrlRoleNotSupportException
    {
        Document document = null;
        XbrlCalculationLink xbrlcalculationlink = getCalculationLink(s);
        if(xbrlcalculationlink != null)
            document = xbrlcalculationlink.exportToXML();
        else
            throw new XbrlRoleNotSupportException(s);
        return document;
    }

    protected Hashtable hCalculationLinkTable;
}
