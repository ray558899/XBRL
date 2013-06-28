package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import jcx.xbrl.data.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.*;

public class XbrlElement{

    protected Element element=null;
    protected XbrlSchemaElement objSchema;
    protected XbrlTaxonomy TAXONOMY=null;

    protected XbrlDocument DOCUMENT=null;

	protected XbrlContext REFERENCE;

    protected XbrlElement()  {
        element = null;
    }

	Vector SUB_ITEMS=new Vector();
	
/**
*  取得 context
*
* @return  XbrlContext
*
*/
    public XbrlContext getContext(){
        return REFERENCE;
    }


	protected void setContext(XbrlContext x)
    {
        REFERENCE=x;
    }

	protected void refresh(int level){
		if(!objSchema.isTuple()) return;
//		System.err.println(jcx.xbrl.xml.print(element));

		if(level>10000){
			System.err.println(" Tuple recursive too much!!");
			return;
		}

		for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()){
			if(node.getNodeType() == 1){
				Element element1 = (Element)node;
				String s = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");
				String id = element1.getTagName().replace(':','_');
				XbrlSchemaElement s_element=null;
				try{
					s_element = TAXONOMY.getSchemaElement(id);
				} catch(Exception ee){
				}
				if(s_element==null) continue;
				
				String context_id=element1.getAttribute("contextRef");
				XbrlContext xbrlcontext = DOCUMENT.objContextList.get(context_id);

		        XbrlElement XbrlElement = null;
				XbrlElement = new XbrlElement();
                XbrlElement.setDOMData(element1);
                XbrlElement.REFERENCE = xbrlcontext;
                XbrlElement.objSchema = s_element;
                XbrlElement.TAXONOMY = TAXONOMY;
                XbrlElement.DOCUMENT = DOCUMENT;

//				System.err.println("--- "+XbrlElement.getID()+" "+s_element.isTuple());


				if(element1.getAttribute("unitRef")!=null) XbrlElement.setUnit(element1.getAttribute("unitRef"));
				try{
					if(element1.getAttribute("decimals")!=null) XbrlElement.setDecimal(Integer.parseInt(""+element1.getAttribute("decimals")));
				} catch(Exception e){}
//                XbrlElement.setDecimal(2);
                
 				if(element1.getNodeValue()!=null) XbrlElement.setValue(element1.getNodeValue());

				SUB_ITEMS.addElement(XbrlElement);

				if(s_element.isTuple()){
					XbrlElement.refresh(level+1);
				}

//		System.err.println(element1.getTagName()+"="+s);
			}
		}


	}

/**
	是否為 tuple
*/
    public boolean isTuple() {
		return objSchema.isTuple();
	}

/**
	取得 tuple 底下所有的子元素
*/

    public XbrlElement[] getAllItems(){
        return (XbrlElement[])SUB_ITEMS.toArray(new XbrlElement[0]);
    }


