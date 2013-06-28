package jcx.xbrl.data;

import jcx.xbrl.exception.*;
import jcx.xbrl.taxonomy.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;


public class XbrlDocument{
    protected Document doc;
    protected Element ROOT;
    protected Element LINK;
    protected Hashtable TAXONOMYS;
    protected Hashtable ELEMENTS;
    protected XbrlContextList objContextList;
    protected String DefaultEntityURI;
    protected String DefaultCompanyCode;
    protected String DefaultCurrency;
    protected int iDefaultDecimal=-3;
	protected XbrlTaxonomy TAXONOMY=null;

    public XbrlDocument(XbrlTaxonomy xbrltaxonomy)throws Exception{
		this();
		initiate(xbrltaxonomy);
		TAXONOMY=xbrltaxonomy;
	}
    public XbrlDocument(){
        doc = null;
        LINK = null;
        TAXONOMYS = null;
        ELEMENTS = null;
        DefaultEntityURI = null;
        DefaultCompanyCode = null;
        DefaultCurrency = null;
        objContextList = new XbrlContextList();
        TAXONOMYS = new Hashtable();
        ELEMENTS = new Hashtable();
        doc = jcx.xbrl.xml.getNewDocument();
        ROOT = doc.createElement("xbrl");
        doc.appendChild(ROOT);
        LINK = doc.createElement("link:schemaRef");
        LINK.setAttribute("xlink:type", "simple");
        LINK.setAttribute("xlink:arcrole", "http://www.w3.org/1999/xlink/properties/linkbase");
        ROOT.appendChild(LINK);
        DefaultCurrency = "TWD";
        DefaultEntityURI = "http://www.tse.com.tw";

        XbrlUnit xbrlunit = new XbrlUnit();
        xbrlunit.create(ROOT, "TWD");
        xbrlunit.setMeasure("iso4217:TWD");

        xbrlunit = new XbrlUnit();
        xbrlunit.create(ROOT, "Shares");
        xbrlunit.setMeasure("xbrli:shares");

		XbrlUnitDivide xbrlunitdivide = new XbrlUnitDivide();
        xbrlunitdivide.create(ROOT, "EarningsPerShare");
        xbrlunitdivide.setMeasure("iso4217:TWD", "xbrli:shares");

	}

    protected void initiate(XbrlTaxonomy xbrltaxonomy)
        throws Exception
    {
            putTaxonomy(xbrltaxonomy.getPrefix(), xbrltaxonomy);
            if(ROOT != null)
            {
                ROOT.setAttribute("xmlns", "http://www.xbrl.org/2003/instance");
                ROOT.setAttribute("xmlns:xbrli", "http://www.xbrl.org/2003/instance");
                ROOT.setAttribute("xmlns:link", "http://www.xbrl.org/2003/linkbase");
                ROOT.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
                ROOT.setAttribute("xmlns:iso4217", "http://www.xbrl.org/2003/iso4217");
                ROOT.setAttribute("xmlns:xbrldt", "http://xbrl.org/2005/xbrldt");
                ROOT.setAttribute("xmlns:xbrldi", "http://xbrl.org/2006/xbrldi");
//                ROOT.setAttribute("xmlns:" + xbrltaxonomy.getPrefix(), xbrltaxonomy.getURI());
				for(int i=0;i<xbrltaxonomy.PREFIXS.size();i++){
	                ROOT.setAttribute("xmlns:" + xbrltaxonomy.PREFIXS.elementAt(i), ""+xbrltaxonomy.URIS.elementAt(i));
				}
                LINK.setAttribute("xlink:href", xbrltaxonomy.getSchemaFileName());
            }
    }

    protected void clear()
    {
        objContextList = new XbrlContextList();
        TAXONOMYS = new Hashtable();
        ELEMENTS = new Hashtable();
    }


/**
*  取得 Root 節點.
*
* @return  回傳 RootElement
*
*/

    public Element getRootElement()
    {
        return ROOT;
    }

/**
*  取得 所有的元素(XbrlElementPool).
*
* @return  Hashtable,所有的元素(XbrlElementPool)
*
*/
    public Hashtable getElements()
    {
        return ELEMENTS;
    }

/**
*  指定 元素 的數字(decimals).
*
* @param   id XbrlElementPool 的 id .
* @param   i 數字.
*
*/

    public void setElementDecimal(String id, int i)
    {
        XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
        if(xbrlelementpool != null)
            xbrlelementpool.setDecimal(i);
    }


    protected XbrlTaxonomy getTaxonomy(String s)
    {
        XbrlTaxonomy xbrltaxonomy = (XbrlTaxonomy)TAXONOMYS.get(s);
        return xbrltaxonomy;
    }

    protected void putTaxonomy(String s, XbrlTaxonomy xbrltaxonomy)  {

        TAXONOMYS.put(s, xbrltaxonomy);
    }

/**
*  建立一份空的 XbrlDocument.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   URI 公司的URI.
* @param   CompanyCode 公司代碼.
* @param   RPT 公司種類，母公司或合併報表.
* @param   SDATE 報表起始日期，格式YYYYMMDD.
* @param   EDATE 報表結束日期 ，格式YYYYMMDD.
*
*/
    public void createEmpty(XbrlTaxonomy xbrltaxonomy, String URI, String CompanyCode, String RPT, String SDATE, String EDATE)
        throws Exception
    {
        initiate(xbrltaxonomy);
        XbrlContext xbrlcontext = objContextList.containsVlaue(CompanyCode, SDATE, EDATE);
        if(xbrlcontext == null){
                XbrlContext xbrlcontext1 = objContextList.createInstantContext(ROOT, URI, CompanyCode, EDATE);
                XbrlContext xbrlcontext2 = objContextList.createDurationContext(ROOT, URI, CompanyCode, SDATE, EDATE);
                DefaultCompanyCode = CompanyCode;
                DefaultEntityURI = URI;
		}
    }

/**
*  建立一份空的 XbrlDocument.
*
* @param   URI 公司的URI.
* @param   CompanyCode 公司代碼.
* @param   RPT 公司種類，母公司或合併報表.
* @param   SDATE 報表起始日期，格式YYYYMMDD.
* @param   EDATE 報表結束日期 ，格式YYYYMMDD.
*
*/
    public void createEmpty(String URI, String CompanyCode, String RPT, String SDATE, String EDATE)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		createEmpty(TAXONOMY,URI,CompanyCode,RPT,SDATE,EDATE);
    }


/**
*  建立一份完整的 XbrlDocument.
*
* @param   URI 公司的URI.
* @param   CompanyCode 公司代碼.
* @param   RPT 公司種類，母公司或合併報表.
* @param   SDATE 報表起始日期，格式YYYYMMDD.
* @param   EDATE 報表結束日期 ，格式YYYYMMDD.
*
*/
    public void create( String URI, String CompanyCode, String RPT, String SDATE, String EDATE)
        throws Exception
    {

		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		create(TAXONOMY,URI,CompanyCode,RPT,SDATE,EDATE);
    }

