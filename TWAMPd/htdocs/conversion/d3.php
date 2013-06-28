{
 "name": "flare",
 "children": [
<?php
header("Content-Type:text/html; charset=utf-8");
if (($handle = fopen('input.csv', 'r')) === false) {
    die('Error opening file');
}
$headers = fgetcsv($handle, 1024, ',');
$complete = array();
$s=array();
while ($r = fgetcsv($handle, 1024, ',')) {
    $rpos=mb_strlen($r[1],"UTF-8")-1;
    $name=mb_substr($r[1],0,1,"UTF-8")."X".mb_substr($r[1],$rpos,1,"UTF-8");
    $name=$r[0];
    $s[]='{"name":"'.$name.'"'.",\n".
    '"children":['."\n".
    ' {"name":"'.$name.'","size":'.$r[1]."} \n".
     "]\n}";
}

echo implode(",\n",$s);
fclose($handle);
?>
]
}
