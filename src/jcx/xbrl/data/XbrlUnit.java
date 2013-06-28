
package jcx.xbrl.data;

import org.w3c.dom.*;
import java.util.*;


public class XbrlUnit
{
	static Hashtable UNITS=new Hashtable();

    static public XbrlUnit getUnit(String s)
    {
        XbrlUnit xbrlunit = null;
        xbrlunit = (XbrlUnit)UNITS.get(s);
        return xbrlunit;
    }

    public XbrlUnit()
    {
        objMeasure = null;
    }

    public String getID()
    {
        if(eData != null)
            return eData.getAttribute("id");
        else
            return null;
    }

    public void create(Element element, String s)
    {
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("unit");
            element.appendChild(eData);
            eData.setAttribute("id", s);
			UNITS.put(s,this);
        }
    }

    protected void loadUnit(Element element)
    {
        if(element != null)
        {
            eData = element;
            Element element1 = jcx.xbrl.xml.getFirstElement(element);
            if(element1.getTagName().equals("measure"))
            {
                objMeasure = new XbrlUnitMeasure();
                objMeasure.loadMeasure(eData);
            } else
            if(!element1.getTagName().equals("divide"));
        }
    }

    public void setMeasure(String s)
    {
        if(eData != null)
            objMeasure = new XbrlUnitMeasure(eData, s);
    }

    protected Element eData;
    protected XbrlUnitMeasure objMeasure;
}
