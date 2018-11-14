package jmr.vaadin.weather.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import jmr.vaadin.weather.WeatherService;

@SpringUI( path = "/weather" )
@Title( "Vaadin Weather" )
@Theme( "valo" )
public class WeatherView extends UI
{
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	private NativeSelect<String> unitSelect;
	private TextField cityTextField;
	private Button cityButton;

	private HorizontalLayout resultLayout;
	private Label currentLocationLabel;
	private Label currentTemperatureLabel;
	private String sourceURL;
	private ExternalResource currentImage;
	private Embedded currentEmbedded;

	@Autowired
	private WeatherService weatherService;

	@Override
	protected void init( VaadinRequest request )
	{
		setupLayout();
		setHeader();
		setLogo();
		setupForm();

		cityButton.addClickListener(e -> {
			if (cityTextField.getValue().isEmpty())
			{
				Notification.show("Please, enter a city");
			}
			else
			{
				updateUI();
			}
		});

	}

	private void updateUI()
	{
		String cityName = cityTextField.getValue();
		String units = unitSelect.getValue();
		System.out.println("Input city name: " + cityName);
		System.out.println("Input units: " + units);

		try
		{
			double temperature = weatherService.getTemperature(cityName, units);
			String location = weatherService.getLocation(cityName, units);
			String icon = weatherService.getIcon(cityName, units);

			if (resultLayout != null)
			{
				mainLayout.removeComponent(resultLayout);
			}
			setupDashboard(location, icon, temperature, units);
		}
		catch (JSONException | IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	private void setupLayout()
	{
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setContent(mainLayout);
	}

	private void setHeader()
	{
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		Label labelTitle = new Label("Weather");
		labelTitle.addStyleName(ValoTheme.LABEL_H1);
		labelTitle.addStyleName(ValoTheme.LABEL_BOLD);
		labelTitle.addStyleName(ValoTheme.LABEL_COLORED);
		headerLayout.addComponent(labelTitle);
		mainLayout.addComponent(headerLayout);
	}

	private void setLogo()
	{
		HorizontalLayout logoLayout = new HorizontalLayout();
		logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Image logo = new Image(null, new ClassResource("/WeatherIcon-480x480.png"));
		logo.setWidth("125px");
		logo.setHeight("125px");

		logoLayout.addComponent(logo);
		mainLayout.addComponent(logoLayout);
	}

	private void setupForm()
	{
		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);

		unitSelect = new NativeSelect<>();
		unitSelect.setWidth("80px");
		List<String> items = new ArrayList<>();
		items.add("C");
		items.add("F");
		unitSelect.setItems(items);
		unitSelect.setValue(items.get(0));
		formLayout.addComponent(unitSelect);

		cityTextField = new TextField();
		cityTextField.setWidth("80%");
		formLayout.addComponent(cityTextField);

		cityButton = new Button();
		cityButton.setIcon(VaadinIcons.SEARCH);
		formLayout.addComponent(cityButton);

		mainLayout.addComponent(formLayout);
	}

	private void setupDashboard( String location, String icon, double temp, String units )
	{
		resultLayout = new HorizontalLayout();
		resultLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		currentLocationLabel = new Label("Currently in " + location);
		currentLocationLabel.addStyleName(ValoTheme.LABEL_H2);
		currentLocationLabel.addStyleName(ValoTheme.LABEL_LIGHT);

		sourceURL = "http://openweathermap.org/img/w/" + icon + ".png";
		currentImage = new ExternalResource(sourceURL);
		currentEmbedded = new Embedded(null, currentImage);

		currentTemperatureLabel = new Label(temp + units);
		currentTemperatureLabel.addStyleName(ValoTheme.LABEL_H1);
		currentTemperatureLabel.addStyleName(ValoTheme.LABEL_BOLD);
		currentTemperatureLabel.addStyleName(ValoTheme.LABEL_LIGHT);

		resultLayout.addComponent(currentLocationLabel);
		resultLayout.addComponent(currentEmbedded);
		resultLayout.addComponent(currentTemperatureLabel);
		mainLayout.addComponent(resultLayout);
	}

}
