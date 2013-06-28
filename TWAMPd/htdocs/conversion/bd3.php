{
 "name": "flare",
 "children": [
<?php
/*
$a=array("40051102"=>array("name"=>array("缺"=>0,"遲"=>0,"事"=>0,"病"=>3)));

var_dump($a);

// Print it out as JSON
echo json_encode($a);
echo "\n";
*/
if (($handle = fopen('bput.csv', 'r')) === false) {
    die('Error opening file');
}
$headers = fgetcsv($handle, 1024, ',');
$complete = array();
$s=array();
while ($r = fgetcsv($handle, 1024, ',')) {
//    $complete[] = array_combine($headers, $row);
// 英文,數學,經濟,國文,生美,品格,國防,文書,專題,程式,會計,軟體,計概
    $rpos=mb_strlen($r[1],"UTF-8")-1;
    $name=mb_substr($r[1],0,1,"UTF-8")."X".mb_substr($r[1],$rpos,1,"UTF-8");
    $name=$r[0];
    $s[]='{"name":"'.$name.'"'.",\n".
    '"children":['."\n".
    ' {"name":"'.'ROE","size":'.$r[1]."},\n".
    ' {"name":"'.'ROA","size":'.$r[2]."}, \n".
    ' {"name":"'.'純益率","size":'.$r[3]."},\n".
    ' {"name":"'.'毛利率","size":'.$r[4]."},\n".
    ' {"name":"'.'流動比率","size":'.$r[5]."},\n".
    ' {"name":"'.'負債比率","size":'.$r[6]."},\n".
    ' {"name":"'.'每股淨值","size":'.$r[7]."},\n".
    ' {"name":"'.'存貨週轉率","size":'.$r[8]."},\n".
    ' {"name":"'.'資本週轉率","size":'.$r[9]."},\n".
    ' {"name":"'.'應收帳款週轉率","size":'.$r[10]."}\n".
      "]\n}";
}

echo implode(",\n",$s);
fclose($handle);
?>
]
}
