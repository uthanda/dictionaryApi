package dictionary.api;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Word
{
	private String word;
	private String phonetic;
	private List<Phonetic> phonetics;
	private List<Meaning> meanings;
	private License license;
	private List<String> sourceUrls;

	public String getWord()
	{
		return word;
	}

	public String getPhonetic()
	{
		return phonetic;
	}

	public List<Phonetic> getPhonetics()
	{
		return phonetics;
	}

	public List<Meaning> getMeanings()
	{
		return meanings;
	}

	public License getLicense()
	{
		return license;
	}

	public List<String> getSourceUrls()
	{
		return sourceUrls;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
