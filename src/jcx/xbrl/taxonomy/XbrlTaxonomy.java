package jcx.xbrl.taxonomy;

import java.io.PrintStream;
import java.util.*;
import org.w3c.dom.*;
import jcx.xbrl.exception.*;

/**
*<PRE>
*  ��@ Taxonomy ������C
*  
*</PRE>
*/
public class XbrlTaxonomy
{

    private String URI;
    protected XbrlSchema xbrlschema;
    protected XbrlDefinition xbrldef;
    protected XbrlLabel xbrllabel;
    protected XbrlPresentation xbrlpresent;
    protected XbrlCalculation xbrlcal;
    protected XbrlReference xbrlref;
    private String PREFIX=null;
    private String strSchemaFilePath;
    private String strSchemaFileName;
    private String strDefaultLanguage;
    private String XMLSchemaURI;
    private String LinkbaseURI;


/**
*  �إ߷s�� Taxonomy
*<PRE>
* @param   path taxonomy �ɮשҦb���ؿ�.
* @param   filename taxonomy �ɮצW��.
*</PRE>
*/
	public XbrlTaxonomy(String path, String filename) throws Exception
    {

//		if(true){
//			System.err.println(" -- load "+path+" "+filename);
//			System.exit(0);
//		}
        URI = null;
        strSchemaFilePath = path;
        strSchemaFileName = filename;
        XMLSchemaURI = "http://www.w3.org/2001/XMLSchema";
        LinkbaseURI = "http://www.xbrl.org/2003/linkbase";
        xbrlschema = new XbrlSchema();
        xbrldef = new XbrlDefinition();
        xbrllabel = new XbrlLabel();
        xbrlpresent = new XbrlPresentation();
        xbrlcal = new XbrlCalculation();
        xbrlref = new XbrlReference();
        strDefaultLanguage = "zh";
		load1(path,filename);
    }

/**
*   ���o �w�]���y�t
*
* @return  �^�� �y�t
*
*/

    public String getDefaultLanguage()
    {
        return strDefaultLanguage;
    }


	public Vector URIS=new Vector();
	public Vector PREFIXS=new Vector();
/**
*  ���o�Ҧ� PRIFIX
*<PRE>
* @return  �^�� �Ҧ�PREFIX
*</PRE>
*/

    public Vector getPrefixs()
    {
        return PREFIXS;
    }

/**
*  ���o PRIFIX
*<PRE>
* @return  �^�� PREFIX
*</PRE>
*/

    public String getPrefix()
    {
        return PREFIX;
    }

/**
*  ���o URI
*<PRE>
* @return  �^�� URI
*</PRE>
*/

    public String getURI()
    {
        return URI;
    }

/**
*  ���o Taxonomy filename
*<PRE>
* @return  �^�� filename
*</PRE>
*/
    public String getSchemaFileName() {
        return strSchemaFileName;
    }


    private void setSchemaFilePath(String s)
    {
        strSchemaFilePath = s;
        xbrlschema.strSchemaFilePath = s;
    }

//    public void loadTaxonomy(String s, String s1)
    private void load1(String s, String s1)
        throws TaxonomyFileNotFoundException, TaxonomyFormatErrorException
    {
        {
            setSchemaFilePath(s);
            try {
				if(!s.endsWith("/")) s+="/";
                Document document = jcx.xbrl.xml.load(s + s1);
                Element element = document.getDocumentElement();

				loadSchema(s, element);
            }
            catch(Exception e1) {
				e1.printStackTrace();

                throw new TaxonomyFileNotFoundException("File not found " + s + s1);
            }
        }
        xbrldef.createTree();
        xbrlcal.createTree();
        xbrlpresent.createTree();
    }

