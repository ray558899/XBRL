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
*  取得 這個 Node 的 Schema 物件
*
* @return  Schema 物件
*
*/

    public XbrlSchemaElement getXbrlSchemaElement() {
        return objSchemaElement;
    }

/**
*  取得 這個 Node 的 PreferredLabelRole
*
* @return  PreferredLabelRole
*
*/

    public String getPreferredLabelRole(){
		return preferredLabel;
	}

/**
*  取得 這個 Node 的 Label
*
* @return  Label(PreferredLabel優先)
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
