package jmr.vaadin.weather.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@SpringUI( path = "/weather" )
@Title( "Vaadin Weather" )
@Theme( "valo" )
public class WeatherView extends UI
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void init( VaadinRequest request )
	{
		// TODO Auto-generated method stub

	}

}
