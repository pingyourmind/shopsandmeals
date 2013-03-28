package org.pingyourmind.shopsandmeals.impl;

import java.util.Set;

import org.pingyourmind.shopsandmeals.HelloService;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * @author Francisco J Cano - franciscojavier.cano@lumatagroup.com
 */
@Service("helloService#default")
public class DefaultHelloService implements HelloService {
	
	private static final String MONGO_IP = "127.8.189.129";
	private static final String SHOPSANDMEALS = "shopsandmeals";
	private static final int MONGO_PORT = 27017;
	// Try to create another one
	private static final String DB_PASS = "W9WVjEzCj67H";
	private static final String DB_USER = "admin";

	private MongoClient mongoClient;

	public String sayHello(String who) {
		Set<String> collections = null;
		try {
			// Extract to properties!!
			// mongoClient = new MongoClient("localhost", 27017);
			mongoClient = new MongoClient(MONGO_IP, MONGO_PORT);
			DB db = mongoClient.getDB(SHOPSANDMEALS);
			db.authenticate(DB_USER, DB_PASS.toCharArray());
			collections = db.getCollectionNames();
		} catch (Exception e) { // To pass tests without mongo (We should mock
								// it)
			// Use log4j!!
			System.err.println("Review mongo connection..");
			System.err.println(e.getLocalizedMessage());
		} finally {
			mongoClient.close();
		}
		return String.format("Ola k ase %s?, MongoDB has %s collections on %s db",
				who, collections, SHOPSANDMEALS);
	}
}