/**
*  建立一份完整的 XbrlDocument.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   URI 公司的URI.
* @param   CompanyCode 公司代碼.
* @param   RPT 公司種類，母公司或合併報表.
* @param   SDATE 報表起始日期，格式YYYYMMDD.
* @param   EDATE 報表結束日期 ，格式YYYYMMDD.
*
*/
    public void create(XbrlTaxonomy xbrltaxonomy, String URI, String CompanyCode, String RPT, String SDATE, String EDATE)
        throws Exception
    {

		initiate(xbrltaxonomy);
        XbrlContext xbrlcontext = objContextList.containsVlaue(CompanyCode, SDATE, EDATE);
        if(xbrlcontext == null){
                XbrlContext xbrlcontext1 = objContextList.createInstantContext(ROOT, URI, CompanyCode, EDATE);
                XbrlContext xbrlcontext2 = objContextList.createDurationContext(ROOT, URI, CompanyCode, SDATE, EDATE);
                DefaultCompanyCode = CompanyCode;
                DefaultEntityURI = URI;
                cloneSchemaTable(xbrltaxonomy, xbrlcontext1, xbrlcontext2);
        }
    }

    protected void cloneSchemaTable(XbrlTaxonomy xbrltaxonomy, XbrlContext xbrlcontext, XbrlContext xbrlcontext1)
    {
        Hashtable hashtable = xbrltaxonomy.getSchemaTable();
        XbrlElement XbrlElement;
        XbrlElementPool xbrlelementpool;
        for(Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements(); xbrlelementpool.add(XbrlElement))
        {
            String s = (String)enumeration.nextElement();
            XbrlSchemaElement s_element = (XbrlSchemaElement)hashtable.get(s);
            xbrlelementpool = new XbrlElementPool();
            xbrlelementpool.objSchema = s_element;
            xbrlelementpool.objTaxonomy = xbrltaxonomy;
            ELEMENTS.put(s, xbrlelementpool);
            XbrlElement = new XbrlElement();
            XbrlElement.DOCUMENT=this;
            XbrlElement.objSchema = s_element;
            if(s_element.getPeriodType().equals("duration"))
                XbrlElement.REFERENCE = xbrlcontext1;
            else
                XbrlElement.REFERENCE = xbrlcontext;
            XbrlElement.TAXONOMY = xbrltaxonomy;
            String s1 = xbrltaxonomy.getPrefix() + ":" + s_element.getName();
            Element element = ROOT.getOwnerDocument().createElement(s1);
            ROOT.appendChild(element);
            XbrlElement.setDOMData(element);
            XbrlElement.setDecimal(iDefaultDecimal);
            if(s_element.getPeriodType().equals("instant"))
                element.setAttribute("contextRef", xbrlcontext.getID());
            else
            if(s_element.getPeriodType().equals("duration"))
                element.setAttribute("contextRef", xbrlcontext1.getID());
            if(s_element.getType().equals("xbrli:monetaryItemType"))
            {
                XbrlElement.setUnit(DefaultCurrency);
                XbrlElement.setDecimal(iDefaultDecimal);
            } else
            if(s_element.getType().equals("xbrli:sharesItemType"))
            {
                XbrlElement.setUnit("Shares");
                XbrlElement.setDecimal(0);
            } else
            if(s_element.getType().equals("xbrli:decimalItemType"))
            {
                XbrlElement.setUnit("Pure");
                XbrlElement.setDecimal(iDefaultDecimal);
            }
            XbrlElement.setValue("");
        }

    }


/**
*  從外部XML載入文件.
*
* @param   inputstream inputstream.
*
*/

    public void load(InputStream inputstream)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		load(TAXONOMY,inputstream);
	}

/**
*  從外部XML載入文件.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   inputstream inputstream.
*
*/

    public void load(XbrlTaxonomy xbrltaxonomy, InputStream inputstream)
        throws Exception
    {
            Vector vector = new Vector();
            Document document = jcx.xbrl.xml.parse(inputstream);

            Element element5 = document.getDocumentElement();
            {
                initiate(xbrltaxonomy);
            }
            for(Node node = element5.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element = (Element)node;
                    String s = jcx.xbrl.xml.getElementName(element.getTagName(), ":");
                    if(s.equals("context")) {
                        if(objContextList.get(element.getAttribute("id")) == null) {
                            Element element2 = doc.createElement(element.getTagName());
							cloneNode1(element,element2,true);
                            ROOT.insertBefore(element2, LINK.getNextSibling());
//							element2=(Element)element.cloneNode(true);
//                            jcx.xbrl.xml.cloneElement(element, element2);
                            XbrlContext xbrlcontext = new XbrlContext();
                            xbrlcontext.loadContext(element2);
                            objContextList.put(xbrlcontext.getID(), xbrlcontext);
                        }
                    } else if(s.equals("unit")) {
                        if(XbrlUnit.getUnit(element.getAttribute("id")) == null)
                        {
                            Element element3 = doc.createElement(element.getTagName());
							cloneNode1(element,element3,true);
                            ROOT.insertBefore(element3, LINK.getNextSibling());
//							element3=(Element)element.cloneNode(true);
//                            jcx.xbrl.xml.cloneElement(element, element3);
                            Element element4 = jcx.xbrl.xml.getFirstElement(element3);
                            if(element4.getTagName().equals("measure"))
                            {
                                XbrlUnit xbrlunit = new XbrlUnit();
                                xbrlunit.loadUnit(element3);
                            } else
                            if(element4.getTagName().equals("divide"))
                            {
                                XbrlUnitDivide xbrlunitdivide = new XbrlUnitDivide();
                                xbrlunitdivide.loadUnit(element3);
                            }
                        }
                    } else if(element.getTagName().equals("link:schemaRef")) {
                    } else {

						String s2 = null;
						try{
							s2=xbrltaxonomy.getIDByName(s);
							XbrlSchemaElement s_element = xbrltaxonomy.getSchemaElement(s2);

					        String s1 = jcx.xbrl.xml.getNameSpace(element.getTagName());

							if(!s_element.getPrefix().equals(s1)){ //prefix 不同
								s2=element.getTagName().replace(':','_');
								s_element = xbrltaxonomy.getSchemaElement(s2);
							}

							if(s_element != null) {
			                    String sz = jcx.xbrl.xml.getElementName(s_element.getParentType(), ":");
//System.err.println("---tag="+element.getTagName()+" s="+s2);
//if(sss.indexOf("tw-gaap-notes_")!=-1)  System.out.println("----- s=" + jcx.xbrl.xml.print(s_element.getData()));
								if(sz.equals("tuple")){
//						           System.out.println("----- s=" + jcx.xbrl.xml.print(s_element.getData()));
//						           System.out.println("----- s=" + s);
//						           String s1[]=s_element.getItems();
//								   for(int z=0;z<s1.length;z++){
//							           System.out.println("        s1=" + s1[z]);
//								   }
									if(objContextList.get(s) == null) {
										Element element2 = doc.createElement(element.getTagName());
										cloneNode1(element,element2,true);
//										ROOT.insertBefore(element2, LINK.getNextSibling());
			//							element2=(Element)element.cloneNode(true);
			//                            jcx.xbrl.xml.cloneElement(element, element2);
										XbrlContext xbrlcontext = new XbrlContext();
										xbrlcontext.loadContext(element2);
										objContextList.put(s_element.getID(), xbrlcontext);
//System.err.println("--- put "+s_element.getID());
									}

								}
//					           System.out.println("----- s=" + sz);
							}
						} catch(Exception e){
						}
                      vector.add(element);
                    }
                }

            for(int i = 0; i < vector.size(); i++) {
                Element element1 = (Element)vector.get(i);
//              System.out.println("----- " + element1.getTagName() +" ! "+element1.getAttribute("contextRef"));
				String context1=element1.getAttribute("contextRef");
				if(context1==null) context1="";
				if(context1.equals("")) context1=element1.getTagName().replace(':','_');
//System.err.println("--- "+objContextList+" "+context1);
				XbrlContext xbrlcontext1 = objContextList.get(context1);
//if(element1.getTagName().indexOf("Inventories")!=-1) System.err.println("--- "+context1+"  -> "+jcx.xbrl.xml.print(element1));
//if(element1.getTagName().indexOf("Inventories")!=-1) System.err.println("--- "+context1+"  -> "+xbrlcontext1);
//System.err.println("--- "+xbrlcontext1.getID()+" "+context1);
                if(xbrlcontext1 != null) {
                        loadElement(xbrltaxonomy,element1, xbrlcontext1);
                }else{
//                    System.out.println("Context1 " + context1 + " not found!");
//                    System.out.println("Context1 " + objContextList + " not found!");

                    System.out.println("Context " + element1 + " not found!");
					CONTEXT_NOT_IN_TAXONOMY.addElement(element1);
				}
            }

    }

/**
*  取得所有不存在的Context (有Element 但取出的 ContextID 無對照 String context_id=element1.getAttribute("contextRef");) (從外部XML載入文件時).
*
*
*/
    public Element[] getUnsupportedContext(){
		return (Element[])CONTEXT_NOT_IN_TAXONOMY.toArray(new Element[0]);
		
	}
	Vector CONTEXT_NOT_IN_TAXONOMY=new Vector();


/**
*  從外部XML載入文件.
*
* @param   s xml 字串.
*
*/

    public void load(String s)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		load(TAXONOMY,s);
	}


/**
*  從外部XML載入文件.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   s xml 字串.
*
*/

    public void load(XbrlTaxonomy xbrltaxonomy, String s)
        throws Exception
    {
		load(xbrltaxonomy,new ByteArrayInputStream(s.getBytes()));
    }


