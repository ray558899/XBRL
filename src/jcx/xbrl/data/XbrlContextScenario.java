package jcx.xbrl.data;

import org.w3c.dom.*;

public class XbrlContextScenario
{

    protected XbrlContextScenario()
    {
        eData = null;
        eDimension = null;
    }

    protected XbrlContextScenario(Element element, String s, String s1)
    {
        eData = null;
        eDimension = null;
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("scenario");
            element.appendChild(eData);
            eDimension = element.getOwnerDocument().createElement("xbrldi:explicitMember");
            eData.appendChild(eDimension);
            eDimension.setAttribute("dimension", s);
            jcx.xbrl.xml.setTextStringInChild(eDimension, s1);
        }
    }

    protected void setData(Element element)
    {
        eData = element;
        if(eData != null)
        {
            Element element1 = jcx.xbrl.xml.getFirstElement(eData);
            if(element1.getTagName().equals("xbrldi:explicitMember"))
                eDimension = element1;
            else
            if(element1.getTagName().equals("xbrldi:typedMember"))
                eDimension = element1;
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
    protected Element eDimension;
}
