
package jcx.xbrl.taxonomy;

import java.io.PrintStream;
import java.util.Vector;
import org.w3c.dom.Element;

class XlinkArc
{

    public XlinkArc(Element element)
    {
        eData = null;
        eData = element;
        strRoleAttribute = "xlink:role";
        objToResource = new Vector();
    }

    public void add(Object obj)
    {
//		System.err.println("--- add "+obj.getClass().getName());
        objToResource.add(obj);
    }

    public String getRole()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute(strRoleAttribute);
        return s;
    }

    public String getArcRole()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("xlink:arcrole");
        return s;
    }

    public Object getFirstResource()
    {
        return objToResource.get(0);
    }

    public double getOrder()
    {
        double d = -1D;
        String s = eData.getAttribute("order");
        if(s.compareTo("") != 0)
            d = Double.parseDouble(s);
        return d;
    }
/*
    public void print()
    {
        System.out.print("        ");
        System.out.println(jcx.xbrl.xml.print(eData));

        for(int i = 0; i < objToResource.size(); i++)
        {
            XlinkResource xlinkresource = (XlinkResource)objToResource.get(i);
            xlinkresource.print();
        }

    }
*/
    public String strRole;
    public String strFrom;
    public String strTo;
    public String strRoleAttribute;
    public Vector objToResource;
    public Element eData;
}
