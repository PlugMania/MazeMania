package info.plugmania.mazemania;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Lang {

	MazeMania plugin;
	private static Lang lang_instance;
	
	private static final String MESSAGES = "resources/messages";
	private final transient Locale defaultLocale = Locale.getDefault();
	private transient Locale userLocale = defaultLocale;
	private transient ResourceBundle userBundle;
	private transient ResourceBundle localeBundle;
	private transient ResourceBundle defaultBundle;
	public Lang(MazeMania instance) {
		plugin = instance;
		lang_instance = this;
		
		localeBundle = ResourceBundle.getBundle(MESSAGES, defaultLocale);
		userBundle = ResourceBundle.getBundle(MESSAGES, userLocale);
		defaultBundle = ResourceBundle.getBundle(MESSAGES, Locale.ENGLISH);
	}
	
	public void updateLocale(final String loc)
	{
		if (loc == null || loc.isEmpty())
		{
			return;
		}
		final String[] parts = loc.split("[_\\.]");
		if (parts.length == 1)
		{
			userLocale = new Locale(parts[0]);
		}
		if (parts.length == 2)
		{
			userLocale = new Locale(parts[0], parts[1]);
		}
		if (parts.length == 3)
		{
			userLocale = new Locale(parts[0], parts[1], parts[2]);
		}
		ResourceBundle.clearCache();
		userBundle = ResourceBundle.getBundle(MESSAGES, userLocale);
	}

	
	public static String _(final String string)
	{
		if (lang_instance == null)
			return "";
		else
			return lang_instance.translate(string);
	}
	
	public String translate(final String string)
	{
		try
		{
			try
			{
				return userBundle.getString(string);
			}
			catch (MissingResourceException ex)
			{
				return localeBundle.getString(string);
			}
		}
		catch (MissingResourceException ex)
		{
			Util.log("Missing translation key in translation file"); //Must never be put into properties since a loop can be created
			return defaultBundle.getString(string);
		}
	}
	
}
