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
        <%String user = (String) request.getSession().getAttribute("user");
        
        if(user!= null){
            
            response.sendRedirect(request.getContextPath() + "/InitCaddie");
        } %>
        <div class="container">
            <h2>Login </h2> 
            <form action="LoginServlet" class ="form-group" method="POST" >
                   <label for="login "> Login :  </label>
                   <br>
                   <input type="text" class="form-control" name="login" required  >
                   <br>

                   <label for="passWord "> Password :  </label>
                   <br>
                   <input type="password" class="form-control" name="passWord" required=""  > <br><br>
                   <input type="checkbox" class="form-check-input" id="create" name="create">
                   <label for="create" class="form-check-label" >Je veux créer un compte</label><br>
                   <br>
                   <input type="submit" class="btn btn-primary" value="envoyer" >
               </form>
        </div>
        
    </body>
</html>
