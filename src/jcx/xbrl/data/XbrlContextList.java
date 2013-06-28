package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.Element;

class XbrlContextList {

    public XbrlContextList()
    {
        hContextTable = new Hashtable();
    }

    protected XbrlContext get(String s)
    {
        XbrlContext xbrlcontext = null;
        xbrlcontext = (XbrlContext)hContextTable.get(s);
        return xbrlcontext;
    }

    protected void put(String s, XbrlContext xbrlcontext)
    {

        hContextTable.put(s, xbrlcontext);
    }

    protected XbrlContext reomve(String s)
    {
        XbrlContext xbrlcontext = null;
        xbrlcontext = (XbrlContext)hContextTable.remove(s);
        return xbrlcontext;
    }

    protected XbrlContext createInstantContext(Element element, String s, String s1, String s2)
    {
        XbrlContext xbrlcontext = null;
        xbrlcontext = new XbrlContext();
        xbrlcontext.createInstantContext(element, s, s1, s2);
        put(xbrlcontext.getID(), xbrlcontext);
        return xbrlcontext;
    }

    protected XbrlContext createDurationContext(Element element, String s, String s1, String s2, String s3)
    {
        XbrlContext xbrlcontext = null;
        xbrlcontext = new XbrlContext();
        xbrlcontext.createDurationContext(element, s, s1, s2, s3);
        put(xbrlcontext.getID(), xbrlcontext);
        return xbrlcontext;
    }

    protected XbrlContext getSchemaDefinedContext(XbrlSchemaElement xbrlschemaelement, String s, String s1)
    {
        XbrlContext xbrlcontext = null;
        if(xbrlschemaelement != null)
            if(xbrlschemaelement.getPeriodType().equals("duration"))
                xbrlcontext = containsDurationContext(s, s1);
            else
            if(xbrlschemaelement.getPeriodType().equals("instant"))
                xbrlcontext = containsInstantContext(s1);
        return xbrlcontext;
    }

    protected XbrlContext containsVlaue(String s, String s1, String s2)
    {
        XbrlContext xbrlcontext1 = null;
        for(Enumeration e = hContextTable.keys(); e.hasMoreElements() && xbrlcontext1 == null;)
        {
            String s3 = (String)e.nextElement();
            XbrlContext xbrlcontext = (XbrlContext)hContextTable.get(s3);
            XbrlEntity xbrlentity = xbrlcontext.getEntity();
            if(xbrlentity.getCode().compareTo(s) == 0)
            {
                XbrlPeriod xbrlperiod = xbrlcontext.getPeriod();
                if(xbrlperiod.getPeriodType().compareTo("duration") == 0 && xbrlperiod.getStartDateString().equals(s1) && xbrlperiod.getEndDateString().equals(s2))
                    xbrlcontext1 = xbrlcontext;
            }
        }

        return xbrlcontext1;
    }

    protected XbrlContext containsDurationContext(String s, String s1)
    {
        XbrlContext xbrlcontext1 = null;
        for(Enumeration e = hContextTable.keys(); e.hasMoreElements() && xbrlcontext1 == null;)
        {
            String s2 = (String)e.nextElement();
            XbrlContext xbrlcontext = (XbrlContext)hContextTable.get(s2);
            if(xbrlcontext.objScenario == null)
            {
                XbrlPeriod xbrlperiod = xbrlcontext.getPeriod();
				if(xbrlperiod==null) continue;
                if(xbrlperiod.getPeriodType().equals("duration") && xbrlperiod.getStartDateString().equals(s) && xbrlperiod.getEndDateString().equals(s1))
                    xbrlcontext1 = xbrlcontext;
            }
        }

        return xbrlcontext1;
    }

