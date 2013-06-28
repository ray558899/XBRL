package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.*;


public class XbrlDefinitionTree extends XbrlTree
{
	String TREE_ID="";
    protected XbrlDefinitionTree(String tree_id)
    {
		TREE_ID=tree_id;
//        super.hNodeTable = new Hashtable();
    }
	public String getName(){
		return TREE_ID;
	}

    protected XbrlDefinitionTreeNode appendChild(String s, String s1, Element element, XbrlSchemaElement xbrlschemaelement, double d)
    {
        Object obj = null;
        XbrlDefinitionTreeNode xbrldefinitiontreenode1 = new XbrlDefinitionTreeNode(s1, element, xbrlschemaelement);
        xbrldefinitiontreenode1.setOrder(d);

        //hNodeTable.put(s1, xbrldefinitiontreenode1);
		getNodeTable().put(s1, xbrldefinitiontreenode1);
        if(getNode() == null)  {
//            objNodes = xbrldefinitiontreenode1;
			setNode(xbrldefinitiontreenode1);
        } else {
            XbrlDefinitionTreeNode xbrldefinitiontreenode = (XbrlDefinitionTreeNode)getNodeByID(s);
            if(xbrldefinitiontreenode != null)
                xbrldefinitiontreenode.appendChild(xbrldefinitiontreenode1);
            else
                System.out.println("Can't find parent node for id: " + s);
        }
        return xbrldefinitiontreenode1;
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
        return exportToXML((XbrlDefinitionTreeNode)getRootNode(), element);
    }

    protected Element exportToXML(XbrlDefinitionTreeNode xbrldefinitiontreenode, Element element){
        Element element1 = element.getOwnerDocument().createElement(xbrldefinitiontreenode.getID());
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xbrldefinitiontreenode)).getOrder()));
//        element1.setAttribute("level", (new String()).valueOf(((XbrlTreeNode) (xbrldefinitiontreenode)).iLevel));
        element1.setAttribute("arcrole", xbrldefinitiontreenode.arcrole);
        element.appendChild(element1);
        for(XbrlDefinitionTreeNode xbrldefinitiontreenode1 = (XbrlDefinitionTreeNode)xbrldefinitiontreenode.getFirstChild(); xbrldefinitiontreenode1 != null; xbrldefinitiontreenode1 = (XbrlDefinitionTreeNode)xbrldefinitiontreenode1.getNextSibling())
            exportToXML(xbrldefinitiontreenode1, element1);

        return element1;
    }
/*

    public void printXML(XbrlDefinitionTreeNode xbrldefinitiontreenode, Element element)
    {
        String s = "";
        for(int i = 0; i < ((XbrlTreeNode) (xbrldefinitiontreenode)).iLevel; i++)
            s = s + "    ";

        Element element1 = element.getOwnerDocument().createElement("element");
        element1.setAttribute("id", xbrldefinitiontreenode.getID());
        element1.setAttribute("caption", s);
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xbrldefinitiontreenode)).dOrder));
        if(xbrldefinitiontreenode.objSchemaElement.isAbstract())
        {
            element1.setAttribute("value", "----------------");
            element1.setAttribute("type", "abstract");
        } else
        {
            element1.setAttribute("value", "");
        }
        element.appendChild(element1);
        for(XbrlDefinitionTreeNode xbrldefinitiontreenode1 = (XbrlDefinitionTreeNode)xbrldefinitiontreenode.getFirstChild(); xbrldefinitiontreenode1 != null; xbrldefinitiontreenode1 = (XbrlDefinitionTreeNode)xbrldefinitiontreenode1.getNextSibling())
            printXML(xbrldefinitiontreenode1, element);

    }

    public void printXML(Element element)
    {
        printXML((XbrlDefinitionTreeNode)getRootNode(), element);
    }
	*/
}
