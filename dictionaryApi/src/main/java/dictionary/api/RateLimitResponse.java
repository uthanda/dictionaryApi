package dictionary.api;

public class RateLimitResponse extends Response
{
	private RateLimitResponse(Builder builder)
	{
		super(builder);
	}

	public static final class Builder extends Response.Builder<RateLimitResponse, Builder>
	{
		public Builder()
		{
			super(ResponseType.RATE_LIMIT, RateLimitResponse::new);
		}
	}
}
