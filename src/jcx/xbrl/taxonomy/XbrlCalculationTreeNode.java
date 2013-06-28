package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import org.w3c.dom.Element;
import java.math.*;


public class XbrlCalculationTreeNode extends XbrlTreeNode
{

    protected XbrlCalculationTreeNode(String s, Element element, XbrlSchemaElement xbrlschemaelement)
    {
        super(s, element);
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
/**
*  ���o �o�� Node �� Weight
*
* @return  Weight
*
*/

	public BigDecimal getWeight(){
		return iWeight;
	}

    protected XbrlSchemaElement objSchemaElement;
    protected String arcrole;
    protected BigDecimal iWeight=new BigDecimal("1.0");
}
