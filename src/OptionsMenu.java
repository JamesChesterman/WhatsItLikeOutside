import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class OptionsMenu extends JFrame{
	JLabel longLabel, latLabel;
	JLabel cityLabel;
	JLabel changingLabel;
	JTextField longTextField, latTextField;
	JTextField cityTextField;
	JButton refreshButton;
	
	public OptionsMenu(String mode, MainSurface mainSurface) {
		// TODO Auto-generated constructor stub
		//MAKE IT SO YOU CAN DO EITHER LONGITUDE LATITUDE OR USE CITY
		changingLabel = new JLabel("Changing: ");
		changingLabel.setBounds(10, 10, 100, 30);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		if(mode == "latLong") {
			comboBox.addItem("Latitude + Longitude");
			comboBox.addItem("City");
			
			longLabel = new JLabel("Longitude:");
			longLabel.setBounds(10, 50, 100, 30);
			
			longTextField = new JTextField(WeatherCalculator.GetLongitude());
			longTextField.setBounds(120, 50, 200, 30);
			
			latLabel = new JLabel("Latitude");
			latLabel.setBounds(10, 90, 100, 30);
			
			latTextField = new JTextField(WeatherCalculator.GetLatitude());
			latTextField.setBounds(120, 90, 200, 30);
			
			add(longLabel);
			add(longTextField);
			add(latLabel);
			add(latTextField);
		}else {
			comboBox.addItem("City");
			comboBox.addItem("Latitude + Longitude");
			
			cityLabel = new JLabel("City: ");
			cityLabel.setBounds(10, 50, 100, 30);
			
			cityTextField = new JTextField(WeatherCalculator.GetCity());
			cityTextField.setBounds(120, 50, 200, 30);
			
			add(cityLabel);
			add(cityTextField);
		}
		comboBox.setBounds(120, 10, 200, 30);
		comboBox.setBackground(Color.PINK);
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Gets which option the user has clicked from the combo box
				String option = (String)((JComboBox<String>) e.getSource()).getSelectedItem();
				OptionsMenu optionsMenu;
				if(option == "City") {
					optionsMenu = new OptionsMenu("city", mainSurface);
				}else {
					optionsMenu = new OptionsMenu("latLong", mainSurface);
				}
				optionsMenu.setVisible(true);
				setVisible(false);
				
			}
		});
		
		refreshButton = new JButton("Refresh");
		refreshButton.setBounds(10, 130, 300, 30);
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mode == "latLong") {
					mainSurface.RefreshWeather(mode, latTextField.getText(), longTextField.getText(), "");
				}else {
					mainSurface.RefreshWeather(mode, "", "", cityTextField.getText());
				}
				setVisible(false);
				//mainSurface.RefreshWeather(mode, );
			}
		});
		
		add(refreshButton);
		add(comboBox);
		add(changingLabel);
		setTitle("Options");
		setSize(400, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);
	}
	

}
