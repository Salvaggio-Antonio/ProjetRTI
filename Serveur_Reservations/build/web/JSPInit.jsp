<%-- 
    Document   : JSPInit
    Created on : 14-déc.-2022, 16:56:19
    Author     : Salva
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <% String user = (String)request.getSession().getAttribute("user");
    
        if(user== null){
            
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
        } %>
    <% ArrayList<String> reservations = (ArrayList<String>)request.getSession().getAttribute("reservations");%>
    
    <body>
        <div class="container">
            
            <h1>Bonjour ! <% out.println(user); %></h1>
        
            <h2>Voici le caddie</h2>

            <table class="table">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Numéro de réservation</th>
                        <th scope="col">Numéro de chambre</th>
                        <th scope="col">Date de début</th>
                        <th scope="col">Date de fin</th>
                        <th scope="col">Nombre de nuits</th>
                        <th scope="col">Prix net</th>
                        <th scope="col">Reste a payer</th>
                        
                    </tr>
                </thead>
                <tbody>
                <%for (int i = 0; i < reservations.size(); i++) {%>
                <tr>
                    <th scope="row"><%out.println(i+1);%></th>
                    <%String []elements = reservations.get(i).split(":");
                    for(int j = 0; j< elements.length ; j++){   
                    %>
                    <td>

                        <%out.println(elements[j]);%>

                    </td>
                    <%}%>
                </tr>
                <%}%>
                </tbody>


            </table> 
              <br>
            <form class="form-group" action="JSPCaddie.jsp">
                <input type="submit" class="btn btn-primary" value="Ajouter un article">
            </form>
                <br>

            <form action="JSPPay.jsp" class="form-group">
                <input type="submit" class="btn btn-primary" value="Payer reservations">
            </form>
                <br>

            <form action="LogOut" class="form-group">
                <input type="submit" class="btn btn-primary" value="Se déconnecter">
            </form>
            
            
        </div>
        
        
    </body>
</html>
