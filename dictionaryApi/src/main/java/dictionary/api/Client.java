package dictionary.api;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ProtocolException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client implements Closeable
{
	private static final String URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

	private CloseableHttpClient httpClient = HttpClients.createDefault();
	private ObjectMapper mapper = new ObjectMapper();
	
	private int rateLimit = Integer.MAX_VALUE;
	private int remainingRequests = Integer.MAX_VALUE;
	private long rateLimitReset = Long.MAX_VALUE;

	public Response performLookup(String word)
	{
		if(remainingRequests == 0 && System.currentTimeMillis() > rateLimitReset)
		{
			return new RateLimitResponse.Builder()
					.withRateLimit(rateLimit)
					.withRateLimitReset(rateLimitReset)
					.withRemainingRequests(remainingRequests)
					.build();
		}
		
		HttpGet httpget = new HttpGet(URL + word);

		try
		{
			return httpClient.execute(httpget, response -> 
			{
				Response.Builder<?, ?> builder;
				
				switch(response.getCode())
				{
					case HttpStatus.SC_OK:
						builder = parseOkResponse(response);
						break;
					case HttpStatus.SC_NOT_FOUND:
						builder = parseNotFound(response);
						break;
					default:
						ErrorResponse.Builder erb = new ErrorResponse.Builder();
						builder = erb
							.withMessage("Unknown response code : " + response.getCode());
				}

				return builder.build();
			});
		}
		catch (IOException e)
		{
			InternalErrorResponse.Builder erb = new InternalErrorResponse.Builder();
			
			parseException(e, erb);
			
			return erb.build();
		}
	}
	
	private Response.Builder<?, ?> parseNotFound(ClassicHttpResponse response)
	{
		return parseResponse(response, this::parseNotFound, this::parseException);
	}

	@FunctionalInterface
	public interface ResposeBuilder
	{
		Response.Builder<? extends Response,?> parse(HttpEntity entity, InputStream is) throws Exception;
	}
	
	private Response.Builder<? extends Response, ?> parseNotFound(HttpEntity entity, InputStream is) throws StreamReadException, DatabindException, IOException
	{
		@SuppressWarnings("unchecked")
		Map<String,Object> response = mapper.readValue(is, Map.class);
		
		Object message = response.get("message");
		Object resolution = response.get("resolution");
		Object title = response.get("title");
		
		message = message == null ? "No message" : message;
		resolution = resolution == null ? "No resolution" : resolution;
		title = title == null ? "No title" : title;
		
		return new ErrorResponse.Builder()
				.withMessage((String)message)
				.withResolution((String) resolution)
				.withTitle((String) title);
	}
	
	private Response.Builder<? extends Response,?> parseResponse(ClassicHttpResponse response, ResposeBuilder builder, BiConsumer<Exception, InternalErrorResponse.Builder> errorBuilder)
	{
		HttpEntity entity = response.getEntity();
		
		Response.Builder<? extends Response,?> rb;
		
		try (InputStream is = entity.getContent())
		{
			rb = builder.parse(entity, is); 
		}
		catch (Exception e)
		{
			InternalErrorResponse.Builder eb = new InternalErrorResponse.Builder();
			
			errorBuilder.accept(e, eb);
			
			rb = eb;
		}

		try
		{
			rateLimit = Integer.parseInt(response.getHeader("x-ratelimit-limit").getValue());
			remainingRequests = Integer.parseInt(response.getHeader("x-ratelimit-remaining").getValue());
			rateLimitReset = Long.parseLong(response.getHeader("x-ratelimit-reset").getValue());
			
			return rb.withRateLimit(rateLimit)
				.withRateLimitReset(rateLimitReset)
				.withRemainingRequests(remainingRequests);
		}
		catch (NumberFormatException | ProtocolException e)
		{
			return new ErrorResponse.Builder()
					.withMessage(e.getMessage())
					.withResolution("Contact developer")
					.withTitle("Internal client error");
		}
	}
	
	private Response.Builder<? extends Response,?> parseOkResponse(ClassicHttpResponse response)
	{
		return parseResponse(response, (e,is) -> new RecordResponse.Builder()
				.withRecord(mapper.readValue(is, Record.class)),
				this::parseException);
	}

	public int getRateLimit()
	{
		return rateLimit;
	}
	
	@Override
	public void close() throws IOException
	{
		httpClient.close();
	}

	public static void main(String[] args) throws UnsupportedOperationException, IOException
	{
		try(Client client = new Client())
		{
			Response response = client.performLookup("l;kjasdf");

			switch(response.getType())
			{
				case ERROR:
					System.err.println(((ErrorResponse)response).getMessage());
					break;
				case OK:
					System.out.println(((RecordResponse)response).getRecord());
					break;
				case RATE_LIMIT:
					System.err.println("Stop requesting until " + response.getRateLimitReset());
					break;
				default:
					System.err.println(response);
					break;
			}
			
			System.out.printf("Remaining requests %d%n", response.getRemainingRequests());
		}
	}

	private void parseException(Exception e, InternalErrorResponse.Builder b)
	{
		b.withCause(e);
	}
}