    private void loadSchema(String s, Element element)
    {
        Vector vector = new Vector();
        xbrlschema.load(s, element, vector);
        URI = xbrlschema.URI;
        PREFIX = xbrlschema.PREFIX;

        URIS = xbrlschema.URIS;
        PREFIXS = xbrlschema.PREFIXS;
// System.out.println("Loading PREFIX=" + PREFIX);

        for(int i = 0; i < vector.size(); i++)
        {
            String s1 = (String)vector.get(i); {
                try {
                    Document document = jcx.xbrl.xml.load(s1);
                    addLinkBase(s1, document.getDocumentElement());
                }
                catch(Exception e2)   {
					e2.printStackTrace();
                    System.out.println("Loading linkbase error" + s1);
                }
            }
        }

    }

    private void addLinkBase(String s, Element element)
        throws TaxonomyFormatErrorException
    {
        String s1 = jcx.xbrl.xml.getElementName(element.getTagName(), ":");
        String s2 = element.getNamespaceURI();
//System.err.println("-- "+s+" s1="+s1);
//System.err.println("--- "+s);
        if(s1.equalsIgnoreCase("schema")){
            loadSchema(s, element);
        } else if(s1.equalsIgnoreCase("linkbase")){
            try
            {
                for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
                    if(node.getNodeType() == 1) {
                        Element element1 = (Element)node;
                        String s3 = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");
                        if(s3.equalsIgnoreCase("definitionLink"))
                            xbrldef.load(xbrlschema.hSchemaTable, element1);
                        else
                        if(s3.equalsIgnoreCase("labelLink")){
                            xbrllabel.load(xbrlschema.hSchemaTable, element1);
//				System.err.println("------ "+element1.getTagName());
                        } else if(s3.equalsIgnoreCase("presentationLink")){
                            xbrlpresent.load(xbrlschema.hSchemaTable, element1);
                        }else if(s3.equalsIgnoreCase("calculationLink"))
                            xbrlcal.load(xbrlschema.hSchemaTable, element1);
                        else
                        if(s3.equalsIgnoreCase("referenceLink"))
                            xbrlref.load(xbrlschema.hSchemaTable, element1);
                    }

            }
            catch(TaxonomyFormatErrorException taxonomyformaterrorexception)
            {
                throw taxonomyformaterrorexception;
            }
		}
    }


/**
*  ���o Schema List
*
* @return  �^�ǩҦ��� Schema List
*
*/
	public Vector getSchemaList()
    {
        return xbrlschema.getSchemaList();
    }

/**
*  ���o Schema 
*
* @return  �^�ǩҦ��� Schema Hashtable
*
*/
    public Hashtable getSchemaTable()
    {
        return xbrlschema.getSchemaTable();
    }

/**
*  ���o Schema ID
*
* @return  �^�ǩҦ��� Schema ID
*
*/
    public Enumeration getElementIDs()
    {
        Enumeration enumeration = null;
        enumeration = xbrlschema.getElementIDs();
        return enumeration;
    }


/**
*  ���oID������ Schema 
*<PRE>
* @return  �^�� Schema element
*</PRE>
*/

    public XbrlSchemaElement getSchemaElement(String s)
        throws Exception
    {
        XbrlSchemaElement xbrlschemaelement = (XbrlSchemaElement)xbrlschema.hSchemaTable.get(s);
        if(xbrlschemaelement == null)
            throw new Exception("Element id not found ! " + s);
        else
            return xbrlschemaelement;
    }

/**
*  ���oelement ID������element name(�tname space)
*<PRE>
* @return  �^�� element name
*</PRE>
*/
    public String getNameByID(String s)
        throws Exception
    {
		String s2=xbrlschema.getNameByID(s);
		if(s2==null) throw new Exception("ID("+s+") not found !");
		XbrlSchemaElement ss=getSchemaElement(s);
//        String s1=getPrefix() + ":" + s2;
        String s1=ss.getPrefix() + ":" + s2;

//		System.err.println("--- "+s+" -> "+s1);
        return s1;
    }

/**
*  ���oelement ID������element name(�tname space)
*<PRE>
* @return  �^�� element name
*</PRE>
*/
    public String getLocalNameByID(String s)
        throws Exception
    {
        String s1 = xbrlschema.getNameByID(s);

		if(s1==null) throw new Exception(s);

		return s1;
    }
/**
*  ���oelement name������element ID(���tname space)
*<PRE>
* @return  �^�� element ID
*</PRE>
*/

