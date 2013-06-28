package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.*;


public class XbrlPresentationTree extends XbrlTree
{

    protected XbrlPresentationTree()
    {
       // hNodeTable = new Hashtable();
    }

    protected XbrlPresentationTreeNode appendChild(String s, String s1, Element element, XbrlSchemaElement xbrlschemaelement, double d)
    {
        Object obj = null;
        XbrlPresentationTreeNode xbrlpresentationtreenode1 = new XbrlPresentationTreeNode(s1, element, xbrlschemaelement);
        xbrlpresentationtreenode1.setOrder(d);
        getNodeTable().put(s1, xbrlpresentationtreenode1);
        if(getNode() == null)
        {
            setNode(xbrlpresentationtreenode1);
        } else
        {
            XbrlPresentationTreeNode xbrlpresentationtreenode = (XbrlPresentationTreeNode)getNodeByID(s);
            if(xbrlpresentationtreenode != null)
                xbrlpresentationtreenode.appendChild(xbrlpresentationtreenode1);
            else
                System.out.println("Can't find parent node for id: " + s);
        }
        return xbrlpresentationtreenode1;
    }

/**
*  ?X Tree
*
* @param   element target.
* @return  element
*
*/

    public Element exportToXML(Element element)
    {
        return exportToXML((XbrlPresentationTreeNode)getRootNode(), element);
    }

    protected Element exportToXML(XbrlPresentationTreeNode xbrlpresentationtreenode, Element element)
    {
        Element element1 = element.getOwnerDocument().createElement(xbrlpresentationtreenode.getID());
//        element1.setAttribute("level", (new String()).valueOf(((XbrlTreeNode) (xbrlpresentationtreenode)).iLevel));
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xbrlpresentationtreenode)).getOrder()));
        if(xbrlpresentationtreenode.preferredLabel != null)
            element1.setAttribute("preferredLabel", xbrlpresentationtreenode.preferredLabel);
        element.appendChild(element1);
        for(XbrlPresentationTreeNode xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode.getFirstChild(); xbrlpresentationtreenode1 != null; xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode1.getNextSibling())
            exportToXML(xbrlpresentationtreenode1, element1);

        return element1;
    }
/*

    public void printXML(XbrlPresentationTreeNode xbrlpresentationtreenode, Element element)
    {
        String s = "";
        for(int i = 0; i < ((XbrlTreeNode) (xbrlpresentationtreenode)).iLevel; i++)
            s = s + "    ";

        Element element1 = element.getOwnerDocument().createElement("element");
        element1.setAttribute("id", xbrlpresentationtreenode.getID());
        element1.setAttribute("caption", s);
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xbrlpresentationtreenode)).dOrder));
        if(xbrlpresentationtreenode.objSchemaElement.isAbstract())
        {
            element1.setAttribute("value", "----------------");
            element1.setAttribute("type", "abstract");
        } else
        {
            element1.setAttribute("value", "");
        }
        element.appendChild(element1);
        for(XbrlPresentationTreeNode xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode.getFirstChild(); xbrlpresentationtreenode1 != null; xbrlpresentationtreenode1 = (XbrlPresentationTreeNode)xbrlpresentationtreenode1.getNextSibling())
            printXML(xbrlpresentationtreenode1, element);

    }

    public void printXML(Element element)
    {
        printXML((XbrlPresentationTreeNode)getRootNode(), element);
    }
	*/
}