/**
*  取得所有未在 taxonomy 定義的 element (從外部XML載入文件時).
*
*
*/
    public Element[] getUnsupportedElement(){
		return (Element[])ELEMENT_NOT_IN_TAXONOMY.toArray(new Element[0]);
		
	}
	Vector ELEMENT_NOT_IN_TAXONOMY=new Vector();
	
    protected XbrlElement loadElement(XbrlTaxonomy xbrltaxonomy,Element element, XbrlContext xbrlcontext)
        throws Exception
    {
  
//if(element.getTagName().indexOf("Inventories")!=-1)   System.err.println(xbrltaxonomy);

		XbrlElement XbrlElement = null;
        String s1 = jcx.xbrl.xml.getNameSpace(element.getTagName());
//        XbrlTaxonomy xbrltaxonomy = getTaxonomy(s1);
        if(xbrltaxonomy==null) xbrltaxonomy = getTaxonomy(s1);




//System.err.println(jcx.xbrl.xml.print(element));
        if(xbrltaxonomy != null) {
            String s = jcx.xbrl.xml.getElementName(element.getTagName(), ":");

//if(element.getTagName().indexOf("Inventories")!=-1) System.err.println("--- "+xbrltaxonomy.getPrefix()+" "+s1+" "+element.getTagName());
 
            String s2 = null;
			try{
				s2=xbrltaxonomy.getIDByName(s);
//				s2=element.getTagName().replace(':','_');
//if(element.getTagName().indexOf("Inventories")!=-1) System.err.println("--- "+s2+" "+s1+" "+element.getTagName());
//System.err.println("--- "+s2+" "+s1+" "+xbrltaxonomy.getIDByName(s));
			} catch(Exception e){

//e.printStackTrace();
//System.err.println("--- "+e);
				ELEMENT_NOT_IN_TAXONOMY.addElement(element);

				XbrlElement = new XbrlElement();
                XbrlElement.DOCUMENT=this;
                Element element1 = doc.createElement(element.getTagName());
				cloneNode1(element,element1,true);
                ROOT.appendChild(element1);
//				element1=(Element)element.cloneNode(true);
                XbrlElement.setDOMData(element1);
                XbrlElement.REFERENCE = xbrlcontext;
                XbrlElement.objSchema = null;
                XbrlElement.TAXONOMY = xbrltaxonomy;
                XbrlElement.setDecimal(iDefaultDecimal);
				return XbrlElement;
			}

            if(s2 == null){
                System.out.println("Element(" + element.getTagName() + ") is null!");
                throw new Exception("Element(" + element.getTagName() + ") is null!");
            }

            XbrlSchemaElement s_element = xbrltaxonomy.getSchemaElement(s2);
			if(!s_element.getPrefix().equals(s1)){ //prefix 不同
				s2=element.getTagName().replace(':','_');
				s_element = xbrltaxonomy.getSchemaElement(s2);
			}
            if(s_element != null) {

				
//if(element.getTagName().indexOf("Inventories")!=-1) System.err.println("--- "+s2+" "+s1+" "+s_element.getPrefix());

				XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(s2);
//if(element.getTagName().indexOf("Inventories")!=-1) System.err.println("--- "+xbrlcontext.getID()+" "+s1+" "+s2+"  -> "+xbrlelementpool);
                if(xbrlelementpool != null) {
                    XbrlElement = xbrlelementpool.get(xbrlcontext);
                } else {
                    xbrlelementpool = new XbrlElementPool();
                    xbrlelementpool.objSchema = s_element;
                    xbrlelementpool.objTaxonomy = xbrltaxonomy;
                    ELEMENTS.put(s2, xbrlelementpool);
//if(element.getTagName().indexOf("Inventories")!=-1) System.err.println("--- put "+s2+"  "+ xbrlelementpool);
                }
                XbrlElement = new XbrlElement();
                XbrlElement.DOCUMENT=this;


                Element element1 = doc.createElement(xbrltaxonomy.getNameByID(s2));
//                element1.setAttribute("contextRef", xbrlcontext.getID());
//				element1.setNodeValue(element.getNodeValue());
				cloneNode1(element,element1,true);
                ROOT.appendChild(element1);

//				element1=(Element)element.cloneNode(true);

//              jcx.xbrl.xml.cloneElement(element, element1);
                XbrlElement.setDOMData(element1);
                XbrlElement.REFERENCE = xbrlcontext;
                XbrlElement.objSchema = s_element;
                XbrlElement.TAXONOMY = xbrltaxonomy;
				if(s_element.isTuple()){
					XbrlElement.refresh(0);
//System.err.println("--- "+xbrlcontext);
				} else {
	                XbrlElement.setDecimal(iDefaultDecimal);
				}
                xbrlelementpool.add(XbrlElement);

//				System.err.println("--- type="+s_element.getType());

                if(s_element.getType().equalsIgnoreCase("xbrli:monetaryItemType"))
                    XbrlElement.setDecimal(iDefaultDecimal);
                else if(s_element.getType().equalsIgnoreCase("xbrli:sharesItemType"))
                    XbrlElement.setDecimal(0);
                else if(s_element.getType().equalsIgnoreCase("xbrli:decimalItemType")){
                    XbrlElement.setDecimal(2);
				}
            } else {
                throw new Exception("Element (" + element.getTagName() + ") not found!");
            }
        } else {
            System.out.println("Name space (" + s1 + ") not found!");
        }
        return XbrlElement;
    }

	protected static void cloneNode1(Element a,Element b,boolean recursive){
		NamedNodeMap n=a.getAttributes();
		if(n!=null){
			for(int i=0;i<n.getLength();i++){
				Node n1=n.item(i);
//				System.err.println("--n1="+n1.getNodeName()+" -> "+n1.getNodeValue());
				b.setAttribute(n1.getNodeName(), n1.getNodeValue());
			}
		}
		String v1=a.getNodeValue();
		if(v1!=null) b.setNodeValue(v1);
//System.err.println("--- v1="+v1);
		if(a.hasChildNodes()){
			NodeList list1=a.getChildNodes();
			for(int i=0;i<list1.getLength();i++){
				Node n1=list1.item(i);
				if(n1.getNodeType()==n1.ELEMENT_NODE){
					if(recursive){
						Element mm=(Element)n1;
						Element new_mm=b.getOwnerDocument().createElement(mm.getTagName());
						b.appendChild(new_mm);
						cloneNode1(mm,new_mm,recursive);
					}
				} else if(n1.getNodeType()==n1.TEXT_NODE){
					String v2=n1.getNodeValue();
					Node nn=b.getOwnerDocument().createTextNode(v2);
					b.appendChild(nn);
//System.err.println("--- getNodeType="+v2);
//					b.setNodeValue(v2);
				} else if(n1.getNodeType()==n1.CDATA_SECTION_NODE){
					String v2=n1.getNodeValue();
					Node nn=b.getOwnerDocument().createCDATASection(v2);
					b.appendChild(nn);
				} else if(n1.getNodeType()==n1.COMMENT_NODE){
					String v2=n1.getNodeValue();
					Node nn=b.getOwnerDocument().createComment(v2);
					b.appendChild(nn);
				} else if(n1.getNodeType()==n1.ATTRIBUTE_NODE){
					String v2=n1.getNodeName();
					Node nn=b.getOwnerDocument().createAttribute(v2);
					b.appendChild(nn);
				} else if(n1.getNodeType()==n1.ENTITY_NODE){
					System.err.println("--node type(ENTITY_NODE) not support now!");
				} else if(n1.getNodeType()==n1.NOTATION_NODE){
					System.err.println("--node type(PROCESSING_INSTRUCTION_NODE) not support now!");
				} else if(n1.getNodeType()==n1.NOTATION_NODE){
					System.err.println("--node type(PROCESSING_INSTRUCTION_NODE) not support now!");
				} else if(n1.getNodeType()==n1.ENTITY_REFERENCE_NODE){
					System.err.println("--node type(ENTITY_REFERENCE_NODE) not support now!");
				} else {
					System.err.println("--node type("+n1.getNodeType()+") unknown");
//					try{
//						Node nn=(Node)n1.clone();
//						b.appendChild(nn);
//					}catch (CloneNotSupportedException e) {}
				}
			}
		}
	}

    protected boolean checkValueType(XbrlSchemaElement s_element, String s)
        throws Exception
    {
		return checkValueType(TAXONOMY,s_element, s);
	}

    protected boolean checkValueType(XbrlTaxonomy a,XbrlSchemaElement s_element, String s)
        throws Exception
    {
        boolean flag = true;
        if(s_element.isAbstract()){
//			System.err.println("    "+a.getTerseLabelByID(s_element.getID())+"("+a.getLabelByID(s_element.getID(),"zh")+")");
			String aa="";
			try{
				aa=a.getTerseLabelByID(s_element.getID());
			} catch(Exception e){}
            throw new Exception("Element not allow abstract " + s_element.getID()+"("+aa+")");
		}

        String s1 = s_element.getType();
        if(s1.equalsIgnoreCase("xbrli:monetaryItemType"))  {
			try{
	            Float.parseFloat(s);
			} catch(Exception e){
                throw new Exception("Element " + s_element.getID() + " should be number " + s);
			}
        } else if(s1.equalsIgnoreCase("xbrli:sharesItemType"))    {
			try{
	            Integer.parseInt(s);
			} catch(Exception e){
                throw new Exception("Element " + s_element.getID() + " should be Integer " + s);
			}
        } else if(s1.equals("xbrli:decimalItemType")){
			try{
	            Float.parseFloat(s);
			} catch(Exception e){
                throw new Exception("Element " + s_element.getID() + " should be number " + s);
			}
		}
        return flag;
    }


