package jcx.xbrl.data;

import jcx.xbrl.exception.*;
import jcx.xbrl.taxonomy.*;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.*;
import java.math.*;

class XbrlCalculationTree extends XbrlTree
{

    public XbrlCalculationTree(XbrlTaxonomy xbrltaxonomy)
    {
        objTaxonomy = null;
        objTaxonomy = xbrltaxonomy;
    }

    protected XbrlCalculationTreeNode appendChild(String s, String s1, Element element, XbrlElement xelement, double d)
    {
        Object obj = null;
        XbrlCalculationTreeNode xst_node1 = new XbrlCalculationTreeNode(s1, element, xelement, xelement.objSchema);
        xst_node1.setOrder(d);
        getNodeTable().put(s1, xst_node1);
        if(getNode() == null)
        {
            setNode(xst_node1);
        } else
        {
            XbrlCalculationTreeNode xst_node0 = (XbrlCalculationTreeNode)getNodeByID(s);
            if(xst_node0 != null)
                xst_node0.appendChild(xst_node1);
            else
                System.out.println("Can't find parent node for id:" + s);
        }
        return xst_node1;
    }

    protected void cloneTree(jcx.xbrl.taxonomy.XbrlCalculationTree xbrlcalculationtree, Hashtable hashtable, XbrlContext xbrlcontext, XbrlContext xbrlcontext1) throws Exception
    {
        XbrlSchemaElement xbrlschemaelement = null;
        XbrlElement xelement = null;
        jcx.xbrl.taxonomy.XbrlCalculationTreeNode xbrlcalculationtreenode = (jcx.xbrl.taxonomy.XbrlCalculationTreeNode)xbrlcalculationtree.getRootNode();
        XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(xbrlcalculationtreenode.getID());

//		System.err.println(" -- xx("+xbrlcalculationtreenode.getID()+") ");

        if(xbrlelementpool != null){
            if(xbrlelementpool.objSchema.getPeriodType().equals("duration")){
                xelement = xbrlelementpool.get(xbrlcontext1);
            } else {
                xelement = xbrlelementpool.get(xbrlcontext);
			}
		}
        if(xelement != null){
            xbrlschemaelement = xelement.objSchema;
        } else {
            xbrlschemaelement = objTaxonomy.getSchemaElement(xbrlcalculationtreenode.getID());
		}

//		System.err.println(" -- aa "+xelement);

        XbrlCalculationTreeNode xst_node0 = new XbrlCalculationTreeNode(xbrlcalculationtreenode.getID(), ((XbrlTreeNode) (xbrlcalculationtreenode)).eData, xelement, xbrlschemaelement);
        xst_node0.iWeight = xbrlcalculationtreenode.getWeight();
        setNode(xst_node0);
        cloneNode(xbrlcalculationtreenode, (XbrlCalculationTreeNode)getRootNode(), hashtable, xbrlcontext, xbrlcontext1);
        calculateLevel(getRootNode(), 0);
    }

