package jcx.xbrl.taxonomy;

import java.util.Vector;
import org.w3c.dom.Element;


class XlinkLocator
{

    public XlinkLocator(String s, String s1, Element element)
    {
        objFromArc = null;
        hTable = null;
        eData = null;
        strID = s;
        strLabel = s1;
        eData = element;
        hTable = new Vector();
    }

    protected XlinkArc add(XlinkArc xlinkarc)
    {
        hTable.add(xlinkarc);
        return xlinkarc;
    }

    protected XlinkLocator getLocator(String s)
    {
        XlinkLocator xlinklocator = null;
        for(int i = 0; i < hTable.size() && xlinklocator == null; i++)
        {
            XlinkArc xlinkarc = (XlinkArc)hTable.get(i);
            for(int j = 0; j < xlinkarc.objToResource.size() && xlinklocator == null; j++) {
				Object o1=xlinkarc.objToResource.get(j);
				if(o1 instanceof XlinkLocator){
					XlinkLocator xlinklocator1 = (XlinkLocator)o1;
					if(xlinklocator1.strID.equals(s)){
						xlinklocator = xlinklocator1;
					}
				}
            }

        }

        return xlinklocator;
    }
/**
*  取得 Label
*
* @param   lang .
* @return  Label
*
*/

    public String getLabel(String lang)
    {
        String s1 = null;
        for(int i = 0; i < hTable.size() && s1 == null; i++)
        {
            XlinkArc xlinkarc = (XlinkArc)hTable.get(i);
            for(int j = 0; j < xlinkarc.objToResource.size() && s1 == null; j++) {
				Object o1=xlinkarc.objToResource.get(j);
				if(o1 instanceof XlinkResource){
					XlinkResource xlinkresource = (XlinkResource)o1;
					if(xlinkresource.eData.getAttribute("xml:lang").compareTo(lang) == 0 && xlinkresource.eData.getAttribute("xlink:role").compareTo("http://www.xbrl.org/2003/role/label") == 0){
						s1 = xlinkresource.getText();
					}
				}
            }

        }

        return s1;
    }

/**
*  取得 Terse Label
*
* @return  Label
*
*/

    public String getTerseLabel()
    {
        String s = null;
        for(int i = 0; i < hTable.size() && s == null; i++)
        {
            XlinkArc xlinkarc = (XlinkArc)hTable.get(i);
            for(int j = 0; j < xlinkarc.objToResource.size() && s == null; j++)
            {
                XlinkResource xlinkresource = (XlinkResource)xlinkarc.objToResource.get(j);
                if(xlinkresource.eData.getAttribute("xlink:role").compareTo("http://www.xbrl.org/2003/role/terseLabel") == 0)
                    s = xlinkresource.getText();
            }

        }

        return s;
    }
/*
    public void print()
    {
        for(int i = 0; i < hTable.size(); i++)
        {
            XlinkArc xlinkarc = (XlinkArc)hTable.get(i);
            xlinkarc.print();
        }

    }
*/
    public String strLabel;
    public String strID;
    public XlinkArc objFromArc;
    public Vector hTable;
    public Element eData;
}
