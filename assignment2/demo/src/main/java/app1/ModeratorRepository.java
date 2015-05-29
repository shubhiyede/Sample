package app1;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.config;

//@EnableMongoRepositories
public interface ModeratorRepository extends MongoRepository<Moderator, String> {

//	public List<Customer> findByFirstName(String firstName);
//	public List<Customer> findByLastName(String lastName);
/*
	//Create moderator
        @RequestMapping (value="/moderators", method=RequestMethod.POST, headers={"content-type=application/json"})
        public @ResponseBody Moderator create_Moderator(@Valid @RequestBody Moderator moderator,HttpServletResponse httpResponse)
        {
                moderator_id_counter++;
                moderator.setId(moderator_id_counter);
                Date date=new Date();
        	String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
                moderator.setCreated_at(formatted.substring(0, 22) + ":" + formatted.substring(22));
                moderator.setCreated_polls(new ArrayList<Integer>());
                Moderator_HashMap.put(moderator_id_counter,moderator);
                httpResponse.setStatus(HttpServletResponse.SC_CREATED);//201
                return moderator;
        }

*/

	public Moderator findById(int moderator_id);
//	public void Update(Moderator moderator);

}

