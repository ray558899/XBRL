package jcx.xbrl.taxonomy;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.*;

public class XbrlSchemaElement
{

    public XbrlSchemaElement(Element element)
    {
        eData = null;
        eData = element;
    }


/**
*  取得 ID
*
* @return  ID
*
*/

    public String getID()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("id");
        return s;
    }


/**
*  取得 Name
*
* @return  Name
*
*/
    public String getName()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("name");
        return s;
    }

/**
	是否為 tuple
*/
    public boolean isTuple()
    {
		String a1=getParentType();
		if(a1.indexOf(":")!=-1) a1=a1.substring(a1.lastIndexOf(":")+1);
		if(a1.equals("tuple")){
			return true;
		} else {
			return false;
		}
    }

/**
*  取得 ParentType(substitutionGroup)
*
* @return  ParentType
*
*/
    public String getParentType()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("substitutionGroup");
        return s;
    }

    public String[] getItems(){ //取得 Items , (適用於 getParentType().equals("tuple"))
//	System.err.println("-- ="+getParentType());
		Vector v=new Vector();
		String a1=getParentType();
		if(a1.indexOf(":")!=-1) a1=a1.substring(a1.lastIndexOf(":")+1);
		if(a1.equals("tuple")){
			Node n1=eData.getFirstChild();
			while(n1!=null){
				while(n1.getNodeType()!=n1.ELEMENT_NODE) n1=n1.getNextSibling();
//				System.err.println("-- n1="+n1.getNodeName()+" -> "+n1.getNodeValue());
				String n2=n1.getNodeName();
				if(n2.indexOf(":")!=-1) n2=n2.substring(n2.lastIndexOf(":")+1);
				if(n2.equals("element")){
					Element m1=(Element)n1;
					String ref1=m1.getAttribute("ref");
					v.addElement(ref1);
//					System.err.println("-- ref="+ref1);
//						System.err.println("-- ref1="+m1.getNextSibling());
					while((n1=n1.getNextSibling())!=null){
						if(n1.getNodeType()==n1.ELEMENT_NODE){
							m1=(Element)n1;
							ref1=m1.getAttribute("ref");
							v.addElement(ref1);
//							System.err.println("-- m="+ref1);
						}
//						m1=(Element)n1.getNextSibling();
//						ref1=m1.getAttribute("ref");
					}

					break;
				}
				n1=n1.getFirstChild();
			}
			return (String[])v.toArray(new String[0]);
		} else {
			return null;
		}
//		return null;
	}

/**
*  取得 Type
*
* @return  Type
*
*/
    public String getType()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("type");
        return s;
    }

	public String PREFIX=null;
	public String getPrefix()
    {
        return PREFIX;
    }


/**
*  取得 PeriodType
*
* @return  PeriodType
*
*/

    public String getPeriodType()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("xbrli:periodType");
        return s;
    }

/**
*  取得 BalanceType
*
* @return  BalanceType
*
*/

    public String getBalanceType()
    {
        String s = null;
        if(eData != null)
            s = eData.getAttribute("xbrli:balance");
        return s;
    }

/**
*  是否抽象物件
*
* @return  boolean
*
*/

    public boolean isAbstract()
    {
        boolean flag = false;
        if(eData != null)
            if(eData.getAttribute("abstract").compareTo("true") == 0)
                flag = true;
            else
            if(eData.getAttribute("abstract").compareTo("false") == 0)
                flag = false;
        return flag;
    }

/**
*  是否可不輸入
*
* @return  boolean
*
*/

    public boolean isNillable()
    {
        boolean flag = false;
        if(eData != null)
            if(eData.getAttribute("nillable").compareTo("true") == 0)
                flag = true;
            else
            if(eData.getAttribute("nillable").compareTo("false") == 0)
                flag = false;
        return flag;
    }
    public Element getData(){
		return eData;
	}

    protected Element eData;
}
