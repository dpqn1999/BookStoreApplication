package bookaholic.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bookaholic.dao.AccountDAO;
import bookaholic.dao.AuthorDAO;
import bookaholic.dao.BookDAO;
import bookaholic.dao.OrderDAO;
import bookaholic.dao.PublisherDAO;
import bookaholic.entity.Account;
import bookaholic.entity.Author;
import bookaholic.entity.Book;
import bookaholic.entity.Publisher;
import bookaholic.form.AccountForm;
import bookaholic.form.AuthorForm;
import bookaholic.form.BookForm;
import bookaholic.form.DeliveryForm;
import bookaholic.form.PasswordForm;
import bookaholic.form.PublisherForm;
import bookaholic.form.SearchForm;
import bookaholic.model.AccountInfo;
import bookaholic.model.AuthorInfo;
import bookaholic.model.BookInfo;
import bookaholic.model.CartInfo;
import bookaholic.model.DeliveryInfo;
import bookaholic.model.OrderDetailInfo;
import bookaholic.model.OrderInfo;
import bookaholic.model.PublisherInfo;
import bookaholic.pagination.PaginationResult;
import bookaholic.utils.Utils;

@Controller
public class MainController {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private AuthorDAO authorDAO;

	@Autowired
	private PublisherDAO publisherDAO;

