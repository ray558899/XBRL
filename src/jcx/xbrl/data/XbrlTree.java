
package jcx.xbrl.data;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;


public class XbrlTree
{

    public XbrlTree()
    {
        objNodes = null;
        selectedNode = null;
        hNodeTable = new Hashtable();
    }

/**
*  取得樹的根節點
*
* @return  XbrlTreeNode
*
*/
    public XbrlTreeNode getRootNode()
    {
        return objNodes;
    }

/**
*  取得樹的節點數
*
* @return  int 
*
*/
    public int size()
    {
        return hNodeTable.size();
    }

/**
*  以ID取得樹的節點數
*
* @param   id node id
* @return  XbrlTreeNode 
*
*/
    public XbrlTreeNode getNodeByID(String id)
    {
		if(id==null) return null;
        XbrlTreeNode xbrltreenode = null;
        xbrltreenode = (XbrlTreeNode)hNodeTable.get(id);
        return xbrltreenode;
    }

    protected void selectNode(String id)
    {
        XbrlTreeNode xbrltreenode = getNodeByID(id);
        if(xbrltreenode != null)
        {
            xbrltreenode.isSelected = 1;
            if(selectedNode != null)
                selectedNode.isSelected = 0;
            selectedNode = xbrltreenode;
        }
    }

    protected void expand()
    {
        for(Enumeration enumeration = hNodeTable.keys(); enumeration.hasMoreElements();)
        {
            XbrlTreeNode xbrltreenode = (XbrlTreeNode)hNodeTable.get(enumeration.nextElement());
            if(xbrltreenode.hasChild())
                xbrltreenode.isOpened = 1;
        }

    }

    public void calculateLevel(XbrlTreeNode xbrltreenode, int i)
    {
		if(xbrltreenode==null) return;

        xbrltreenode.iLevel = i;
        for(XbrlTreeNode xbrltreenode1 = xbrltreenode.getFirstChild(); xbrltreenode1 != null; xbrltreenode1 = xbrltreenode1.getNextSibling())
            calculateLevel(xbrltreenode1, xbrltreenode.iLevel + 1);

    }

    protected void print(XbrlTreeNode xbrltreenode)
    {
        for(int i = 0; i < xbrltreenode.iLevel; i++)
            System.out.print(",");

        System.out.print(xbrltreenode.getID());
        System.out.println("");
        for(XbrlTreeNode xbrltreenode1 = xbrltreenode.getFirstChild(); xbrltreenode1 != null; xbrltreenode1 = xbrltreenode1.getNextSibling())
            print(xbrltreenode1);

    }

    protected void print()
    {
        print(objNodes);
    }
	protected Hashtable getNodeTable(){
		return hNodeTable;
	}
	public XbrlTreeNode getNode(){
		return objNodes;
	}

	protected void setNode(XbrlTreeNode node1){
		objNodes=node1;
	}

    private XbrlTreeNode objNodes;
    protected XbrlTreeNode selectedNode;
    private Hashtable hNodeTable;
}
