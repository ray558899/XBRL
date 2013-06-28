package jcx.xbrl.taxonomy;

import jcx.xbrl.exception.*;
import java.io.PrintStream;
import java.util.*;
import org.w3c.dom.*;

public class XbrlLabel
{

    public XbrlLabel()
    {
        hLocatorTable = new Hashtable();
        hCodeTable = new Hashtable();
    }

    protected int load(Hashtable hashtable, Element element)
        throws TaxonomyFormatErrorException
    {
        int k = 0;
        Object obj = null;
        Vector vector = new Vector();
        Hashtable hashtable1 = new Hashtable();
        Hashtable hashtable2 = new Hashtable();
        eLabel = element;
        hSchemaTable = hashtable;
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            if(node.getNodeType() == 1) {
                Element element1 = (Element)node;
				String type1=element1.getAttribute("xlink:type");

                if(type1.compareTo("resource") == 0) {
                    XlinkResource xlinkresource = new XlinkResource(element1);
                    Vector vector1 = (Vector)hashtable1.get(xlinkresource.getID());
                    if(vector1 == null)  {
                        vector1 = new Vector();
                        hashtable1.put(element1.getAttribute("xlink:label"), vector1);
                    }
                    vector1.add(xlinkresource);
//					System.out.println("-- "+xlinkresource.getID()+"="+xlinkresource.getText());
                } else if(type1.compareTo("arc") == 0)   {
                    XlinkArc xlinkarc = new XlinkArc(element1);
                    vector.add(xlinkarc);
                } else if(type1.compareTo("locator") == 0)  {
                    XlinkLocator xlinklocator = new XlinkLocator(jcx.xbrl.xml.getElementName(element1.getAttribute("xlink:href"), "#"), element1.getAttribute("xlink:label"), element1);

//System.err.println("--- locater ="+xlinklocator.strID+" "+hLocatorTable.get(xlinklocator.strID));
					Vector vv=(Vector)hLocatorTable.get(xlinklocator.strID);
					if(vv==null){
						vv=new Vector();
						hLocatorTable.put(xlinklocator.strID, vv);
					}
					vv.addElement(xlinklocator);

//                    hLocatorTable.put(xlinklocator.strID, xlinklocator); //調為 Vector,label 定義可能為複數

                    hashtable2.put(element1.getAttribute("xlink:label"), xlinklocator);
                }
            }

        for(int i = 0; i < vector.size(); i++)
        {
            XlinkArc xlinkarc1 = (XlinkArc)vector.get(i);
            XlinkLocator xlinklocator1 = (XlinkLocator)hashtable2.get(xlinkarc1.eData.getAttribute("xlink:from"));
            xlinklocator1.add(xlinkarc1);
            Vector vector2 = (Vector)hashtable1.get(xlinkarc1.eData.getAttribute("xlink:to"));
            for(int j = 0; j < vector2.size(); j++) {
                XlinkResource xlinkresource1 = (XlinkResource)vector2.get(j);
                xlinkarc1.add(xlinkresource1);
                if(xlinkresource1.eData.getAttribute("xlink:role").equals("http://www.xbrl.org/2003/role/terseLabel")){
					String r1=xlinkresource1.getText();
//					System.err.println("-- "+r1);
                    if(r1!=null){
						Vector v=(Vector)hCodeTable.get(r1);
						if(v==null){
							v=new Vector();
							hCodeTable.put(r1, v);
						}
						v.addElement(xlinklocator1);
//						hCodeTable.put(r1, xlinklocator1);
					}
				}
            }

        }

//        System.out.println("Label loaded...");
        return k;
    }

/**
*  以 ID 取得 Label,預設 Role 是 http://www.xbrl.org/2003/role/label
*
* @param   ID .
* @param   LANG .
* @return  Label
*
*/

    public String getLabelByID(String ID, String LANG)
        throws Exception
    {
        String s2 = null;
        try
        {
            s2 = getLabel(ID, LANG, "http://www.xbrl.org/2003/role/label");
        }
        catch(Exception e1)
        {
            throw e1;
        }
        return s2;
    }

/**
*  以 ID 取得 TerseLabel,預設 Role 是 http://www.xbrl.org/2003/role/terseLabel
*
* @param   ID .
* @return  Label
*
*/

