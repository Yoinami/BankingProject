package com.cashnex.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.cashnex.dao.UserDao;
import com.cashnex.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class adminDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDao userDao = null;

	public adminDashboardController() {
		super();
		userDao = new UserDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action == null) {
			action = "LIST";
		}
		switch (action) {
		case "LIST":
			try {
				listUser(request, response);
			} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			break;

		case "EDIT":
			try {
				System.out.println("EDiT go in");
				getUser(request, response);
			} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			break;

		case "DELETE":
			try {
				deleteUser(request, response);
			} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			break;
		default:
			try {
				listUser(request, response);
			} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			break;
		}

	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		List<User> userList = userDao.getUserList();
		request.setAttribute("userList", userList);
		System.out.println("We got here");
		request.getRequestDispatcher("/views/adminPage.jsp").forward(request, response);
	}

	private void getUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, ClassNotFoundException, SQLException {
		
		String id = request.getParameter("id");
		User user = null;
		try {
			user = UserDao.getUserById(Integer.parseInt(id));
		} catch (NumberFormatException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		request.setAttribute("someone", user);
		//request.getRequestDispatcher("/views/userRegistration.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath() + "/views/adminPage.jsp");

	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, ClassNotFoundException, SQLException {
		String id = request.getParameter("id");
		if (userDao.delete(Integer.parseInt(id))) {
			request.setAttribute("MSG", "Successfully Deleted");
		}
		listUser(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String action = request.getParameter("action");
		switch(action) {
			case "ADD_BALANCE":
				try {
					System.out.println("AYO");
					add_balance(request, response);
				} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				break;
			default:
				try {
					listUser(request, response);
				} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				break;
			}
	}
	private void add_balance(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException, ClassNotFoundException, SQLException {
		
		String id = request.getParameter("id");
		String amount = request.getParameter("amount");
		String message = "Money Transfer was Successful";
		
		try {
			userDao.addUserAmount(Integer.parseInt(id), Double.parseDouble(amount));
		} catch(Exception e) {
			message = "";
		}
		request.getSession().setAttribute("notification", message);
		System.out.println("mewssage: " + message);
		response.sendRedirect(request.getContextPath() + "/adminDashboardController?condition=true");
		
		//request.setAttribute("notification", message);
		//request.getRequestDispatcher("/adminDashboardController").forward(request, response);

	}

}
