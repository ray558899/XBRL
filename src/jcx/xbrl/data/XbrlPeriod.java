package jcx.xbrl.data;
import java.util.*;

import org.w3c.dom.*;


public class XbrlPeriod
{

    public XbrlPeriod()
    {
        strPeriodType = null;
        eData = null;
        objInstantDate = null;
        objStartDate = null;
        objEndDate = null;
        eInstantDate = null;
        eStartDate = null;
        eEndDate = null;
    }

    public XbrlPeriod(Element element, String s)
    {
        strPeriodType = null;
        eData = null;
        objInstantDate = null;
        objStartDate = null;
        objEndDate = null;
        eInstantDate = null;
        eStartDate = null;
        eEndDate = null;
        if(element != null)
        {
            strPeriodType = "instant";
            objInstantDate = new XbrlDate();
            objInstantDate.setDate(s);
            eData = element.getOwnerDocument().createElement("period");
            element.appendChild(eData);
            eInstantDate = element.getOwnerDocument().createElement("instant");
            eData.appendChild(eInstantDate);
            jcx.xbrl.xml.setTextStringInChild(eInstantDate, s.substring(0,4)+"-"+s.substring(4,6)+"-"+s.substring(6,8));
        }
    }

    public XbrlPeriod(Element element, String s, String s1)
    {
        strPeriodType = null;
        eData = null;
        objInstantDate = null;
        objStartDate = null;
        objEndDate = null;
        eInstantDate = null;
        eStartDate = null;
        eEndDate = null;
        if(element != null)
        {
            strPeriodType = "duration";
            objStartDate = new XbrlDate();
            objStartDate.setDate(s);
            objEndDate = new XbrlDate();
            objEndDate.setDate(s1);
            eData = element.getOwnerDocument().createElement("period");
            element.appendChild(eData);
            eStartDate = element.getOwnerDocument().createElement("startDate");
            eData.appendChild(eStartDate);
            jcx.xbrl.xml.setTextStringInChild(eStartDate, s.substring(0,4)+"-"+s.substring(4,6)+"-"+s.substring(6,8));
            eEndDate = element.getOwnerDocument().createElement("endDate");
            eData.appendChild(eEndDate);
            jcx.xbrl.xml.setTextStringInChild(eEndDate, s1.substring(0,4)+"-"+s1.substring(4,6)+"-"+s1.substring(6,8));
        }
    }

    public String getInstantDateString()
    {
        String s = null;
        s = objInstantDate.getDate();
        return s;
    }

    public String getStartDateString()
    {
        String s = null;
        s = objStartDate.getDate();
        return s;
    }

    public String getEndDateString()
    {
        String s = null;
        s = objEndDate.getDate();
        return s;
    }

    public String getPeriodType()
    {
        return strPeriodType;
    }


	private static String[] StringToken(String str1,String str2) {	//max

			Vector v=cutToken(str1,str2);
			return (String[])v.toArray(new String[0]);
	}
	static private Vector cutToken(String data1,String deli){
		int i=0;
		int p=0;
		String sep=deli;

		Vector v=new Vector();
		while((p=data1.indexOf(sep,i))!=-1){
			v.addElement(data1.substring(i,p));
			i=p+sep.length();
		}
		v.addElement(data1.substring(i));

		return v;
	}

    private String getDate1(String s, String s1)
    {
        String s2 = null;
        if(s.length() >= 8)
        {
            String as[] = StringToken(s, s1);
            if(as[1].length() < 2)
                as[1] = "0" + as[1];
            if(as[2].length() < 2)
                as[2] = "0" + as[2];
            s2 = as[0] + as[1] + as[2];
        }
        return s2;
    }

	protected void setData(Element element)
    {
        if(element != null)
        {
            eData = element;
            for(Node node = eData.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    String s = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");
                    if(s.equals("instant"))
                    {
                        eInstantDate = element1;
                        strPeriodType = "instant";
                        objInstantDate = new XbrlDate();
						String s1=jcx.xbrl.xml.getTextStringInChild(eInstantDate);
                        objInstantDate.setDate(getDate1(s1, "-"));
                    } else
                    if(s.equals("startDate"))
                        eStartDate = element1;
                    else
                    if(s.equals("endDate"))
                        eEndDate = element1;
                }

            if(eStartDate != null && eEndDate != null)
            {
                strPeriodType = "duration";
                objStartDate = new XbrlDate();
                objStartDate.setDate(getDate1(jcx.xbrl.xml.getTextStringInChild(eStartDate), "-"));
                objEndDate = new XbrlDate();
                objEndDate.setDate(getDate1(jcx.xbrl.xml.getTextStringInChild(eEndDate), "-"));
            }
        }
    }

    protected String strPeriodType;
    protected Element eData;
    protected XbrlDate objInstantDate;
    protected XbrlDate objStartDate;
    protected XbrlDate objEndDate;
    private Element eInstantDate;
    private Element eStartDate;
    private Element eEndDate;
}
