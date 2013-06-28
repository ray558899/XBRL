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
*  ���o �o�� Node �� Schema ����
*
* @return  Schema ����
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
