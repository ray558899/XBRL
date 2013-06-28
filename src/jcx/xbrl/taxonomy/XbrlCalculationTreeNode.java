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
*  取得 這個 Node 的 Schema 物件
*
* @return  Schema 物件
*
*/

    public XbrlSchemaElement getXbrlSchemaElement()
    {
        return objSchemaElement;
    }
/**
*  取得 這個 Node 的 Weight
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