	// Access denied
	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}

	// Return account username
	public String returnAccountUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String accountUsername = null;
		if (principal instanceof UserDetails) {
			accountUsername = ((UserDetails) principal).getUsername();
		} else {
			accountUsername = principal.toString();
		}
		return accountUsername;
	}

	// Homepage
	@RequestMapping("/")
	public String home(Model model, @RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		final int maxResult = 100;
		final int maxNavigationPage = 100;
		PaginationResult<BookInfo> result = bookDAO.queryBooks(page, //
				maxResult, maxNavigationPage, likeName);
		model.addAttribute("paginationBooks", result);
		SearchForm searchForm = null;
		searchForm = new SearchForm();
		model.addAttribute("searchForm", searchForm);
		return "index";
	}

	// Search
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(Model model, //
			@ModelAttribute("searchForm") @Validated SearchForm searchForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes,
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		final int maxResult = 20;
		final int maxNavigationPage = 100;
		System.out.println(searchForm.getSearchKeyword() + "---" + likeName + "---");
		PaginationResult<BookInfo> bookList = bookDAO.searchBooks(page, //
				maxResult, maxNavigationPage, searchForm.getSearchKeyword().toLowerCase());
		model.addAttribute("paginationBooks", bookList);
		return "search";
	}

	// Login
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		Account account = accountDAO.findAccount(returnAccountUsername());
		if (account != null) {
			return "redirect:/";
		}
		return "login";
	}

	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public String admin(Model model) {
		Account account = accountDAO.findAccount(returnAccountUsername());
		if (account != null) {
			return "redirect:/";
		}
		return "admin";
	}

	// Show list of book
	@RequestMapping({ "/admin/bookList" })
	public String bookList(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		final int maxResult = 10;
		final int maxNavigationPage = 100;
		PaginationResult<BookInfo> result = bookDAO.queryBooks(page, //
				maxResult, maxNavigationPage, likeName);
		model.addAttribute("paginationBooks", result);
		Map<String, String> authorList = authorDAO.getMapAuthors();
		model.addAttribute("authorList", authorList);
		Map<String, String> publisherList = publisherDAO.getMapPublishers();
		model.addAttribute("publisherList", publisherList);
		return "bookList";
	}

	public Map<String, String> getMapTypes() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Comic", "Comic");
		map.put("Novel", "Novel");
		map.put("Fiction", "Fiction");
		map.put("Business", "Business");
		map.put("Literature", "Literature");
		map.put("Science", "Science");
		map.put("Biography", "Biography");
		map.put("Education", "Education");
		return map;
	}

	// Create and edit book
	@RequestMapping(value = { "/admin/book" }, method = RequestMethod.GET)
	public String book(Model model, @RequestParam(value = "bookId", defaultValue = "") String bookId) {
		BookForm bookForm = null;
		if (bookId != null && bookId.length() > 0) {
			Book book = bookDAO.findBook(Integer.parseInt(bookId));
			if (book != null) {
				bookForm = new BookForm(book);
			}
		}
		if (bookForm == null) {
			bookForm = new BookForm();
			bookForm.setNewBook(true);
		}

		PaginationResult<AuthorInfo> paginationAuthors //
				= authorDAO.listAuthorInfo(1, 20, 100);
		PaginationResult<PublisherInfo> paginationPublishers //
				= publisherDAO.listPublisherInfo(1, 20, 100);
		model.addAttribute("paginationAuthors", paginationAuthors);
		model.addAttribute("paginationPublishers", paginationPublishers);
		model.addAttribute("bookForm", bookForm);
		Map<String, String> types = getMapTypes();
		model.addAttribute("types", types);
		return "book";
	}

	// Save book
	@RequestMapping(value = { "/admin/book" }, method = RequestMethod.POST)
	public String bookSave(Model model, //
			@ModelAttribute("bookForm") @Validated BookForm bookForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		System.out.println(bookForm.getAuthorId() + "----------------");
		if (result.hasErrors()) {
			return "book";
		}
		try {
			bookDAO.saveBook(bookForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "book";
		}

		return "redirect:/admin/bookList";
	}

	// Delete book
	@RequestMapping(value = { "/admin/deleteBook" }, method = RequestMethod.GET)
	public String bookDelete(Model model, @RequestParam(value = "bookId", defaultValue = "") String bookId) {
		bookDAO.deleteBook(Integer.parseInt(bookId));
		return "redirect:/admin/bookList";

	}

	// Show list of author
	@RequestMapping(value = { "/admin/authorList" }, method = RequestMethod.GET)
	public String authorList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<AuthorInfo> paginationResult //
				= authorDAO.listAuthorInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
		model.addAttribute("paginationResult", paginationResult);
		return "authorList";
	}

	// Create and edit author
	@RequestMapping(value = { "/admin/author" }, method = RequestMethod.GET)
	public String author(Model model, @RequestParam(value = "authorId", defaultValue = "") String authorId) {
		AuthorForm authorForm = null;

		if (authorId != null && authorId.length() > 0) {
			Author author = authorDAO.findAuthor(Integer.parseInt(authorId));
			if (author != null) {
				authorForm = new AuthorForm(author);
			}
		}
		if (authorForm == null) {
			authorForm = new AuthorForm();
			authorForm.setNewAuthor(true);

		}
		model.addAttribute("authorForm", authorForm);
		return "author";
	}

	// Save author
	@RequestMapping(value = { "/admin/author" }, method = RequestMethod.POST)
	public String authorSave(Model model, //
			@ModelAttribute("authorForm") @Validated AuthorForm authorForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "author";
		}
		try {
			authorDAO.saveAuthor(authorForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "author";
		}
		return "redirect:/admin/authorList";
	}

	// Delete author
	@RequestMapping(value = { "/admin/deleteAuthor" }, method = RequestMethod.GET)
	public String authorDelete(Model model, @RequestParam(value = "authorId", defaultValue = "") String authorId) {
		authorDAO.deleteAuthor(Integer.parseInt(authorId));
		return "redirect:/admin/authorList";

	}

	// Show list of publisher
	@RequestMapping(value = { "/admin/publisherList" }, method = RequestMethod.GET)
	public String publisherList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<PublisherInfo> paginationResult //
				= publisherDAO.listPublisherInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
		model.addAttribute("paginationResult", paginationResult);
		return "publisherList";
	}

	// Create and edit publisher
	@RequestMapping(value = { "/admin/publisher" }, method = RequestMethod.GET)
	public String publisher(Model model, @RequestParam(value = "publisherId", defaultValue = "") String publisherId) {
		PublisherForm publisherForm = null;

		if (publisherId != null && publisherId.length() > 0) {
			Publisher publisher = publisherDAO.findPublisher(Integer.parseInt(publisherId));
			if (publisher != null) {
				publisherForm = new PublisherForm(publisher);
			}
		}
		if (publisherForm == null) {
			publisherForm = new PublisherForm();
			publisherForm.setNewPublisher(true);
		}
		model.addAttribute("publisherForm", publisherForm);
		return "publisher";
	}

	// Save publisher
	@RequestMapping(value = { "/admin/publisher" }, method = RequestMethod.POST)
	public String publisherSave(Model model, //
			@ModelAttribute("publisherForm") @Validated PublisherForm publisherForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "publisher";
		}
		try {
			publisherDAO.savePublisher(publisherForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "publisher";
		}

		return "redirect:/admin/publisherList";
	}

	// Delete publisher
	@RequestMapping(value = { "/admin/deletePublisher" }, method = RequestMethod.GET)
	public String publisherDelete(Model model,
			@RequestParam(value = "publisherId", defaultValue = "") String publisherId) {
		publisherDAO.deletePublisher(Integer.parseInt(publisherId));
		return "redirect:/admin/publisherList";

	}

	// Show list of account
	@RequestMapping(value = { "/admin/customerList" }, method = RequestMethod.GET)
	public String accountList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;
		PaginationResult<AccountInfo> paginationResult //
				= accountDAO.listAccountInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
		model.addAttribute("paginationResult", paginationResult);
		return "customerList";
	}

	// Show signup form
	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String account(Model model) {
		AccountForm accountForm = null;
		if (accountForm == null) {
			accountForm = new AccountForm();
			accountForm.setNewAccount(true);
		}
		model.addAttribute("accountForm", accountForm);
		return "signup";
	}

	// Create account
	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public String accountCreate(Model model, //
			@ModelAttribute("accountForm") @Validated AccountForm accountForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "signup";
		}
		if (accountDAO.findAccount(accountForm.getAccountUsername()) != null) {
			String message = "Username exists";
			model.addAttribute("errorMessage", message);
			return "signup";
		}
		try {
			accountDAO.createAccount(accountForm);
		} catch (Exception e) {	
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "signup";
		}
		return "redirect:/login";
	}

	// Account info
	@RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
	public String accountInfo(HttpServletRequest request, Model model) {
		Account account = accountDAO.findAccount(returnAccountUsername());
		AccountForm accountForm = new AccountForm(account);
		AccountInfo accountInfo = new AccountInfo(accountForm);
		model.addAttribute("accountInfo", accountInfo);
		return "accountInfo";
	}

	// Edit account
	@RequestMapping(value = { "/changeInfo" }, method = RequestMethod.GET)
	public String accountEdit(Model model) {
		String accountUsername = returnAccountUsername();
		AccountForm accountForm = null;
		if (accountUsername != null && accountUsername.length() > 0) {
			Account account = accountDAO.findAccount(accountUsername);
			if (account != null) {
				accountForm = new AccountForm(account);
			}
		}
		model.addAttribute("accountForm", accountForm);
		return "changeInfo";
	}

	// Save account
	@RequestMapping(value = { "/changeInfo" }, method = RequestMethod.POST)
	public String accountSave(Model model, //
			@ModelAttribute("accountForm") @Validated AccountForm accountForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "changeInfo";
		}
		Account account = accountDAO.findAccount(returnAccountUsername());
		accountForm.setAccountId(account.getAccountId());
		accountForm.setAccountUsername(account.getAccountUsername());
		accountForm.setAccountPassword(account.getAccountPassword());
		accountForm.setAccountRole(account.getAccountRole());
		try {
			accountDAO.saveAccount(accountForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "changeInfo";
		}

		return "redirect:/accountInfo";
	}

	// Show list of order
	@RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
	public String orderList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;

		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

		model.addAttribute("paginationResult", paginationResult);

		return "orderList";
	}

	// Delete book
	@RequestMapping(value = { "/admin/deleteOrder" }, method = RequestMethod.GET)
	public String orderDelete(Model model, @RequestParam(value = "orderId", defaultValue = "") String orderId) {
		orderDAO.deleteOrder(Integer.parseInt(orderId));
		return "redirect:/admin/orderList";

	}

	public Map<String, String> getMapStatuses() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Received", "Received");
		map.put("Packaged", "Packaged");
		map.put("Shipping", "Shipping");
		map.put("Deliveried", "Deliveried");
		return map;
	}

	// Show list of order detail
	@RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
	public String orderView(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = this.orderDAO.getOrderInfo(Integer.parseInt(orderId));
		}
		if (orderInfo == null) {
			return "redirect:/admin/orderList";
		}
		List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(Integer.parseInt(orderId));
		orderInfo.setDetails(details);
		AccountForm accountForm = new AccountForm(accountDAO.findAccount(orderInfo.getAccountUsername()));
		AccountInfo accountInfo = new AccountInfo(accountForm);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("accountInfo", accountInfo);
		Map<String, String> statuses = getMapStatuses();
		model.addAttribute("statuses", statuses);
		return "order";
	}

	@RequestMapping(value = { "/admin/order" }, method = RequestMethod.POST)
	public String editOrder(Model model, //
			@ModelAttribute("orderInfo") @Validated OrderInfo orderInfo, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "admin/order";
		}
		try {
			orderDAO.updateOrderStatus(orderInfo.getOrderId(), orderInfo.getOrderStatus());
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "admin/orderList";
		}
		return "redirect:/admin/orderList";
	}

	// Show list of order for customer
	@RequestMapping(value = { "/orderList" }, method = RequestMethod.GET)
	public String customeOrderList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}
		final int MAX_RESULT = 5;
		final int MAX_NAVIGATION_PAGE = 10;
		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listCustomerOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE, returnAccountUsername());
		model.addAttribute("paginationResult", paginationResult);
		return "customerOrderList";
	}

	// Show list of order detail for customer
	@RequestMapping(value = { "/order" }, method = RequestMethod.GET)
	public String customerOrderView(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = this.orderDAO.getCustomerOrderInfo(Integer.parseInt(orderId));
		}
		if (orderInfo == null) {
			return "redirect:customerOrderList";
		}
		List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(Integer.parseInt(orderId));
		orderInfo.setDetails(details);
		AccountForm accountForm = new AccountForm(accountDAO.findAccount(orderInfo.getAccountUsername()));
		AccountInfo accountInfo = new AccountInfo(accountForm);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("accountInfo", accountInfo);
		return "customerOrder";
	}

