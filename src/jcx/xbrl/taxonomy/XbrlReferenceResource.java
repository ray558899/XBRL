package jcx.xbrl.taxonomy;

import org.w3c.dom.Element;

class XbrlReferenceResource extends XlinkResource
{

    public XbrlReferenceResource(Element element)
    {
        super(element);
    }

    public String getName()
    {
        String s = null;
        Element element = jcx.xbrl.xml.getChildByName(eData, "ref:Name");
        if(element != null)
            s = jcx.xbrl.xml.getTextStringInChild(element);
        return s;
    }

    public String getPublisher()
    {
        String s = null;
        Element element = jcx.xbrl.xml.getChildByName(eData, "ref:Publisher");
        if(element != null)
            s = jcx.xbrl.xml.getTextStringInChild(element);
        return s;
    }

    public String getNumber()
    {
        String s = null;
        Element element = jcx.xbrl.xml.getChildByName(eData, "ref:Number");
        if(element != null)
            s = jcx.xbrl.xml.getTextStringInChild(element);
        return s;
    }

    public String getParagraph()
    {
        String s = null;
        Element element = jcx.xbrl.xml.getChildByName(eData, "ref:Paragraph");
        if(element != null) s = jcx.xbrl.xml.getTextStringInChild(element);
        return s;
    }

    public String getURI()
    {
        String s = null;
        Element element = jcx.xbrl.xml.getChildByName(eData, "ref:URI");
        if(element != null) s = jcx.xbrl.xml.getTextStringInChild(element);
        return s;
    }

    public String getURIDate()
    {
        String s = null;
        Element element = jcx.xbrl.xml.getChildByName(eData, "ref:URIDate");
        if(element != null)
            s = jcx.xbrl.xml.getTextStringInChild(element);
        return s;
    }
}
