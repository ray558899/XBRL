package jcx.xbrl.taxonomy;

import jcx.xbrl.data.*;
import org.w3c.dom.Element;

public class XbrlPresentationTreeNode extends XbrlTreeNode
{

    protected XbrlPresentationTreeNode(String s, Element element, XbrlSchemaElement xbrlschemaelement)
    {
        super(s, element);
        objSchemaElement = null;
        preferredLabel = null;
        arcrole = null;
        objSchemaElement = xbrlschemaelement;
    }

/**
*  ���o �o�� Node �� Schema ����
*
* @return  Schema ����
*
*/

    public XbrlSchemaElement getXbrlSchemaElement() {
        return objSchemaElement;
    }

/**
*  ���o �o�� Node �� PreferredLabelRole
*
* @return  PreferredLabelRole
*
*/

    public String getPreferredLabelRole(){
		return preferredLabel;
	}

/**
*  ���o �o�� Node �� Label
*
* @return  Label(PreferredLabel�u��)
*
*/

    public String getLabel(XbrlTaxonomy t1,String lang){
		String label="";
		try{
			if(preferredLabel==null){
				label=t1.getLabelByID(objSchemaElement.getID(),lang);
			} else {
				label=t1.getLabelByID(objSchemaElement.getID(),lang,preferredLabel);
			}
		}catch(Exception ee){
		}
		return label;
	}

    protected XbrlSchemaElement objSchemaElement;
    protected String preferredLabel;
    protected String arcrole;
}
