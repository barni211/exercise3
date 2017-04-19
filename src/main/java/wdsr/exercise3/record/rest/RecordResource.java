package wdsr.exercise3.record.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise3.record.Record;
import wdsr.exercise3.record.RecordInventory;

@Path("/records")
public class RecordResource {
	private static final Logger log = LoggerFactory.getLogger(RecordResource.class);
	
	@Inject
	private RecordInventory recordInventory;
	
	/**
	 * TODO Add methods to this class that will implement the REST API described below.
	 * Use methods on recordInventory to create/read/update/delete records. 
	 * RecordInventory instance (a CDI bean) will be injected at runtime.
	 * Content-Type used throughout this exercise is application/xml.
	 * API invocations that must be supported:
	 */
	

	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getRecords()
	{
		List<Record> results = new ArrayList<>();
		results.addAll(recordInventory.getRecords());
		GenericEntity<List<Record>> genericResult = new GenericEntity<List<Record>>(results) {};
		return Response.ok(genericResult).build();
	}

	@POST	@Consumes(MediaType.APPLICATION_XML)	@Produces(MediaType.APPLICATION_XML)	public Response addRecord(Record record, @Context UriInfo uriInfo) {		if (record.getId() != null) {			return Response.status(Status.BAD_REQUEST).build();		}		recordInventory.addRecord(record);		UriBuilder uriB = uriInfo.getAbsolutePathBuilder();		uriB.path(Integer.toString(record.getId()));		return Response.created(uriB.build()).build();	}	
	
	
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getRecord(@PathParam(value = "id") int id) {
		
		Record record = recordInventory.getRecord(id);
		if(record!=null)
		{
			return Response.ok(record).build();
		}
		return Response.status(Status.NOT_FOUND).build();
	} 
	
	 	@PUT	@Path("/{id}")	@Produces(MediaType.APPLICATION_XML)	public Response putRecord(Record record, @PathParam(value = "id") int id) {				if (record.getId() != null &&  record.getId() != id) 		{			return Response.status(Status.BAD_REQUEST).build();		}		else if(recordInventory.updateRecord(id, record)) 		{			return Response.noContent().build();		}		return Response.status(Status.NOT_FOUND).build();	}
			@DELETE	@Consumes(MediaType.APPLICATION_XML)	@Produces(MediaType.APPLICATION_XML)	@Path("/{id}")	public Response deleteRecord(@PathParam(value = "id") int id) {		if(recordInventory.deleteRecord(id)){			return Response.noContent().build();		}		return Response.status(Status.NOT_FOUND).build();	} 
}
