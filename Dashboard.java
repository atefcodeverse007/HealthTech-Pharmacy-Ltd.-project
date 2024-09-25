import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.io.*;
import java.util.*;
import java.text.*;


public class Dashboard extends JFrame{
	HashMap<String,String[]> medicineData = new HashMap<>();
	HashMap<String,String[]> sellHistory = new HashMap<>();
	JTable table;
	DefaultTableModel tableModel;
	JPanel tableHeaderPanel;
	//JTextField medicineBox, stockBox, priceBox, soldBox; [1st update : these are less details]
	//cannot use JTextArea for the style of HTML
	//JTextArea detailsArea;//area for showing more details of medicine 
	//using JEditorPane instead of JTextArea to use HTML style for medicine full details
	JEditorPane detailsArea;
	String filePath = "medicineData.txt";//file to handle parmanent storage
	
	Dashboard(){
		this.setSize(1280,720);
		this.setTitle("HealthTech System Management");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.getContentPane().setBackground(new Color(15,10,25,180));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Creating a label for header
		JLabel headerLabel = new JLabel("HealthTech Pharmaceutical Ltd.", SwingConstants.CENTER);
		headerLabel.setBounds(250,10,750,120);
		headerLabel.setOpaque(true);
		headerLabel.setForeground(Color.WHITE); 
		headerLabel.setBackground(Color.RED); 
		headerLabel.setFont(new Font("Courier New",Font.BOLD,38));
		
		//Defining the colors to cycle through
		Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.ORANGE, Color.MAGENTA};
		int[] colorIndex = {0}; 

		//setting Timer to change color every second
		javax.swing.Timer colorChangeTimer = new javax.swing.Timer(1000, new ActionListener() { //there is also java.awt.Timer package
			@Override
			public void actionPerformed(ActionEvent e) {
				headerLabel.setBackground(colors[colorIndex[0]]);
				int reverseIndex = colors.length - 1 - colorIndex[0];
				headerLabel.setForeground(colors[reverseIndex]);
				colorIndex[0] = (colorIndex[0] + 1) % colors.length;
			}
		});

		colorChangeTimer.start();
		
		// Adding Dashboard Text Panel in the top left corner of the frame
		JLabel dashboardIcon = new JLabel();
		ImageIcon dashboardImage = new ImageIcon("icons/dashboard.png");
		Image resizedDashboardImage = dashboardImage.getImage().getScaledInstance(250,150,Image.SCALE_SMOOTH);
		dashboardImage = new ImageIcon(resizedDashboardImage);
		dashboardIcon.setIcon(dashboardImage);
		dashboardIcon.setBounds(0,0,250,150);
		

		
		//adding insert button below dashboard panel 
		JButton insertButton = new JButton("INSERT");
		insertButton.setBounds(10, 154, 230, 117);
		ImageIcon buttonIcon = new ImageIcon("icons/insert.png");
		Image resized_icon = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Adjust size as needed
		buttonIcon = new ImageIcon(resized_icon);
		insertButton.setIcon(buttonIcon);
		insertButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text to the right of the icon
		insertButton.setVerticalTextPosition(SwingConstants.CENTER); // Center text vertically
		insertButton.setBackground(new Color(0, 150, 0));
		insertButton.setForeground(Color.WHITE);
		insertButton.setFocusPainted(false);
		insertButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Adding delete button below insert button
		JButton deleteButton = new JButton("DELETE");
		deleteButton.setBounds(10, 271, 230, 117);
		buttonIcon = new ImageIcon("icons/delete.png");
		resized_icon = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Adjust size as needed
		buttonIcon = new ImageIcon(resized_icon);
		deleteButton.setIcon(buttonIcon);
		deleteButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text to the right of the icon
		deleteButton.setVerticalTextPosition(SwingConstants.CENTER); // Center text vertically
		deleteButton.setBackground(new Color(150, 0, 0));
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setFocusPainted(false);
		deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Adding sell button
		JButton sellButton = new JButton("SELL");
		sellButton.setBounds(10, 388, 230, 117);
		buttonIcon = new ImageIcon("icons/sell.png");
		resized_icon = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Adjust size as needed
		buttonIcon = new ImageIcon(resized_icon);
		sellButton.setIcon(buttonIcon);
		sellButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		sellButton.setVerticalTextPosition(SwingConstants.CENTER);
		sellButton.setBackground(new Color(0, 0, 150));
		sellButton.setForeground(Color.WHITE);
		sellButton.setFocusPainted(false);
		sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Adding history button
		JButton historyButton = new JButton("HISTORY");
		historyButton.setBounds(10, 505, 230, 117);
		buttonIcon = new ImageIcon("icons/history.png");
		resized_icon = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Adjust size as needed
		buttonIcon = new ImageIcon(resized_icon);
		historyButton.setIcon(buttonIcon);
		historyButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		historyButton.setVerticalTextPosition(SwingConstants.CENTER);
		historyButton.setBackground(new Color(150, 75, 0));
		historyButton.setForeground(Color.WHITE);
		historyButton.setFocusPainted(false);
		historyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		//1st update notice : removed display button and added displayMedicine() to insert,update,delete methods
		//adding display button below delete button
		// JButton displayButton = new JButton("DISPLAY");
        // displayButton.setBounds(10,505,230,117);
		
