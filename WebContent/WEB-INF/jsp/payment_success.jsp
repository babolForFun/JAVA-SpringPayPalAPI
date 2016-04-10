<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<html>
	<meta http-equiv="refresh" content="5;http://[SERVER_IP]:[PORT]/SpringBaseLayout/" />
	<jsp:include page="/WEB-INF/jsp/header.jsp"/>

	<body>
    	<div class="jumbotron vertical-center">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-12 text-center">
						<!-- PayPal Logo --><br>
						<table border="0" cellpadding="10" cellspacing="0" align="center"><tr><td align="center"></td></tr><tr><td align="center"><a href="https://www.paypal.com/uk/webapps/mpp/paypal-popup" title="How PayPal Works" onclick="javascript:window.open('https://www.paypal.com/uk/webapps/mpp/paypal-popup','WIPaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=1060, height=700'); return false;"><img src="https://www.paypalobjects.com/webstatic/mktg/Logo/pp-logo-200px.png" border="0" alt="PayPal Logo"></a></td></tr></table>
						<!-- PayPal Logo -->
					</div>
				</div>
				<div class="row">
					<div class="col-md-4">
					</div>
					<div class="col-md-4 text-center">
						<p>
							The payment has been completed successful. You will be redirected to the Homepage 
						</p>
					</div>
					<div class="col-md-4">
					</div>
				</div>
			</div>
		</div>
	</body>
	
</html>