/**
*  建立一個新的資料項目.
*
* @param   account_id 會計 ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   sdate 起始日期.
* @param   edate 結束日期.
* @return  XbrlElement
*
*/
    public XbrlElement createElement( String account_id, String value1, String unit1, String sdate, String edate)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		return createElement(TAXONOMY,account_id,value1,unit1, sdate, edate);
	}



/**
*  建立一個新的資料項目.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   account_id 會計 ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   sdate 起始日期.
* @param   edate 結束日期.
* @return  XbrlElement
*
*/
    public XbrlElement createElement(XbrlTaxonomy xbrltaxonomy, String account_id, String value1, String unit1, String sdate, String edate)
        throws Exception
    {
        XbrlElement XbrlElement = null;
        {
			XbrlSchemaElement s_element=null;
			try{
	            s_element = xbrltaxonomy.getSchemaElement(account_id);
			} catch(Exception ee){
				s_element = xbrltaxonomy.getSchemaElement(xbrltaxonomy.getIDByTerseLabel(account_id));
			}

//        System.err.println("-- s_element="+s_element.isTuple());
			if(s_element.isTuple()){
                XbrlElement = createElement(xbrltaxonomy, account_id, value1, unit1, null);
//				throw new Exception(account_id+" is tuple element ,please use createElement(XbrlTaxonomy,ID,String[] subID,String[] context,value )");
			} else {
                XbrlContext xbrlcontext = objContextList.getSchemaDefinedContext(s_element, sdate, edate);
                if(xbrlcontext == null){
                    if(s_element.getPeriodType().equals("duration")){
                        xbrlcontext = objContextList.createDurationContext(ROOT, DefaultEntityURI, DefaultCompanyCode, sdate, edate);
                    } else if(s_element.getPeriodType().equals("instant")){
                        xbrlcontext = objContextList.createInstantContext(ROOT, DefaultEntityURI, DefaultCompanyCode, edate);
					}
				}
//System.err.println("-- xbrlcontext="+xbrlcontext);
                XbrlElement = createElement(xbrltaxonomy, account_id, value1, unit1, xbrlcontext.getID());
//System.err.println("------element="+XbrlElement.getPrefix());
            }
        }
        return XbrlElement;
    }

/**
*  建立一個新的資料項目.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   account_id 會計 ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   context_id context id.
* @return  XbrlIElement
*
*/

    public XbrlElement createElement(XbrlTaxonomy xbrltaxonomy, String account_id, String value1, String unit1, String context_id)
        throws Exception
    {
        Object obj = null;
        XbrlElement XbrlElement = null;
        {
			XbrlSchemaElement s_element=null;
			try{
	            s_element = xbrltaxonomy.getSchemaElement(account_id);
			} catch(Exception ee){
				s_element = xbrltaxonomy.getSchemaElement(xbrltaxonomy.getIDByTerseLabel(account_id));
			}

			if(s_element.isTuple()){
//                XbrlContext xbrlcontext = objContextList.get(context_id);
                XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(account_id);
                if(xbrlelementpool == null)
                {
                    xbrlelementpool = new XbrlElementPool();
                    xbrlelementpool.objSchema = s_element;
                    xbrlelementpool.objTaxonomy = xbrltaxonomy;
                    ELEMENTS.put(account_id, xbrlelementpool);
                }
//                String s4 = xbrltaxonomy.getPrefix() + ":" + s_element.getName();
                String s4 = s_element.getPrefix() + ":" + s_element.getName();
//System.err.println("--s4="+s4+"  s="+s_element.getPrefix());
                Element element = ROOT.getOwnerDocument().createElement(s4);
//                element.setAttribute("contextRef", xbrlcontext.getID());
//				element.setNodeValue("123");
//System.err.println("--- element="+jcx.xbrl.xml.print(element));
                ROOT.appendChild(element);

                ROOT.appendChild(ROOT.getOwnerDocument().createTextNode("\r\n"));
                XbrlElement = new XbrlElement();
                XbrlElement.DOCUMENT=this;
                XbrlElement.setDOMData(element);
				XbrlContext xbrlcontext = new XbrlContext();
//				xbrlcontext.loadContext(element2);
                XbrlElement.REFERENCE = xbrlcontext;
                XbrlElement.objSchema = s_element;
                XbrlElement.TAXONOMY = xbrltaxonomy;
                xbrlelementpool.add(XbrlElement);
            } else {
                checkValueType(xbrltaxonomy,s_element, value1);
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

                XbrlContext xbrlcontext = objContextList.get(context_id);
                XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(account_id);
                if(xbrlelementpool == null)
                {
                    xbrlelementpool = new XbrlElementPool();
                    xbrlelementpool.objSchema = s_element;
                    xbrlelementpool.objTaxonomy = xbrltaxonomy;
                    ELEMENTS.put(account_id, xbrlelementpool);
                }
//                String s4 = xbrltaxonomy.getPrefix() + ":" + s_element.getName();
                String s4 = s_element.getPrefix() + ":" + s_element.getName();
//System.err.println("--s4="+s4+"  s="+s_element.getPrefix());
                Element element = ROOT.getOwnerDocument().createElement(s4);
                element.setAttribute("contextRef", xbrlcontext.getID());
                ROOT.appendChild(element);
                XbrlElement = new XbrlElement();
                XbrlElement.DOCUMENT=this;
                XbrlElement.setDOMData(element);
                XbrlElement.REFERENCE = xbrlcontext;
				
                XbrlElement.objSchema = s_element;
                XbrlElement.TAXONOMY = xbrltaxonomy;
				XbrlElement.setDecimal(iDefaultDecimal);
                if(s_element.getType().compareTo("xbrli:monetaryItemType") == 0) {
       				if(xbrlunit!=null){
						XbrlElement.setUnit(xbrlunit.getID());
					} else {
						XbrlElement.setUnit("TWD");
					}
                    XbrlElement.setDecimal(iDefaultDecimal);
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
        }
        return XbrlElement;
    }
/**
*  建立一個新的資料項目.
*
* @param   account_id 會計 ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   context_id context id.
* @return  XbrlElement
*
*/
    public XbrlElement createElement( String account_id, String value1, String unit1, String context_id)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		return createElement(TAXONOMY,account_id,value1,unit1, context_id);
	}

/**
*  依element id在文件中更新一個資料項目的值.
*
* @param   id element id .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   sdate 起始日期.
* @param   edate 結束日期.
* @return  XbrlInstanceElement
*
*/

    public XbrlElement setElement(String id, String value1, String unit1, String sdate, String edate)
        throws Exception
    {
        XbrlElement XbrlElement = null;
        XbrlElementPool xbrlelementpool = null;
        xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
        if(xbrlelementpool != null)
        {
            XbrlContext xbrlcontext = objContextList.getSchemaDefinedContext(xbrlelementpool.objSchema, sdate, edate);
            setElement(id, value1, value1, xbrlcontext.getID());
        } else {
            throw new Exception("Element "+id + " is not existed.");
        }
        return XbrlElement;
    }
/**
*  依element id在文件中更新一個資料項目的值.
*
* @param   id element id .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   context_id context id.
* @return  XbrlInstanceElement
*
*/

    public XbrlElement setElement(String id, String value1, String unit1, String context_id)
        throws Exception
    {
        XbrlElementPool xbrlelementpool = null;
        XbrlElement XbrlElement = null;
        xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
        if(xbrlelementpool != null){
                XbrlContext xbrlcontext = objContextList.get(context_id);
                XbrlElement = xbrlelementpool.get(xbrlcontext);
                if(XbrlElement != null) {
                        checkValueType(XbrlElement.objSchema, value1);
                        XbrlUnit xbrlunit = XbrlUnit.getUnit(unit1);
                        XbrlElement.setValue(value1);
                        XbrlElement.setUnit(unit1);
                } else {
                    throw new Exception("Context is not found: " + xbrlcontext.getID());
				}
        } else {
            throw new Exception("Element "+id + " is not existed.");
		}
        return XbrlElement;
    }



/**
*  依element id在文件中取得一個資料項目的Element.
*
* @param   id element id .
* @param   sdate 起始日期.
* @param   edate 結束日期.
* @return  XbrlElement
*
*/
    public XbrlElement getElementByID(String id, String sdate, String edate)throws Exception  {
        XbrlElement s3 = null;
        XbrlElementPool xbrlelementpool = null;
        xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
        if(xbrlelementpool != null) {
            XbrlContext xbrlcontext = objContextList.getSchemaDefinedContext(xbrlelementpool.objSchema, sdate, edate);
//System.err.println("---YYYYYYY xbrlcontext="+xbrlcontext+"  id="+xbrlcontext.getID());
            s3 = getElementByID(id, xbrlcontext.getID());
        } else {
            throw new Exception("Element "+id + " is not existed.");
        }
        return s3;
    }

/**
*  依element id在文件中取得一個資料項目的Element.
*
* @param   id element id .
* @param   context_id context id.
* @return  XbrlElement
*
*/
    public XbrlElement getElementByID(String id, String context_id) throws Exception {
        XbrlElement s2 = null;
        XbrlElementPool xbrlelementpool = null;
        Object obj = null;
        xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
        if(xbrlelementpool != null)
        {
            XbrlContext xbrlcontext = objContextList.get(context_id);
            XbrlElement XbrlElement = xbrlelementpool.get(xbrlcontext);
//System.err.println("---ZZZZZZZZZZ XbrlElemen="+XbrlElement+"  vv="+XbrlElement.getValue());
            if(XbrlElement != null)
                s2 = XbrlElement;
        } else  {
            throw new Exception("Element "+id + " is not existed.");
        }
        return s2;
    }



/**
*  依element id在文件中取得一個資料項目的值.
*
* @param   id element id .
* @param   sdate 起始日期.
* @param   edate 結束日期.
* @return  String
*
*/
    public String getElementValueByID(String id, String sdate, String edate)throws Exception  {
        String s3 = null;
        XbrlElementPool xbrlelementpool = null;
        xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
//System.err.println("--- xbrlelementpool="+xbrlelementpool);
        if(xbrlelementpool != null) {
            XbrlContext xbrlcontext = objContextList.getSchemaDefinedContext(xbrlelementpool.objSchema, sdate, edate);
//System.err.println("--- xbrlelementpool="+xbrlcontext.getID());
            s3 = getElementValueByID(id, xbrlcontext.getID());
        } else {
            throw new Exception("Element "+id + " is not existed.");
        }
        return s3;
    }

/**
*  依element id在文件中取得一個資料項目的值.
*
* @param   id element id .
* @param   context_id context id.
* @return  String
*
*/
    public String getElementValueByID(String id, String context_id) throws Exception {
        String s2 = null;
        XbrlElementPool xbrlelementpool = null;
        Object obj = null;
        xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);

//System.err.println("--- pool="+ELEMENTS);

		if(xbrlelementpool != null)
        {
            XbrlContext xbrlcontext = objContextList.get(context_id);
//System.err.println("--- xbrlcontext="+xbrlcontext.getID());
  
            XbrlElement XbrlElement = xbrlelementpool.get(xbrlcontext);
//System.err.println("--- XbrlElement="+XbrlElement+" data="+XbrlElement.getData()+" vv="+XbrlElement.getValue());
            if(XbrlElement != null)
                s2 = XbrlElement.getValue();
        } else  {
            throw new Exception("Element "+id + " is not existed.");
        }
        return s2;
    }


