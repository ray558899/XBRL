package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.*;


class XbrlPresentationTree extends XbrlTree
{

    public XbrlPresentationTree(XbrlTaxonomy xbrltaxonomy)
    {
        objTaxonomy = xbrltaxonomy;
    }

    protected XbrlPresentationTreeNode appendChild(String s, String s1, Element element, XbrlElement XbrlElement, double d)
    {
        Object obj = null;
        XbrlPresentationTreeNode XbrlPresentationTreeNode1 = new XbrlPresentationTreeNode(s1, element, XbrlElement, XbrlElement.objSchema);
        XbrlPresentationTreeNode1.setOrder(d);
        getNodeTable().put(s1, XbrlPresentationTreeNode1);
        if(getNode() == null)
        {
            setNode(XbrlPresentationTreeNode1);
        } else
        {
            XbrlPresentationTreeNode XbrlPresentationTreeNode = (XbrlPresentationTreeNode)getNodeByID(s);
            if(XbrlPresentationTreeNode != null)
                XbrlPresentationTreeNode.appendChild(XbrlPresentationTreeNode1);
            else
                System.out.println("Can't find parent node for id:" + s);
        }
        return XbrlPresentationTreeNode1;
    }

    protected void cloneTree(XbrlPresentationTree xbrlpresentationtree, Hashtable hashtable, XbrlContext xbrlcontext, XbrlContext xbrlcontext1) throws Exception
    {
        XbrlSchemaElement xbrlschemaelement = null;
        XbrlElement XbrlElement = null;
        Object obj = null;
        XbrlPresentationTreeNode xbrlpresentationtreenode = (XbrlPresentationTreeNode)xbrlpresentationtree.getRootNode();
        XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(xbrlpresentationtreenode.getID());
        if(xbrlelementpool != null)
        {
            XbrlContext xbrlcontext2;
            if(xbrlelementpool.objSchema.getPeriodType().equals("duration"))
                xbrlcontext2 = xbrlcontext1;
            else
                xbrlcontext2 = xbrlcontext;
            XbrlElement = xbrlelementpool.get(xbrlcontext2);
        }
        if(XbrlElement != null)
            xbrlschemaelement = XbrlElement.objSchema;
        else
            xbrlschemaelement = objTaxonomy.getSchemaElement(xbrlpresentationtreenode.getID());
        setNode(new XbrlPresentationTreeNode(xbrlpresentationtreenode.getID(), ((XbrlTreeNode) (xbrlpresentationtreenode)).eData, XbrlElement, xbrlschemaelement));
        cloneNode(xbrlpresentationtreenode, (XbrlPresentationTreeNode)getRootNode(), hashtable, xbrlcontext, xbrlcontext1);
        calculateLevel(getRootNode(), 0);
    }

    protected void cloneNode(XbrlPresentationTreeNode xbrlpresentationtreenode, XbrlPresentationTreeNode XbrlPresentationTreeNode, Hashtable hashtable, XbrlContext xbrlcontext, XbrlContext xbrlcontext1) throws Exception
    {
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        XbrlPresentationTreeNode.preferredLabel = xbrlpresentationtreenode.preferredLabel;
        for(XbrlPresentationTreeNode xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode.getFirstChild(); xbrlpresentationtreenode1 != null; xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode1.getNextSibling())
        {
            XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(xbrlpresentationtreenode1.getID());
            XbrlElement XbrlElement;
            if(xbrlelementpool != null)
            {
                XbrlContext xbrlcontext2;
                if(xbrlelementpool.objSchema.getPeriodType().equals("duration"))
                    xbrlcontext2 = xbrlcontext1;
                else
                    xbrlcontext2 = xbrlcontext;
                XbrlElement = xbrlelementpool.get(xbrlcontext2);
            } else
            {
                XbrlElement = null;
            }
            XbrlSchemaElement xbrlschemaelement;
            if(XbrlElement != null)
                xbrlschemaelement = XbrlElement.objSchema;
            else
                xbrlschemaelement = objTaxonomy.getSchemaElement(xbrlpresentationtreenode1.getID());
            XbrlPresentationTreeNode XbrlPresentationTreeNode1 = new XbrlPresentationTreeNode(xbrlpresentationtreenode1.getID(), ((XbrlTreeNode) (xbrlpresentationtreenode1)).eData, XbrlElement, xbrlschemaelement);
            XbrlPresentationTreeNode.appendChild(XbrlPresentationTreeNode1);
            cloneNode(xbrlpresentationtreenode1, XbrlPresentationTreeNode1, hashtable, xbrlcontext, xbrlcontext1);
        }

    }

