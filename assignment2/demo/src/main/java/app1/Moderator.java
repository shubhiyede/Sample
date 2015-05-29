package app1;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.*;

//import javax.print.attribute.standard.Severity;
import javax.validation.constraints.*;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection="ModeratorCollection")
public class Moderator {
//	@Id
	private int id;
	@NotNull(message="Name is mandatory!")
	@Size(min=1,max=30)
	private String name;
	@Email
	@NotNull(message="Email is mandatory!")
	@Size(min=1,max=30)
	private String email;
	@NotNull
	@Size(min=1,max=10)
	private String password;
	//private Date created_at;
	private String created_at;
	
	private ArrayList<Integer> created_polls;
	
	public Moderator(){
		
	}
	public Moderator(String name, String email, String password){
		this.name=name;
		this.email=email;
		this.password=password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String new_name) {
		this.name = new_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/*public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}*/
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	@JsonIgnore
	@JsonProperty(value="created_polls")
	public ArrayList<Integer> getCreated_polls() {
		return created_polls;
	}
	public void setCreated_polls(ArrayList<Integer> created_polls) {
		this.created_polls = created_polls;
	}
	
	
}
