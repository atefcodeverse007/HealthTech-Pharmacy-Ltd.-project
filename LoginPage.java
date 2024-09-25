import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoginPage extends JFrame{
	JLabel bg;
	ImageIcon bg_img;
	// JPanel headerPanel;
	int colorIndex = 0;
	JPanel loginPanel;
	JLabel headerText;
	Timer colorTimer;
	Color[] colors = {Color.BLUE,Color.PINK,Color.GREEN,Color.RED,Color.MAGENTA};
	JLabel userText;
	JLabel passText;
	JTextField userBox;
	JPasswordField passBox;
	JCheckBox rememberMe;
	JButton loginButton;
	JButton signupButton;
	
	LoginPage(){
		//creating the frame
		this.setSize(1280,720);
		this.setTitle("Login Page");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		//setting background Image
		bg_img = new ImageIcon("bg.jpg");
		Image temp1 = bg_img.getImage();
		Image temp2 = temp1.getScaledInstance(1280,720,Image.SCALE_SMOOTH);//resizing the image
		bg_img = new ImageIcon(temp2);
		bg = new JLabel();
		bg.setBounds(0,0,1280,720);
		bg.setIcon(bg_img);//setting the image on the background label
		
		//adding header Panel to the frame
		// headerPanel = new JPanel();
		// headerPanel.setLayout(null);
		// headerPanel.setBounds(0,30,1280,100);
		// headerPanel.setBackground(new Color(0,0,0,50));
		// headerPanel.setOpaque(false);
		
		//adding header text in header Panel
		
		
		//adding color changing effect in header text
		colorTimer = new Timer(1000,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				colorIndex = (colorIndex + 1) % colors.length;
				headerText.setForeground(colors[colorIndex]);
			}
		});
		colorTimer.start();
		
		//creating login panel
		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		loginPanel.setBounds(440,160,400,320);
		loginPanel.setBackground(new Color(240,240,255,180));
		loginPanel.setOpaque(true);
		
		//setting username,password labels and fields on loginPanel
		Font f = new Font("Arial",Font.BOLD,20);
		Color c = new Color(0,15,0,95);
		
		//adding header text in login panel
		headerText = new JLabel("HealthTech Pharmacy Ltd",SwingConstants.CENTER);
		headerText.setBounds(0,0,400,50);
		headerText.setFont(new Font("Arial",Font.BOLD,30));
		headerText.setOpaque(true);
		loginPanel.add(headerText);
		
		userText = new JLabel("Username :");
		userText.setBounds(50,70,130,30);
		userText.setFont(f);
		userText.setForeground(c);
		loginPanel.add(userText);

		passText = new JLabel("Password :");
		passText.setBounds(50,140,130,30);
		passText.setFont(f);
		passText.setForeground(c);
		loginPanel.add(passText);

		userBox = new JTextField();
		userBox.setBounds(180,70,170,30);
		loginPanel.add(userBox);
		
		passBox = new JPasswordField();
		passBox.setBounds(180,140,170,30);
		loginPanel.add(passBox);
		
		
		//adding checkbox [Remember User] on loginPanel
		rememberMe = new JCheckBox("Remember Me");
		rememberMe.setBounds(215,190,135,25);
		rememberMe.setFont(f.deriveFont(Font.PLAIN,16));
		rememberMe.setForeground(c);
		rememberMe.setBackground(new Color(240,240,255,180));
		loginPanel.add(rememberMe);
		
		//adding loginButton to loginPanel
		loginButton = new JButton("Log in");
		loginButton.setBounds(250,235,100,40);
		loginButton.setFont(f.deriveFont(Font.BOLD,18));
		loginButton.setForeground(Color.WHITE);
		loginButton.setBackground(new Color(66,103,178));
		loginButton.setFocusPainted(false);
		loginButton.setBorder(BorderFactory.createEmptyBorder());
		loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		loginPanel.add(loginButton);
		
		//adding inner class for handling LoginDetails through .txt file 
		class LoginDetails{
			private HashMap<String,String> users;
			public LoginDetails(String Filename){
				users = new HashMap<>();
				try(BufferedReader br = new BufferedReader(new FileReader(Filename))){
					String line;
					while((line = br.readLine()) != null){
						String[] combination = line.split(",");
						users.put(combination[0],combination[1]);
					}
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
			public boolean isValidUser(String username,String password){
				return users.containsKey(username) && users.get(username).equals(password);
			}
		}
		
		//adding action to login button
		loginButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				//getting entered username and password 
				String enteredUsername = userBox.getText();
				char[] pass = passBox.getPassword();
				String enteredPassword = new String(pass);
				
				//getting username and password from Notepad file 
				LoginDetails loginDetails = new LoginDetails("login.txt");
				
				if(loginDetails.isValidUser(enteredUsername,enteredPassword)){
					JOptionPane.showMessageDialog(LoginPage.this,"Login Successful");
					if(rememberMe.isSelected()){
						saveCredentials(enteredUsername,enteredPassword);
					}else{
						clearSavedCredentials();
					}
					new Dashboard();
					dispose();
				} else {
					JOptionPane.showMessageDialog(LoginPage.this,"Login Failed");
				}
			}
		});
		
		
		//adding signup button to login panel 
		signupButton = new JButton("Sign Up");
		signupButton.setBounds(65,235,100,40);
		signupButton.setFont(f.deriveFont(Font.BOLD,18));
		signupButton.setForeground(Color.WHITE);
		signupButton.setBackground(new Color(34,139,34));
		signupButton.setFocusPainted(false);
		signupButton.setBorder(BorderFactory.createEmptyBorder());
		signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		loginPanel.add(signupButton);
		
		//adding action to signup button
		signupButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new Register();
			}
		});
		
		this.add(loginPanel);
		this.add(bg);
		this.setVisible(true);
		
		loadSavedCredentials();
	}
	
	//rememberMe checkBox methods
    private void saveCredentials(String username,String password){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("loginData.txt"))) {
            writer.write(username + "\n");
            writer.write(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load saved credentials from file
    private void loadSavedCredentials() {
        try {
            if (Files.exists(Paths.get("loginData.txt"))){
                BufferedReader reader = new BufferedReader(new FileReader("loginData.txt"));
                String username = reader.readLine();
                String password = reader.readLine();
                if (username != null && password != null) {
                    userBox.setText(username);
                    passBox.setText(password);
                    rememberMe.setSelected(true);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	// Clear the saved credentials from file
    private void clearSavedCredentials() {
        try {
            Files.deleteIfExists(Paths.get("loginData.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	//adding register user method
	private void registerUser(String username, String fullName, String password, String reEnterPassword, String email, String phoneNumber) {
        if (password.equals(reEnterPassword)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("profiles.txt", true))) {
                writer.write(username + "," + fullName + "," + password + "," + email + "," + phoneNumber + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving profile to file!");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("login.txt", true))) {
                writer.write(username + "," + password + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving login credentials to file!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
        }
    }
	
	//adding inner class for registering User Details in .txt file
	class Register{
		JFrame signUp;
		JLabel regUserText;
		JLabel regPassText;
		JLabel regFullNameText;
		JLabel regEmailText;
		JLabel regPhoneText;
		JTextField regUserBox;
		JPasswordField regPassBox;
		JTextField regFullNameBox;
		JTextField regEmailBox;
		JTextField regPhoneBox;
		JButton registerButton;

		Register(){
			signUp = new JFrame("Sign Up");
			signUp.setSize(400,400);
			signUp.setLocationRelativeTo(null);
			signUp.setLayout(null);
			signUp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			signUp.setResizable(true);

			//adding username label
			regUserText = new JLabel("Username : ");
			regUserText.setBounds(50,50,120,30);
			signUp.add(regUserText);

			//adding password label
			regPassText = new JLabel("Password : ");
			regPassText.setBounds(50,100,120,30);
			signUp.add(regPassText);

			//adding full name label
			regFullNameText = new JLabel("Full Name : ");
			regFullNameText.setBounds(50,150,120,30);
			signUp.add(regFullNameText);

			//adding email label
			regEmailText = new JLabel("Email (optional) : ");
			regEmailText.setBounds(50,200,120,30);
			signUp.add(regEmailText);

			//adding phone number label
			regPhoneText = new JLabel("Phone #(optional):");
			regPhoneText.setBounds(50,250,120,30);
			signUp.add(regPhoneText);

			//adding username textfield
			regUserBox = new JTextField();
			regUserBox.setBounds(180,50,150,30);
			signUp.add(regUserBox);

			//adding password textfield 
			regPassBox = new JPasswordField();
			regPassBox.setBounds(180,100,150,30);
			signUp.add(regPassBox);

			//adding full name textfield
			regFullNameBox = new JTextField();
			regFullNameBox.setBounds(180,150,150,30);
			signUp.add(regFullNameBox);

			//adding email textfield
			regEmailBox = new JTextField();
			regEmailBox.setBounds(180,200,150,30);
			signUp.add(regEmailBox);

			//adding phone number textfield
			regPhoneBox = new JTextField();
			regPhoneBox.setBounds(180,250,150,30);
			signUp.add(regPhoneBox);

			//adding register button 
			registerButton = new JButton("Register");
			registerButton.setBounds(140,300,100,40);
			registerButton.setForeground(Color.WHITE);
			registerButton.setBackground(new Color(66,103,178));
			signUp.add(registerButton);

			//adding action to register button 
			registerButton.addActionListener(new ActionListener(){
				@Override 
				public void actionPerformed(ActionEvent e){
					String newUsername = regUserBox.getText();
					char[] tempPassword = regPassBox.getPassword();
					String newPassword = new String(tempPassword);
					String fullName = regFullNameBox.getText();
					String email = regEmailBox.getText();
					String phoneNumber = regPhoneBox.getText();
					if(!newUsername.isEmpty() && !newPassword.isEmpty()){
						registerUser(newUsername, fullName, newPassword, newPassword, email, phoneNumber);
						JOptionPane.showMessageDialog(signUp,"User Registered Successfully!");
						signUp.dispose();
					} else {
						JOptionPane.showMessageDialog(signUp,"Please Enter Valid Details");
					}
				}
			});    
			signUp.setVisible(true);
		}
	}
}