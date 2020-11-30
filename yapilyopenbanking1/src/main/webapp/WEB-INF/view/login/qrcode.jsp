<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>
<body>
<script type="text/javascript">
$( document ).ready(function() {
	getQRCode();
});

function getQRCode(){
    $.get("generateQrCode?userId="+$("#userId").val(),function(data) {
        $("#qr").append('<img src="'+data.url+'" />');
        document.authenticatorForm.secret.value=data.secret;
    });
}

 $(document).ready(function(){
	$("#verify").bind('click', function(){
		$("#authenticatorError").text(""); 
		var authenticatorCode = $("#authenticatorCode").val();
		if(authenticatorCode == null || authenticatorCode == "")
		{
			$("#authenticatorError").text("Verification Code should not be empty"); 
			return false;
		}
		$.get("verifyQrCode?authenticatorCode="+$("#authenticatorCode").val()+"&secret="+$("#secret").val(),function(data) {
			if(data.result == "success")
			{
				document.authenticatorForm.action = "tppAuthenticator";
				document.authenticatorForm.submit();
			}
			else
			{
				$("#authenticatorError").text("Invalid Verification Code, Try Again"); 
				$("#authenticatorCode").val("");
			}
	    });
	});
});

 	$(window).ready(function() { 
     $("#authenticatorForm").on("keypress", function (event) { 
         var keyPressed = event.keyCode || event.which; 
         if (keyPressed === 13) { 
             event.preventDefault(); 
             return false; 
         } 
     }); 
     }); 

</script>

<div class="container">
 <input type="hidden" id="userId" value="${userId}"/>
	<div id="qr" align="center">
	<br>
	    <Strong>Scan this Barcode using Google Authenticator app on your phone to use.
	     <a href="https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2">Android</a> and 
	     <a href="https://itunes.apple.com/us/app/google-authenticator/id388497605">iPhone</a></Strong>
	     <br>
    </div>
          <br>
	      <form:form name="authenticatorForm" id="authenticatorForm" method="post" align="center">
	         <input type="hidden" name="secret" id="secret">
            <input type="text" placeholder="Enter Fss Authenticator Code" required="required" size="25" id="authenticatorCode" name="authenticatorCode">
	   		 <button type="button" id="verify">Verify</button>
	   		 <br>
	            <span id="authenticatorError" style="color: red"></span>
	            <br>
	            <a href="tppLogin"><strong>Click Here</strong></a> to go Login page.
	   		 <br>
   		 </form:form> 
</div>

</body>
</html>