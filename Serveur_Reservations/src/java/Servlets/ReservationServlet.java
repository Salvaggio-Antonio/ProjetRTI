/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salva
 */
public class ReservationServlet extends HttpServlet {

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
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        Boolean Trouve = false;
        String msg ="hello";
        String user = (String) request.getSession().getAttribute("user");
        String mdp = (String) request.getSession().getAttribute("mdp");

        try {
            BDHolidays bd = new BDHolidays("root","root","bd_holidays");
            String newDateString = request.getParameter("dated");

            
            ResultSet s =bd.getChambreLibre(request.getParameter("categorie"), request.getParameter("typeChambre"), newDateString, Integer.parseInt(request.getParameter("nombreNuit")));
            ResultSet u = bd.getUserByEmail(user, mdp);
            u.next();
            Double prixnet =0.0;
            
            if(s != null)
            {
                if(s.next()){
                    prixnet =Double.parseDouble(s.getString("prix_htva")) *1.21*Integer.parseInt(request.getParameter("nombreNuit"));
                    if(bd.insertReservationChambre(Integer.parseInt(s.getString("idchambres")), Integer.parseInt(u.getString("idvoyageurs")), newDateString, Integer.parseInt(request.getParameter("nombreNuit")),prixnet))
                    {
                        Trouve = true;
                    }  
                    else{
                        msg = "Aucune Chambre disponible 3!";
                    }
                }else{
                     msg = "Aucune Chambre disponible 2!";
                }
            }else{
                 msg = "Aucune Chambre disponible 1!";
            }
            
            if(Trouve){
               msg = "La chambre n°"+s.getString("idchambres")+ " a été reservé pour un total de : "+prixnet;
            }else{
               
            }
            
            session.setAttribute("repres",msg );
            response.sendRedirect(request.getContextPath() + "/ReponseReservation.jsp");  
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect(request.getContextPath() + "/ReponseReservation.jsp");
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
