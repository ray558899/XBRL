package jcx.xbrl.taxonomy;

import java.io.PrintStream;
import org.w3c.dom.Element;

class XlinkResource{

	String ID1="";
	String TEXT1="";
    public XlinkResource(Element element)
    {
        eData = element;
        strIDAttribute = "xlink:label";
		{
			String s = null;
			if(eData != null) s = eData.getAttribute(strIDAttribute);
			ID1=s;
		}

		{
			TEXT1=jcx.xbrl.xml.getTextStringInChild(eData);
		}
//		if(ID1.startsWith("label_CashCash")) System.err.println(" "+eData.getAttribute("xlink:role")+"  "+ID1+" -> "+TEXT1);
    }

/**
*  取得 ID
*
* @return  ID
*
*/

    public String getID()
    {
        return ID1;
    }

/**
*  取得 內文
*
* @return  內文
*
*/

    public String getText()
    {
        return TEXT1;
    }

/*
    public void print()
    {
        System.out.print("              ");
        System.out.println(jcx.xbrl.xml.print(eData));
    }
*/
    public String strIDAttribute;
    public Element eData;
}
