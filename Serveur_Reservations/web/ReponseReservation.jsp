<%-- 
    Document   : ReponseReservation
    Created on : 14-déc.-2022, 19:32:56
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
        <% String rep = (String)request.getSession().getAttribute("repres");%>
        <h1><%out.println(rep);%>!</h1>
        
        <form action="InitCaddie" method="post" >
            <input type="submit"  value="Retour à l'accueil">
        </form>
        
    </body>
</html>
