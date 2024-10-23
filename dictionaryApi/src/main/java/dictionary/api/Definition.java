package dictionary.api;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Definition
{
	private String definition;
	private String example;
	private List<String> synonyms;
	private List<String> antonyms;

	public String getExample()
	{
		return example;
	}

	public String getDefinition()
	{
		return definition;
	}

	public List<String> getSynonyms()
	{
		return synonyms;
	}

	public List<String> getAntonyms()
	{
		return antonyms;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