/**
*  建立一個新的資料項目(目前Element須為 tuple).
*
* @param   account_id 會計 ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   sdate 起始日期.
* @param   edate 結束日期.
* @return  XbrlElement
*
*/
    public XbrlElement createElement(String account_id, String value1, String unit1, String sdate, String edate)
        throws Exception
    {
        XbrlElement XbrlElement = null;
        {
			XbrlSchemaElement s_element=null;
			try{
	            s_element = TAXONOMY.getSchemaElement(account_id);
			} catch(Exception ee){
				s_element = TAXONOMY.getSchemaElement(TAXONOMY.getIDByTerseLabel(account_id));
			}

			XbrlContext xbrlcontext = DOCUMENT.objContextList.getSchemaDefinedContext(s_element, sdate, edate);
			if(xbrlcontext == null){
				if(s_element.getPeriodType().equals("duration")){
					xbrlcontext = DOCUMENT.objContextList.createDurationContext(DOCUMENT.ROOT, DOCUMENT.DefaultEntityURI, DOCUMENT.DefaultCompanyCode, sdate, edate);
				} else if(s_element.getPeriodType().equals("instant")){
					xbrlcontext = DOCUMENT.objContextList.createInstantContext(DOCUMENT.ROOT, DOCUMENT.DefaultEntityURI, DOCUMENT.DefaultCompanyCode, edate);
				}
			}

           XbrlElementPool xbrlelementpool = (XbrlElementPool)DOCUMENT.ELEMENTS.get(account_id);
			if(xbrlelementpool == null)
			{
				xbrlelementpool = new XbrlElementPool();
				xbrlelementpool.objSchema = s_element;
				xbrlelementpool.objTaxonomy = TAXONOMY;
				DOCUMENT.ELEMENTS.put(account_id, xbrlelementpool);
			}

//        System.err.println("-- s_element="+s_element.isTuple());
//			if(s_element.isTuple()){
//                XbrlElement = createElement(TAXONOMY, account_id, value1, unit1, null);
//				throw new Exception(account_id+" is tuple element ,please use createElement(XbrlTaxonomy,ID,String[] subID,String[] context,value )");
//			} else {
			{

				XbrlUnit xbrlunit = null;
				String digits="";
				if(unit1.indexOf(",")!=-1){
					digits=unit1.substring(unit1.indexOf(",")+1);
					unit1=unit1.substring(0,unit1.indexOf(","));
				}

				if(unit1!=null && !unit1.equals("")){
					xbrlunit=XbrlUnit.getUnit(unit1);
					if(xbrlunit == null) throw new Exception("Unit " + unit1 + " not found!");
				}

                String s4 = s_element.getPrefix() + ":" + s_element.getName();
                Element element1 = element.getOwnerDocument().createElement(s4);
                if(xbrlcontext!=null) element1.setAttribute("contextRef", xbrlcontext.getID());
                element.appendChild(element1);
//                element.appendChild(element.getOwnerDocument().createTextNode("\r\n"));

                XbrlElement = new XbrlElement();
                XbrlElement.setDOMData(element1);
                XbrlElement.REFERENCE = xbrlcontext;
                XbrlElement.objSchema = s_element;
                XbrlElement.TAXONOMY = TAXONOMY;
                XbrlElement.DOCUMENT = DOCUMENT;

//                XbrlElement.setDecimal(DOCUMENT.iDefaultDecimal);
                if(s_element.getType().compareTo("xbrli:monetaryItemType") == 0) {
       				if(xbrlunit!=null){
						XbrlElement.setUnit(xbrlunit.getID());
					} else {
						XbrlElement.setUnit("TWD");
					}
                    XbrlElement.setDecimal(DOCUMENT.iDefaultDecimal);
                } else if(s_element.getType().compareTo("xbrli:sharesItemType") == 0)  {
       				if(xbrlunit!=null){
	                    XbrlElement.setUnit(xbrlunit.getID());
					} else {
						XbrlElement.setUnit("Shares");
					}
                    XbrlElement.setDecimal(0);
                } else if(s_element.getType().compareTo("xbrli:decimalItemType") == 0)  {
       				if(xbrlunit!=null){
	                    XbrlElement.setUnit(xbrlunit.getID());
					} else {
						XbrlElement.setUnit("TWD");
					}
                    XbrlElement.setDecimal(2);
                }
				if(!digits.equals("")){
					try{
	                    XbrlElement.setDecimal(Integer.parseInt(digits));
					} catch(Exception ee){
						if(digits.equalsIgnoreCase("NA") ||digits.equalsIgnoreCase("N/A") ){
							XbrlElement.element.removeAttribute("decimals");
						}
					}
				}
                
                {
                    XbrlElement.setValue(value1);
                }
                xbrlelementpool.add(XbrlElement);

            }
			SUB_ITEMS.addElement(XbrlElement);
        }
        return XbrlElement;
    }

	
/**
*  取得 SchemaElement
*
* @return  String ID
*
*/

    public XbrlSchemaElement getSchemaElementD()
    {
        return objSchema;
    }


/**
*  取得 ID
*
* @return  String ID
*
*/

    public String getID()
    {
        return objSchema.getID();
    }

/**
*  取得 Prefix 
*
* @return  Prefix name
*
*/

    public String getPrefix()
    {
        return jcx.xbrl.xml.getNameSpace(element.getTagName(), "");
    }

/**
*  取得 資料內容 getAttribute("decimals")
*
* @return  String
*
*/

    public String getDecimal()
    {
        if(element == null) return null;
        return element.getAttribute("decimals");
    }

/**
*  設定 資料內容 setAttribute("decimals",i)
*
* @param   i 資料
*
*/

    public void setDecimal(int i)
    {
        if(element != null)
            element.setAttribute("decimals", (new String()).valueOf(i));
    }

/**
*  設定 資料單位 setAttribute("unitRef",unit)
*
* @param   unit 單位
*
*/
    public void setUnit(String unit)
    {
        if(element != null)
            element.setAttribute("unitRef", unit);
    }
/**
*  取得 資料單位 getAttribute("unitRef")
*
* @return  String
*
*/

    public String getUnit()
    {
        return element.getAttribute("unitRef");
    }

    protected void setDOMData(Element element1)
    {
        element = element1;
    }

/**
*  取得 Dom element
*
* @return  Element
*
*/

    public Element getData()
        throws NullPointerException
    {
		return element;
    }


/**
*  取得 資料內容 
*
* @return  String
*
*/

    public String getValue()
        throws NullPointerException
    {
        String s = null;
        if(element != null)
            s = jcx.xbrl.xml.getTextStringInChild(element);
        else
            throw new NullPointerException("Null element , setDOMData(Element element) first!");
        return s;
    }

    protected long getLong()
        throws NullPointerException
    {
        long l = 0L;
        try
        {
            l = Long.parseLong(getValue());
        }
        catch(Exception exception) { }
        return l;
    }
/**
*  設定 資料內容 
*
* @param   s 資料
*
*/

    public void setValue(String s)
        throws NullPointerException
    {
        if(element != null)
        {
            if(s != null)
                jcx.xbrl.xml.setTextStringInChild(element, s);
            else
                throw new NullPointerException("value not allow null");
        } else {
            throw new NullPointerException("Null element , setDOMData(Element element) first!");
        }
    }

    protected boolean isEmpty()
    {
        return element == null;
    }

}
