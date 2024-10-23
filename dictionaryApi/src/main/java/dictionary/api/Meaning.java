package dictionary.api;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Meaning
{
	private String partOfSpeech;
	private List<Definition> definitions;
	private List<String> synonyms;
	private List<String> antonyms;

	public List<String> getSynonyms()
	{
		return synonyms;
	}

	public List<String> getAntonyms()
	{
		return antonyms;
	}

	public String getPartOfSpeech()
	{
		return partOfSpeech;
	}

	public List<Definition> getDefinitions()
	{
		return definitions;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
