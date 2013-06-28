package jcx.xbrl.data;

import org.w3c.dom.*;


public class XbrlEntity
{

    public XbrlEntity()
    {
        strURI = null;
        strCode = null;
        eData = null;
        objSegment = null;
    }

    public XbrlEntity(Element element, String s, String s1)
    {
        strURI = null;
        strCode = null;
        eData = null;
        objSegment = null;
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("entity");
            element.appendChild(eData);
            eIdentifier = element.getOwnerDocument().createElement("identifier");
            eData.appendChild(eIdentifier);
            eIdentifier.setAttribute("scheme", s);
            jcx.xbrl.xml.setTextStringInChild(eIdentifier, s1);
            strURI = s;
            strCode = s1;
        }
    }

    public String getCode()
    {
        return strCode;
    }

    public void setCode(String s)
    {
        strCode = s;
    }

    public void setCodeURI(String s)
    {
        strURI = s;
    }

    public String getSegmentCode()
    {
        String s = null;
        if(objSegment != null)
            s = objSegment.getValue();
        return s;
    }

    public void setSegmentCode(String s)
    {
        if(objSegment != null)
            objSegment = new XbrlEntitySegment(eData, s);
        else
            objSegment.setValue(s);
    }

    public void setData(Element element)
    {
        eData = element;
        if(eData != null)
        {
            for(Node node = eData.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    String s = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");
                    if(s.equals("identifier"))
                    {
                        eIdentifier = element1;
                        strURI = eIdentifier.getAttribute("scheme");
                        strCode = jcx.xbrl.xml.getTextStringInChild(eIdentifier);
                    } else
                    if(s.equals("segment"))
                    {
                        objSegment = new XbrlEntitySegment();
                        objSegment.loadSegment(element1);
                    }
                }

        }
    }

    public void setSegment(String s)
    {
        if(objSegment != null)
            objSegment.setValue(s);
        else
        if(eData != null)
            objSegment = new XbrlEntitySegment(eData, s);
    }

    public void setSegment(Element element)
    {
        if(objSegment != null)
            objSegment.eData = element;
        else
        if(eData != null)
        {
            objSegment = new XbrlEntitySegment();
            objSegment.eData = element;
        }
    }

    protected String strURI;
    protected String strCode;
    protected Element eData;
    protected XbrlEntitySegment objSegment;
    protected Element eIdentifier;
}
