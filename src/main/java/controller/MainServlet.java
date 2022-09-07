package controller;

import command.CommandProvider;
import command.impl.abstraction.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sun.security.util.DerEncoder;

import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter("cmd");
        CommandProvider.findGetCommand(commandName).execute(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter("cmd");
        CommandProvider.findPostCommand(commandName).execute(req, resp);
    }
}
