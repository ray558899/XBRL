package jcx.xbrl.data;

import jcx.xbrl.taxonomy.XbrlSchemaElement;
import org.w3c.dom.Element;


class XbrlPresentationTreeNode extends XbrlTreeNode
{

    public XbrlPresentationTreeNode(String s, Element element, XbrlElement XbrlElement, XbrlSchemaElement xbrlschemaelement)
    {
        super(s, element);
        objInstanceElement = null;
        objSchemaElement = null;
        preferredLabel = null;
        objInstanceElement = XbrlElement;
        objSchemaElement = xbrlschemaelement;
    }

    public XbrlSchemaElement getXbrlSchemaElement()
    {
        return objSchemaElement;
    }

    protected XbrlElement objInstanceElement;
    protected XbrlSchemaElement objSchemaElement;
    protected String preferredLabel;
}
