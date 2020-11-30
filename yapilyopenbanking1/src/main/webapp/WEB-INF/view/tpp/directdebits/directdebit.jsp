<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<script type="text/javascript">
	$("#loading-text").text('Fetching Direct Debits from Bank');
</script>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="container-fluid">

		<h4>Direct Debits</h4>

		<br>
		<div class="row">
			<table
				class="table table-hover table-striped table-bordered mpayTable">
				<thead>
					<tr>
						<td>Direct Debit Id</td>
						<td>Remitter Account Number</td>
						<td>Credit Institution Name</td>
						<td>Previous Payment Date</td>
						<td>Previous Payment Amount</td>
						<td>Status</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="directDebitObject" items="${tppDirectDebitResponse.directDebitDetails}">
						<tr>
							<td><c:out value="${directDebitObject.directDebitId}" /></td>
							<td><c:out value="${directDebitObject.maskAccountId}" /></td>
							<td><c:out value="${directDebitObject.payeeName}" /></td>
							<td><c:out value="${directDebitObject.prevPaymentDate}" /></td>
							<td><c:out value="${directDebitObject.prevPaymentAmount}" /></td>
							<td><c:out value="${directDebitObject.debitStatusCode}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div style="clear: both"></div>
				<div class="col-lg-9 col-lg-offset-3">
					<a href="showBanks"><strong>Click Here</strong></a> to link other
					bank accounts with FSS Open Banking HUB.
					<br>
					<a href="tppAccounts"><strong>Click Here</strong></a> to go Accounts page.
				</div>
		</div>
	</div>
</body>
</html>