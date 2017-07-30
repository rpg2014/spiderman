package com.rpg2014.spiderman.types;

public class Webpage {
	private static String help="<!DOCTYPE html>\n" + 
			"<html>\n" + 
			"\n" + 
			"\n" + 
			"<head>\n" + 
			"    <title>Spiderman 2.0 Help</title>\n" + 
			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + 
			"    <link rel=\"stylesheet\" href=\"https://netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\">\n" + 
			"</head>\n" + 
			"\n" + 
			"<body>\n" + 
			"    <div class=\"container\">\n" + 
			"        <nav>\n" + 
			"            <h1>How to use Spiderman 2.0</h1>\n" + 
			"        </nav>\n" + 
			"        <section>\n" + 
			"\n" + 
			"            <h2>Command Syntax</h2>\n" + 
			"            <span><i>@spiderman command : arg1 , arg2 ...</i></span>\n" + 
			"            <h5>With Spiderman 2.0 commands and names are no longer case sensitive. They can also contain spaces (eg. \"Parker\n" + 
			"                G\" is the same as \"parker g\")</h5>\n" + 
			"        </section>\n" + 
			"        <section>\n" + 
			"            <h2>List of Commands</h2>\n" + 
			"            <h4>All commands start with \"@spiderman\"</h4>\n" + 
			"            <ul>\n" + 
			"                <li>View\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage: <b>view :<i> name 1, name 2, ...</b></i>\n" + 
			"                        </li>\n" + 
			"                        <li>Example: \"@spiderman view : parker g, jj\"</li>\n" + 
			"                        <li>The name arguments are optional. \"View\" and \"view : all\" display the entire web.</li>\n" + 
			"                        <li>This command displays a picture of the web of the people given.</li>\n" + 
			"\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>Create\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage: <b>create :<i> name 1, name 2, ...</i></b></li>\n" + 
			"                        <li>This command addes people to the web. The new person must then be connected to an existing person\n" + 
			"                            on the map or else they will have to be readded later.</li>\n" + 
			"                    </ul>\n" + 
			"\n" + 
			"                </li>\n" + 
			"                <li>Add\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage <b>add : <i>(person to add connections to) , (new connection 1), (new connection 2), ...</i></b></li>\n" + 
			"                        <li>Example: \"@spiderman add : pat s, mckinze, kaylin, freshman girl 3\".</li>\n" + 
			"                        <li>The example would add connections between Pat and the freshman girls.</li>\n" + 
			"                        <li>People who are not currently on the web <b>have to be created using the create command</b> or else\n" + 
			"                            the command will fail. </li>\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>Remove\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage <b>remove : <i>(person to remove connections from) , (connection 1), (connection 2), ...</i></b></li>\n" + 
			"                        <li>Example: \"@spiderman remove : pat s, mckinze, kaylin, freshman girl 3\".</li>\n" + 
			"                        <li>The example would remove connections between Pat and the freshman girls.</li>\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>List\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage <b>list : <i>name 1, name 2, ...</i></b></li>\n" + 
			"                        <li>Example: \"@spiderman list : parker g, jj\"</li>\n" + 
			"                        <li>The name arguments are optional. \"List\" and \"list : all\" display all the people who are on the web.</li>\n" + 
			"                        <li>This command lists all of the connections the given person has on the web.</li>\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>Path\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage <b>path : <i>start name, finish name</i></b></li>\n" + 
			"                        <li>Example: \"@spiderman path: parker g, jj\"</li>\n" + 
			"                        <li>This command displays the shortest path on the web between two people</li>\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>Size\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage <b>size : <i>name 1, name 2, ...</i></b></li>\n" + 
			"                        <li>Example: \"@spiderman size : parker g, jj\"</li>\n" + 
			"                        <li>The name arguments are optional. \"size\" will display the 3 people with the most connections</li>\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>Quote\n" + 
			"                    <ul>\n" + 
			"                        <li>Usages\n" + 
			"                            <ul>\n" + 
			"                                <li>quote\n" + 
			"                                    <ul>\n" + 
			"                                        <li>This will display a random quote.</li>\n" + 
			"                                    </ul>\n" + 
			"                                </li>\n" + 
			"                                <li>quote : name\n" + 
			"                                    <ul>\n" + 
			"                                        <li>This will display a random quote that was said by the person.</li>\n" + 
			"                                    </ul>\n" + 
			"                                </li>\n" + 
			"                                <li>quote : name , text to search\n" + 
			"                                    <ul>\n" + 
			"                                        <li>This will search for a quote said by the person that contains the given text.</li>\n" + 
			"                                    </ul>\n" + 
			"                                </li>\n" + 
			"                            </ul>\n" + 
			"                        </li>\n" + 
			"\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"                <li>Quote Link\n" + 
			"                    <ul>\n" + 
			"                        <li>Usage <b>quote link</b></li>\n" + 
			"                        <li>This command will display the current link to the quote page.</li>\n" + 
			"                    </ul>\n" + 
			"                </li>\n" + 
			"            </ul>\n" + 
			"\n" + 
			"        </section>\n" + 
			"        <section>\n" + 
			"            <h2>Quote Page</h2>\n" + 
			"            <p><h5>This webpage displays all the quotes in the database.  You can search by the name of the person who said the quote or by the quote itself.  You can also add new quotes using the big green button</h5></p>\n" + 
			"            <p>In order to make it so that random people cannot see the stupid shit that we've said, the link to the page changes everytime a quote is added or removed. The new link can be gotten with the \"quote link\" command.</p>\n" + 
			"        </section>\n" + 
			"    </div>\n" + 
			"    <div class=\"footer\" style=\"margin: auto; text-align: center;font-style: italic; clear: both;\">Spiderman 2.0</div>\n" + 
			"</body>\n" + 
			"\n" + 
			"</html>";
	private static String head="<!DOCTYPE html>\n" + 
			"<html><head>\n" + 
			"    <title>Quotes</title>\n" + 
			"    <meta charset=\"utf-8\">\n" + 
			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + 
			"    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" + 
			"\n" + 
			"\n" + 
			"    <link rel=\"stylesheet\" href=\"https://netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\">\n" + 
			"    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css\">\n" + 
			"    <link rel=\"stylesheet\" href=\"https://cdn.datatables.net/1.10.11/css/dataTables.bootstrap.min.css\">\n" + 
			"    <link rel=\"stylesheet\" href=\"https://ajax.googleapis.com/ajax/libs/angular_material/1.1.0-rc2/angular-material.min.css\">\n" + 
			"    <link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/icon?family=Material+Icons\">\n" + 
			"\n" + 
			"\n" + 
			"    <script>\n" + 
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
			"\n" + 
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
			"    // Get the modal\n" + 
			"\n" + 
			"\n" + 
			"    // When the user clicks anywhere outside of the modal, close it\n" + 
			"    window.onclick = function(event) {\n" + 
			"        var modal = document.getElementById('id01');\n" + 
			"        if (event.target == modal) {\n" + 
			"            modal.style.display = \"none\";\n" + 
			"        }\n" + 
			"    }\n" + 
			"</script>\n" + 
			"    <style>\n" + 
			"\n" + 
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
			"/* Full-width input fields */\n" + 
			"input[type=text], input[type=password] {\n" + 
			"    width: 100%;\n" + 
			"    padding: 12px 20px;\n" + 
			"    margin: 8px 0;\n" + 
			"    display: inline-block;\n" + 
			"    border: 1px solid #ccc;\n" + 
			"    box-sizing: border-box;\n" + 
			"}\n" + 
			"\n" + 
			"/* Set a style for all buttons */\n" + 
			"button {\n" + 
			"    background-color: #4CAF50;\n" + 
			"    color: white;\n" + 
			"    padding: 14px 20px;\n" + 
			"    margin: 8px 0;\n" + 
			"    border: none;\n" + 
			"    cursor: pointer;\n" + 
			"    width: 100%;\n" + 
			"}\n" + 
			"\n" + 
			"button:hover {\n" + 
			"    opacity: 0.8;\n" + 
			"}\n" + 
			".removeBtn{\n" + 
			"	background-color: red;\n" + 
			"	padding: 2px 2px;\n" + 
			"	margin: 2px 0;\n" + 
			"}\n" + 
			"\n" + 
			"/* Extra styles for the cancel button */\n" + 
			".cancelbtn {\n" + 
			"    width: auto;\n" + 
			"    padding: 10px 18px;\n" + 
			"    background-color: #f44336;\n" + 
			"    text-align:center;\n" + 
			"}\n" + 
			"\n" + 
			"\n" + 
			"/* Center the image and position the close button */\n" + 
			".imgcontainer {\n" + 
			"    text-align: center;\n" + 
			"    margin: 24px 0 12px 0;\n" + 
			"    position: relative;\n" + 
			"}\n" + 
			"\n" + 
			"img.avatar {\n" + 
			"    width: 40%;\n" + 
			"    border-radius: 50%;\n" + 
			"}\n" + 
			"\n" + 
			".modal-container {\n" + 
			"    padding: 16px;\n" + 
			"}\n" + 
			".quoteContainer {\n" + 
			"	padding-top: 16px;\n" + 
			"}\n" + 
			".quoteBox{\n" + 
			"	width: 100%;\n" + 
			"}\n" + 
			"\n" + 
			"span.psw {\n" + 
			"    float: right;\n" + 
			"    padding-top: 10px;\n" + 
			"}\n" + 
			"\n" + 
			"/* The Modal (background) */\n" + 
			".modal {\n" + 
			"    display: none; /* Hidden by default */\n" + 
			"    position: fixed; /* Stay in place */\n" + 
			"    z-index: 1; /* Sit on top */\n" + 
			"    left: 0;\n" + 
			"    top: 0;\n" + 
			"    width: 100%; /* Full width */\n" + 
			"    height: 100%; /* Full height */\n" + 
			"    overflow: auto; /* Enable scroll if needed */\n" + 
			"    background-color: rgb(0,0,0); /* Fallback color */\n" + 
			"    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */\n" + 
			"    padding-top: 60px;\n" + 
			"}\n" + 
			"\n" + 
			"/* Modal Content/Box */\n" + 
			".modal-content {\n" + 
			"    background-color: #fefefe;\n" + 
			"    margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */\n" + 
			"    border: 1px solid #888;\n" + 
			"    width: 80%; /* Could be more or less, depending on screen size */\n" + 
			"}\n" + 
			"\n" + 
			"/* The Close Button (x) */\n" + 
			".close {\n" + 
			"    position: absolute;\n" + 
			"    right: 25px;\n" + 
			"    top: 0;\n" + 
			"    color: #000;\n" + 
			"    font-size: 35px;\n" + 
			"    font-weight: bold;\n" + 
			"}\n" + 
			"\n" + 
			".close:hover,\n" + 
			".close:focus {\n" + 
			"    color: red;\n" + 
			"    cursor: pointer;\n" + 
			"}\n" + 
			"\n" + 
			"/* Add Zoom Animation */\n" + 
			".animate {\n" + 
			"    -webkit-animation: animatezoom 0.6s;\n" + 
			"    animation: animatezoom 0.6s\n" + 
			"}\n" + 
			"\n" + 
			"@-webkit-keyframes animatezoom {\n" + 
			"    from {-webkit-transform: scale(0)}\n" + 
			"    to {-webkit-transform: scale(1)}\n" + 
			"}\n" + 
			"\n" + 
			"@keyframes animatezoom {\n" + 
			"    from {transform: scale(0)}\n" + 
			"    to {transform: scale(1)}\n" + 
			"}\n" + 
			"\n" + 
			"/* Change styles for span and cancel button on extra small screens */\n" + 
			"@media screen and (max-width: 500px) {\n" + 
			"    span.psw {\n" + 
			"       display: block;\n" + 
			"       float: none;\n" + 
			"    }\n" + 
			"    .cancelbtn {\n" + 
			"       width: 100%;\n" + 
			"    }\n" + 
			"}</style>\n" + 
			"\n" + 
			"</head>";
	
