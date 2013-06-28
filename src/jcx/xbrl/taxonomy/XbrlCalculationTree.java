package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.*;


public class XbrlCalculationTree extends XbrlTree
{

    protected XbrlCalculationTree() {
//        hNodeTable = new Hashtable();
    }

    protected XbrlCalculationTreeNode appendChild(String s, String s1, Element element, XbrlSchemaElement xbrlschemaelement, double d)
    {
        Object obj = null;
        XbrlCalculationTreeNode xbrlcalculationtreenode1 = new XbrlCalculationTreeNode(s1, element, xbrlschemaelement);
        xbrlcalculationtreenode1.setOrder(d);
        getNodeTable().put(s1, xbrlcalculationtreenode1);
        if(getNode() == null)
        {
            setNode(xbrlcalculationtreenode1);
        } else
        {
            XbrlCalculationTreeNode xbrlcalculationtreenode = (XbrlCalculationTreeNode)getNodeByID(s);
            if(xbrlcalculationtreenode != null)
                xbrlcalculationtreenode.appendChild(xbrlcalculationtreenode1);
            else
                System.out.println("Can't find parent node for id: " + s);
        }
        return xbrlcalculationtreenode1;
    }
/**
*  ¶×¥X Tree
*
* @param   element target.
* @return  element
*
*/
    public Element exportToXML(Element element)
    {
        return exportToXML((XbrlCalculationTreeNode)getRootNode(), element);
    }

    protected Element exportToXML(XbrlCalculationTreeNode xbrlcalculationtreenode, Element element)
    {
        Element element1 = element.getOwnerDocument().createElement(xbrlcalculationtreenode.getID());
//        element1.setAttribute("level", (new String()).valueOf(((XbrlTreeNode) (xbrlcalculationtreenode)).iLevel));
        element1.setAttribute("weight", (new String()).valueOf(xbrlcalculationtreenode.iWeight));
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xbrlcalculationtreenode)).getOrder()));
        element1.setAttribute("arcrole", xbrlcalculationtreenode.arcrole);
        element.appendChild(element1);
        for(XbrlCalculationTreeNode xbrlcalculationtreenode1 = (XbrlCalculationTreeNode)xbrlcalculationtreenode.getFirstChild(); xbrlcalculationtreenode1 != null; xbrlcalculationtreenode1 = (XbrlCalculationTreeNode)xbrlcalculationtreenode1.getNextSibling())
            exportToXML(xbrlcalculationtreenode1, element1);

        return element1;
    }

    protected void printXML(XbrlCalculationTreeNode xbrlcalculationtreenode, Element element)
    {
        String s = "";
//        for(int i = 0; i < ((XbrlTreeNode) (xbrlcalculationtreenode)).iLevel; i++)
//            s = s + "    ";

        Element element1 = element.getOwnerDocument().createElement("element");
        element1.setAttribute("id", xbrlcalculationtreenode.getID());
        element1.setAttribute("caption", s);
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xbrlcalculationtreenode)).getOrder()));
        if(xbrlcalculationtreenode.objSchemaElement.isAbstract())
        {
            element1.setAttribute("value", "----------------");
            element1.setAttribute("type", "abstract");
        } else
        {
            element1.setAttribute("value", "");
        }
        element.appendChild(element1);
        for(XbrlCalculationTreeNode xbrlcalculationtreenode1 = (XbrlCalculationTreeNode)xbrlcalculationtreenode.getFirstChild(); xbrlcalculationtreenode1 != null; xbrlcalculationtreenode1 = (XbrlCalculationTreeNode)xbrlcalculationtreenode1.getNextSibling())
            printXML(xbrlcalculationtreenode1, element);

    }

    protected void printXML(Element element)
    {
        printXML((XbrlCalculationTreeNode)getRootNode(), element);
    }
}