		//adding table to the frame 
		String[] columns = {"Name","In Stock","Price","Total Sold","Update"};
		tableModel = new DefaultTableModel(columns,0);
		table = new JTable(tableModel){
			public boolean isCellEditable(int row,int column){
				return column == 4; //only that cell is editable which holds the update button 
			}
		};
		
		table.setBackground(new Color(15,10,25));
		table.setRowHeight(30);
		table.setForeground(Color.WHITE);
		table.setSelectionBackground(new Color(125,100,0,100));
		table.setSelectionForeground(Color.WHITE);
		table.setGridColor(new Color(155,0,50,100));
		JTableHeader header = table.getTableHeader();
		header.setBackground(new Color(15,10,25,180));
		header.setForeground(Color.WHITE);
		table.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setBackground(new Color(15,10,25));
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);
		table.setFillsViewportHeight(true);
		
		
		//adding button renderer for update button in each row of column 4, because JTable doesn't automatically render JButton or other components
		table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), table));
		
		DefaultTableCellRenderer center_renderer = new DefaultTableCellRenderer();
		center_renderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(center_renderer);
		table.getColumnModel().getColumn(1).setCellRenderer(center_renderer);
		table.getColumnModel().getColumn(2).setCellRenderer(center_renderer);
		table.getColumnModel().getColumn(3).setCellRenderer(center_renderer);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(new Color(15,10,25));
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
		scrollPane.setBounds(250,135,750,500);
		
		
		//initializing detailsArea [Error faced : Exception in thread "main" java.lang.NullPointerException: Cannot read field "parent" because "comp" is null]
		detailsArea = new JEditorPane();
		detailsArea.setBounds(1020,135,230,250);
		detailsArea.setEditable(false);
		detailsArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
		
		// Adding profile button in the top right corner of the frame
		JButton profileButton = new JButton();
		ImageIcon profileIcon = new ImageIcon("icons/profile.png");
		Image resizedProfileIcon = profileIcon.getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH);
		profileIcon = new ImageIcon(resizedProfileIcon);
		profileButton.setIcon(profileIcon);
		profileButton.setBorder(null);
		profileButton.setContentAreaFilled(false);
		profileButton.setFocusPainted(false);
		profileButton.setBackground(Color.BLACK);
		profileButton.setOpaque(true);
		profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		profileButton.setBounds(1150, 10, 80, 80);

		profileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				JMenuItem logoutItem = new JMenuItem("Logout");

				logoutItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Logout");
						new LoginPage();
						dispose();
					}
				});
				popupMenu.add(logoutItem);
				popupMenu.show(profileButton,0,profileButton.getHeight());
			}
		});
		
		// Adding dark mode button
		JButton darkModeButton = new JButton("Dark Mode");
		darkModeButton.setBounds(0,0,60,20);
		darkModeButton.setBackground(Color.BLACK);
		darkModeButton.setFocusPainted(false);
		darkModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		ImageIcon tempModeIcon = new ImageIcon("icons/darkMode.png");
		Image resized_tempModeIcon = tempModeIcon.getImage().getScaledInstance(60,20,Image.SCALE_SMOOTH);
		ImageIcon darkModeIcon = new ImageIcon(resized_tempModeIcon);
		tempModeIcon = new ImageIcon("icons/lightMode.png");
		resized_tempModeIcon = tempModeIcon.getImage().getScaledInstance(60,20,Image.SCALE_SMOOTH);
		ImageIcon lightModeIcon = new ImageIcon(resized_tempModeIcon);
		darkModeButton.setIcon(darkModeIcon);
		darkModeButton.setOpaque(true);
		
		darkModeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (darkModeButton.getIcon().equals(darkModeIcon)){
					darkModeButton.setIcon(lightModeIcon);
					getContentPane().setBackground(new Color(240, 240, 240));
				}else{
					darkModeButton.setIcon(darkModeIcon);
					getContentPane().setBackground(new Color(15, 10, 25, 180));
				}
			}
		});



		
		// Adding actions to the buttons
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // insertMedicine();
				openInsertDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMedicine();
            }
        });
		
		sellButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			openSellDialog();
			}
		});
		
		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			showHistory();
			}
		});
		
		//after 1st update :
		//adding mouse listener to the table to show details on row click
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				int selectedRow = table.getSelectedRow();
				if(selectedRow != -1){
					String medicineName = table.getValueAt(selectedRow,0).toString();
					showDetails(medicineName);
				}
			}
		});
		
		// Adding search bar
		JTextField searchBar = new JTextField();
		searchBar.setBounds(1020, 600, 200, 30);
		searchBar.setFont(new Font("Courier New", Font.BOLD, 16));
		searchBar.setForeground(Color.WHITE);
		searchBar.setBackground(new Color(15, 10, 25));
		searchBar.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		searchBar.setOpaque(true);

		// Adding search button
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(1120, 640, 100, 30);
		searchButton.setFont(new Font("Courier New", Font.BOLD, 16));
		searchButton.setForeground(Color.WHITE);
		searchButton.setBackground(new Color(0, 150, 0));
		searchButton.setFocusPainted(false);
		searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		searchButton.setOpaque(true);

		// Adding action listener to search button
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchQuery = searchBar.getText().toLowerCase();
				tableModel.setRowCount(0);
				for (String name : medicineData.keySet()) {
					if (name.toLowerCase().contains(searchQuery)) {
						String[] data = medicineData.get(name);
						tableModel.addRow(new Object[]{name, data[5], data[4], data[7], "Update"});
					}
				}
			}
		});

		// Adding key listener to search bar
		searchBar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String searchQuery = searchBar.getText().toLowerCase();
				tableModel.setRowCount(0);
				for (String name : medicineData.keySet()) {
					if (name.toLowerCase().contains(searchQuery)) {
						String[] data = medicineData.get(name);
						tableModel.addRow(new Object[]{name, data[5], data[4], data[7], "Update"});
					}
				}
			}
		});
		
		this.add(searchBar);
		this.add(searchButton);
		this.add(dashboardIcon);
		this.add(headerLabel);
		this.add(insertButton);
		this.add(deleteButton);
		this.add(sellButton);
		this.add(historyButton);
		this.add(scrollPane);
		this.add(detailsArea);
		this.add(profileButton);
		this.add(darkModeButton);
		loadDataFromFile();
		loadHistoryFromFile();
		this.setVisible(true);
	}

	
	//adding method to load data from file
	private void loadDataFromFile(){
		File file = new File(filePath);
		if(file.exists()){
			try(BufferedReader br = new BufferedReader(new FileReader(file))){
				String line;
				while((line = br.readLine()) != null){
					String[] values = line.split(",");
					String name = values[0];
					String[] data = {values[1],values[2],values[3],values[4],values[5],values[6],values[7],values[8]};
					medicineData.put(name,data);
				}
				displayMedicine();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	//adding method to save data to file
	private void saveDataToFile(){
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))){
			for(String name : medicineData.keySet()){
				String[] data = medicineData.get(name);
				String line = name + "," + String.join(",",data);
				bw.write(line);
				bw.newLine();
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private void saveHistoryToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("sellHistory.txt",true))) {
			for (String medicineName : sellHistory.keySet()) {
				String[] details = sellHistory.get(medicineName);
				writer.write(medicineName + "," + details[0] + "," + details[1] + "," + details[2]);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving history to file!");
		}
	}
	
	private void loadHistoryFromFile() {
		File file = new File("sellHistory.txt");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] values = line.split(",");
					if (values.length == 4) { // Ensure there are exactly 4 elements
						String medicineName = values[0];
						String price = values[1];
						String quantity = values[2];
						String totalPrice = values[3];
						sellHistory.put(medicineName, new String[]{price, quantity, totalPrice});
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error loading history from file!");
			}
		}
	}
	
	//method to open dialog box to insert new medicine details
	private void openInsertDialog(){
		JTextField medicineBox = new JTextField();
		JTextField companyBox = new JTextField();
		JSpinner expirySpinner = new JSpinner(new SpinnerDateModel());
		expirySpinner.setEditor(new JSpinner.DateEditor(expirySpinner, "yyyy-MM-dd"));
		JSpinner mfgSpinner = new JSpinner(new SpinnerDateModel());
		mfgSpinner.setEditor(new JSpinner.DateEditor(mfgSpinner, "yyyy-MM-dd"));
		JTextField detailsBox = new JTextField();
		JTextField priceBox = new JTextField();
		JTextField stockBox = new JTextField();
		JTextField discountBox = new JTextField();
		JTextField soldBox = new JTextField();

		Object[] message = {
			"Medicine Name:",medicineBox,
			"Company:",companyBox,
			"Expiry Date:",expirySpinner,
			"Manufacturing Date:",mfgSpinner,
			"Medicine Details:",detailsBox,
			"Price:",priceBox,
			"In Stock:",stockBox,
			"Discount:",discountBox,
			"Total Sold:",soldBox
		};

		int option = JOptionPane.showConfirmDialog(null,message,"Insert Medicine",JOptionPane.OK_CANCEL_OPTION);

		if(option == JOptionPane.OK_OPTION){
			String name = medicineBox.getText();
			String company = companyBox.getText();
			Date expiry = (Date) expirySpinner.getValue();
			SimpleDateFormat expiryFormat = new SimpleDateFormat("yyyy-MM-dd");
			String expiryString = expiryFormat.format(expiry);
			Date mfg = (Date) mfgSpinner.getValue();
			SimpleDateFormat mfgFormat = new SimpleDateFormat("yyyy-MM-dd");
			String mfgString = mfgFormat.format(mfg);
			String details = detailsBox.getText();
			String price = priceBox.getText();
			String stock = stockBox.getText();
			String discount = discountBox.getText();
			String sold = soldBox.getText();

			if(!name.isEmpty() && !stock.isEmpty() && !price.isEmpty() && !sold.isEmpty()){
				String[] data = {company,expiryString,mfgString,details,price,stock,discount,sold};
				medicineData.put(name,data);
				displayMedicine();
				saveDataToFile();
				JOptionPane.showMessageDialog(this, name + " added successfully!");
			}else{
				JOptionPane.showMessageDialog(this,"Fill all the required fields first");
			}
		}
	}
	
	
	private void updateMedicine(String name, String[] details) {
		JTextField updateMedicineBox = new JTextField(name);
		JTextField updateCompanyBox = new JTextField(details[0]);
		JSpinner updateExpirySpinner = new JSpinner(new SpinnerDateModel());
		updateExpirySpinner.setEditor(new JSpinner.DateEditor(updateExpirySpinner, "yyyy-MM-dd"));
		SimpleDateFormat expiryFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			updateExpirySpinner.setValue(expiryFormat.parse(details[1]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSpinner updateMfgSpinner = new JSpinner(new SpinnerDateModel());
		updateMfgSpinner.setEditor(new JSpinner.DateEditor(updateMfgSpinner, "yyyy-MM-dd"));
		try {
			updateMfgSpinner.setValue(expiryFormat.parse(details[2]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JTextField updateDetailsBox = new JTextField(details[3]);
		JTextField updatePriceBox = new JTextField(details[4]);
		JTextField updateStockBox = new JTextField(details[5]);
		JTextField updateDiscountBox = new JTextField(details[6]);
		JTextField updateSoldBox = new JTextField(details[7]);

		Object[] message = {
			"Medicine Name:", updateMedicineBox,
			"Company:", updateCompanyBox,
			"Expiry Date:", updateExpirySpinner,
			"Manufacturing Date:", updateMfgSpinner,
			"Medicine Details:", updateDetailsBox,
			"Price:", updatePriceBox,
			"In Stock:", updateStockBox,
			"Discount:", updateDiscountBox,
			"Total Sold:", updateSoldBox
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Update Medicine", JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			String updatedName = updateMedicineBox.getText();
			String updatedCompany = updateCompanyBox.getText();
			Date updatedExpiry = (Date) updateExpirySpinner.getValue();
			SimpleDateFormat updatedExpiryFormat = new SimpleDateFormat("yyyy-MM-dd");
			String updatedExpiryString = updatedExpiryFormat.format(updatedExpiry);
			Date updatedMfg = (Date) updateMfgSpinner.getValue();
			SimpleDateFormat updatedMfgFormat = new SimpleDateFormat("yyyy-MM-dd");
			String updatedMfgString = updatedMfgFormat.format(updatedMfg);
			String updatedDetails = updateDetailsBox.getText();
			String updatedPrice = updatePriceBox.getText();
			String updatedStock = updateStockBox.getText();
			String updatedDiscount = updateDiscountBox.getText();
			String updatedSold = updateSoldBox.getText();

			String[] updatedData = {updatedCompany, updatedExpiryString, updatedMfgString, updatedDetails, updatedPrice, updatedStock, updatedDiscount, updatedSold};

			medicineData.remove(name);  // Remove the old entry if the name is changed

			medicineData.put(updatedName, updatedData);

			displayMedicine();

			saveDataToFile();

			JOptionPane.showMessageDialog(this, updatedName + " updated successfully!");
		} else {
			JOptionPane.showMessageDialog(this, "Update canceled.");
		}
	}
	
	private void deleteMedicine(){
		int selectedRow = table.getSelectedRow();
		if(selectedRow != -1){
			String medicineName = table.getValueAt(selectedRow,0).toString();
			medicineData.remove(medicineName);
			tableModel.removeRow(selectedRow);
			detailsArea.setText("");
			saveDataToFile();
			JOptionPane.showMessageDialog(this,medicineName + " deleted successfully!");
		}else{
			JOptionPane.showMessageDialog(this, "no medicine is selected");
		}
	}
	
	private void openSellDialog() {
		String[] medicineNames = medicineData.keySet().toArray(new String[0]);
		JComboBox<String> medicineComboBox = new JComboBox<>(medicineNames);
		JTextField quantityField = new JTextField("0");
		JTextField discountField = new JTextField("0");
		JLabel priceLabel = new JLabel("Price: ");

		medicineComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String medicineName = (String) medicineComboBox.getSelectedItem();
				String price = medicineData.get(medicineName)[4];
				priceLabel.setText("Price: " + price);
			}
		});

		Object[] message = {
			"Select Medicine:", medicineComboBox,
			"Quantity:", quantityField,
			"", priceLabel,
			"Discount (%):", discountField
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Sell Medicine", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			String medicineName = (String) medicineComboBox.getSelectedItem();
			int quantity = Integer.parseInt(quantityField.getText());
			double discount = Double.parseDouble(discountField.getText());
			sellMedicine(medicineName, quantity, discount);
		}
	}
	
	private void sellMedicine(String medicineName,int quantity,double discount) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1){
			medicineName = table.getValueAt(selectedRow, 0).toString();
		}

		if (medicineData.containsKey(medicineName)) {
			String[] details = medicineData.get(medicineName);
			int stock = Integer.parseInt(details[5]);
			double price = Double.parseDouble(details[4]);

			if (quantity <= stock){
				double totalPrice = price * quantity * (1 - discount / 100);
				details[5] = String.valueOf(stock - quantity);
				details[7] = String.valueOf(Integer.parseInt(details[7]) + quantity); 

				medicineData.put(medicineName, details);
				saveDataToFile();

				// Update history
				updateHistory(medicineName, price, quantity, totalPrice);
				saveHistoryToFile();
					
				// Update table model
				displayMedicine();

				JOptionPane.showMessageDialog(this, "Sold " + quantity + " of " + medicineName + " for $" + totalPrice);
			}else{
				JOptionPane.showMessageDialog(this, "Not enough stock!");
			}
		}
	}
	
	private void updateHistory(String medicineName, double price, int quantity, double totalPrice) {
		String[] historyDetails = {String.valueOf(price), String.valueOf(quantity), String.valueOf(totalPrice)};
		sellHistory.put(medicineName, historyDetails);
	}
	
	private void showHistory() {
		StringBuilder history = new StringBuilder("<html><body>");
		for (String medicineName : sellHistory.keySet()) {
			String[] details = sellHistory.get(medicineName);
			history.append("Medicine: ").append(medicineName)
				.append(", Price: ").append(details[0])
				.append(", Quantity: ").append(details[1])
				.append(", Total: ").append(details[2])
				.append("<br>");
		}
		history.append("</body></html>");
		detailsArea.setContentType("text/html");
		detailsArea.setText(history.toString());
	}
	
	private void showDetails(String medicineName){
		if(medicineData.containsKey(medicineName)){
			String[] details = medicineData.get(medicineName);
			
			//using HTML to style text(color,font size etc.) in the detailsArea
			String text = "<html><body style='font-size:12px;'>"
            + "<span style='color:blue;'><b>Medicine Name:</b></span> " + medicineName + "<br>"
            + "<span style='color:green;'><b>Company:</b></span> " + details[0] + "<br>"
            + "<span style='color:red;'><b>Expiry Date:</b></span> " + details[1] + "<br>"
            + "<span style='color:purple;'><b>Manufacturing Date:</b></span> " + details[2] + "<br>"
            + "<span style='color:orange;'><b>Medicine Details:</b></span> " + details[3] + "<br>"
            + "<span style='color:green;'><b>Price:</b></span> " + details[4] + "<br>"
            + "<span style='color:teal;'><b>In Stock:</b></span> " + details[5] + "<br>"
            + "<span style='color:red;'><b>Discount:</b></span> " + details[6] + "<br>"
            + "<span style='color:green;'><b>Total Sold:</b></span> " + details[7] + "<br>"
            + "</body></html>";
			
			detailsArea.setContentType("text/html");
			detailsArea.setText(text);
		}
	}
	
	private void displayMedicine(){
		//clearing the existing rows in table
		tableModel.setRowCount(0);
		//Looping through the medicineData HashMap
		//adding each entry as a row in the table 
		for(String name : medicineData.keySet()){
			String[] data = medicineData.get(name);
			tableModel.addRow(new Object[]{name,data[5],data[4],data[7],"Update"});
		}
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer{
		public ButtonRenderer(){
			setOpaque(true);
			ImageIcon icon = new ImageIcon("icons/update.png");
			Image img = icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
			icon = new ImageIcon(img);
			setIcon(icon);
		}
		@Override
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
			if(isSelected){
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			}else{
				setForeground(table.getForeground());
				setBackground(new Color(245,245,125,100));
			}
			setText((value==null) ? "Update" : value.toString());
			return this;
		}
	}
	class ButtonEditor extends DefaultCellEditor{
		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable table;
		private int row;
	
		public ButtonEditor(JCheckBox checkBox,JTable table){
			super(checkBox);
			this.table = table;
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					fireEditingStopped();
					String medicineName = table.getValueAt(row,0).toString();
					String[] details = medicineData.get(medicineName);
					updateMedicine(medicineName,details);
				}
			});
		}
	
		@Override
		public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column){
			label = (value==null) ? "Update" : value.toString();
			button.setText(label);
			isPushed = true;
			this.row = row;
			return button;
		}
	
		@Override
		public Object getCellEditorValue(){
			return label;
		}
	}
}
