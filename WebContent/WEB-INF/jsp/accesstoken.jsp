<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
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
						<p><br><br>
							Got it! Your access token is <br><b>${accessToken}</b>
						</p>
						<p>
							With this we can perform a payment. <br> 
							<form role="form" action="createpayment.html" method="post">
								<div class="form-group">
									<input type="hidden" class="form-control" id="accesstoken" name="accesstoken" value=${accessToken} />
								</div>
								<button type="submit" class="btn btn-primary" value="Send Request">
									Execute
								</button>
							</form>
						</p>
					</div>
					<div class="col-md-4">
					</div>
				</div>
			</div>
		</div>
	</body>
</html>