    protected void cloneTree(XbrlPresentationTree xbrlpresentationtree, Hashtable hashtable, String s, String s1, XbrlContextList xbrlcontextlist)throws Exception
    {
        XbrlSchemaElement xbrlschemaelement = null;
        XbrlElement XbrlElement = null;
        Object obj = null;
        XbrlPresentationTreeNode xbrlpresentationtreenode = (XbrlPresentationTreeNode)xbrlpresentationtree.getRootNode();
        XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(xbrlpresentationtreenode.getID());
        if(xbrlelementpool != null)
        {
            XbrlContext xbrlcontext;
            if(xbrlelementpool.objSchema.getPeriodType().equals("duration"))
                xbrlcontext = xbrlcontextlist.containsDurationContext(s, s1);
            else
                xbrlcontext = xbrlcontextlist.containsInstantContext(s1);
            XbrlElement = xbrlelementpool.get(xbrlcontext);
        }
        if(XbrlElement != null)
            xbrlschemaelement = XbrlElement.objSchema;
        else
            xbrlschemaelement = objTaxonomy.getSchemaElement(xbrlpresentationtreenode.getID());
        setNode(new XbrlPresentationTreeNode(xbrlpresentationtreenode.getID(), ((XbrlTreeNode) (xbrlpresentationtreenode)).eData, XbrlElement, xbrlschemaelement));
        cloneNode(xbrlpresentationtreenode, (XbrlPresentationTreeNode)getRootNode(), hashtable, s, s1, xbrlcontextlist);
        calculateLevel(getRootNode(), 0);
    }

    protected void cloneNode(XbrlPresentationTreeNode xbrlpresentationtreenode, XbrlPresentationTreeNode XbrlPresentationTreeNode, Hashtable hashtable, String s, String s1, XbrlContextList xbrlcontextlist)throws Exception
    {
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        XbrlPresentationTreeNode.preferredLabel = xbrlpresentationtreenode.preferredLabel;
        for(XbrlPresentationTreeNode xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode.getFirstChild(); xbrlpresentationtreenode1 != null; xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode1.getNextSibling())
        {
            XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(xbrlpresentationtreenode1.getID());
            XbrlElement XbrlElement;
            if(xbrlelementpool != null)
            {
                XbrlContext xbrlcontext;
                if(xbrlelementpool.objSchema.getPeriodType().equals("duration"))
                    xbrlcontext = xbrlcontextlist.containsDurationContext(s, s1);
                else
                    xbrlcontext = xbrlcontextlist.containsInstantContext(s1);
                XbrlElement = xbrlelementpool.get(xbrlcontext);
            } else
            {
                XbrlElement = null;
            }
            XbrlSchemaElement xbrlschemaelement;
            if(XbrlElement != null)
                xbrlschemaelement = XbrlElement.objSchema;
            else
                xbrlschemaelement = objTaxonomy.getSchemaElement(xbrlpresentationtreenode1.getID());
            XbrlPresentationTreeNode XbrlPresentationTreeNode1 = new XbrlPresentationTreeNode(xbrlpresentationtreenode1.getID(), ((XbrlTreeNode) (xbrlpresentationtreenode1)).eData, XbrlElement, xbrlschemaelement);
            XbrlPresentationTreeNode.appendChild(XbrlPresentationTreeNode1);
            cloneNode(xbrlpresentationtreenode1, XbrlPresentationTreeNode1, hashtable, s, s1, xbrlcontextlist);
        }

    }

	protected Element exportToXML(Element element)
    {
        return exportToXML((XbrlPresentationTreeNode)getRootNode(), element);
    }