    protected void cloneNode(jcx.xbrl.taxonomy.XbrlCalculationTreeNode xbrlcalculationtreenode, XbrlCalculationTreeNode xst_node0, Hashtable hashtable, XbrlContext xbrlcontext, XbrlContext xbrlcontext1) throws Exception
    {
        Object obj = null;
        xst_node0.iWeight = xbrlcalculationtreenode.getWeight();
        for(jcx.xbrl.taxonomy.XbrlCalculationTreeNode xbrlcalculationtreenode1 = (jcx.xbrl.taxonomy.XbrlCalculationTreeNode)xbrlcalculationtreenode.getFirstChild(); xbrlcalculationtreenode1 != null; xbrlcalculationtreenode1 = (jcx.xbrl.taxonomy.XbrlCalculationTreeNode)xbrlcalculationtreenode1.getNextSibling())
        {

			XbrlElement xelement = null;

			XbrlElementPool xbrlelementpool = (XbrlElementPool)hashtable.get(xbrlcalculationtreenode1.getID());
            if(xbrlelementpool != null){
                if(xbrlelementpool.objSchema.getPeriodType().equals("duration")){
                    xelement = xbrlelementpool.get(xbrlcontext1);
                } else {
                    xelement = xbrlelementpool.get(xbrlcontext);
				}
			}

            XbrlSchemaElement xbrlschemaelement;
            if(xelement != null)
                xbrlschemaelement = xelement.objSchema;
            else
                xbrlschemaelement = objTaxonomy.getSchemaElement(xbrlcalculationtreenode1.getID());

			String a1=objTaxonomy.getTerseLabelByID(xbrlcalculationtreenode1.getXbrlSchemaElement().getID())+"("+objTaxonomy.getLabelByID(xbrlcalculationtreenode1.getXbrlSchemaElement().getID(),"zh")+")";

/*
if(xelement!=null){
	String a2=objTaxonomy.getTerseLabelByID(xelement.getID())+"("+objTaxonomy.getLabelByID(xelement.getID(),"zh")+")";
	if(!a1.equals(a2)){
//		System.err.println("------ "+xbrlelementpool.objSchema.getPeriodType()+" "+xbrlcontext1);
		System.err.println("-- create "+a1+" ->"+a2);
	}
}
*/

            XbrlCalculationTreeNode xst_node1 = new XbrlCalculationTreeNode(xbrlcalculationtreenode1.getID(), ((XbrlTreeNode) (xbrlcalculationtreenode1)).eData, xelement, xbrlschemaelement);
            xst_node0.appendChild(xst_node1);
            cloneNode(xbrlcalculationtreenode1, xst_node1, hashtable, xbrlcontext, xbrlcontext1);
        }

    }

    protected int validate(XbrlTaxonomy xbrltaxonomy, Element element)
        throws Exception
    {
        iErrorCount = 0;
		StringBuffer sb=new StringBuffer();
        validateElement(sb,0,xbrltaxonomy, (XbrlCalculationTreeNode)getRootNode(), element);
//System.err.println("--sb="+sb);
        return iErrorCount;
    }

    protected BigDecimal validateElement(StringBuffer sb,int level,XbrlTaxonomy a, XbrlCalculationTreeNode xst_node0, Element element)
        throws Exception
    {


		String tab1="";
		for(int i=0;i<level;i++) tab1+="  ";

		BigDecimal a0=new BigDecimal("0"); //未 乘 iWeight 的值
		BigDecimal a1=null;
		String v1="";
        if(xst_node0.objInstanceElement != null){
			v1=xst_node0.objInstanceElement.getValue();
			try{
				a1=new BigDecimal(v1);
				a0=a1;
				a1=a1.multiply(xst_node0.iWeight);
			} catch(NumberFormatException e){
				throw e;
			}
			sb.append(a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"("+v1+"*"+xst_node0.iWeight+")"+"+");
//System.err.println("    "+tab1+a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"("+a.getLabelByID(xst_node0.objSchemaElement.getID(),"zh")+")"+" = "+v1+" * "+xst_node0.iWeight+" = "+a1);
		} else {
			sb.append(a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"(0)"+"+");
//System.err.println("    "+tab1+a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"("+a.getLabelByID(xst_node0.objSchemaElement.getID(),"zh")+")"+" = null");
			// 申報文字檔內無此項目
		}

	
		StringBuffer sb1=new StringBuffer();

        if(xst_node0.hasChild()){
			BigDecimal sum=new BigDecimal("0");

			boolean all_child_value_null=true;
			for(XbrlCalculationTreeNode xst_node1 = (XbrlCalculationTreeNode)xst_node0.getFirstChild(); xst_node1 != null; xst_node1 = (XbrlCalculationTreeNode)xst_node1.getNextSibling())  {
					BigDecimal a2=new BigDecimal("0");
					a2=validateElement(sb1,level+1,a, xst_node1, element);
					if(a2!=null){
						sum=sum.add(a2);
						all_child_value_null=false;
					} else {
					}
			}

	        xst_node0.iCalculated = sum;

			if(xst_node0.objInstanceElement != null ){
				if(a0.compareTo(sum)!=0 && !all_child_value_null) { //不合

//System.err.println("    "+tab1+a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"("+a.getLabelByID(xst_node0.objSchemaElement.getID(),"zh")+") "+a1+" <> "+sum);

					Element element1 = element.getOwnerDocument().createElement("element");
					element.appendChild(element1);
					element1.setAttribute("id", xst_node0.getID());
					element1.setAttribute("value", v1);
					element1.setAttribute("calculation", (new String()).valueOf(sum));
					if(sb1.length()!=0) sb1.setLength(sb1.length()-1);

					element1.setAttribute("formula", a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"("+v1+")="+sb1.toString());
					iErrorCount++;
				}
//if(sb1.length()!=0) System.err.println("--"+a.getTerseLabelByID(xst_node0.objSchemaElement.getID())+"("+v1+")="+sb1.toString());
			}
		} else {
		}



//if(xst_node0.objInstanceElement!=null) System.err.println("--"+tab1+"sum="+sum+"\r\n");


		return a1;
    }

