package app1;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.*;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.mongodb.Mongo;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.ResourceBundle;
import org.springframework.dao.DataAccessException;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Properties;
import org.springframework.scheduling.annotation.Scheduled;
/*
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
*/


@RestController
@RequestMapping("/api/v1")
public class ControllerClass{
	static int moderator_id_counter=123456;
	static int poll_id_counter;
	String username="foo";
	String password="bar";
	String authString = username + ":" + password;
	byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
	String authStringEnc = new String(authEncBytes);

    
    final static String TOPIC = "test";
/*
  
 //PRODUCER
 
 final static String TOPIC = "test_topic";
 Properties props = new Properties();
 properties.put("metadata.broker.list","localhost:9092");
 props.put("serializer.class", "kafka.serializer.StringEncoder");
 props.put("partitioner.class", "example.producer.SimplePartitioner");
 props.put("request.required.acks", "1");
 ProducerConfig config = new ProducerConfig(props);
 Producer<String, String> producer = new Producer<String, String>(config);
 SimpleDateFormat sdf = new SimpleDateFormat();
 KeyedMessage<String, String> message =new KeyedMessage<String, String>(TOPIC,"Test message from java program " + sdf.format(new Date()));
 producer.send(data);
 producer.close();
 
 
 //CONSUMER
 
 Properties properties = new Properties();
 properties.put("zookeeper.connect","localhost:2181");
 properties.put("group.id","test-group");
 ConsumerConfig consumerConfig = new ConsumerConfig(properties);
 consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
 
*/

	private static final String DB_User="voteDBadmin";
    private static final String DB_Password="voteDBpassword";
    private static final String DB_Name="voteDB";
    private static final String Server="localhost";
    int Port=27017;
    UserCredentials userCredentials ;
	MongoOperations mongoOps;
	DBCollection collection;

