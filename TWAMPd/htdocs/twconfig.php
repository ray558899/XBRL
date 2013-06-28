<?php
/*--------------------------------------------------------------------
 * TWAMP Web Configurator
 *
 * @package    TWAMP Web Configurator
 * @copyright  Copyright (c) 2009-2012 TWAMP Group (http://orz99.com/twamp)
 * @license    GPL licenses
 * @version    $Id$
 *
--------------------------------------------------------------------*/
if ( $_GET['q'] == 'phpinfo' )
{
	phpinfo();
	exit();

}


$str_path_twamp = substr($_SERVER["DOCUMENT_ROOT"], 0, strrpos($_SERVER["DOCUMENT_ROOT"], '/'));
$str_path_httpd_conf = $str_path_twamp. '/ap/conf/httpd.conf';
$arr_php_ver = array('52'=>'#', '53'=>'#', '54'=>'#');

if ($_POST['ver']) {
	foreach ($arr_php_ver as $k => $v) {
  	if ( $k == $_POST['ver'] ) {
    	$arr_php_ver[$k] = '';
  	}
	}
}

if ($_GET['ver']) {
	$str_ver_setup_hint = "<div id=\"change_version_hint\">PHP Version is changed, Please <strong>Restart TWAMP</strong>!!</div>";
}
$arr_httpd_conf = file($str_path_httpd_conf);
$str_httpd_conf_new = '';
foreach ( $arr_httpd_conf as $v ) {
  if (preg_match("/^(#|)Include php52\/php.conf/i", $v)) {
    if (preg_match("/^Include php52\/php.conf/i", $v)) {
      $php[52] = 'checked';
    }
    $str_httpd_conf_new .= "{$arr_php_ver['52']}Include php52/php.conf\n";

  } elseif (preg_match("/^(#|)Include php53\/php.conf/i", $v)) {
    if (preg_match("/^Include php53\/php.conf/i", $v)) {
      $php[53] = 'checked';
    }
    $str_httpd_conf_new .= "{$arr_php_ver['53']}Include php53/php.conf\n";

  } elseif (preg_match("/^(#|)Include php54\/php.conf/i", $v)) {
    if (preg_match("/^Include php54\/php.conf/i", $v)) {
      $php[54] = 'checked';
    }
    $str_httpd_conf_new .= "{$arr_php_ver['54']}Include php54/php.conf\n";

  } else {
    $str_httpd_conf_new .= $v;
  }

}

$str_ver_setup = "<div id=\"change_version\"><form action=\"{$_SERVER['REQUEST_URI']}\" method=\"post\"><input type=\"radio\" name=\"ver\" value=\"52\" {$php['52']}> PHP 5.2.17 <input type=\"radio\" name=\"ver\" value=\"53\" {$php['53']}> PHP 5.3.16 <input type=\"radio\" name=\"ver\" value=\"54\" {$php['54']}> PHP 5.4.6 <input type=\"submit\" value=\" 　Switch　\" id=\"submit_button\"></form></div>";

if ( in_array($_POST['ver'], array('52','53','54')) ) {
  $fp = fopen($str_path_httpd_conf,'w');
  fputs($fp, $str_httpd_conf_new);
  fclose($fp);

  header('Location: http://'. $_SERVER["HTTP_HOST"]. '?ver='. $_POST['ver']);
}

$twamp_title = 'TWAMP v7.15.1-BeYourSelf';
$twamp_ver = '7.15.1';
$twamp_ver_nick = '做自己的主人';
$twamp_ver_nick_desc = '每天都在發生的家暴事件，報案的比率卻很低，原因在於很多人還是搞不清楚組成家庭的夫妻是合作關係而非從屬關係，沒有誰一定是屬於誰(的財產)，逆來順受的委屈自己並不能顧全大局，自我個體的尊重也無法光靠法律保障，唯有自己才是自己的主人的自覺並勇於爭取，才能贏得平等與尊重。國家對國家之間也是一樣，正所謂人必自侮而後人侮之，是也。';
$drupal6_path = 'drupal-6.26';
$drupal7_path = 'drupal-7.15';

