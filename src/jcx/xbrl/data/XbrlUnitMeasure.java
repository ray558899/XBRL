package jcx.xbrl.data;

import org.w3c.dom.*;

class XbrlUnitMeasure
{

    public XbrlUnitMeasure()
    {
        eData = null;
    }

    public XbrlUnitMeasure(Element element, String s)
    {
        eData = null;
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("measure");
            element.appendChild(eData);
            jcx.xbrl.xml.setTextStringInChild(eData, s);
        }
    }

    protected void loadMeasure(Element element)
    {
        eData = element;
    }

    public String getCode()
    {
        if(eData != null)
            return jcx.xbrl.xml.getTextStringInChild(eData);
        else
            return null;
    }

    public void setCode(String s)
    {
        if(eData != null)
            jcx.xbrl.xml.setTextStringInChild(eData, s);
    }

    protected Element eData;
}