/*
    public XbrlPresentationTree getPreTree(XbrlTaxonomy xbrltaxonomy, String s, String s1, String s2)
        throws XbrlRoleNotSupportException
    {
        XbrlPresentationTree XbrlPresentationTree = null;
        XbrlContext xbrlcontext = null;
        XbrlContext xbrlcontext1 = null;
        xbrlcontext = objContextList.containsInstantContext(s2);
        xbrlcontext1 = objContextList.containsDurationContext(s1, s2);

        jcx.xbrl.taxonomy.XbrlPresentationTree xbrlpresentationTree[] = xbrltaxonomy.getPresentationTree(s);
        if(xbrlpresentationTree != null)
        {
            XbrlPresentationTree = new XbrlPresentationTree(xbrltaxonomy);
//            XbrlPresentationTree.cloneTree(xbrlpresentationlink.getSchemaTree(), ELEMENTS, xbrlcontext, xbrlcontext1);
        }
        return XbrlPresentationTree;
    }
*/
    protected Vector getCalTree(XbrlTaxonomy xbrltaxonomy, String ROLE, String s1, String s2)
        throws Exception
    {
        Vector vector1 = null;
        Object obj = null;
        XbrlContext xbrlcontext = null;
        XbrlContext xbrlcontext1 = null;
        vector1 = new Vector();
        xbrlcontext = objContextList.containsInstantContext(s2);
//System.err.println("  s1="+s1+" "+s2);
        xbrlcontext1 = objContextList.containsDurationContext(s1, s2);

//        Vector vector = xbrltaxonomy.getCalculationList();
//		for(int i = 0; i < vector.size(); i++){
//			String id=(String)vector.elementAt(i);
            jcx.xbrl.taxonomy.XbrlCalculationTree trees[]=xbrltaxonomy.getCalculationTree(ROLE);
			for(int z = 0; z < trees.length; z++){
                XbrlCalculationTree xbrlcalculationtree = new XbrlCalculationTree(xbrltaxonomy);
                xbrlcalculationtree.cloneTree(trees[z], ELEMENTS, xbrlcontext, xbrlcontext1);
                vector1.add(xbrlcalculationtree);
			}
//		}
/*

		XbrlCalculationLink xbrlcalculationlink = xbrltaxonomy.xbrlcal.getCalculationLink(s);
        if(xbrlcalculationlink != null)
        {
            Vector vector = xbrlcalculationlink.getSchemaTree();
            for(int i = 0; i < vector.size(); i++)
            {
                XbrlCalculationTree xbrlcalculationtree = new XbrlCalculationTree(xbrltaxonomy);
                xbrlcalculationtree.cloneTree((jcx.xbrl.taxonomy.XbrlCalculationTree)vector.get(i), ELEMENTS, xbrlcontext, xbrlcontext1);
                vector1.add(xbrlcalculationtree);
            }

        }
*/
        return vector1;
    }


/**
*  依照definition定義的關係，建立所有相關的context.
*
* @param   URI 公司的URI.
* @param   CompanyCode 公司代碼.
* @param   SDATE 報表起始日期，格式YYYYMMDD.
* @param   EDATE 報表結束日期 ，格式YYYYMMDD.
* @param   ROLE definitionLink的ROLE.
*
*/
    public void createDimensionContext(String URI, String CompanyCode, String SDATE, String EDATE, String ROLE)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		createDimensionContext(TAXONOMY,URI, CompanyCode,  SDATE,  EDATE,  ROLE);
	}

/**
*  依照definition定義的關係，建立所有相關的context.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   URI 公司的URI.
* @param   CompanyCode 公司代碼.
* @param   SDATE 報表起始日期，格式YYYYMMDD.
* @param   EDATE 報表結束日期 ，格式YYYYMMDD.
* @param   ROLE definitionLink的ROLE.
*
*/
    public void createDimensionContext(XbrlTaxonomy xbrltaxonomy, String URI, String CompanyCode, String SDATE, String EDATE, String ROLE)
        throws Exception
    {
        try {
//            XbrlDefinitionTree[] a1 = xbrltaxonomy.getDefinitionTrees();
//			System.err.println("tree length="+a1.length+" "+a1[0].getName());

            XbrlDefinitionTree trees[] = xbrltaxonomy.getDefinitionTree(ROLE);

            XbrlDefinitionTree xbrldefinitiontree = trees[0];

            objContextList.createDimensionContext(xbrltaxonomy, getRootElement(), URI, CompanyCode, SDATE, EDATE, xbrldefinitiontree);
            if(getTaxonomy(xbrltaxonomy.getPrefix()) == null) {
                putTaxonomy(xbrltaxonomy.getPrefix(), xbrltaxonomy);
                ROOT.setAttribute("xmlns:" + xbrltaxonomy.getPrefix(), xbrltaxonomy.getURI());
                LINK.setAttribute("xlink:href", xbrltaxonomy.getSchemaFileName());
            }
        }
        catch(XbrlRoleNotSupportException xbrlrolenotsupportexception)
        {
            throw xbrlrolenotsupportexception;
        }
    }



