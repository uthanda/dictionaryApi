package dictionary.api;

public class Phonetic
{
	private String text;
	private String audio;
	private String sourceUrl;
	private License license;

	public String getText()
	{
		return text;
	}

	public String getAudio()
	{
		return audio;
	}

	public String getSourceUrl()
	{
		return sourceUrl;
	}

	public License getLicense()
	{
		return license;
	}
}
