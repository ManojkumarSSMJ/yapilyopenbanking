<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"  %>
<html>
<body>

<script type="text/javascript">

 $(document).ready(function(){
	$("#update").bind('click', function(){
		
		
		 var flag = document.getElementsByName('flag'); 
         
         for(i = 0; i < flag.length; i++) { 
             if(flag[i].checked) 
             {
            	 if(flag[i].value == "yes")
            		 document.updateLoginAuthenticator.loginAuthenticatorFlag.value = "yes";
            	 else
            		 document.updateLoginAuthenticator.loginAuthenticatorFlag.value = "no";
            	 
            	 document.updateLoginAuthenticator.submit();
             }
         }
		
	});
});
 

</script>

<div>
     <h4>Login Authenticator</h4>
     
          <form:form name="updateLoginAuthenticator" method="post" action="updateLoginAuthenticator">
           <input type="hidden" name="loginAuthenticatorFlag">
   		 </form:form>  
   		  <c:if test="${responseFlag eq  0}">
	     <div class="alert alert-danger">
	    	<strong>Error!</strong> Login Authenticator Flag Updation Failed
	  	 </div>
	  	 </c:if>
	  	 <c:if test="${responseFlag > 0}">
	     <div class="alert alert-success">
	    	<strong>Success!</strong> Login Authenticator Flag Updated Successfully
	  	 </div>
	  	 </c:if>
	  	 <div class="col-lg-5" align="center">
        <div class="graybox" align="center">
            <Strong>Update Login Authenticator</Strong>
            <br>
            <br>
            <input type="radio" name="flag" value="yes" <c:if test="${loginAuthenticatorFlag eq 'yes'}"> checked </c:if>>Enable
			<input type="radio" name="flag" value="no" <c:if test="${loginAuthenticatorFlag eq 'no'}"> checked </c:if>>Disable
			<br>
			<br>
			<button type="button" id="update">Update</button>
         </div>
         </div>
 </div>
</body>
</html>