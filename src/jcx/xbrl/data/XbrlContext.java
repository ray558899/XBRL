package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import org.w3c.dom.*;


public class XbrlContext
{

    protected XbrlContext()
    {
        objEntity = null;
        objPeriod = null;
        objScenario = null;
    }

/**
*  ¾켹 context ID
*
* @return  String ID
*
*/
    public String getID()
    {
        if(eData != null)
            return eData.getAttribute("id");
        else
            return null;
    }

/**
*  ¾켹 XbrlEntity
*
* @return  XbrlEntity
*
*/

    public XbrlEntity getEntity()
    {
        return objEntity;
    }

/**
*  ¾켹 XbrlPeriod
*
* @return  XbrlPeriod
*
*/

    public XbrlPeriod getPeriod()
    {
        return objPeriod;
    }

/**
*  ¾켹 context ID
*
* @return  String ID
*
*/

    public String getPeriodType()
    {
        String s = null;
        if(objPeriod != null)
            s = objPeriod.getPeriodType();
        return s;
    }

    protected void createInstantContext(Element element, String s, String s1, String s2)
    {
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("context");
            Node node = element.getFirstChild();
            element.insertBefore(eData, node.getNextSibling());
			if(s2.length()==0) new Exception("-------AsOf data is null---------").printStackTrace(System.out);
            strID = "AsOf" + s2;
            eData.setAttribute("id", strID);
            objEntity = new XbrlEntity(eData, s, s1);
            objPeriod = new XbrlPeriod(eData, s2);
        }
    }

    protected void createDurationContext(Element element, String s, String s1, String s2, String s3)
    {
        if(element != null)
        {
            eData = element.getOwnerDocument().createElement("context");
            Node node = element.getFirstChild();
            element.insertBefore(eData, node.getNextSibling());
            strID = "From" + s2 + "To" + s3;
            eData.setAttribute("id", strID);
            objEntity = new XbrlEntity(eData, s, s1);
            objPeriod = new XbrlPeriod(eData, s2, s3);
        }
    }

    protected void loadContext(Element element)
    {
        if(element != null){
            eData = element;
            strID = element.getAttribute("id");
//			System.err.println("-- load "+element.getTagName()+" "+strID+" "+element.hasChildNodes());

            for(Node node = eData.getFirstChild(); node != null; node = node.getNextSibling()){
//				System.err.println("--   type "+node.getNodeName()+" "+node.getNodeType());
                if(node.getNodeType() == 1) {
                    Element element1 = (Element)node;
                    String s = jcx.xbrl.xml.getElementName(element1.getTagName(), ":");

//					System.err.println("-- load "+element1.getTagName());
//					System.err.println("-- load "+s);

                    if(s.equals("entity")){
                        objEntity = new XbrlEntity();
                        objEntity.setData(element1);
                    } else if(s.equals("period")) {
                        objPeriod = new XbrlPeriod();
                        objPeriod.setData(element1);
                    } else if(s.equals("scenario")) {
                        objScenario = new XbrlContextScenario();
                        objScenario.setData(element1);
                    } else {
					}
                }
			}

        }
    }

    protected void createMemberDurationContext(XbrlTaxonomy xbrltaxonomy, Element element, String s, String s1, String s2, String s3, String s4, 
            XbrlDefinitionTreeNode xbrldefinitiontreenode) throws Exception
    {
        if(element != null)
        {
			PARENT=element;

			eData = element.getOwnerDocument().createElement("context");

//            Node node = element.getFirstChild();
//            element.insertBefore(eData, node.getNextSibling());

            objEntity = new XbrlEntity(eData, s, s1);
            strID = "From" + s2 + "To" + s3;
            objPeriod = new XbrlPeriod(eData, s2, s3);
            strID += "_";
            strID += xbrldefinitiontreenode.getXbrlSchemaElement().getName();
            eData.setAttribute("id", strID);
            objScenario = new XbrlContextScenario(eData, s4, xbrltaxonomy.getNameByID(xbrldefinitiontreenode.getID()));
        }
    }

	boolean element_build=false;
    protected void createMemberInstantContext(XbrlTaxonomy xbrltaxonomy, Element element, String s, String s1, String s2, String s3, XbrlDefinitionTreeNode xbrldefinitiontreenode) throws Exception {
        if(element != null)  {
			PARENT=element;

            eData = element.getOwnerDocument().createElement("context");
//            Node node = element.getFirstChild();
//            element.insertBefore(eData, node.getNextSibling());
            objEntity = new XbrlEntity(eData, s, s1);
			if(s2.length()==0) new Exception("-------AsOf data is null---------").printStackTrace(System.out);
            strID = "AsOf" + s2;
            objPeriod = new XbrlPeriod(eData, s2);
            strID += "_";
            strID += xbrldefinitiontreenode.getXbrlSchemaElement().getName();
            eData.setAttribute("id", strID);
            objScenario = new XbrlContextScenario(eData, s3, xbrltaxonomy.getNameByID(xbrldefinitiontreenode.getID()));
        }
    }

    public String getDimensionID()
    {
        String s = null;
        if(objScenario != null){
            s = objScenario.getDimensionID();
        } else {
			if(objEntity!=null){
				if(objEntity.objSegment != null){
					s = objEntity.objSegment.getDimensionID();
				}
			}
		}
        return s;
    }

    public String getDimensionMember()
    {
        String s = null;
        if(objScenario != null)
            s = objScenario.getDimensionMember();
        else
        if(objEntity.objSegment != null)
            s = objEntity.objSegment.getDimensionMember();
        return s;
    }

    public boolean hasDimension()
    {
        boolean flag = false;
        if(objScenario != null)
            flag = true;
        return flag;
    }

    public XbrlContextScenario getScenario()  {
        return objScenario;
    }

    protected void setScenario(XbrlContextScenario x)  {
       objScenario=x;
    }

    protected String strID;
    protected Element eData;

    protected Element PARENT=null;

    protected XbrlEntity objEntity;
    protected XbrlPeriod objPeriod;
    protected XbrlContextScenario objScenario;
}
