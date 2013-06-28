package jcx.xbrl.taxonomy;

import jcx.xbrl.exception.*;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.Element;


class XbrlReference
{

    public XbrlReference()
    {
        hReferenceLinkTable = new Hashtable();
    }

    protected int load(Hashtable hashtable, Element element)
        throws TaxonomyFormatErrorException
    {
        int i = 0;
        eReference = element;
        Element element1 = element;
        XbrlReferenceLink xbrlreferencelink = getReferenceLink(element1.getAttribute("xlink:role"));
        if(xbrlreferencelink == null)
        {
            xbrlreferencelink = new XbrlReferenceLink();
            xbrlreferencelink.hSchemaTable = hashtable;
            hReferenceLinkTable.put(element1.getAttribute("xlink:role"), xbrlreferencelink);
            i++;
        }
        xbrlreferencelink.load(element1);
//        System.out.println("Reference load  "+element1.getAttribute("xlink:role"));
        return i;
    }

    protected XbrlReferenceLink getReferenceLink(String s)
    {
        return (XbrlReferenceLink)hReferenceLinkTable.get(s);
    }

    protected XbrlReferenceResource getReferenceByID(String s, String s1)
        throws Exception, XbrlRoleNotSupportException
    {
        XbrlReferenceResource xbrlreferenceresource = null;
        XbrlReferenceLink xbrlreferencelink = getReferenceLink(s);
        if(xbrlreferencelink != null)
            try
            {
                xbrlreferenceresource = xbrlreferencelink.getReferenceByID(s1);
            }
            catch(Exception elementidnotfoundexception)
            {
                throw elementidnotfoundexception;
            }
        else
            throw new XbrlRoleNotSupportException("Unspported role: " + s);
        return xbrlreferenceresource;
    }

    protected Element getCollection(String s, Element element)
    {
        Element element1 = null;
        XbrlReferenceLink xbrlreferencelink = getReferenceLink(s);
        if(xbrlreferencelink != null)
            element1 = xbrlreferencelink.getCollection(element);
        return element1;
    }

    protected Element eReference;
    Hashtable hReferenceLinkTable;
}