    protected XbrlContext containsInstantContext(String s)
    {
        XbrlContext xbrlcontext1 = null;
        for(Enumeration e = hContextTable.keys(); e.hasMoreElements() && xbrlcontext1 == null;)
        {
            String s1 = (String)e.nextElement();
            XbrlContext xbrlcontext = (XbrlContext)hContextTable.get(s1);
            if(xbrlcontext.objScenario == null)
            {
                XbrlPeriod xbrlperiod = xbrlcontext.getPeriod();
                if(xbrlperiod != null)
                {
                    if(xbrlperiod.getPeriodType().equals("instant") && xbrlperiod.getInstantDateString().equals(s)){
                        xbrlcontext1 = xbrlcontext;
					}
                } else {
//					new Exception().printStackTrace();
//                    System.out.println("No period in this context. "+xbrlcontext);
						if(xbrlcontext.getPeriodType()!=null){
							System.out.println("No period in this context. "+xbrlcontext);
						}
//                      System.out.println("No period in this context. "+s);
                }
            }
        }

        return xbrlcontext1;
    }

    protected void createDimensionContext(XbrlTaxonomy xbrltaxonomy, Element element, String s, String s1, String s2, String s3, XbrlDefinitionTree xbrldefinitiontree)  throws Exception
    {
//		System.err.println("--- ccc");
        XbrlDefinitionTreeNode xbrldefinitiontreenode1 = null;
        XbrlDefinitionTreeNode xbrldefinitiontreenode = (XbrlDefinitionTreeNode)xbrldefinitiontree.getRootNode();
        xbrldefinitiontreenode1 = getHypercube(xbrldefinitiontreenode);
        if(xbrldefinitiontreenode1 != null){
            createMemeberContext(xbrltaxonomy, element, s, s1, s2, s3, xbrltaxonomy.getNameByID(xbrldefinitiontreenode1.getID()), xbrldefinitiontreenode1);
		}
    }

    protected XbrlDefinitionTreeNode getHypercube(XbrlDefinitionTreeNode xbrldefinitiontreenode)
    {
        XbrlDefinitionTreeNode xbrldefinitiontreenode1 = null;
        if(xbrldefinitiontreenode.getArcrole() != null && xbrldefinitiontreenode.getArcrole().equals("http://xbrl.org/int/dim/arcrole/hypercube-dimension"))
            xbrldefinitiontreenode1 = xbrldefinitiontreenode;
        if(xbrldefinitiontreenode1 == null)
            for(xbrldefinitiontreenode = (XbrlDefinitionTreeNode)xbrldefinitiontreenode.getFirstChild(); xbrldefinitiontreenode != null && xbrldefinitiontreenode1 == null; xbrldefinitiontreenode = (XbrlDefinitionTreeNode)xbrldefinitiontreenode.getNextSibling())
                xbrldefinitiontreenode1 = getHypercube(xbrldefinitiontreenode);

        return xbrldefinitiontreenode1;
    }

    protected void createMemeberContext(XbrlTaxonomy xbrltaxonomy, Element element, String s, String s1, String s2, String s3, String s4, 
            XbrlDefinitionTreeNode xbrldefinitiontreenode) throws Exception
    {

        if(xbrldefinitiontreenode.getArcrole() != null && (xbrldefinitiontreenode.getArcrole().equals("http://xbrl.org/int/dim/arcrole/domain-member") || xbrldefinitiontreenode.getArcrole().equals("http://xbrl.org/int/dim/arcrole/dimension-domain")))
        {
            XbrlContext xbrlcontext = new XbrlContext();
            xbrlcontext.createMemberDurationContext(xbrltaxonomy, element, s, s1, s2, s3, s4, xbrldefinitiontreenode);
            put(xbrlcontext.getID(), xbrlcontext);
//System.err.println("--- put1 "+xbrlcontext.getID());
			xbrlcontext = new XbrlContext();
            xbrlcontext.createMemberInstantContext(xbrltaxonomy, element, s, s1, s3, s4, xbrldefinitiontreenode);
            put(xbrlcontext.getID(), xbrlcontext);
//System.err.println("--- put2 "+xbrlcontext.getID());
        }
        for(XbrlDefinitionTreeNode xbrldefinitiontreenode1 = (XbrlDefinitionTreeNode)xbrldefinitiontreenode.getFirstChild(); xbrldefinitiontreenode1 != null; xbrldefinitiontreenode1 = (XbrlDefinitionTreeNode)xbrldefinitiontreenode1.getNextSibling()){
//System.out.println("--- put "+xbrldefinitiontreenode1.getID());
            createMemeberContext(xbrltaxonomy, element, s, s1, s2, s3, s4, xbrldefinitiontreenode1);
		}

    }