//-------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------------------------------------------------------//	
//-------------------------------------------------------------------------------------------------------------------------------------//	
//-------------------------------------------------------------------------------------------------------------------------------------//	

	// Edit account
	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.GET)
	public String changePassword(Model model) {
		PasswordForm passwordForm = new PasswordForm();
		model.addAttribute("passwordForm", passwordForm);
		return "changePassword";
	}

	// Save account
	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.POST)
	public String changePassword(Model model, //
			@ModelAttribute("passwordForm") @Validated PasswordForm passwordForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "changePassword";
		}

		Account account = accountDAO.findAccount(returnAccountUsername());
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		boolean isPasswordMatches = bcrypt.matches(passwordForm.getOldPassword(), account.getAccountPassword());
		if (!isPasswordMatches) {
			return "changePassword";
		}
		try {
			accountDAO.changePassword(account.getAccountUsername(), passwordForm.getNewPassword());
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			return "changePassword";
		}
		return "redirect:/accountInfo";
	}

	// Book info
	@RequestMapping(value = { "/bookInfo" }, method = RequestMethod.GET)
	public String bookInfo(HttpServletRequest request, Model model,
			@RequestParam(value = "bookId", defaultValue = "") String bookId) {
		Book book = bookDAO.findBook(Integer.parseInt(bookId));
		BookInfo bookInfo = new BookInfo(book);
		model.addAttribute("bookInfo", bookInfo);
		Map<String, String> authorList = authorDAO.getMapAuthors();
		model.addAttribute("authorList", authorList);
		Map<String, String> publisherList = publisherDAO.getMapPublishers();
		model.addAttribute("publisherList", publisherList);
		return "bookInfo";
	}

	@RequestMapping({ "/buyBook" })
	public String listProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "bookId", defaultValue = "") String bookId) {

		Book book = null;
		if (bookId != null && bookId.length() > 0) {
			book = bookDAO.findBook(Integer.parseInt(bookId));
		}
		if (book != null) {

			//
			CartInfo cartInfo = Utils.getCartInSession(request);

			BookInfo bookInfo = new BookInfo(book);

			cartInfo.addBook(bookInfo, 1);
		}

		return "redirect:/shoppingCart";
	}

	public String listBookHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "bookId", defaultValue = "") String bookId) {

		Book book = null;
		if (bookId != null && bookId.length() > 0) {
			book = bookDAO.findBook(Integer.parseInt(bookId));
		}
		if (book != null) {
			CartInfo cartInfo = Utils.getCartInSession(request);
			BookInfo bookInfo = new BookInfo(book);
			cartInfo.addBook(bookInfo, 1);
		}
		return "redirect:/shoppingCart";
	}

	@RequestMapping({ "/shoppingCartRemoveBook" })
	public String removeBookHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "bookId", defaultValue = "") String bookId) {
		Book book = null;
		if (bookId != null && bookId.length() > 0) {
			book = bookDAO.findBook(Integer.parseInt(bookId));
		}
		if (book != null) {
			CartInfo cartInfo = Utils.getCartInSession(request);
			BookInfo bookInfo = new BookInfo(book);
			cartInfo.removeBook(bookInfo);
		}
		return "redirect:/shoppingCart";
	}

	// POST: Update quantity for product in cart
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
	public String shoppingCartUpdateQty(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("cartForm") CartInfo cartForm) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		cartInfo.updateQuantity(cartForm);
		return "redirect:/shoppingCart";
	}

	// GET: Show cart.
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartInSession(request);
		model.addAttribute("cartForm", myCart);
		return "shoppingCart";
	}

	// GET: Enter customer information.
	@RequestMapping(value = { "/shoppingCartAccount" }, method = RequestMethod.GET)
	public String shoppingCartAccountForm(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		if (cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		}
		String accountUsername = returnAccountUsername();
		AccountForm accountForm = null;
		if (accountUsername != null && accountUsername.length() > 0) {
			Account account = accountDAO.findAccount(accountUsername);
			if (account != null) {
				accountForm = new AccountForm(account);
			}
		}
		DeliveryForm deliveryForm = null;
		deliveryForm = new DeliveryForm();
		deliveryForm.setOrderAddress(accountForm.getAccountAddress());
		deliveryForm.setOrderPhone(accountForm.getAccountPhone());
		model.addAttribute("deliveryForm", deliveryForm);
		return "shoppingCartAccount";
	}

	// POST: Save customer information.
	@RequestMapping(value = { "/shoppingCartAccount" }, method = RequestMethod.POST)
	public String shoppingCartAccountSave(HttpServletRequest request, Model model,
			@ModelAttribute("deliveryForm") @Validated DeliveryForm deliveryForm, BindingResult result,
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			deliveryForm.setValid(false);
			return "shoppingCartAccount";
		}
		deliveryForm.setValid(true);
		CartInfo cartInfo = Utils.getCartInSession(request);
		DeliveryInfo deliveryInfo = new DeliveryInfo(deliveryForm);
		cartInfo.setDeliveryInfo(deliveryInfo);

		Account account = accountDAO.findAccount(returnAccountUsername());
		if (account == null) {
			return "login";
		}
		AccountForm accountForm = new AccountForm(account);
		accountForm.setValid(true);
		AccountInfo accountInfo = new AccountInfo(accountForm);
		cartInfo.setAccountInfo(accountInfo);

		return "redirect:/shoppingCartConfirmation";
	}

	// GET: Show information to confirm.
	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		if (cartInfo == null || cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidAccount()) {
			return "redirect:/shoppingCartAccount";
		}
		model.addAttribute("myCart", cartInfo);

		return "shoppingCartConfirmation";
	}

	// POST: Submit Cart (Save)
	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)

	public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		if (cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidAccount()) {

			return "redirect:/shoppingCartAccount";

		}
		model.addAttribute("myCart", cartInfo);
		try {
			orderDAO.saveOrder(cartInfo);
		} catch (Exception e) {
			return "shoppingCartConfirmation";
		}

		// Remove Cart from Session.
		Utils.removeCartInSession(request);

		// Store last cart.
		Utils.storeLastOrderedCartInSession(request, cartInfo);

		return "redirect:/shoppingCartFinalize";
	}

	@RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {
		CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		return "shoppingCartFinalize";
	}

	@RequestMapping(value = { "/bookImage" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("bookId") String bookId) throws IOException {
		Book book = null;
		if (bookId != null) {
			book = this.bookDAO.findBook(Integer.parseInt(bookId));
		}
		if (book != null && book.getBookImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(book.getBookImage());
		}
		response.getOutputStream().close();
	}

	@RequestMapping(value = { "/resetPassword" }, method = RequestMethod.GET)
	public String resetPassword(HttpServletRequest request, Model model) {
		return "resetPassword";
	}

	@RequestMapping(value = { "/resetPassword" }, method = RequestMethod.POST)
	public String sendResetPassword(HttpServletRequest request, @RequestParam(value = "recipient") String recipient,
			Model model) {
		String subject = "Your Password has been reset";
		String newPassword = accountDAO.resetAccountPassword(recipient);

		String content = "Hi, this is your new password: " + newPassword;
		content += "\nNote: for security reason, " + "you must change your password after logging in.";

		String message = "";
		String host = "smtp.gmail.com";
		String port = "587";
		String email = "boyfromdadouble9@gmail.com";
		String name = "Bookaholic";
		String pass = "HoangTien0310";
		try {
			Utils.sendEmail(host, port, email, name, pass, recipient, subject, content);
			message = "Your password has been reset. Please check your e-mail.";
		} catch (Exception e) {
			e.printStackTrace();
			message = "There were an error: " + e.getMessage();
		}
		model.addAttribute("message", message);

		return "resetPassword";
	}
}
