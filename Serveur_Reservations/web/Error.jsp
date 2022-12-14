<%-- 
    Document   : Error.jsp
    Created on : 14-déc.-2022, 15:40:28
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
        <%
            String messageError = (String)request.getSession().getAttribute("ErrMessage");
        %>
        <label>
            <%
                out.println(messageError);
            %>
        </label>
        <form action="Login.jsp" method="POST" >
            <input <input type="submit" value="Retour" >
        </form>
        
    </body>
</html>