/**
*  依照definition定義的關係，取得 XbrlTable.
*
* @param   ROLE definitionLink的ROLE.
* @param   SDATE 起始日期，格式YYYYMMDD.
* @param   EDATE 結束日期，格式YYYYMMDD.
* @param   dim table使用的Dimension名稱.
* @param   XID X軸的ID .
* @param   YID Y軸的ID.
* @param   EXPAND Expand 如果有子節點是否展開.
* @param   element Target element.
* @return  XbrlTable.
*
*/
    public XbrlTable getDimensionTable( String ROLE, String SDATE, String EDATE, String dim, String XID, String YID,  boolean EXPAND, Element element)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		return getDimensionTable( TAXONOMY, ROLE,  SDATE,  EDATE,  dim,  XID,  YID,   EXPAND,  element);
	}
/**
*  依照definition定義的關係，取得 XbrlTable.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   ROLE definitionLink的ROLE.
* @param   SDATE 起始日期，格式YYYYMMDD.
* @param   EDATE 結束日期，格式YYYYMMDD.
* @param   dim table使用的Dimension名稱.
* @param   XID X軸的ID .
* @param   YID Y軸的ID.
* @param   EXPAND Expand 如果有子節點是否展開.
* @param   element Target element.
* @return  XbrlTable.
*
*/
    public XbrlTable getDimensionTable(XbrlTaxonomy xbrltaxonomy, String ROLE, String SDATE, String EDATE, String dim, String XID, String YID, 
            boolean EXPAND, Element element)
        throws Exception
    {
        XbrlTable table1 = null;
        jcx.xbrl.taxonomy.XbrlPresentationTree xbrlpresentationtrees[] = xbrltaxonomy.getPresentationTree(ROLE);
        if(xbrlpresentationtrees != null)  {
//            jcx.xbrl.taxonomy.XbrlPresentationTree xbrlpresentationtree = xbrlpresentationlink.getSchemaTree();
            jcx.xbrl.taxonomy.XbrlPresentationTree xbrlpresentationtree = xbrlpresentationtrees[0];
            Document document = jcx.xbrl.xml.getNewDocument();
            Element element1 = document.createElement("tree");
            document.appendChild(element1);
            xbrlpresentationtree.exportToXML(element1);
            table1 = new XbrlTable(xbrltaxonomy, element);
            table1.createTable(element1, ELEMENTS, SDATE, EDATE, objContextList, dim, XID, YID, EXPAND);
        }
        return table1;
    }



/**
*  依照calculation定義的關係，驗證Table的值否正確.
*
* @param   ROLE definitionLink的ROLE.
* @param   sdate 起始日期，格式YYYYMMDD.
* @param   edate 結束日期，格式YYYYMMDD.
* @param   dname table使用的Dimension名稱.
* @param   XID X軸的ID .
* @param   YID Y軸的ID.
* @param   element Target element.
* @return  boolean 是否完全正確.
*
*/

    public boolean validateDimension( String ROLE, String sdate, String edate, String dname, String XID, String YID, Element element)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		return validateDimension(TAXONOMY,  ROLE,  sdate,  edate,  dname,  XID,  YID,  element);
	}
/**
*  依照calculation定義的關係，驗證Table的值否正確.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   ROLE definitionLink的ROLE.
* @param   sdate 起始日期，格式YYYYMMDD.
* @param   edate 結束日期，格式YYYYMMDD.
* @param   dname table使用的Dimension名稱.
* @param   XID X軸的ID .
* @param   YID Y軸的ID.
* @param   element Target element.
* @return  boolean 是否完全正確.
*
*/

    public boolean validateDimension(XbrlTaxonomy xbrltaxonomy, String ROLE, String sdate, String edate, String dname, String XID, String YID, 
            Element element)
        throws Exception
    {
        int j = 0;
        Object obj = null;
        XbrlDefinitionTree xbrldefinitiontrees[] = xbrltaxonomy.getDefinitionTree(ROLE);
        XbrlDefinitionTree xbrldefinitiontree = xbrldefinitiontrees[0];

        jcx.xbrl.taxonomy.XbrlCalculationTree xbrlcalculationTrees[] = xbrltaxonomy.getCalculationTree(ROLE);
        if(xbrlcalculationTrees != null && xbrldefinitiontree != null) {
            Document document1 = jcx.xbrl.xml.getNewDocument();
            Element element2 = document1.createElement("definition");
            document1.appendChild(element2);

            xbrldefinitiontree.exportToXML(element2);
//            Vector vector = xbrlcalculationlink.getSchemaTree();

            for(int i = 0; i < xbrlcalculationTrees.length; i++) {
                jcx.xbrl.taxonomy.XbrlCalculationTree rule = xbrlcalculationTrees[i];

//				System.err.println(" Validate "+s+"("+i+") "+rule.getRootNode());

				Document document = jcx.xbrl.xml.getNewDocument();
                Element element1 = document.createElement("calculation");
                document.appendChild(element1);
                rule.exportToXML(element1);
                XbrlCalTable xcaltable = new XbrlCalTable(xbrltaxonomy, element);
                xcaltable.createTable(element2, element1, ELEMENTS, sdate, edate, objContextList, dname, XID, YID);
                j += xcaltable.validate(xbrltaxonomy, element);
            }

        }
        if(j == 0){
            element.setAttribute("errorCount", "0");
			return true;
        } else {
            element.setAttribute("errorCount", (new String()).valueOf(j));
			return false;
		}
    }



/**
*  建立一個新的資料項目.
*
* @param   dname table使用的Dimension名稱.
* @param   YID Y軸的ID.
* @param   XID X軸的ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   sdate 起始日期，格式YYYYMMDD.
* @param   edate 結束日期，格式YYYYMMDD.
* @return  XbrlIElement
*
*/

    public XbrlElement createElement( String dname, String YID, String XID, String value1, String unit1, String sdate, String edate)
        throws Exception
    {
		return createElement(TAXONOMY,  dname,  YID,  XID,  value1,  unit1,  sdate,  edate);
	}
/**
*  建立一個新的資料項目.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   dname table使用的Dimension名稱.
* @param   YID Y軸的ID.
* @param   XID X軸的ID .
* @param   value1 數字或金額.
* @param   unit1 單位.
* @param   sdate 起始日期，格式YYYYMMDD.
* @param   edate 結束日期，格式YYYYMMDD.
* @return  XbrlIElement
*
*/

    public XbrlElement createElement(XbrlTaxonomy xbrltaxonomy, String dname, String YID, String XID, String value1, String unit1, String sdate, String edate)
        throws Exception
    {
        XbrlElement XbrlElement = null;
        XbrlContext xbrlcontext = null;
        XbrlSchemaElement s_element = xbrltaxonomy.getSchemaElement(YID);
//System.err.println("-- YID="+YID+"  e="+s_element);
        if(s_element != null){
//System.err.println("---------- "+s_element.getPeriodType()+" s1="+xbrltaxonomy.getNameByID(XID)+" s2="+xbrltaxonomy.getNameByID(YID));
			String dname1="";
			if(dname!=null){
				if(!dname.equals("")){
					dname1=xbrltaxonomy.getNameByID(dname);
				}
			}

			if(s_element.getPeriodType().equals("instant")){

                xbrlcontext = objContextList.containsDimensionMemberInstantContext(dname1, xbrltaxonomy.getNameByID(XID), edate);
            } else {
                xbrlcontext = objContextList.containsDimensionMemberDurationContext(dname1, xbrltaxonomy.getNameByID(XID), sdate, edate);
			}
		}

		if(unit1==null || "".equals(unit1)){
			if(s_element.getType().compareTo("xbrli:monetaryItemType") == 0) {
				unit1="TWD";
			} else if(s_element.getType().compareTo("xbrli:sharesItemType") == 0)  {
				unit1="Shares";
			} else if(s_element.getType().compareTo("xbrli:decimalItemType") == 0)  {
				unit1="TWD,2";
			}		
		}

        if(xbrlcontext != null){
//System.err.println("---------- "+s_element.getPeriodType()+" YID="+xbrltaxonomy.getTerseLabelByID(YID)+"("+xbrltaxonomy.getLabelByID(YID,"zh")+") "+xbrltaxonomy.getTerseLabelByID(XID)+"("+xbrltaxonomy.getLabelByID(XID,"zh")+") ");
//                System.out.println("Context: " + xbrlcontext.getID());
                XbrlElement = createElement(xbrltaxonomy, YID, value1, unit1, xbrlcontext.getID());
        } else {
            throw new Exception("Dimension-X member " + XID + " not found!");
		}
        return XbrlElement;
    }



