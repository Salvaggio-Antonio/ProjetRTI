<%-- 
    Document   : Login
    Created on : 14-déc.-2022, 11:02:32
    Author     : Salva
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous"/>
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

                <label for="passWord "> Password :  </label>
                <br>
                <input type="password" name="passWord" required=""  > <br><br>
                <input type="checkbox" id="create" name="create">
                <label for="create">Je veux créer un compte</label><br>
                <br>
                <input type="submit" value="envoyer" >
            </form>
    </body>
</html>
