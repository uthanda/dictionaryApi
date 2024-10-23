package dictionary.api;

public class RecordResponse extends Response
{
	private final Record record;

	private RecordResponse(Builder builder)
	{
		super(builder);

		this.record = builder.record;
	}

	public Record getRecord()
	{
		return record;
	}

	public static final class Builder extends Response.Builder<RecordResponse, Builder>
	{
		private Record record;

		protected Builder()
		{
			super(ResponseType.OK, RecordResponse::new);
		}

		public Builder withRecord(Record record)
		{
			this.record = record;
			return this;
		}

		public RecordResponse build()
		{
			return new RecordResponse(this);
		}
	}
}
