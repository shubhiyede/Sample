package app1;

import java.util.*;

import javax.validation.constraints.*; //NotNull, Size, Future

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.*; //JsonView
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PollCollection")
public class Poll {
	
	@JsonView(View.PollWithoutResult.class)
	private String id;
	@NotNull(message="Question is mandatory!")
	//@Size(min=1,max=50)
	@JsonView(View.PollWithoutResult.class)
	private String question;
	@NotNull
	//@Size(min=1,max=25)
	@JsonView(View.PollWithoutResult.class)
	private String started_at;
	
	@NotNull
	//@Size(min=1,max=25)
	//@Future(message="Please enter a date in the future.")
	@JsonView(View.PollWithoutResult.class)
	private String expired_at;
	
	@NotNull
	//@Size(min=2)
	@JsonView(View.PollWithoutResult.class)
	private ArrayList<String> choice;
	@JsonView(View.PollWithResult.class)
	private ArrayList<Integer> results;
	
	public String getid() {
		return id;
	}
	public void setid(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	/*public Date getStarted_at() {
		return started_at;
	}
	public void setStarted_at(Date started_at) {
		this.started_at = started_at;
	}
	public Date getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(Date expired_at) {
		this.expired_at = expired_at;
	}*/
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}
	public ArrayList<String> getChoice() {
		return choice;
	}
	public void setChoice(ArrayList<String> choice) {
		this.choice = choice;
	}

	
	public ArrayList<Integer> getResults() {
		return results;
	}
	public void setResults(ArrayList<Integer> results) {
		this.results = results;
	}
	
	
}
