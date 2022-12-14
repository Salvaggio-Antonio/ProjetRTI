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
    <% String user = (String)request.getSession().getAttribute("user");%>
    <% ArrayList<String> reservations = (ArrayList<String>)request.getSession().getAttribute("reservations");%>
    
    <body>
        <h1>Bonjour ! <% out.println(user); %></h1>
        
        <h2>Voici le caddie</h2>
        
        <table>
            <tr>
                <th>Numéro de réservation</th>
                <th>Numéro de chambre</th>
                <th>Date de début</th>
                <th>Date de fin</th>
                <th>Nombre de nuits</th>
                <th>Prix net</th>
            </tr>
            <%for (int i = 0; i < reservations.size(); i++) {%>
            <tr>
                <%String []elements = reservations.get(i).split(":");
                for(int j = 0; j< elements.length ; j++){   
                %>
                <td>
                    
                    <%out.println(elements[j]);%>
                    
                </td>
                <%}%>
            </tr>
            <%}%>
            
          
        </table> 
        
        <form action="JSPCaddie.jsp">
            <input type="submit" value="Ajouter un article">
        </form>

        <form action="JSPPay.jsp">
            <input type="submit" value="Payer reservations">
        </form>
        
    </body>
</html>