/**
*  依element id在文件中取得一個資料項目的值.
*
* @param   dname Dimension的名稱 .
* @param   XID X軸的ID .
* @param   YID Y軸的ID.
* @param   SDATE 起始日期.
* @param   EDATE 結束日期.
* @return  String
*
*/
    public String getElementValueByID(String dname, String XID, String YID, String SDATE, String EDATE)
        throws Exception
    {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		return getElementValueByID(TAXONOMY,  dname,  XID,  YID,  SDATE,  EDATE);
	}
/**
*  依element id在文件中取得一個資料項目的值.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   dname Dimension的名稱 .
* @param   XID X軸的ID .
* @param   YID Y軸的ID.
* @param   SDATE 起始日期.
* @param   EDATE 結束日期.
* @return  String
*
*/
    public String getElementValueByID(XbrlTaxonomy xbrltaxonomy, String dname, String XID, String YID, String SDATE, String EDATE)
        throws Exception
    {
        String s5 = null;
        XbrlContext xbrlcontext = null;
        XbrlSchemaElement s_element = xbrltaxonomy.getSchemaElement(XID);
//System.err.println("s_element="+s_element);
        if(s_element != null){
			String dname1="";
			if(dname!=null){
				if(!dname.equals("")){
					dname1=xbrltaxonomy.getNameByID(dname);
				}
			}
//System.err.println("dname1="+dname1+" "+s_element.getPeriodType());

			if(s_element.getPeriodType().equals("instant"))
                xbrlcontext = objContextList.containsDimensionMemberInstantContext(dname1, xbrltaxonomy.getNameByID(YID), EDATE);
            else
                xbrlcontext = objContextList.containsDimensionMemberDurationContext(dname1, xbrltaxonomy.getNameByID(YID), SDATE, EDATE);
		}

        if(xbrlcontext != null) {
//                System.out.println("Context: " + xbrlcontext.getID());
                s5 = getElementValueByID(XID, xbrlcontext.getID());
        }else{
            throw new Exception("Dimension member " + YID + " not found.");
		}
        return s5;
    }

/**
*  依照calculation定義的關係，驗證值否正確.
*
* @param   ROLE calculationLink的ROLE.
* @param   SDATE 起始日期，格式YYYYMMDD.
* @param   EDATE 結束日期，格式YYYYMMDD.
* @param   element Target element.
* @return  boolean 是否完全正確.
*
*/

	public boolean validate(String ROLE, String SDATE, String EDATE, Element element)
        throws Exception  {
		if(TAXONOMY==null) throw new Exception("Please initial with XbrlDocument(XbrlTaxonomy xbrltaxonomy) !");
		return validate(TAXONOMY, ROLE,  SDATE,  EDATE,  element);

	}

/**
*  依照calculation定義的關係，驗證值否正確.
*
* @param   xbrltaxonomy 所使用的Taxonomy物件 .
* @param   ROLE calculationLink的ROLE.
* @param   SDATE 起始日期，格式YYYYMMDD.
* @param   EDATE 結束日期，格式YYYYMMDD.
* @param   element Target element.
* @return  boolean 是否完全正確.
*
*/

	public boolean validate(XbrlTaxonomy xbrltaxonomy, String ROLE, String SDATE, String EDATE, Element element)
        throws Exception  {
        int j = 0;
        Vector vector = getCalTree(xbrltaxonomy, ROLE, SDATE, EDATE);
//System.err.println("-- ROLE="+ROLE+" v="+vector.size());
        for(int i = 0; i < vector.size(); i++)  {
            XbrlCalculationTree rule = (XbrlCalculationTree)vector.get(i);  

//					Document doc= jcx.xbrl.xml.getNewDocument();
//					Element e1=doc.createElement("response1");
//					rule.printXML(e1);
//		System.err.println(jcx.xbrl.xml.print(e1));

//				System.err.println(" Validate "+s+"("+i+") "+rule.getRootNode());
            j += rule.validate(xbrltaxonomy, element);
            
        }

        element.setAttribute("errorCount", (new String()).valueOf(j));
		return (j==0);
    }



/**
*  取得所有的context.
*
* @return  Vector(XbrlContext).
*
*/


    public Vector getAllContext() {
        Vector vector = null;
        vector = new Vector();
        for(Enumeration enumeration = objContextList.hContextTable.keys(); enumeration.hasMoreElements(); vector.add(objContextList.get((String)enumeration.nextElement())));
        return vector;
    }
/**
*  取得所有的context(不含Dimension的context).
*
* @return  Vector(XbrlContext).
*
*/

    public Vector getPureContext()
    {
        Vector vector = null;
        vector = new Vector();
        for(Enumeration enumeration = objContextList.hContextTable.keys(); enumeration.hasMoreElements();)
        {
            XbrlContext xbrlcontext = objContextList.get((String)enumeration.nextElement());
            if(!xbrlcontext.hasDimension())
                vector.add(xbrlcontext);
        }

        return vector;
    }
/*
    public void printXML(XbrlTaxonomy xbrltaxonomy, String s, String s1, String s2, Element element)
        throws Exception
    {
        Object obj = null;
        XbrlPresentationTree XbrlPresentationTree = getPreTree(xbrltaxonomy, s, s1, s2);
        if(XbrlPresentationTree != null)
        {
            XbrlPresentationTree.printXML(element);
            for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    String s3 = element1.getAttribute("caption");
                    String s4;
                    try{
                        if(element1.hasAttribute("preferredLabel"))
                            s4 = xbrltaxonomy.getLabel(element1.getAttribute("id"), xbrltaxonomy.getDefaultLanguage(), element1.getAttribute("preferredLabel"));
                        else
                            s4 = xbrltaxonomy.getLabelByID(element1.getAttribute("id"));
                    } catch(Exception e1)
                    {
                        s4 = "";
                    }
                    s3 = s3 + s4;
                    element1.setAttribute("caption", s3);
                }

        } else
        {
            throw new XbrlRoleNotSupportException("Unsupport role: " + s);
        }
    }
*/
	public String toString(){
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		jcx.xbrl.xml.save(bout, "Big5", getRootElement());
		return new String(bout.toByteArray());
	}
	public String toString(String encoding){
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		jcx.xbrl.xml.save(bout, encoding, getRootElement());
		try{
			return new String(bout.toByteArray());
		} catch(Exception e){
			return new String(bout.toByteArray());
		}
	}
/**
*  取得XbrlElementPool.
*
* @param   id  .
* @return  XbrlElementPool.
*
*/	
	public XbrlElementPool getElementPool(String id){
		XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
		if(xbrlelementpool != null) {
			return xbrlelementpool;
//			XbrlContext xbrlcontext = objContextList.getSchemaDefinedContext(xbrlelementpool.objSchema, sdate, edate);
//			s3 = getElementValueByID(id, xbrlcontext.getID());
		} else {
			return null;
//			throw new Exception("Element "+id + " is not existed.");
		}
	}

/**
*	取得底下所有的Element.
* @param   id  .
*/
    public XbrlElement[] getAllItems(String id){
		XbrlElementPool xbrlelementpool = (XbrlElementPool)ELEMENTS.get(id);
//System.err.println("---- "+xbrlelementpool);
		if(xbrlelementpool != null) {
			return xbrlelementpool.getAllItems();
//			XbrlContext xbrlcontext = objContextList.getSchemaDefinedContext(xbrlelementpool.objSchema, sdate, edate);
//			s3 = getElementValueByID(id, xbrlcontext.getID());
		} else {
			return new XbrlElement[0];
//			throw new Exception("Element "+id + " is not existed.");
		}
    }

    public static void main(String args[]) throws Exception{
//		XbrlTaxonomy a=new XbrlTaxonomy("schema/ci/Consolidated","tw-gaap-ci-2010-03-31-cr.xsd");
//		XbrlTaxonomy a=new XbrlTaxonomy("schema/","tw-gaap-bd-2011-06-30.xsd");
		XbrlTaxonomy a=new XbrlTaxonomy("schema/","tw-gaap-bd-2011-09-30-cr.xsd");

		XbrlDocument d1 = new XbrlDocument();

		if(true){
			XbrlDocument xd=d1;
//			d1.load(a, new FileInputStream("instance3.xml"));
			Document doc= jcx.xbrl.xml.getNewDocument();


			String amt="111";

			XbrlElement tp0=d1.createElement(a, "tw-gaap-notes_LoansToOthers-CIAndMIM", amt, "TWD", "20070101", "20070331");

//System.err.println("-- "+tp0.isTuple());

			XbrlElement tp1=tp0.createElement("tw-gaap-notes_Collateral", "", "TWD", "20070101", "20070331");
			

			d1.createElement(a,"tw-gaap-notes_Item1","一987","","20070101","20070331");
			d1.createElement(a,"tw-gaap-notes_Value","987","","20070101","20070331");
//			tp1.createElement("tw-gaap-mim_Amount","123123","TWD,0","20090101","20090631");

//			XbrlElement tp2=d1.createElement(a, "tw-gaap-notes_Others", amt, "TWD", "20070101", "20070331");
//			tp2.createElement("tw-gaap-notes_Item","一987","","20070101","20070331");
//			tp2.createElement("tw-gaap-notes_Description","one 987","","20070101","20070331");

			String a1=d1.toString("utf-8");

//			a1+="\r\n<!-- TESTTEST -->\r\n";
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("aa.xml"),"UTF8"));
			
			out.write(a1);
			out.close();

