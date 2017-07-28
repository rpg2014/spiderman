package com.rpg2014.spiderman.types;

public class Webpage {
	private static String head="<!DOCTYPE html>\n" + 
			"\n" + 
			"<html>\n" + 
			"<head>\n" + 
			"<title>Quotes</title>\n" + 
			"<meta charset=\"utf-8\" />\n" + 
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + 
			"<script\n" + 
			"	src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" + 
			"\n" + 
			"\n" + 
			"<link rel=\"stylesheet\"\n" + 
			"	href=\"https://netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\">\n" + 
			"<link rel=\"stylesheet\"\n" + 
			"	href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css\" />\n" + 
			"<link rel=\"stylesheet\"\n" + 
			"	href=\"https://cdn.datatables.net/1.10.11/css/dataTables.bootstrap.min.css\" />\n" + 
			"<link rel=\"stylesheet\"\n" + 
			"	href=\"https://ajax.googleapis.com/ajax/libs/angular_material/1.1.0-rc2/angular-material.min.css\">\n" + 
			"<link rel=\"stylesheet\"\n" + 
			"	href=\"https://fonts.googleapis.com/icon?family=Material+Icons\">\n" + 
			"\n" + 
			"\n" + 
			"<script>\n" + 
			"	function searchAuthor() {\n" + 
			"		var input, filter, table, tr, td, i;\n" + 
			"		input = document.getElementById(\"authorSearch\");\n" + 
			"		filter = input.value.toUpperCase();\n" + 
			"		table = document.getElementById(\"quotes\");\n" + 
			"		tr = table.getElementsByTagName(\"tr\");\n" + 
			"		for (i = 0; i < tr.length; i++) {\n" + 
			"			td = tr[i].getElementsByTagName(\"td\")[0];\n" + 
			"			if (td) {\n" + 
			"				if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {\n" + 
			"					tr[i].style.display = \"\";\n" + 
			"				} else {\n" + 
			"					tr[i].style.display = \"none\";\n" + 
			"				}\n" + 
			"			}\n" + 
			"		}\n" + 
			"	}\n" + 
			"	\n" + 
			"	function searchQuote() {\n" + 
			"		var input, filter, table, tr, td, i;\n" + 
			"		input = document.getElementById(\"quoteSearch\");\n" + 
			"		filter = input.value.toUpperCase();\n" + 
			"		table = document.getElementById(\"quotes\");\n" + 
			"		tr = table.getElementsByTagName(\"tr\");\n" + 
			"		for (i = 0; i < tr.length; i++) {\n" + 
			"			td = tr[i].getElementsByTagName(\"td\")[1];\n" + 
			"			if (td) {\n" + 
			"				if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {\n" + 
			"					tr[i].style.display = \"\";\n" + 
			"				} else {\n" + 
			"					tr[i].style.display = \"none\";\n" + 
			"				}\n" + 
			"			}\n" + 
			"		}\n" + 
			"	}\n" + 
			"</script>\n" + 
			"<style>\n" + 
			"tr.header {\n" + 
			"	cursor: pointer;\n" + 
			"}\n" + 
			"\n" + 
			"/* ---------------------- Content ---------------------- */\n" + 
			"[id^=quotes] {\n" + 
			"	font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n" + 
			"	border-collapse: collapse;\n" + 
			"	width: 100%;\n" + 
			"}\n" + 
			"\n" + 
			"[id^=quotes] td, [id^=quotes] th {\n" + 
			"	border: 1px solid #ddd;\n" + 
			"	padding: 8px;\n" + 
			"}\n" + 
			"\n" + 
			"[id^=quotes] tr:nth-child(even) {\n" + 
			"	background-color: #a9cce3;\n" + 
			"}\n" + 
			"\n" + 
			"[id^=quotes] tr:hover {\n" + 
			"	background-color: #e3c0a9;\n" + 
			"}\n" + 
			"\n" + 
			"[id^=quotes] th {\n" + 
			"	padding-top: 12px;\n" + 
			"	padding-bottom: 12px;\n" + 
			"	text-align: left;\n" + 
			"	background-color: #1b4f72;\n" + 
			"	color: white;\n" + 
			"	background-repeat: no-repeat;\n" + 
			"	background-position: right center;\n" + 
			"}\n" + 
			"\n" + 
			".footer {\n" + 
			"	margin: auto;\n" + 
			"	text-align: center;\n" + 
			"}\n" + 
			"\n" + 
			"table {\n" + 
			"	border-spacing: 5px;\n" + 
			"}\n" + 
			"\n" + 
			"#authorSearch {\n" + 
			"	font-size: 12px;\n" + 
			"	padding: 5px 40px 5px 10px;\n" + 
			"	border: 1px solid #ddd;\n" + 
			"	margin-bottom: 5px;\n" + 
			"	-webkit-transition: width 0.4s ease-in-out;\n" + 
			"	transition: width 0.4s ease-in-out;\n" + 
			"	float: left;\n" + 
			"}\n" + 
			"\n" + 
			"#authorSearch:focus {\n" + 
			"	border: 5px solid #ddd\n" + 
			"}\n" + 
			"\n" + 
			"#quoteSearch {\n" + 
			"	font-size: 12px;\n" + 
			"	padding: 5px 15px 5px 10px;\n" + 
			"	border: 1px solid #ddd;\n" + 
			"	margin-bottom: 5px;\n" + 
			"	-webkit-transition: width 0.4s ease-in-out;\n" + 
			"	transition: width 0.4s ease-in-out;\n" + 
			"	float: left;\n" + 
			"}\n" + 
			"\n" + 
			"#quoteSearch:focus {\n" + 
			"	border: 5px solid #ddd\n" + 
			"}\n" + 
			"\n" + 
			"@\n" + 
			"-moz-document url-prefix () {fieldset {\n" + 
			"	display: table-cell;\n" + 
			"}\n" + 
			"}\n" + 
			"</style>\n" + 
			"\n" + 
			"</head>";
	