if ( !$_GET['d7']) {
  define('DRUPAL_ROOT', getcwd(). "/{$drupal7_path}");
  require_once DRUPAL_ROOT . '/includes/bootstrap.inc';
  drupal_bootstrap( DRUPAL_BOOTSTRAP_CONFIGURATION );
  $drupa7_databas = $GLOBALS['databases']['default']['default'];
  $conn = mysql_connect ( $drupa7_databas[host], $drupa7_databas[username], $drupa7_databas[password] );

  if (!$conn) {
    die('Could not connect : ' . mysql_error($conn));
  }
  mysql_select_db($drupa7_databas['database']);
  $query  = "SELECT value FROM ". $drupa7_databas['prefix']. "variable WHERE name = 'install_task'";
  $result = mysql_query($query);
  mysql_close( $conn );

  if ($result) {
    $d7 = 'Using';

  } else {
    $d7 = 'Install';
  }
  header("Location: ". $_SERVER['REQUEST_URI']. "&d7={$d7}");  exit();

} elseif ( !$_GET['d6'] ) {
  chdir('./'. $drupal6_path);
  include_once './includes/bootstrap.inc';

  drupal_bootstrap( DRUPAL_BOOTSTRAP_DATABASE );

  $drupal6_creatable = @db_result(db_query("SELECT value FROM {variable} WHERE name = '%s'", 'install_task'));
  if (empty($drupal6_creatable))
  {
  	$d6 = 'Install';

  }
  else
  {
  	$d6 = 'Using';

  }
  header("Location: ". $_SERVER['REQUEST_URI']. "&d6={$d6}");exit();
}

//*******************************************************
// print array content
//*******************************************************
function printr( $object ) {
  if ( is_array( $object ) ) {
    print( '<pre>' );
    print_r( $object );
    print( '</pre>' );
  } elseif ( is_string($object) || is_int($object) ) {

    print( '<h3>' );
    print_r( $object );
    print( '</h3>' );
  } else {
    var_dump( $object );
  }
  flush();
}

function ieversion() {
  $match=preg_match('/MSIE ([0-9]\.[0-9])/',$_SERVER['HTTP_USER_AGENT'],$reg);
  if($match==0)
    return -1;
  else
    return floatval($reg[1]);
}


if ($_GET['d7'] == 'Install') {
  $url_drupal7 = 'install.php';
  } else {
  $url_drupal7 = '';
}

if ($_GET['d6'] == 'Install') {
  $url_drupal6 = 'install.php';

} else {
  $url_drupal6 = '';
}

//$ieversion = ieversion();
$str_drupal6 = "{$_GET['d6']} {$drupal6_path}";
$str_drupal7 = "{$_GET['d7']} {$drupal7_path}";
$str_phpmyadmin = "phpMyAdmin (root)";
$str_phpinfo = "phpinfo with SSL";
$str_perl = "perl CGI with SSL";
?><html>
<head><link href="data:image/x-icon;base64,AAABAAEAEBAQAAAAAAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAgAAAAAAAAAAAAAAAEAAAAAAAAACW3iEA4HoLAPq/fwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIRERERERERIRERABEREREREREAEREREREREAABEREREREAAAAREREREQAAABERERERAAAAAREREREAAAABERERERAAAAEREREREAAAARERERERAAAAEREREREQAAAREREREREAAAERERERERAAAREREREREQARERIRERERERERIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><?php echo $twamp_title; ?></title>
<script type="text/javascript">
</script>

<style type="text/css">
body {
	margin: 0;
	padding: 0;
	text-align: center;
}

body, td, th, h1, h2 {
  font-family: "Microsoft JhengHei", 微軟正黑體, "Microsoft Yahei", 微软雅黑, "Apple LiGothic Medium", STHeiti, "Comic Sans MS", sans-serif !important; }

.oneCol #container {
	width: 930px;
	background: #618CD5;
	margin: 0 auto;
	border: 1px solid #000000;
	text-align: left; }

img {
	border: 0px;
}

#header a, {
  background: none !important;
  color: #959690;
}

#headerContent a {
  background: none !important;
  color: #C0CCC0 !important;
}
#headerContent {
  text-align: center;
  margin-left: auto; margin-right: auto;
  border: 0px;
}

#header h1, #main h1 {
  background: #1250AB;
  color: #959690;
  padding: 0 10px;
}

