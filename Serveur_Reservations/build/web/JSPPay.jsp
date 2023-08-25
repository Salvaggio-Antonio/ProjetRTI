<%-- 
    Document   : JSPPay
    Created on : 15-déc.-2022, 6:26:14
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
        <div class="container">
            <form action="PaiementServlet" class="form-group" method="post" >
                <label>Veuillez saisir le numéro de réservation que voulez payer</label>
                <input type="number" class="form-control" name="reservation">
                <label>Veuillez saisir le montant</label>
                <input type="text" class="form-control" name="montant">
                <lable>Numéro de votre carte de crédit </lable>
                <input type="text" class="form-control" name="credit">
                <input type="submit" class="btn btn-primary" value="Payer">
            </form>
        </div>
        
    </body>
</html>