	private static String tableRow = "<tr>\n" + 
			"							<td valign=\"top\" type=\"text\">{0}</td>\n" + 
			"							<td valign=\"top\" type=\"text\">{1}</td>\n" + 
			"						</tr>";
	private static String body1 = "<body>\n" + 
			"<div class=\"container\">\n" + 
			"\n" + 
			"    <div class=\"row\">\n" + 
			"        <h2>Quotes</h2><button onclick=\"document.getElementById('id01').style.display='block'\" style=\"width:auto;\">Submit Quote</button>\n" + 
			"        <form class=\"form-inline\">\n" + 
			"            <div class=\"form-group\">\n" + 
			"                <input id=\"authorSearch\" onkeyup=\"searchAuthor()\" placeholder=\"Search for person..\" title=\"Type in a name\" type=\"text\">\n" + 
			"            </div>\n" + 
			"            <div class=\"form-group\">\n" + 
			"                <input id=\"quoteSearch\" onkeyup=\"searchQuote()\" placeholder=\"Search for quote..\" title=\"Type in a word\" type=\"text\">\n" + 
			"            </div>\n" + 
			"        </form>\n" + 
			"        <div class=\"table-responsive\">\n" + 
			"            <table id=\"quotes\" class=\"table-responsive\">\n" + 
			"                <thead>\n" + 
			"                <tr class=\"header\">\n" + 
			"                    <th style=\"width: 15%;\">Name</th>\n" + 
			"                    <th style=\"width: 85%;\">Quote</th>\n" + 
			"                </tr>\n" + 
			"                </thead>\n" + 
			"                <tbody>";
	
