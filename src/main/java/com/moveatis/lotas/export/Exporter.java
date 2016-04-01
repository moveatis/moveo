package com.moveatis.lotas.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sami Kallio <phinaliumz at outlook.com>
 */
@WebServlet("/exporter")
public class Exporter extends HttpServlet {
    
    private final String fileName = "testi.csv";
    
    private void processRequest(HttpSession session, HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        
        OutputStream outputStream = response.getOutputStream();
        
        try (FileInputStream inputStream = new FileInputStream(constructFile(fileName))) {
            byte[] buffer = new byte[4096];
            int length;
            
            while((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        outputStream.flush();
    }
    
    private File constructFile(String fileName) throws IOException {
        File f = new File(fileName);
        
        try (FileWriter fw = new FileWriter(f); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Diipa,Daapa,Duupa,Wuupa,Wuu");
        }
        
        return f;
    }

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
        
        HttpSession session = request.getSession(false);
        //if(session != null) {
            processRequest(session, request, response);
        //}
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