    public String getTerseLabelByID(String ID)
        throws Exception
    {
        String s1 = null;
        try
        {
            s1 = getLabel(ID, null, "http://www.xbrl.org/2003/role/terseLabel");
        }
        catch(Exception e1)
        {
            throw e1;
        }
        return s1;
    }

/**
*  以 TerseLabel 取得 ID
*
* @param   TerseLabel .
* @return  ID
*
*/
    public String getIDByTerseLabel(String TerseLabel)
        throws Exception
    {
        String s1 = null;
		Vector v=(Vector)hCodeTable.get(TerseLabel);
		if(v==null){
            throw new Exception("Element id (" + TerseLabel + ") not found.");
		}

		StringBuffer sb=new StringBuffer();
		for(int i=0;i<v.size();i++){
	        XlinkLocator xlinklocator = (XlinkLocator)v.elementAt(i);
			sb.append(xlinklocator.strID);
			if(i!=v.size()-1){
				sb.append(",");
			}
		}
		return sb.toString();

//        XlinkLocator xlinklocator = (XlinkLocator)hCodeTable.get(TerseLabel);
//        if(xlinklocator != null)
//            s1 = xlinklocator.strID;
//        else
//            throw new Exception("Element id (" + TerseLabel + ") not found.");
//        return s1;
    }

/**
*  以 ID 取得 Label
*
* @param   ID .
* @param   LANG .
* @param   ROLE .
* @return  Label
*
*/


    public String getLabel(String ID, String LANG, String ROLE)
        throws Exception
    {

        String s3 = null;
        String s3_non_en = null;

//        XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
//        if(xlinklocator != null) {
		Vector vv=(Vector)hLocatorTable.get(ID);
		if(vv!=null){
			for(int z=0;z<vv.size();z++){
				XlinkLocator xlinklocator = (XlinkLocator)vv.elementAt(z);
				for(int i = 0; i < xlinklocator.hTable.size() && s3 == null; i++) {
					XlinkArc xlinkarc = (XlinkArc)xlinklocator.hTable.get(i);
					for(int j = 0; j < xlinkarc.objToResource.size() && s3 == null; j++){
						XlinkResource xlinkresource = (XlinkResource)xlinkarc.objToResource.get(j);
						if(LANG != null) {
							String lang=xlinkresource.eData.getAttribute("xml:lang");
	//						System.err.println("--- "+lang+" -> "+xlinkresource.getText());
							if(lang.compareTo(LANG) == 0 && xlinkresource.eData.getAttribute("xlink:role").compareTo(ROLE) == 0){
								s3 = xlinkresource.getText();
							} else if(lang.compareTo("en") != 0 && xlinkresource.eData.getAttribute("xlink:role").compareTo(ROLE) == 0){
								s3_non_en = xlinkresource.getText();
							}
						} else {
							if(xlinkresource.eData.getAttribute("xlink:role").compareTo(ROLE) == 0) {
								s3 = xlinkresource.getText();
							}
						}
					}

				}
			}
			if(s3==null && s3_non_en!=null) s3=s3_non_en;

        } else {
//            throw new Exception("Element id not found (" + s+")");
			return null;
        }
        return s3;
    }

    protected void getAllLabel(String s, Element element)
    {
        XlinkLocator xlinklocator = (XlinkLocator)hLocatorTable.get(s);
        if(xlinklocator != null)
        {
            for(int i = 0; i < xlinklocator.hTable.size(); i++)
            {
                XlinkArc xlinkarc = (XlinkArc)xlinklocator.hTable.get(i);
                for(int j = 0; j < xlinkarc.objToResource.size(); j++)
                {
                    XlinkResource xlinkresource = (XlinkResource)xlinkarc.objToResource.get(j);
                    Element element1 = element.getOwnerDocument().createElement("label");
                    element.appendChild(element1);
                    element1.setAttribute("language", xlinkresource.eData.getAttribute("xml:lang"));
                    element1.setAttribute("role", xlinkresource.eData.getAttribute("xlink:role"));
                    element1.setAttribute("caption", xlinkresource.getText());
                }

            }

        }
    }


    protected Element eLabel;
    Hashtable hSchemaTable;
    Hashtable hLocatorTable;
    Hashtable hCodeTable;
}
