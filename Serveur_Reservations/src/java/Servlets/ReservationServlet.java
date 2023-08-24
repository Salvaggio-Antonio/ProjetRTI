/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlets;

import Holidays.BDHolidays;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
        if(user== null || user.equals("")){
            
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
        }
        String mdp = (String) request.getSession().getAttribute("mdp");

        try {
            String newDateString = request.getParameter("dated");
            
            ResultSet u = BDHolidays.getInstance().getUserByEmail(user, mdp);
            
            if(u.next())
            {
                ResultSet s = BDHolidays.getInstance().getChambreLibre(request.getParameter("categorie"), request.getParameter("typeChambre"), newDateString, Integer.parseInt(request.getParameter("nombreNuit")));
                if(s != null)
                {
                    Double prixnet =0.0;
                    prixnet = Double.parseDouble(s.getString("prix_htva")) *1.21*Integer.parseInt(request.getParameter("nombreNuit"));
                    boolean b = BDHolidays.getInstance().insertReservationChambre(Integer.parseInt(s.getString("idchambres")), Integer.parseInt(u.getString("idvoyageurs")), newDateString, Integer.parseInt(request.getParameter("nombreNuit")),prixnet);
                    if(b)
                    {
                        msg = "La chambre n°"+s.getString("idchambres")+ " a été reservé pour un total de : "+prixnet;
                    }
                    else
                    {
                        msg ="La réservation n'a pas pu se faire";
                    }
                }
                else
                {
                    msg ="Aucune Chambre disponible";
                }
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