	public ControllerClass() throws Exception
    {
        this.mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(),"voteDB"));
		this.collection = this.mongoOps.getCollection("ModeratorCollection");

	}

	//Create moderator
	@RequestMapping (value="/moderators", method=RequestMethod.POST, headers={"content-type=application/json"})
	public @ResponseBody Moderator create_Moderator(@Valid @RequestBody Moderator moderator,HttpServletResponse httpResponse)
	{
		collection=mongoOps.getCollection("ModeratorCollection");
        Date date=new Date();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        
		moderator.setId((int)collection.count()+1);
		moderator.setCreated_at(formatted.substring(0, 22) + ":" + formatted.substring(22));
		moderator.setCreated_polls(new ArrayList<Integer>());
		mongoOps.save(moderator);
		httpResponse.setStatus(HttpServletResponse.SC_CREATED);//201
		return moderator;
	}
	
	//Get moderator
	@RequestMapping (value="/moderators/{moderator_id}", method=RequestMethod.GET, headers={"accept=application/json"})
	public @ResponseBody Moderator get_Moderator(@PathVariable int moderator_id, @RequestHeader(value = "Authorization") String Input_authString,HttpServletResponse httpResponse)
	{
		Moderator moderator=new Moderator();
		String[] SplitInput_authString=Input_authString.split(" ");
		if(SplitInput_authString[1].equals(authStringEnc))
		{
			moderator = mongoOps.findById(moderator_id, Moderator.class);
			if(moderator != null)
				httpResponse.setStatus(HttpServletResponse.SC_OK);//200
			else
				httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);//404
		}
		else
		{
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		}
		return moderator;
	}
	
	//Update moderator
	@RequestMapping (value="/moderators/{moderator_id}", method=RequestMethod.PUT, headers={"content-type=application/json"})
	public @ResponseBody Moderator update_Moderator(@PathVariable int moderator_id, /*@Valid*/ @RequestBody Moderator moderator, @RequestHeader(value = "Authorization") String Input_authString, HttpServletResponse httpResponse)
	{
		Moderator updated_moderator=new Moderator();
		String[] SplitInput_authString=Input_authString.split(" ");
		if(SplitInput_authString[1].equals(authStringEnc))
		{
			updated_moderator=mongoOps.findById(moderator_id, Moderator.class);
            if(moderator != null)
			{
				updated_moderator.setName(moderator.getName());
				updated_moderator.setEmail(moderator.getEmail());
				updated_moderator.setPassword(moderator.getPassword());
				mongoOps.save(updated_moderator);
				httpResponse.setStatus(HttpServletResponse.SC_CREATED);//201
			}
			else
				httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);//404
		}
		else
		{
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		}
		return updated_moderator;
	}
	
	//Create poll
	@RequestMapping (value="/moderators/{moderator_id}/polls", method=RequestMethod.POST, headers={"content-type=application/json"})
	@ResponseBody
	public Poll create_Poll(@PathVariable int moderator_id,@Valid @RequestBody Poll poll, @RequestHeader(value = "Authorization") String Input_authString,HttpServletResponse httpResponse)
	{
		String[] SplitInput_authString=Input_authString.split(" ");
		if(SplitInput_authString[1].equals(authStringEnc))
		{
			collection=mongoOps.getCollection("PollCollection");
                	poll_id_counter=(int)collection.count()+1;
			Moderator m=mongoOps.findById(moderator_id, Moderator.class);
			if(m!=null)
			{
				m.getCreated_polls().add(poll_id_counter); 
				poll.setid(Integer.toString(poll_id_counter,36));
				poll.setResults(new ArrayList<Integer>());
				int choice_count=poll.getChoice().size();
				for(int i=1; i<=choice_count; ++i){
					poll.getResults().add(0);
				}
				mongoOps.save(m);
				mongoOps.save(poll);
			}
			else
			{
				poll=null;
			}
			httpResponse.setStatus(HttpServletResponse.SC_CREATED);//201
		}
		else
		{
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		}
		return poll;
	}
	
	//Get poll without Result
	@JsonView(View.PollWithoutResult.class)
	@RequestMapping (value="/polls/{poll_id_36}", method=RequestMethod.GET, headers={"accept=application/json"})
	public @ResponseBody Poll get_Poll_without_result(@PathVariable String poll_id_36)
	{
		Poll poll=mongoOps.findById(poll_id_36, Poll.class);
		if(poll==null)
		{
			poll=new Poll();
		}
		return poll;
	}
	
	public boolean find_poll_in_moderator(int moderator_id,String poll_id_36)
	{
		boolean has_flag=false;
		int poll_id=(int) Long.parseLong(poll_id_36, 36);
		Moderator m=mongoOps.findById(moderator_id, Moderator.class);
		if(m!=null)
		{
			has_flag=m.getCreated_polls().contains(poll_id);
		}
		if(has_flag==true)
			return true;
		else
			return false;
	}
	
	//Get poll with Result
	@JsonView(View.PollWithResult.class)
	@RequestMapping (value="/moderators/{moderator_id}/polls/{poll_id_36}", method=RequestMethod.GET, headers={"accept=application/json"})
	public @ResponseBody Poll get_Poll_with_result(@PathVariable int moderator_id, @PathVariable String poll_id_36,@RequestHeader(value = "Authorization") String Input_authString, HttpServletResponse httpResponse)
	{
		Poll poll=new Poll();
		String[] SplitInput_authString=Input_authString.split(" ");
		if(SplitInput_authString[1].equals(authStringEnc))
		{
			if(find_poll_in_moderator(moderator_id,poll_id_36)==true){
				poll=mongoOps.findById(poll_id_36, Poll.class);
			}
		}
		else
		{
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		}
		return poll;
	}
	
	//Delete a Poll
	@RequestMapping (value="/moderators/{moderator_id}/polls/{poll_id_36}", method=RequestMethod.DELETE)
	public @ResponseBody void delete_poll(@PathVariable int moderator_id,@PathVariable String poll_id_36, @RequestHeader(value = "Authorization") String Input_authString,HttpServletResponse httpResponse)
	{
		String[] SplitInput_authString=Input_authString.split(" ");
		if(SplitInput_authString[1].equals(authStringEnc))
		{
			Moderator moderator = mongoOps.findById(moderator_id, Moderator.class);
			if(moderator!=null)
			{
				Poll poll=mongoOps.findById(poll_id_36, Poll.class);
				if(poll!=null)
				{
					ArrayList<Integer> polls_list=moderator.getCreated_polls();
					Integer poll_id_10=(int)Long.parseLong(poll_id_36, 36);
	
					if(polls_list.remove(poll_id_10)==true)
					{
						moderator.setCreated_polls(polls_list);
						mongoOps.save(moderator);
						mongoOps.remove(poll);
					}
					httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);//204
				}
			}
		}
		else
		{
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		}	
	}
	
	//List all polls with result
	@JsonView(View.PollWithResult.class)
	@RequestMapping (value="/moderators/{moderator_id}/polls", method=RequestMethod.GET, headers="accept=application/json")
	public @ResponseBody ArrayList<Poll> List_polls(@PathVariable int moderator_id, @RequestHeader(value = "Authorization") String Input_authString,HttpServletResponse httpResponse)
	{
        ArrayList<Poll> moderator_polls=new ArrayList<Poll>();
		String[] SplitInput_authString=Input_authString.split(" ");
		if(SplitInput_authString[1].equals(authStringEnc))
		{
			Moderator m=mongoOps.findById(moderator_id, Moderator.class);
			ArrayList<Integer> moderator_poll_ids=m.getCreated_polls();
			for(int i=0; i<moderator_poll_ids.size(); ++i)
			{
				int poll_id_10=moderator_poll_ids.get(i);
				String poll_id_36=Integer.toString(poll_id_10,36);
				moderator_polls.add(mongoOps.findById(poll_id_36, Poll.class));
			}
			httpResponse.setStatus(HttpServletResponse.SC_OK);//200
		}
		else
		{
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
		}
		return moderator_polls;
	}
	
	//Vote for a Poll
	@RequestMapping (value="/polls/{poll_id_36}/choice/{choice_index}", method=RequestMethod.PUT)
	public @ResponseBody void vote_a_poll(@PathVariable String poll_id_36, @PathVariable int choice_index)
	{
		Poll poll=mongoOps.findById(poll_id_36, Poll.class);		

		ArrayList<Integer> update_results=poll.getResults();
		update_results.set(choice_index, poll.getResults().get(choice_index)+1);
		poll.setResults(update_results);

		mongoOps.save(poll);
		
	}
/*
	//Schedule Expired Polls Lookup
    @Scheduled(fixedRate=5000)
    public void reportExpiredPolls()
    {
        //SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        //System.out.println("Now " + dateFormat.format(new Date()));
 
        Properties props = new Properties();
        props.put("metadata.broker.list","localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        SimpleDateFormat sdf = new SimpleDateFormat();
        KeyedMessage<String, String> message =new KeyedMessage<String, String>(TOPIC,"Test message from java program " + sdf.format(new Date()));
        producer.send(message);
        producer.close();
 

    }
 */

}