#header h1 a:link {
  background: #1250AB;
  color: #95E600;
}

#header h1 a:visited {
  background: #1250AB;
  color: #95E600;
}

#header h1 a:hover {
  background: #1250AB;
  color: #EEEE00;
}

#header h1 a:active {
  background: #1250AB;
  color: #9596CC;
}

.oneCol #footer { 
	padding: 0 10px;
	background:#AABBCC;
	font-size: 12px;
}

.oneCol #footer p {
	margin: 0;
	padding: 10px 0;
}

#ploy {
  text-align:center;
  margin-bottom: 25px;
}

#lyrics {
	text-align: center;
	margin-bottom: 25px;
}

#lyrics img {
		float: none !important;
}

.classname {
  float: left;
  border:solid 1px #2d2d2d;
  text-align:center;
  background:#575757;
  margin-left: 5px;
  padding:5px 20px 5px 20px;
  -moz-border-radius: 5px;
  -webkit-border-radius: 5px;
  border-radius: 5px;
}

.classname a:link {
  color: #EEEE00 !important;
}

#change_version {
	text-align:center;
}

#submit_button {
	position: relative;
	top: -2px; left: 10px;
	font-family: arial; font-weight: 700;

}

#change_version_hint {
	text-align: center;
	margin-left: auto; margin-right: auto; margin-bottom: 25px;
	background: #E0E000;
}


/* =Your Generated css 
|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
.classname{-moz-box-shadow:5px 5px 5px #000000;-webkit-box-shadow:5px 5px 5px #000000;box-shadow:5px 5px 5px #000000;}
/* End of Your Generated css 
|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/

.clr {
  clear: both;
}

</style>

</head>
<body class="oneCol">
	<div id="container">
	  <div id="header">
			<h1><a href="/index.php" class="header_title pointer" title="<?php print $twamp_ver_nick_desc; ?>">TWAMP v<?php print $twamp_ver; ?> </a> <?php print $twamp_ver_nick; ?> 版 </h1>
	  </div><!-- end #header -->
	  <div id="headerContent">

					<div class="classname"><a href="http://<?php print $_SERVER["HTTP_HOST"]; ?>" title="Home" class="pointer">
							Home
					</a></div>
					<div class="classname"><a href="./<?php print $drupal7_path; ?>/<?php print $url_drupal7; ?>" title="<?php print $_GET['d7']. " {$drupal7_path}"; ?>" class="pointer">
							<?php print $str_drupal7; ?>
						</a></div>
					<div class="classname"><a href="./<?php print $drupal6_path; ?>/<?php print $url_drupal6; ?>" title="<?php print $_GET['d6']. " {$drupal6_path}"; ?>" class="pointer">
							<?php print $str_drupal6; ?>
					</a></div>

					<div class="classname"><a href="./phpMyAdmin/" title="login as root, no password" class="pointer">
							<?php print $str_phpmyadmin; ?>
					</a></div>
					<div class="classname"><a href="https://<?php print $_SERVER["HTTP_HOST"]. $_SERVER['REQUEST_URI']; ?>&amp;q=phpinfo" title="phpinfo" class="pointer">
							<?php print $str_phpinfo; ?>
					</a></div>

	  </div>
	  <div class="clr">&nbsp;</div>
	  <div id="main">
	  	<h1 class="main_title">phpinfo</h1>
	  </div>
		<div id="mainContent">
<?php
	print $str_ver_setup;
	print $str_ver_setup_hint;
	phpinfo();
	//$pinfo = ob_get_contents();
	//ob_end_clean();
	//$pinfo = preg_replace( '%^.*<body>(.*)</body>.*$%ms','$1',$pinfo);
	//echo $pinfo;
?>

		</div><!-- end #mainContent -->
		<div id="ploy"></div>
		<div id="lyrics"><a href="http://camp.drupaltaiwan.org/2012/feedback/session?nid=197"><img src="/phpMyAdmin/_upload/drupalcamp2012.png" /></a></div>
	  <div id="footer">
			<p>Powered by TWAMP v7 &copy;2011-2012 by orz99.com. All Rights Reserved.</p>
	  </div><!-- end #footer -->
	</div><!-- end #container -->
</body></html>