	private static String body2 = "</tbody>\n" + 
			"            </table>\n" + 
			"        </div>\n" + 
			"    </div>\n" + 
			"\n" + 
			"\n" + 
			"	<button class=\"removeBtn\" onclick=\"document.getElementById('removeQuote').style.display='block'\" style=\"width:auto;\">Remove Quote</button>\n" + 
			"</div>\n" + 
			"<div class=\"footer\" style=\"font-style: italic; clear: both;\">Spiderman 2.0</div>\n" + 
			"\n" + 
			"<div id=\"id01\" class=\"modal\" style=\"display: none;\">\n" + 
			"    <form class=\"modal-content animate\" action=\"https://obscure-inlet-41841.herokuapp.com/quotes\" method=\"post\">\n" + 
			"        <div class=\"imgcontainer\">\n" + 
			"            <span onclick=\"document.getElementById('id01').style.display='none'\" class=\"close\" title=\"Close Modal\">×</span>\n" + 
			"\n" + 
			"        </div>\n" + 
			"\n" + 
			"        <div class=\"modal-container\">\n" + 
			"            <h2>Quote Submission</h2>\n" + 
			"            <label><b>Person</b></label>\n" + 
			"            <input placeholder=\"Enter Name\" name=\"name\" required=\"\" type=\"text\">\n" + 
			"\n" + 
			"            <label><b>Quote</b></label>\n" + 
			"            <div class=\"quoteContainer\">\n" + 
			"                <textarea class=\"quoteBox\" type=\"text\" placeholder=\"Enter quote\" name=\"quote\" rows=\"4\" required=\"\"></textarea>\n" + 
			"            </div>\n" + 
			"\n" + 
			"            <button type=\"submit\">Submit</button>\n" + 
			"\n" + 
			"        </div>\n" + 
			"\n" + 
			"        <div class=\"modal-container\" style=\"background-color:#f1f1f1\">\n" + 
			"            <button type=\"button\" onclick=\"document.getElementById('id01').style.display='none'\" class=\"cancelbtn\">Cancel</button>\n" + 
			"\n" + 
			"        </div>\n" + 
			"    </form>\n" + 
			"</div>\n" + 
			"<div id=\"removeQuote\" class=\"modal\" style=\"display: none;\">\n" + 
			"    <form class=\"modal-content animate\" action=\"https://obscure-inlet-41841.herokuapp.com/quotes\" method=\"post\">\n" + 
			"        <div class=\"imgcontainer\">\n" + 
			"            <span onclick=\"document.getElementById('removeQuote').style.display='none'\" class=\"close\" title=\"Close Modal\">×</span>\n" + 
			"\n" + 
			"        </div>\n" + 
			"\n" + 
			"        <div class=\"modal-container\">\n" + 
			"            <h2>Quote Removal - Talk to Parker if you want to remove a quote</h2>\n" + 
			"            <label><b>Person</b></label>\n" + 
			"            <input placeholder=\"Enter Name\" name=\"name\" required=\"\" type=\"text\">\n" + 
			"\n" + 
			"            <label><b>Quote to remove (exact match)</b></label>\n" + 
			"            <div class=\"quoteContainer\">\n" + 
			"                <textarea class=\"quoteBox\" type=\"text\" placeholder=\"Enter quote\" name=\"quoteToRemove\" rows=\"4\" required=\"\"></textarea>\n" + 
			"            </div>\n" + 
			"			<label><b>Password </b></label>\n" + 
			"            <input placeholder=\"Enter Password\" name=\"pss\" required=\"\" type=\"password\">\n" + 
			"            \n" + 
			"            \n" + 
			"            <button type=\"submit\">Remove</button>\n" + 
			"\n" + 
			"        </div>\n" + 
			"\n" + 
			"        <div class=\"modal-container\" style=\"background-color:#f1f1f1\">\n" + 
			"            <button type=\"button\" onclick=\"document.getElementById('removeQuote').style.display='none'\" class=\"cancelbtn\">Cancel</button>\n" + 
			"\n" + 
			"        </div>\n" + 
			"    </form>\n" + 
			"</div>\n" + 
			"</body></html>\n" + 
			"";
	
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
	public static String getBody1() {
		return body1;
	}
	public static String getBody2() {
		return body2;
	}
	public static String getTableRow() {
		return tableRow;
	}
	public static String getError() {
		return error;
	}
	public static String getHelp() {
		return help;
	}
}
