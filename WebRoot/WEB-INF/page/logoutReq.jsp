<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="renderer" content="webkit">
        <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10,IE=11">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>IdeaSAML - Service Provider</title>
    </head>
    <body>
        <form id="ssoForm" method="post" action="http://localhost:9080/wanr-usercenter/ssologout">
            <input type="hidden" id="logoutRequest" name="logoutRequest" value='${logoutReq}' />
        </form>

        <script type="text/javascript">
            document.getElementById("ssoForm").submit();
        </script>
    </body>
</html>