    public String getIDByName(String s)
        throws Exception
    {
        String s1 = xbrlschema.getIDByName(s);
		if(s1==null) throw new Exception(s);
        return s1;
    }

    private void exportSchemaElement(Element element)
    {
        xbrlschema.exportXML(element);
    }


    private void getAllLabel(String s, Element element)
    {
        xbrllabel.getAllLabel(s, element);
    }
/**
*  ���oelement ID������element �W��(����)
*<PRE>
* @return  �^�� element �W��
*</PRE>
*/

    public String getLabelByID(String s)throws Exception{
		return getLabelByID(s,strDefaultLanguage);
	}
/**
*  ���oelement ID������element �W��
*<PRE>
* @return  �^�� element �W��
*</PRE>
*/
    public String getLabelByID(String s, String s1)
        throws Exception
    {
        String s2 = null;
        try
        {
            s2 = xbrllabel.getLabelByID(s, s1);
        }
        catch(Exception elementidnotfoundexception)
        {
            throw elementidnotfoundexception;
        }
        return s2;
    }

/**
*  ���oelement ID������element �W��
*<PRE>
* @return  �^�� element �W��
*</PRE>
*/
    public String getLabelByID(String s, String s1,String role)
        throws Exception
    {
        String s2 = null;
        try
        {
            s2 = xbrllabel.getLabel(s, s1,role);
        }
        catch(Exception elementidnotfoundexception)
        {
            throw elementidnotfoundexception;
        }
        return s2;
    }

/**
*  �H�|�p��إN�X���oelement id 
*<PRE>
* @return  �^�� element ID
*</PRE>
*/
    public String getIDByTerseLabel(String s)
        throws Exception
    {
        String s1 = null;
        try
        {
            s1 = xbrllabel.getIDByTerseLabel(s);
        }
        catch(Exception elementidnotfoundexception)
        {
            throw elementidnotfoundexception;
        }
        return s1;
    }
/**
*  �Helement id ���o �|�p��إN�X
*<PRE>
* @return  �^�� �|�p��إN�X
*</PRE>
*/

    public String getTerseLabelByID(String s)
        throws Exception
    {
        String s1 = null;
        try
        {
            s1 = xbrllabel.getTerseLabelByID(s);
        }    catch(Exception elementidnotfoundexception) {
            throw elementidnotfoundexception;
        }
        return s1;
    }


/**
*  �Helement id ���o �W��
*
* @param   id .
* @param   lang �y�t.
* @param   role Label �� role.
* @return  �^�� �W��
*
*/

    public String getLabel(String id, String lang, String role)
        throws Exception
    {
        String s3 = null;
        try
        {
            s3 = xbrllabel.getLabel(id, lang, role);
        }
        catch(Exception e2)
        {
			e2.printStackTrace();
        }
        return s3;
    }

    public XbrlReferenceResource getReferenceByID(String s)
        throws Exception
    {
        XbrlReferenceResource xbrlreferenceresource = null;
        try
        {
            xbrlreferenceresource = xbrlref.getReferenceByID("http://www.xbrl.org/2003/role/link", s);
        }
        catch(Exception elementidnotfoundexception)   {
			throw elementidnotfoundexception;
        }
        return xbrlreferenceresource;
    }

    public Element getReferenceCollection(Element element){
        Element element1 = null;
        element1 = xbrlref.getCollection("http://www.xbrl.org/2003/role/link", element);
        return element1;
    }


/**
*  ���o�Ҧ��� Calculation �W��
*
* @return  �^�� Vector,���t�Ҧ���Calculation �W��,�κA�� String
*
*/

    public Vector getCalculationList()
    {
        return xbrlcal.getCalculationList();
    }

