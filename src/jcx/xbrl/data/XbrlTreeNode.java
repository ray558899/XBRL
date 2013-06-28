package jcx.xbrl.data;

import org.w3c.dom.Element;

public class XbrlTreeNode
{

    protected String strDefID;
    protected Element eData;
    protected int iNodeType;
    protected XbrlTreeNode PARENT=null;
    protected XbrlTreeNode FIRSTCHILD=null;
    protected XbrlTreeNode LASTCHILD=null;
    protected XbrlTreeNode PREVIOUS=null;
    protected XbrlTreeNode NEXT=null;
    protected int isSelected=0;
    protected int isOpened=0;
    protected int iCount=0;
    protected int iLevel=0;
    private double dOrder;

	public XbrlTreeNode(String s, Element element)
    {
        strDefID = s;
        eData = element;
    }
	public void setOrder(double d1){
		dOrder=d1;
	}

	public double getOrder(){
		return dOrder;
	}

    public String getID()
    {
        return strDefID;
    }

    public XbrlTreeNode getFirstChild()
    {
        return FIRSTCHILD;
    }

    public XbrlTreeNode getNextSibling()
    {
        return NEXT;
    }

    public boolean hasChild()
    {
        boolean flag;
        if(FIRSTCHILD != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public XbrlTreeNode appendChild(XbrlTreeNode xbrltreenode)
    {
        xbrltreenode.PARENT = this;
        XbrlTreeNode xbrltreenode1 = getChildByOrder(xbrltreenode.dOrder);
        if(xbrltreenode1 == null)
        {
            if(FIRSTCHILD != null)
            {
                FIRSTCHILD.PREVIOUS = xbrltreenode;
                xbrltreenode.NEXT = FIRSTCHILD;
                FIRSTCHILD = xbrltreenode;
            } else
            {
                LASTCHILD = xbrltreenode;
                FIRSTCHILD = xbrltreenode;
            }
        } else
        {
            xbrltreenode.NEXT = xbrltreenode1.NEXT;
            xbrltreenode1.NEXT = xbrltreenode;
            xbrltreenode.PREVIOUS = xbrltreenode1;
        }
        return xbrltreenode;
    }

    protected XbrlTreeNode getChildByOrder(double d)
    {
        Object obj = null;
        XbrlTreeNode xbrltreenode1 = null;
        boolean flag = false;
        for(XbrlTreeNode xbrltreenode = getFirstChild(); xbrltreenode != null && !flag;)
        {
            double d1;
            if(xbrltreenode1 == null)
                d1 = 0.0D;
            else
                d1 = xbrltreenode1.dOrder;
            if(d > d1 && d < xbrltreenode.dOrder)
            {
                flag = true;
            } else
            {
                xbrltreenode1 = xbrltreenode;
                xbrltreenode = xbrltreenode.getNextSibling();
            }
        }

        return xbrltreenode1;
    }

}
