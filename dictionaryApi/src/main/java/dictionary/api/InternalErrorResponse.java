package dictionary.api;

public class InternalErrorResponse extends Response
{
	private final Exception cause;

	public Exception getCause()
	{
		return cause;
	}

	private InternalErrorResponse(Builder builder)
	{
		super(builder);
		this.cause = builder.cause;
	}
	
	public static final class Builder extends Response.Builder<InternalErrorResponse, Builder>
	{
		private Exception cause;
		
		public Builder()
		{
			super(ResponseType.INTERNAL_ERROR, InternalErrorResponse::new);
		}

		public Builder withCause(Exception cause)
		{
			this.cause = cause;
			return this;
		}
	}
}