    protected Element exportToXML(Element element)
    {
        return exportToXML((XbrlCalculationTreeNode)getRootNode(), element);
    }

    protected Element exportToXML(XbrlCalculationTreeNode xst_node0, Element element)
    {
        Element element1 = null;
        element1 = element.getOwnerDocument().createElement(xst_node0.getID());
        element.appendChild(element1);
        element1.setAttribute("level", (new String()).valueOf(((XbrlTreeNode) (xst_node0)).iLevel));
        try
        {
            element1.setAttribute("label", objTaxonomy.getLabelByID(xst_node0.getID(), objTaxonomy.getDefaultLanguage()));
        }
        catch(Exception e1) {
			e1.printStackTrace();
		}
        try
        {
            if(xst_node0.objInstanceElement != null)
            {
                element1.setAttribute("value", xst_node0.objInstanceElement.getValue());
                element1.setAttribute("weight", (new String()).valueOf(xst_node0.iWeight));
                element1.setAttribute("calculated", (new String()).valueOf(xst_node0.iCalculated));
            }
        }
        catch(Exception e1) {
			e1.printStackTrace();
		}
        for(XbrlCalculationTreeNode xst_node1 = (XbrlCalculationTreeNode)xst_node0.getFirstChild(); xst_node1 != null; xst_node1 = (XbrlCalculationTreeNode)xst_node1.getNextSibling())
            exportToXML(xst_node1, element1);

        return element1;
    }

    protected void printXML(Element element)
    {
        printXML((XbrlCalculationTreeNode)getRootNode(), element);
    }

    protected void printXML(XbrlCalculationTreeNode xst_node0, Element element)
    {
        String s = "";
        for(int i = 0; i < ((XbrlTreeNode) (xst_node0)).iLevel; i++)
            s = s + "    ";

        Element element1 = element.getOwnerDocument().createElement("element");
        element1.setAttribute("id", xst_node0.getID());
        element1.setAttribute("caption", s);
        element1.setAttribute("order", (new String()).valueOf(((XbrlTreeNode) (xst_node0)).getOrder()));
        if(xst_node0.objSchemaElement.isAbstract())
        {
            element1.setAttribute("value", "----------------");
            element1.setAttribute("type", "abstract");
        } else
        if(xst_node0.objInstanceElement != null)
            try
            {
                String s1 = xst_node0.objInstanceElement.getValue();
                if(s1 != null)
                    element1.setAttribute("value", s1);
            }
            catch(NullPointerException nullpointerexception) { }
        element.appendChild(element1);
        for(XbrlCalculationTreeNode xst_node1 = (XbrlCalculationTreeNode)xst_node0.getFirstChild(); xst_node1 != null; xst_node1 = (XbrlCalculationTreeNode)xst_node1.getNextSibling())
            printXML(xst_node1, element);

    }

    protected XbrlTaxonomy objTaxonomy;
    private int iErrorCount;
}
