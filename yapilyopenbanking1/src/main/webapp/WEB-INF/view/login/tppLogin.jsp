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

</script>
</head>
<body>
<div class="container">
<c:if test="${responseFlag eq  0}">
     <div class="alert alert-danger">
    	<strong>Error!</strong> User Name or Password is incorrect
  	</div>
  </c:if>
<div class="login-form">
    <form:form name="authenticationForm" method="post" action="tppAuthenticate">
        <h2 class="text-center">Log in</h2>
        <div class="form-group">
            <input type="text" class="form-control" placeholder="UserId" required="required" maxlength="20" name="username">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" placeholder="Password" required="required" maxlength="12"  name="password">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block" id="login">Log in</button>
        </div>
        <div class="clearfix">
            <label class="pull-left checkbox-inline"><input type="checkbox"> Remember me</label>
            <a href="#" class="pull-right">Forgot Password?</a>
        </div>        
    </form:form> 
    <p class="text-center"><a href="tppSingUP">Create an Account</a></p>
</div>

</div>

<br>
</body>
</html>