    private Document exportPresentationToXML(String s)
        throws XbrlRoleNotSupportException
    {
        Document document = null;
        try
        {
            document = xbrlpresent.exportToXML(s);
        }
        catch(XbrlRoleNotSupportException xbrlrolenotsupportexception)
        {
            throw xbrlrolenotsupportexception;
        }
        return document;
    }
/*
    public void exportPresentationToCSV(String s, Element element) throws Exception{
        try {
            printXML(s, element);
        } catch(Exception e2) {
            e2.printStackTrace();
			throw e2;
        }
    }
*/
/**
*  �ھڦW�٨��o CalculationTree
*
* @param   id id.
* @return  �^�� CalculationTree[]
*
*/


    public XbrlCalculationTree[] getCalculationTree(String id) throws Exception{
        XbrlCalculationTree[] t = null;
        try {
            t = xbrlcal.getCalculationTree(id);
        } catch(XbrlRoleNotSupportException exception) {
			System.out.println("---- Role list ----");
			Hashtable hhh=xbrlcal.hCalculationLinkTable;
			for(Enumeration e = hhh.keys() ; e.hasMoreElements() ;) {
				Object key=e.nextElement();
				System.out.println("  "+key);
			}

			System.out.println("-------------------");
            throw exception;
        } catch(Exception exception) {
            throw exception;
        }
        return t;
    }

/**
*  ���o�Ҧ��� Presentation �W��
*
* @return  �^�� Vector,���t�Ҧ���Presentation �W��,�κA�� String
*
*/

	public Vector getPresentationList()
    {
        Vector enumeration = null;
        enumeration = xbrlpresent.getPresentationList();
        return enumeration;
    }
/**
*  �ھڦW�٨��o PresentationTree
*
* @param   id id.
* @return  �^�� PresentationTree[]
*
*/

    public XbrlPresentationTree[] getPresentationTree(String id)throws XbrlRoleNotSupportException {
        XbrlPresentationTree xbrlpresentationtree[] = null;
        try {
            xbrlpresentationtree = xbrlpresent.getPresentationTree(id);
        } catch(XbrlRoleNotSupportException xbrlrolenotsupportexception) {
            throw xbrlrolenotsupportexception;
        }
        return xbrlpresentationtree;
    }

/*
    public void printXML(String s, Element element) throws XbrlRoleNotSupportException,Exception {
        XbrlPresentationTree xbrlpresentationtree = xbrlpresent.getPresentationTree(s);
        if(xbrlpresentationtree != null){
            xbrlpresentationtree.printXML(element);
            for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
                if(node.getNodeType() == 1)
                {
                    Element element1 = (Element)node;
                    String s1 = element1.getAttribute("caption");
                    s1 = s1 + xbrllabel.getLabelByID(element1.getAttribute("id"), strDefaultLanguage);
                    element1.setAttribute("caption", s1);
                }

        } else {
            throw new XbrlRoleNotSupportException("Role not supported " + s);
        }
    }
*/

/**
*  ���o�Ҧ��� Definition �W��
*
* @return  �^�� Vector,���t�Ҧ���Definition �W��,�κA�� String
*
*/

    public Vector getDefinitionList()
    {
		return xbrldef.getDefinitionList();
	}

/**
*  �ھڦW�٨��o DefinitionTree
*
* @param   id id.
* @return  �^�� DefinitionTree[]
*
*/

