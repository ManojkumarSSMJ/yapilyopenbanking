<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"  %>
<html>
<head>
<script type="text/javascript">
$("#loading-text").text('Fetching Activity Details from Bank');
</script>
</head>
<body>

<div class="container-fluid">
   
   <h4>Activity</h4>
   
    <c:if test="${empty tppConsentResponse.institutionDetailsList}">
		<div class="alert alert-danger">
   			 Please <a href="showBanks"><strong>Click Here</strong></a> link the Bank with FSS Open Banking HUB.
		</div>
	</c:if>
   
<br>

<div class="row">
<c:if test="${not empty tppConsentResponse.institutionDetailsList}">
	<div class="accordion col-lg-offset-1">
		<c:forEach var="institutionDetail" items="${tppConsentResponse.institutionDetailsList}" varStatus="loop" >
			<div class="group col-lg-5">
				
				<h2><c:out value="${institutionDetail.bankName}"/> &nbsp;&nbsp;&nbsp;<img src="${institutionDetail.bankIcon}"  style="width:25px;height:25px;"/></h2>
				
				<div class="content">
				
						<div class="graybox" >
							
							<table class="table table-hover table-striped table-bordered mpayTable">
							<thead>
								<tr>
									<td>Bank Name</td>
									<td>Bank Id</td>
									<td>Updated DateTime</td>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="updateDTDetails" items="${tppConsentResponse.updateDTDetailsList[loop.index]}">
									<tr>
										<td><c:out value="${institutionDetail.bankName}" /></td>
										<td><c:out value="${updateDTDetails.bankId}" /></td>
										<td><c:out value="${updateDTDetails.updateDt}" /></td>
									</tr>
							</c:forEach>
							</tbody>
						</table>
							
							<div style="clear:both"></div>
							
						</div>
				</div>
			</div>
		</c:forEach>
	</div>
</c:if>
</div>
<div style="clear: both"></div>
<c:if test="${not empty tppConsentResponse.institutionDetailsList}">
<div class="col-lg-9 col-lg-offset-3">
    <a href="showBanks"><strong>Click Here</strong></a> to link other bank accounts with FSS Open Banking HUB.
</div>
</c:if>
</div>
</body>
</html>