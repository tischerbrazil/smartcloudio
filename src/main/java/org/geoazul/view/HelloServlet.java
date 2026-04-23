package org.geoazul.view;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello.js")
public class HelloServlet extends HttpServlet {

		
	
	
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	  	
	  
  PrintWriter writer = resp.getWriter();
  writer.println("if(!window.app)window.app={};");
  writer.println("var app=window.app;");
  writer.println("var typeSelect=document.getElementById('type');");
  }
}