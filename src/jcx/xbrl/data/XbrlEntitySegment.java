package jcx.xbrl.data;

import org.w3c.dom.*;

public class XbrlEntitySegment
{

    protected XbrlEntitySegment()
    {
        eData = null;
        eSegmentCode = null;
        eDimension = null;
    }

    protected XbrlEntitySegment(Element element, String s)
    {
        eData = null;
        eSegmentCode = null;
        eDimension = null;
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("segment");
            element.appendChild(eData);
            eSegmentCode = element.getOwnerDocument().createElement("CtxName");
            eData.appendChild(eSegmentCode);
            jcx.xbrl.xml.setTextStringInChild(eSegmentCode, s);
        }
    }

    public String getValue()
    {
        return jcx.xbrl.xml.getTextStringInChild(eSegmentCode);
    }

    public void setValue(String s)
    {
        jcx.xbrl.xml.setTextStringInChild(eSegmentCode, s);
    }

    protected void loadSegment(Element element)
    {
        if(element != null)
        {
            eData = element;
            for(Node node = eData.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    String s = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");
                    if(s.equalsIgnoreCase("explicitMember"))
                        eDimension = element1;
                    else
                    if(s.equalsIgnoreCase("typedMember"))
                        eDimension = element1;
                    else
                        eSegmentCode = element1;
                }

        }
    }

    public String getDimensionID()
    {
        String s = null;
        if(eDimension != null)
            s = eDimension.getAttribute("dimension");
        return s;
    }

    public String getDimensionMember()
    {
        String s = null;
        if(eDimension != null)
            s = jcx.xbrl.xml.getTextStringInChild(eDimension);
        return s;
    }

    protected Element eData;
    protected Element eSegmentCode;
    protected Element eDimension;
}
