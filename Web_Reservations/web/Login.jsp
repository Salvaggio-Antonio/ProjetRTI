<%-- 
    Document   : Login
    Created on : 13-déc.-2022, 19:35:30
    Author     : Salva
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Login </h2> 
         <form action="LoginServlet" method="POST" >
                <label for="login "> Login :  </label>
                <br>
                <input type="text" name="login" required  >
                <br>

                <label for="passWord "> PassWord :  </label>
                <br>
                <input type="text" name="passWord" required=""  >
                <br>
                <input type="submit" value="envoyer" >
            </form>
    </body>
</html>