    protected XbrlContext containsDimensionMemberDurationContext(String s, String s1, String s2, String s3)
    {
        XbrlContext xbrlcontext1 = null;
        for(Enumeration e = hContextTable.keys(); e.hasMoreElements() && xbrlcontext1 == null;)
        {
            String s4 = (String)e.nextElement();
            XbrlContext xbrlcontext = (XbrlContext)hContextTable.get(s4);
//System.err.println("-------- xbrlcontext="+xbrlcontext.hasDimension());
            if(xbrlcontext.hasDimension())
            {
                XbrlPeriod xbrlperiod = xbrlcontext.getPeriod();
				if(xbrlperiod.getPeriodType().equals("duration")){
//System.err.println("-------- s4="+s4 +" "+xbrlperiod.getStartDateString()+" - "+xbrlperiod.getEndDateString());
					if(xbrlperiod.getStartDateString().equals(s2) && xbrlperiod.getEndDateString().equals(s3)){
						String s5 = xbrlcontext.getDimensionID();
//	System.err.println("-------- s5="+s5+" s="+s);
						if(s5 != null)
						{
							String s6 = xbrlcontext.getDimensionMember();
							if(s5.equals(s)){
//	System.err.println("-------- s1="+s1+" s6="+s6);
								if(s6.equals(s1)){
									xbrlcontext1 = xbrlcontext;
									if(xbrlcontext.eData.getParentNode()==null){
										xbrlcontext.PARENT.insertBefore(xbrlcontext.eData, xbrlcontext.PARENT.getFirstChild().getNextSibling());
									}
								}
							}
								
						}
					}
				}
            }
        }

        return xbrlcontext1;
    }

    protected XbrlContext containsDimensionMemberInstantContext(String s, String s1, String s2)
    {
        XbrlContext xbrlcontext = null;
		String msg="";
        for(Enumeration e = hContextTable.keys(); e.hasMoreElements() && xbrlcontext == null;)
        {
            String s3 = (String)e.nextElement();
            XbrlContext xbrlcontext1 = (XbrlContext)hContextTable.get(s3);

/*
	if(s1.indexOf("OthersStockholdersEquitiesAdjustments")!=-1){
		String ss = xbrlcontext1.getDimensionMember();
		if(ss==null){
System.err.println("-- ccss="+xbrlcontext1.getID());
		} else {
			if(ss.indexOf("OthersStockholdersEquitiesAdjustments")!=-1){
	System.err.println("-- ss="+ss);
			}
		}
	}
*/
//System.err.println("-- xbrlcontext1="+s3+" "+xbrlcontext1.hasDimension());

            if(xbrlcontext1.hasDimension()) {
                XbrlPeriod xbrlperiod = xbrlcontext1.getPeriod();
                if(xbrlperiod.getPeriodType().equals("instant")){
//System.err.println("-- s2="+s2+" "+xbrlperiod.getInstantDateString());
					if(xbrlperiod.getInstantDateString().equals(s2)){
						String s4 = xbrlcontext1.getDimensionID();
						if(s4 != null) {
							String s5 = xbrlcontext1.getDimensionMember();

							if(s==null) s=s4;
							if(s.equals("")) s=s4;

//System.err.println("-- s4="+s4+" s="+s);

							if(s4.equals(s) && s5.equals(s1)){
								xbrlcontext = xbrlcontext1;
								if(xbrlcontext.eData.getParentNode()==null){
									xbrlcontext.PARENT.insertBefore(xbrlcontext.eData, xbrlcontext.PARENT.getFirstChild().getNextSibling());
								}
								return xbrlcontext;
							} else {
//System.err.println("-- s1="+s1+" s5="+s5);
								if(s5.equals(s1)){
									msg+=","+s4;
								}
							}
						}
					}
                }
            }
        }
		if(msg.length()!=0) System.out.println("Dimension ID mistakes,maybe "+msg.substring(1)+"!");
        return xbrlcontext;
    }

    protected Hashtable hContextTable;
}
