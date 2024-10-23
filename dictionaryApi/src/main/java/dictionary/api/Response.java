package dictionary.api;

import java.util.function.Function;

public abstract class Response
{
	private final ResponseType type;
	private final int rateLimit;
	private final int remainingRequests;
	private final long rateLimitReset;

	protected Response(Builder<?,?> builder)
	{
		this.type = builder.type;
		this.rateLimit = builder.rateLimit;
		this.remainingRequests = builder.remainingRequests;
		this.rateLimitReset = builder.rateLimitReset;
	}

	public ResponseType getType()
	{
		return type;
	}

	public int getRateLimit()
	{
		return rateLimit;
	}

	public int getRemainingRequests()
	{
		return remainingRequests;
	}

	public long getRateLimitReset()
	{
		return rateLimitReset;
	}

	public abstract static class Builder<T extends Response, B extends Builder<T,B>>
	{
		private final ResponseType type;
		private final Function<B, T> creator;

		private int rateLimit;
		private int remainingRequests;
		private long rateLimitReset;

		protected Builder(ResponseType type, Function<B,T> creator)
		{
			this.creator = creator;
			this.type = type;
		}

		@SuppressWarnings("unchecked")
		public B withRateLimit(int rateLimit)
		{
			this.rateLimit = rateLimit;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public B withRemainingRequests(int remainingRequests)
		{
			this.remainingRequests = remainingRequests;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public B withRateLimitReset(long rateLimitReset)
		{
			this.rateLimitReset = rateLimitReset;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public T build()
		{
			return creator.apply((B) this);
		}
	}
}
