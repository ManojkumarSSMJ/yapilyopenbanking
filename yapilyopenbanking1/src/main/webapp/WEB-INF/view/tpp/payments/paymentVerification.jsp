<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"  %>
<html>
<head>
<script type="text/javascript">
$("#loading-text").text('Authenticating...');
</script>
<script type="text/javascript">
$(document).ready(function(){
	
	function validateOtp(inputValue) {
		var errorCode = '';
		var numberExpression = /^[0-9]*$/;
		if(inputValue == '') {
			errorCode = 'Otp should not be empty';
		} else if (!numberExpression.test(inputValue)) {
			errorCode = 'Otp should be 6 digits numeric';
		} else if (inputValue.length != 6) {
			errorCode = 'Otp should be 6 digits';
		} else {
			errorCode = '';
		}

		return errorCode;
	}
	
	$("#allow").bind('click', function(){
	
		$("#addtionalVerification").css('display','block');
	});
	$(".accounts").bind('change', function(){
		$("#addtionalVerification").css('display','none');
	});
	
	$("#deny").bind('click', function(){
		$("#addtionalVerification").css('display','none');
		window.location='tppPayments';
	});
	
	$("#otpSubmit").bind('click', function() {
		
		var otpValidationMessage = validateOtp($("#otp").val());
		if(otpValidationMessage == '') {
			document.otpForm.userId.value=$("#userId").val();
			document.otpForm.otp.value=$("#otp").val();
			document.otpForm.submit();
		} else {
			$("#otpError").text(otpValidationMessage);
		}
		
	});
});

</script>
</head>
<body>
<div class="container">
  
   <h4>Payment Authorization</h4>
   
    <br> 
   
    <div class="graybox">
    
     <h4><Strong>FSS Open Banking HUB  application is requesting to payment confirmation:</Strong></h4>

         <div class="card col-lg-1"> </div> <strong> Provider  :  <c:out value="${provider}"/></strong><br>
	     <div class="card col-lg-1"> </div> <strong> Mobile Number  :  <c:out value="${mobileNumber}"/></strong><br>
	     <div class="card col-lg-1"> </div> <strong> Account Id  :  <c:out value="${accountId}"/></strong><br><br>
     <br> 
     <div class="card col-lg-1"> </div><button class="btn btn-primary" id="allow">ALLOW</button>
     <button class="btn btn-primary" id="deny">DENY</button>
     
     <br><br>
     
     <div class="row"  id="addtionalVerification" style="display: none">
<div class="col-lg-2"></div>
 <div class="card col-lg-4">
  <div class="card-body">
    <h4 class="card-title"></h4>
    <p class="card-text">Additional Verification needed for the consent</p>
    
     <strong>Verify your one time password number</strong>
     <br>
     <form:form name="otpForm" action="paymentProcess">
     <input type="text" class="form-control" id="otp" placeholder="One Time Password" name="otp"/>
     <br>
     <span id="otpError" style="color: red"></span>
     <input type="hidden" id="userId" name="userId" value="<c:out value="${loginResponse.userDetails.userId}"/>"/>
     <input type="hidden" id="selectedAccounts" name="selectedAccounts" value=""/>
     <p>Please enter the one time password sent to your mobile number ending with <c:out value="${last4DigitMobNo}"/></p> 
     <button type="button" class="btn btn-primary" id="otpSubmit">Authorize</button>
      <input type="hidden" name="accountId" value="<c:out value="${accountId}"/>"/>
      	<input type="hidden" name="userId" value="<c:out value="${userId}"/>"/>
			 <input type="hidden" name="bankName" value="<c:out value="${bankName}"/>"/>
			  <input type="hidden" name="bankId" value="<c:out value="${bankId}"/>"/>
			 <input type="hidden" name="provider" value="<c:out value="${provider}"/>"/>
			 <input type="hidden" name="mobileNumber" value="<c:out value="${mobileNumber}"/>"/>
			 <input type="hidden" name="amount" value="<c:out value="${amount}"/>"/>
			 <input type="hidden" name="moduleName" value="<c:out value="${moduleName}"/>"/>
     </form:form>
  </div>
  <br>
 </div>
</div>
</div>
 </div>
</body>
</html>