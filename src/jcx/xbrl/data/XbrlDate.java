package jcx.xbrl.data;


class XbrlDate{

    public XbrlDate()
    {
//        strDateValueF8 = jcx.util.datetime.getToday("YYYYmmdd");
    }
    public XbrlDate(String date)
    {
        strDateValueF8 = date;
    }

    public void setDate(String s)
    {
        strDateValueF8 = s;
    }

    public String getDate()
    {
        return strDateValueF8;
    }

    String strDateValueF8=null;
}
