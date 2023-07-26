<%-- 
    Document   : ReponseReservation
    Created on : 14-déc.-2022, 19:32:56
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
        <% String user = (String)request.getSession().getAttribute("user");
    
        if(user== null){
            
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
        } %>
        <% String rep = (String)request.getSession().getAttribute("repres");%>
        
        <div class="container">
            <h1><%out.println(rep);%>!</h1>

            <form action="InitCaddie" method="post" class="form-group">
                <input type="submit" class="btn btn-primary" value="Retour à l'accueil">
            </form>
        </div>
        
    </body>
</html>