    protected Element exportToXML(XbrlPresentationTreeNode XbrlPresentationTreeNode, Element element)
    {
        Element element1 = null;
        element1 = element.getOwnerDocument().createElement(XbrlPresentationTreeNode.getID());
        element.appendChild(element1);
        element1.setAttribute("level", (new String()).valueOf(((XbrlTreeNode) (XbrlPresentationTreeNode)).iLevel));
        try
        {
            element1.setAttribute("label", objTaxonomy.getLabelByID(XbrlPresentationTreeNode.getID(), objTaxonomy.getDefaultLanguage()));
        }
        catch(Exception e1) {
			e1.printStackTrace();
		}
        try
        {
            if(XbrlPresentationTreeNode.objInstanceElement != null)
                element1.setAttribute("value", XbrlPresentationTreeNode.objInstanceElement.getValue());
        }
        catch(NullPointerException e2) { 
			e2.printStackTrace();
		}

        for(XbrlPresentationTreeNode XbrlPresentationTreeNode1 = (XbrlPresentationTreeNode)XbrlPresentationTreeNode.getFirstChild(); XbrlPresentationTreeNode1 != null; XbrlPresentationTreeNode1 = (XbrlPresentationTreeNode)XbrlPresentationTreeNode1.getNextSibling())
            exportToXML(XbrlPresentationTreeNode1, element1);

        return element1;
    }

    protected void printXML(XbrlPresentationTreeNode XbrlPresentationTreeNode, Element element)
    {
        String s = "";
        for(int i = 0; i < ((XbrlTreeNode) (XbrlPresentationTreeNode)).iLevel; i++)
            s = s + "    ";

        Element element1 = element.getOwnerDocument().createElement("element");
        element1.setAttribute("id", XbrlPresentationTreeNode.getID());
        element1.setAttribute("caption", s);
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (XbrlPresentationTreeNode)).getOrder()));
        if(XbrlPresentationTreeNode.preferredLabel != null)
            element1.setAttribute("preferredLabel", XbrlPresentationTreeNode.preferredLabel);
        if(XbrlPresentationTreeNode.objSchemaElement.isAbstract())
        {
            element1.setAttribute("value", "----------------");
            element1.setAttribute("type", "abstract");
        } else
        if(XbrlPresentationTreeNode.objInstanceElement != null)
            try
            {
                String s1 = XbrlPresentationTreeNode.objInstanceElement.getValue();
                element1.setAttribute("value", s1);
            }
            catch(NullPointerException nullpointerexception) { }
        element.appendChild(element1);
        for(XbrlPresentationTreeNode XbrlPresentationTreeNode1 = (XbrlPresentationTreeNode)XbrlPresentationTreeNode.getFirstChild(); XbrlPresentationTreeNode1 != null; XbrlPresentationTreeNode1 = (XbrlPresentationTreeNode)XbrlPresentationTreeNode1.getNextSibling())
            printXML(XbrlPresentationTreeNode1, element);

    }

    protected void printXML(Element element)
    {
        printXML((XbrlPresentationTreeNode)getRootNode(), element);
    }

    protected void print(XbrlPresentationTreeNode XbrlPresentationTreeNode)
    {
        for(int i = 0; i < ((XbrlTreeNode) (XbrlPresentationTreeNode)).iLevel; i++)
            System.out.print(",");

        System.out.print(XbrlPresentationTreeNode.getID());
        if(XbrlPresentationTreeNode.objInstanceElement != null)
        {
            String s;
            try
            {
                s = XbrlPresentationTreeNode.objInstanceElement.getValue();
            }
            catch(NullPointerException nullpointerexception)
            {
                s = "";
            }
            System.out.println("," + s);
        } else
        {
            System.out.println("");
        }
        for(XbrlPresentationTreeNode XbrlPresentationTreeNode1 = (XbrlPresentationTreeNode)XbrlPresentationTreeNode.getFirstChild(); XbrlPresentationTreeNode1 != null; XbrlPresentationTreeNode1 = (XbrlPresentationTreeNode)XbrlPresentationTreeNode1.getNextSibling())
            print(XbrlPresentationTreeNode1);

    }

    protected void print()
    {
        print((XbrlPresentationTreeNode)getRootNode());
    }

    protected XbrlTaxonomy objTaxonomy;
}
