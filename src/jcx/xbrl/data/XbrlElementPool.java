package jcx.xbrl.data;

import jcx.xbrl.taxonomy.*;
import java.io.PrintStream;
import java.util.Vector;


public class XbrlElementPool
{

    protected XbrlSchemaElement objSchema;
    protected XbrlTaxonomy objTaxonomy;
    protected Vector objPool;

	public XbrlElementPool() {
        objTaxonomy = null;
        objPool = new Vector();
    }

    public void add(XbrlElement XbrlElement)
    {
        objPool.add(XbrlElement);
    }

    public Vector getPool(){
        return objPool;
    }

    public XbrlElement getLatest()
    {
        XbrlElement XbrlElement = null;
        if(objPool.size() > 0)
            XbrlElement = (XbrlElement)objPool.get(objPool.size() - 1);
        return XbrlElement;
    }

    public XbrlElement get(XbrlContext xbrlcontext)
    {
        XbrlElement XbrlElement = null;
        Vector vector = getCollection(xbrlcontext);
        if(vector.size() > 0)
            XbrlElement = (XbrlElement)vector.get(0);
        return XbrlElement;
    }

    public Vector getCollection(XbrlContext xbrlcontext)
    {
        Vector vector = new Vector();
        for(int i = 0; i < objPool.size(); i++)
        {
            XbrlElement XbrlElement = (XbrlElement)objPool.get(i);
            if(XbrlElement.REFERENCE == xbrlcontext) vector.add(XbrlElement);
        }

        return vector;
    }

/**
	取得底下所有的Element
*/

    public XbrlElement[] getAllItems(){
        Vector vector = new Vector();
        for(int i = 0; i < objPool.size(); i++)
        {
            XbrlElement XbrlElement = (XbrlElement)objPool.get(i);
			vector.add(XbrlElement);
        }

        return (XbrlElement[])vector.toArray(new XbrlElement[0]);
    }


    public void setDecimal(int i)
    {
        for(int j = 0; j < objPool.size(); j++)
        {
            XbrlElement XbrlElement = (XbrlElement)objPool.get(j);
            XbrlElement.setDecimal(i);
        }

    }

    public String toString()
    {
		StringBuffer sb=new StringBuffer();
        for(int i = 0; i < objPool.size(); i++)
        {
            XbrlElement XbrlElement = (XbrlElement)objPool.get(i);
            sb.append("id: "+objSchema.getID()+" Context id: " + XbrlElement.REFERENCE.getID() + "   value: " + XbrlElement.getValue() + "   dimension: " + XbrlElement.REFERENCE.getDimensionID()+"\r\n");
        }
		return sb.toString();

    }

}
