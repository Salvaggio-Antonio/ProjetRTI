<%-- 
    Document   : JSPCaddie
    Created on : 14-déc.-2022, 18:07:28
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
        <div class ="container">
            <form class="form-group" action="ReservationServlet" method="post">
                
                <select class="form-control" name="categorie" id="categorie">
                    <option value="Motel">Motel</option>
                    <option value="Village">Village</option>
                </select>

                <select class="form-control" name="typeChambre" id="typeChambre">
                    <option value="Simple">Simple</option>
                    <option value="Double">Double</option>
                    <option value="Familiale">Familiale</option>
                </select>

                <input class="form-control" type="date" name="dated">

                <Label>Nombre de nuit : </label>
                <input class="form-control" type="number" name="nombreNuit">

                <input type="submit" class="btn btn-primary" value="Reserver">

            </form>
        </div>
        
        
        
    </body>
</html>