	private static String tableRow = "<tr>\n" + 
			"							<td valign=\"top\">{0}</td>\n" + 
			"							<td valign=\"top\">{1}</td>\n" + 
			"						</tr>";
	private static String body = "<body>\n" + 
			"	<div class=\"container\">\n" + 
			"\n" + 
			"		<div class=\"row\">\n" + 
			"			<h2>Quotes</h2>\n" + 
			"			<form class=\"form-inline\">\n" + 
			"				<div class=\"form-group\">\n" + 
			"					<input type=\"text\" id=\"authorSearch\" onkeyup=\"searchAuthor()\"\n" + 
			"						placeholder=\"Search for person..\" title=\"Type in a name\">\n" + 
			"				</div>\n" + 
			"				<div class=\"form-group\">\n" + 
			"					<input type=\"text\" id=\"quoteSearch\" onkeyup=\"searchQuote()\"\n" + 
			"						placeholder=\"Search for quote..\" title=\"Type in a word\">\n" + 
			"				</div>\n" + 
			"			</form>\n" + 
			"			<div class=\"table-responsive\">\n" + 
			"				<table id=\"quotes\" class=\"table\">\n" + 
			"					<thead>\n" + 
			"						<tr class=\"header\">\n" + 
			"							<th style=\"width: 15%;\">Name</th>\n" + 
			"							<th style=\"width: 85%;\">Quote</th>\n" + 
			"						</tr>\n" + 
			"					</thead>\n" + 
			"					<tbody>\n" + 
			"						{0}\n" + 
			"					</tbody>\n" + 
			"				</table>\n" + 
			"			</div>\n" + 
			"		</div>\n" + 
			"\n" + 
			"\n" + 
			"		\n" + 
			"	</div>\n" + 
			"	<div class=\"footer\" style=\"font-style: italic; clear: both;\">Spiderman 2.0</div>\n" + 
			"</body>\n" + 
			"</html>";
	
	private static String error = "<!DOCTYPE html>\n" + 
			"\n" + 
			"<html>\n" + 
			"    <body>\n" + 
			"        <h2>\n" + 
			"            Please get a new link from the groupme\n" + 
			"        </h2>\n" + 
			"    </body>\n" + 
			"</html>";
	
	public static String getHead() {
		return head;
	}
	public static String getBody() {
		return body;
	}
	public static String getTableRow() {
		return tableRow;
	}
	public static String getError() {
		return error;
	}
}