//			System.out.println(jcx.xbrl.xml.print(e1));

			return;
		}

		
		if(true){
			d1.load(a, new FileInputStream("d:\\a2.xml"));
			Document doc= jcx.xbrl.xml.getNewDocument();

			String g1=d1.getElementValueByID("tw-gaap-notes_Inventories","20110101","20110331");
			System.err.println("----vv1="+  g1);
//--- ID1=tw-gaap-notes_ForeignCurrencyTransactionsAndTranslationOfForeignCurrencyFinancialStatements 20100101~20101231

//			XbrlElement e1=d1.getElementByID("tw-gaap-notes_ForeignCurrencyTransactionsAndTranslationOfForeignCurrencyFinancialStatements","20100101","20101231");
//			XbrlElement e1=d1.getElementByID("tw-gaap-notes_Others","AsOf20091231");
//			org.w3c.dom.Element element=e1.getData();

//			String vv1=d1.getElementValueByID("tw-gaap-notes_ForeignCurrencyTransactionsAndTranslationOfForeignCurrencyFinancialStatements","20100101","20101231");

//			System.err.println("----vv1="+  vv1);

//			System.err.println("----"+e1+"  value="+e1.getValue());
//			for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()){
//System.err.println("---- node="+node);
//				if(node.getNodeType() == node.CDATA_SECTION_NODE){
//					System.err.println(" CDATA="+node.getNodeValue());
//				}
//			}

//			Element e1=doc.createElement("response");
//			d1.validate(a,"http://www.xbrl.org/tw/fr/gaap/role/BalanceSheet", "20100101", "20100331", e1);
//		d1.validate(a,"http://www.xbrl.org/tw/fr/gaap/role/BalanceSheet", "20091001", "20091231", e1);

//		System.err.println(jcx.xbrl.xml.print(e1));
			return;
		}
		
		if(true){
//			d1.load(a, new FileInputStream("20100708/mim/instance.xml"));

//			XbrlElementPool pool = d1.getElementPool("tw-gaap-mim_OtherSupplementalInformation");
//			if(pool != null) {
//				System.err.println(pool);
//				XbrlElement xx[]=pool.getAllItems();
/*
				XbrlElement xx[]=d1.getAllItems("tw-gaap-mim_OtherSupplementalInformation");
				for(int z=0;z<xx.length;z++){
					System.err.println(xx[z].getID()+" -> "+xx[z].isTuple()+" sub="+xx[z].getAllItems().length);
					XbrlElement zz[]=xx[z].getAllItems();
					for(int w=0;w<zz.length;w++){
						System.err.println("     "+zz[w].getID()+" -> "+zz[w].getValue()+" "+zz[w].getContext().getID());
					}

				}
*/
//			}
//			d1.getElement

			String amt="123000";
//			String account=a.getIDByTerseLabel("1111");
			String account="tw-gaap-mim_OtherSupplementalInformation";
//			String account="tw-gaap-notes_FinancialReportnotes";

//			d1.createElement(a, account, amt, "TWD,n/a", "20070101", "20070331");

		d1.createEmpty(a, "http://www.twse.com.tw", "1101","台泥", "20080101", "20080331");

//		d1.createElement(a, account, amt, "TWD", "20080101", "20080331");
//		d1.createElement(a, account, amt, "TWD", "20070101", "20070331");



			XbrlElement tp1=d1.createElement(a, "tw-gaap-notes_Others", amt, "TWD", "20070101", "20070331");
			tp1.createElement("tw-gaap-notes_Item","一987","","20070101","20070331");
			tp1.createElement("tw-gaap-notes_Description","one 987","","20070101","20070331");
//			tp1.createElement("tw-gaap-mim_Amount","123123","TWD,0","20090101","20090631");

			XbrlElement tp2=d1.createElement(a, "tw-gaap-notes_Others", amt, "TWD", "20070101", "20070331");
			tp2.createElement("tw-gaap-notes_Item","一987","","20070101","20070331");
			tp2.createElement("tw-gaap-notes_Description","one 987","","20070101","20070331");

//			System.out.println(d1.toString("utf-8"));
			Element e1=d1.getRootElement();
			Comment c1=e1.getOwnerDocument().createComment("TEST123456");
//			c1.setData("AAAAAAAAAAAAAA");
//System.err.println("--type="+c1.getNodeType());
//			Node c1=e1.getOwnerDocument().createElement("TEST123456");
			e1.appendChild(c1);

			String a1=d1.toString("utf-8");
//			a1+="\r\n<!-- TESTTEST -->\r\n";
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("aa.xml"),"UTF8"));
			
			out.write(a1);
			out.close();

			System.out.println(jcx.xbrl.xml.print(e1));
			
			return;
		}

//		String account="1101";

		String account=a.getIDByTerseLabel("3999");


/*
		d1.load(a, new FileInputStream("c:/temp/z.xml"));

		Element[] aa=d1.getUnsupportedElement();
//		System.err.println(jcx.xbrl.xml.print(aa[0]));
		if(aa.length!=0) System.err.println(aa[0].getTagName());

		Document doc= jcx.xbrl.xml.getNewDocument();
		Element e1=doc.createElement("response");
//		d1.validate(a,"http://www.xbrl.org/tw/fr/gaap/role/BalanceSheet", "20091001", "20091231", e1);

//		System.err.println(jcx.xbrl.xml.print(e1));
		System.out.println(d1.toString());

*/

		d1.createEmpty(a, "http://www.twse.com.tw", "1101","台泥", "20080101", "20080331");

		String amt="123000";
		XbrlElement v1=d1.createElement(a, account, amt, "TWD", "20080101", "20080331");
		d1.createElement(a, account, amt, "TWD", "20070101", "20070331");

		System.err.println(v1);

//		d1.createDimensionContext(a, "http://www.twse.com.tw", "1101", "20070101", "20070331", "http://www.xbrl.org/tw/fr/gaap/role/StatementStockholkersEquity");

/*
		Vector v=d1.getAllContext();
		for(int i=0;i<v.size();i++){
			XbrlContext ss=(XbrlContext)v.elementAt(i);
			System.err.println("--- "+ss.getID()+"="+ss.getDimensionID());
		}
*/
//System.err.println("--- "+d1.getContextByID("tw-gaap-ci_CapitalStockMember"));
//		d1.createElement(a, "", "tw-gaap-ci_BeginningBalanceMember", "tw-gaap-ci_CapitalStockMember", "123000", "TWD", "20070101", "20070331");

//　表格 - 條列項目(http://www.xbrl.org/tw/fr/gaap/role/StockholdersEquity)
//	股東權益變動表	tw-gaap-fsci:StatementStockholdersEquityAbstract_member	tw-gaap-fsci_StatementStockholdersEquityAbstract_member
//	股東權益內容維度	tw-gaap-fsci:StockholdersEquityDimension_member	tw-gaap-fsci_StockholdersEquityDimension_member
//31xx	股本	tw-gaap-fsci:CapitalStock_member	tw-gaap-fsci_CapitalStock_member
//3110	普通股股本 - 金額	tw-gaap-fsci:CommonStockValue_member	tw-gaap-fsci_CommonStockValue_member
//	A1	期初餘額	tw-gaap-fsci:BeginningBalance_member	tw-gaap-fsci_BeginningBalance_member


		System.out.println(d1.toString());

	}
}
