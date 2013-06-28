package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import org.w3c.dom.Element;
import java.math.*;


class XbrlCalculationTreeNode extends XbrlTreeNode
{

    public XbrlCalculationTreeNode(String s, Element element, XbrlElement xelement, XbrlSchemaElement xbrlschemaelement)
    {
        super(s, element);
        objInstanceElement = xelement;
        objSchemaElement = xbrlschemaelement;
    }

    public XbrlSchemaElement getXbrlSchemaElement()
    {
        return objSchemaElement;
    }

    protected XbrlElement objInstanceElement;
    protected XbrlSchemaElement objSchemaElement;
    protected BigDecimal iCalculated=new BigDecimal("0.0");
    protected BigDecimal iWeight=new BigDecimal("1.0");
}