    public XbrlDefinitionTree[] getDefinitionTree(String id)
        throws XbrlRoleNotSupportException
    {
        XbrlDefinitionTree xbrldefinitiontree[] = null;
        try{
            xbrldefinitiontree = xbrldef.getDefinitionTree(id);
        }
        catch(XbrlRoleNotSupportException xbrlrolenotsupportexception)
        {
            throw xbrlrolenotsupportexception;
        }
        return xbrldefinitiontree;
    }


/*
    public Document exportDefinitionToXML(String s)
        throws XbrlRoleNotSupportException
    {
        Document document = null;
        try
        {
            document = xbrldef.exportToXML(s);
        }
        catch(XbrlRoleNotSupportException xbrlrolenotsupportexception)
        {
            throw xbrlrolenotsupportexception;
        }
        return document;
    }

    public Document exportCalculationToXML(String s)
        throws XbrlRoleNotSupportException
    {
        Document document = null;
        try
        {
            document = xbrlcal.exportToXML(s);
        }
        catch(XbrlRoleNotSupportException xbrlrolenotsupportexception)
        {
            throw xbrlrolenotsupportexception;
        }
        return document;
    }
	*/
/*
    public void getMappingTable(String s, Element element, Element element1)
    {

		try{
			XbrlPresentationTree xbrlpresentationtree = xbrlpresent.getPresentationTree(s);
			xbrlpresentationtree.printXML(element1);
			for(Node node = element1.getFirstChild(); node != null; node = node.getNextSibling()){
				if(node.getNodeType() == 1)
				{
					Element element2 = (Element)node;
					String s1 = element2.getAttribute("caption");
					s1 = s1 + xbrllabel.getLabelByID(element2.getAttribute("id"), strDefaultLanguage);
					element2.setAttribute("caption", s1);
					if(element2.getAttribute("type").compareTo("abstract") != 0) {
						Element element3 = jcx.xbrl.xml.getChildByValue(element, "xbrl_element_id", element2.getAttribute("id"));
						if(element3 != null)
							element2.setAttribute("account_code", element3.getAttribute("account_code"));
					} else {
						element2.setAttribute("account_code", "----------------");
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

    }
*/
    public static void main(String args[]) throws Exception{
		long t1=System.currentTimeMillis();
//		XbrlTaxonomy a1=new XbrlTaxonomy("C:\\test\\cfcid-DTS\\cn\\fcid\\common\\pt\\2008-12-01","cfcid-pt-2008-12-01.xsd");
//		XbrlTaxonomy a2=new XbrlTaxonomy("C:\\test\\01\\jp\\fr\\gaap\\t\\cte\\2008-02-01","jpfr-t-cte-2008-02-01.xsd");
//		XbrlTaxonomy a2=new XbrlTaxonomy("C:\\test\\taxonomy\\XBRLUS-USGAAP-Taxonomies-2009-01-31\\dis","us-gaap-dis-acec-2009-01-31.xsd");
//		XbrlTaxonomy a2=new XbrlTaxonomy("C:\\test\\aaaaa","tw-gaap-fsci-2009-01-11.xsd");
//		XbrlTaxonomy a2=new XbrlTaxonomy("http://127.0.0.1/aaaaa","tw-gaap-fsci-2009-01-11.xsd");

		XbrlTaxonomy a2=new XbrlTaxonomy("schema","tw-gaap-ci-2011-06-30.xsd");

		long t2=System.currentTimeMillis();
		System.err.println("Time="+(t2-t1));

//		String s1=a2.getLabelByID("tw-gaap-fsci_CashCashEquivalents");
		String s1=a2.getLabelByID("tw-gaap-notes_Inventories");
		
System.err.println("--- s1="+s1);
          XbrlSchemaElement s_element = a2.getSchemaElement("tw-gaap-notes_Inventories");

System.err.println("--- s2="+s_element+" "+s_element.getPrefix());
//		Thread.sleep(100000000);
		if(true) return;
		
		XbrlTaxonomy a=new XbrlTaxonomy("schema","tw-gaap-fsci-2009-01-11.xsd");
//		a.loadTaxonomy("schema","tw-gaap-fsci-2009-01-11.xsd");
//StockDividendDistributed		
		for(int i=1000;i<9999;i++){
			try{
			String s=a.getIDByTerseLabel(i+"");
//			System.err.println(""+i+" -> "+a.getLabelByID(s,"zh")+" ");
			} catch(Exception e){
			}
		}
		
		System.err.println("--- "+a.getPrefix());
		System.err.println("--- "+a.getURI());
		System.err.println(""+a.getTerseLabelByID("tw-gaap-fsci_StockDividendDistributed")+" -> "+a.getLabelByID("tw-gaap-fsci_StockDividendDistributed","zh")+" ");
	}
}
