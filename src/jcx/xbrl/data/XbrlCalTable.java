package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.*;
import java.math.*;

class XbrlCalTable
{

    public XbrlCalTable(XbrlTaxonomy xbrltaxonomy1, Element element)
    {
        eRoot = null;
        objFieldList = null;
        objRowList = null;
        iErrorCount = 0;
        xbrltaxonomy = xbrltaxonomy1;
        objFieldList = new Vector();
        objRowList = new Vector();
        eRoot = element;
        doc = element.getOwnerDocument();
    }


    public void getColumnMember(Element element, boolean recursive)
    {
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
            if(node.getNodeType() == 1) {
                Element element1 = (Element)node;
                objFieldList.add(element1.getTagName());
                if(recursive){
                    getColumnMember(element1, recursive);
				}
            }

    }

    public void createTable(Element element, Element element1, Hashtable ELEMENTS, String sdate, String edate, XbrlContextList xbrlcontextlist, String dname, 
            String XID, String YID) throws Exception
    {
        NodeList nodelist = element.getElementsByTagName(XID);

        if(nodelist.getLength() > 0) {
            Element element2 = (Element)nodelist.item(0);
            getColumnMember(element2, true);
        }

        Element element3 = jcx.xbrl.xml.getFirstElement(element1);

//		System.err.println("--element3="+xbrltaxonomy.getTerseLabelByID(element3.getTagName())+"("+xbrltaxonomy.getLabelByID(element3.getTagName(),"zh")+")");
//        for(Node node = element3.getFirstChild(); node != null; node = node.getNextSibling()){
//            if(node.getNodeType() == 1){
//                Element elementx = (Element)node;
//				System.err.println("    x="+xbrltaxonomy.getTerseLabelByID(elementx.getTagName())+"("+xbrltaxonomy.getLabelByID(elementx.getTagName(),"zh")+")");
//			}
//		}
			

        for(int i = 0; i < objFieldList.size(); i++){
            String account_id = (String)objFieldList.get(i);

//			System.err.println("--s5="+s5);

            Element element4 = doc.createElement("member");
            eRoot.appendChild(element4);
            element4.setAttribute("contextRef", account_id);
            Element element5 = doc.createElement(element3.getTagName());
			XbrlDocument.cloneNode1(element3,element5,true);

//			Element element5=(Element)element3.cloneNode(true);
//            jcx.xbrl.xml.cloneElement(element3, element5);

            element4.appendChild(element5);

            fillData(element5, ELEMENTS, sdate, edate, xbrlcontextlist, dname, account_id);
        }

    }
    public void fillData(Element element, Hashtable ELEMENTS, String sdate, String edate, XbrlContextList xbrlcontextlist, String dname, String account_id) throws Exception
    {
        Object obj = null;
        Object obj1 = null;
        String YID = element.getTagName();
        XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(YID);
//        XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(account_id);

//System.err.println(" --   YID="+YID+"("+xbrltaxonomy.getTerseLabelByID(YID)+") pool="+xbrlelementpool);


//		System.err.println(" -- pool("+s4+")  "+xbrlelementpool+" "+ELEMENTS.get("tw-gaap-fsci-se_BeginningBalance").getClass().getName());

		if(xbrlelementpool != null) {
            XbrlContext xbrlcontext;
            if(xbrlelementpool.objSchema.getPeriodType().equals("duration"))
                xbrlcontext = xbrlcontextlist.containsDimensionMemberDurationContext(dname, xbrltaxonomy.getNameByID(account_id), sdate, edate);
            else
                xbrlcontext = xbrlcontextlist.containsDimensionMemberInstantContext(dname, xbrltaxonomy.getNameByID(account_id), edate);

            XbrlElement xelement = xbrlelementpool.get(xbrlcontext);

//			System.err.println(" -- aa "+xelement);

            if(xelement != null){
//			System.err.println(" -- SET ("+xbrltaxonomy.getTerseLabelByID(YID)+"."+xbrltaxonomy.getTerseLabelByID(account_id)+")="+xelement.getValue());
                element.setAttribute("value", xelement.getValue());
			}
        }

        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()){
            if(node.getNodeType() == 1) {
                Element element1 = (Element)node;
                fillData(element1, ELEMENTS, sdate, edate, xbrlcontextlist, dname, account_id);
            }
		}

    }

    public int validate(XbrlTaxonomy xbrltaxonomy, Element element)
        throws Exception
    {
//	System.err.println("-- "+element.getNodeName()+"("+element.getNodeType()+")="+element.getNodeValue()+jcx.xbrl.xml.print(element));
//	System.err.println("-- "+jcx.xbrl.xml.print(eRoot));

		iErrorCount = 0;
        for(Node node = eRoot.getFirstChild(); node != null; node = node.getNextSibling()){

//			System.err.println("-- "+node.getNodeName()+"("+node.getNodeType()+")="+jcx.xbrl.xml.print(node));

			if(node.getNodeType() == 1) {
                Element element1 = (Element)node;
//                validateElement(xbrltaxonomy, (Element)element1.getFirstChild(), element);
				StringBuffer sb=new StringBuffer();
                if(element1.hasChildNodes()) validateElement(sb,0,xbrltaxonomy, (Element)element1.getFirstChild(), element);

//				System.err.println("-- "+sb);
            }
		}

        return iErrorCount;
    }

    public BigDecimal validateElement(StringBuffer sb,int level,XbrlTaxonomy xbrltaxonomy, Element element, Element element1)
        throws Exception
    {
		String tab1="";
		for(int i=0;i<level;i++) tab1+="  ";

		BigDecimal a0=new BigDecimal("0"); //未 乘 iWeight 的值
		BigDecimal a1=new BigDecimal("0");


        if(element.hasAttribute("value")) {

//			System.err.println("-- "+element.getTagName()+"="+element.getAttribute("value"));

			String v1=element.getAttribute("value");
			if(v1.equals("")) v1="0";
			try{
				a1=new BigDecimal(v1);
				a0=a1;
				if(element.hasAttribute("weight")){
					a1=a1.multiply(new BigDecimal(element.getAttribute("weight")));
				}
			} catch(NumberFormatException e){
				throw e;
			}
			try{
				sb.append(xbrltaxonomy.getTerseLabelByID(element.getTagName())+"("+v1+"*"+element.getAttribute("weight")+")"+"+");
			} catch(Exception e){
			}
		} else {
			try{
				sb.append(xbrltaxonomy.getTerseLabelByID(element.getTagName())+"(0)"+"+");
			} catch(Exception e){
			}
		}


        if(element.hasChildNodes()){
			StringBuffer sb1=new StringBuffer();

			String tname=element.getTagName();

			BigDecimal sum=new BigDecimal("0");
//			System.err.println("-- "+element.getNodeName()+"("+element.getNodeType()+")="+jcx.xbrl.xml.print(element));
			for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()){
	//			System.err.println("-- "+node.getNodeName()+"("+node.getNodeType()+")="+node.getNodeValue());
				if(node.getNodeType() == 1) {
					Element child = (Element)node;

					BigDecimal a2=new BigDecimal("0");
					a2=validateElement(sb1,level+1,xbrltaxonomy, child, element1);
					if(a2!=null) sum=sum.add(a2);
				}
			}
	        element.setAttribute("calculation", ""+sum);

			if(a0.compareTo(sum)!=0) { //不合
				
					Element element2 = element1.getOwnerDocument().createElement("element");
					element1.appendChild(element2);
					element2.setAttribute("id", element.getTagName());
					element2.setAttribute("value", ""+element.getAttribute("value"));
					element2.setAttribute("calculation", (new String()).valueOf(sum));
					element2.setAttribute("formula", xbrltaxonomy.getTerseLabelByID(element.getTagName())+"("+element.getAttribute("value")+")="+sb1.toString());
					iErrorCount++;

//				System.err.println(xbrltaxonomy.getTerseLabelByID(tname)+"("+element.getAttribute("value")+")="+sb1.toString());
				
			}

			try{
//				System.err.println(xbrltaxonomy.getTerseLabelByID(tname)+"("+element.getAttribute("value")+")="+sb1.toString());
			} catch(Exception ee){
			}

		}



		return a1;
    }

    public Document doc;
    public Element eRoot;
    public Vector objFieldList;
    public Vector objRowList;
    public XbrlTaxonomy xbrltaxonomy;
    public int iErrorCount;
}
