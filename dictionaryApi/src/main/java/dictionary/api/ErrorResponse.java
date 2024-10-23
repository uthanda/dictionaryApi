package dictionary.api;

public class ErrorResponse extends Response
{
	private final String title;
	private final String message;
	private final String resolution;

	private ErrorResponse(Builder builder)
	{
		super(builder);
		this.title = builder.title;
		this.message = builder.message;
		this.resolution = builder.resolution;
	}
	
	public String getTitle()
	{
		return title;
	}

	public String getMessage()
	{
		return message;
	}

	public String getResolution()
	{
		return resolution;
	}

	public static final class Builder extends Response.Builder<ErrorResponse, Builder>
	{
		public Builder()
		{
			super(ResponseType.ERROR, ErrorResponse::new);
		}

		private String title;
		private String message;
		private String resolution;

		public Builder withTitle(String title)
		{
			this.title = title;
			return this;
		}

		public Builder withMessage(String message)
		{
			this.message = message;
			return this;
		}

		public Builder withResolution(String resolution)
		{
			this.resolution = resolution;
			return this;
		}
	}
}
