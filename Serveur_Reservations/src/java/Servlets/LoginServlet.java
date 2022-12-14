/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import database.facility.BDHolidays;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salva
 */
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String pass = request.getParameter("passWord");
        String login = request.getParameter("login");
        String msgError="";
        Boolean err=false;
        HttpSession session = request.getSession(true);
        session.setAttribute("mdp", pass);
        session.setAttribute("user", login);
        
        BDHolidays bd;
        try {
            bd = new BDHolidays("root","root","bd_holidays");
            
            ResultSet s = bd.getVoyageursByEmail(login);

            
        
            response.setContentType("text/html;charset=UTF-8");
            
                /* TODO output your page here. You may use following sample code. */

            if(request.getParameter("create")== null)
            {
                if(s.next()){
                    try {
                        if(!s.getString("motDepasse").equals(pass))
                        {
                            err=true;
                            msgError="Mot de passe érroné"; 
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{

                    err=true;
                    msgError="nom d'utilisateur inexistant";
                }

            }else{
                if(!bd.insertVoyageur(login, pass))
                {
                    err=true;
                    msgError = "Nom d'utilisateur déja existant !!";
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            err=true;
            msgError="Problème avec la base de donnée";
            
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(err){
            session.setAttribute("ErrMessage",msgError );
            response.sendRedirect(request.getContextPath() + "/Error.jsp");  
        }else{
            response.sendRedirect(request.getContextPath() + "/InitCaddie");  
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
