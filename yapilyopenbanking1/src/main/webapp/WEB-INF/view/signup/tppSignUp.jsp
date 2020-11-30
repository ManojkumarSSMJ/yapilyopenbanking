<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core"  %>
<html>
<head>
<style type="text/css">
	.login-form {
		width: 340px;
    	margin: 50px auto;
	}
    .login-form form {
    	margin-bottom: 15px;
        background: #f7f7f7;
        box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
        padding: 30px;
    }
    .login-form h2 {
        margin: 0 0 15px;
    }
    .form-control, .btn {
        min-height: 38px;
        border-radius: 2px;
    }
    .btn {        
        font-size: 15px;
        font-weight: bold;
    }
</style>
<script type="text/javascript">

	function validateForm() 
	{
		var customerName = document.getElementById("CustomerName").value;
		var mobileNumber = document.getElementById("MobileNumber").value;
		var pwd = document.getElementById("password").value;
		var cpwd = document.getElementById("confirmPassword").value;
		var numberExpression = /^[0-9]*$/;
		var alphabetExpression = /^[A-Za-z]*$/

		if(!alphabetExpression.test(customerName))
		{
			document.getElementById("CustomerName").value = "";
			alert ('Customer Name should be numeric');
			return false;
		}
		else if(!numberExpression.test(mobileNumber.substring(1, mobileNumber.length)))
		{
			document.getElementById("MobileNumber").value = "";
			alert ('Mobile Number should be numeric');
			return false;
		}
		else if(!mobileNumber.startsWith("+1"))
		{
			document.getElementById("MobileNumber").value = "";
			alert ('Mobile Number should start with +1');
			return false;
		}
		else if(pwd.length < 6)
		{
			document.getElementById("confirmPassword").value = "";
			document.getElementById("password").value = "";
			alert ('Password minimum length is 6');
			return false;
		}
		else if(pwd != cpwd)
		{
			document.getElementById("confirmPassword").value = "";
			alert ('Password not Matched');
			return false;
		}
		else
			return true;
	}

</script>
</head>
<body>
<div class="container-fluid">
  <c:if test="${responseFlag eq  1}">
     <div class="alert alert-success">
    	<strong>Success!</strong> User Created Successfully
  	</div>
  </c:if>
  <c:if test="${responseFlag eq  2}">
     <div class="alert alert-danger">
    	<strong>Error!</strong> User already linked with FSS OpenBanking HUB
  	</div>
  </c:if>
  <c:if test="${responseFlag eq  0}">
     <div class="alert alert-danger">
    	<strong>Error!</strong> User creation failed
  	</div>
  	</c:if>
  <br>
</div>
<div class="container">
<div class="login-form">
    <form:form name="createAccountForm" method="post" onsubmit="return validateForm()" action="tppCreateAccount">
        <h2 class="text-center">Sign Up</h2>        
        <div class="form-group">
            <input type="text" class="form-control" placeholder="CustomerName" required="required"  id="CustomerName" name="CustomerName" maxlength="20">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" placeholder="MobileNumber" required="required"  id="MobileNumber" name="MobileNumber" maxlength="12">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" placeholder="UserId" required="required"  name="userId" maxlength="20">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" placeholder="Password" required="required"  id="password" name="password" maxlength="12">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" placeholder="Confirm Password" required="required" id="confirmPassword" name="confirmPassword" maxlength="12">
        </div>
        
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block" id="signup">Create an Account</button>
        </div>
    </form:form> 
    <div class="col-lg-9 col-lg-offset-3">
		<a href="tppLogin"><strong>Click Here</strong></a> to go Login page.
	</div>
</div>

</div>

<br>
</body>
</html>