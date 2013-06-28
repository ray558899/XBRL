package jcx.xbrl.data;

import org.w3c.dom.*;

class XbrlUnitDivide extends XbrlUnit
{

    public XbrlUnitDivide()
    {
        objNumerator = null;
        objDenominator = null;
        eDivide = null;
        eUnitNumerator = null;
        eUnitDenominator = null;
    }

    protected void setMeasure(String s, String s1)
    {
        if(eData != null)
        {
            eDivide = eData.getOwnerDocument().createElement("divide");
            eData.appendChild(eDivide);
            eUnitNumerator = eData.getOwnerDocument().createElement("unitNumerator");
            eDivide.appendChild(eUnitNumerator);
            eUnitDenominator = eData.getOwnerDocument().createElement("unitDenominator");
            eDivide.appendChild(eUnitDenominator);
            objNumerator = new XbrlUnitMeasure(eUnitNumerator, s);
            objDenominator = new XbrlUnitMeasure(eUnitDenominator, s1);
        }
    }

    protected void loadUnit(Element element)
    {
        if(element != null)
        {
            eData = element;
            eDivide = jcx.xbrl.xml.getChildByName(eData, "divide");
            eUnitNumerator = jcx.xbrl.xml.getChildByName(eDivide, "unitNumerator");
            eUnitDenominator = jcx.xbrl.xml.getChildByName(eDivide, "unitDenominator");
            objNumerator = new XbrlUnitMeasure();
            objNumerator.loadMeasure(eUnitNumerator);
            objDenominator = new XbrlUnitMeasure();
            objDenominator.loadMeasure(eUnitDenominator);
        }
    }

    protected XbrlUnitMeasure objNumerator;
    protected XbrlUnitMeasure objDenominator;
    protected Element eDivide;
    protected Element eUnitNumerator;
    protected Element eUnitDenominator;
}
