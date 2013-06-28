package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import org.w3c.dom.Element;


public class XbrlDefinitionTreeNode extends XbrlTreeNode
{

    protected XbrlDefinitionTreeNode(String s, Element element, XbrlSchemaElement xbrlschemaelement)
    {
        super(s, element);
        objSchemaElement = null;
        arcrole = null;
        objSchemaElement = xbrlschemaelement;
    }

/**
*  取得 這個 Node 的 Schema 物件
*
* @return  Schema 物件
*
*/

    public XbrlSchemaElement getXbrlSchemaElement()
    {
        return objSchemaElement;
    }
	public String getArcrole(){
		return arcrole;
	}
    protected XbrlSchemaElement objSchemaElement;
    protected String arcrole;
}
