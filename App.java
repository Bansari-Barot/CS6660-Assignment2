package com.mycompany.mavenproject1;
import java.io.File;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.*;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.*;
/*
 * Assignment 2
 * Name of Partners:
 * Bhavikbhai Thummar (db7689)
 * Bansari Barot (yd5884)
 */
public class App 
{   
    private static enum RelTypes implements RelationshipType
    {
        HAS_ACCESS_TO, IS_IN, POSTED, HAS_A
    }
    private static enum NodeLabels implements Label {
    	WORKSPACE, USER, CHANNEL, POST
    }
    static GraphDatabaseService graphDb;
    private static final File DB_PATH = new File("C:\\Users\\USER\\.Neo4jDesktop\\neo4jDatabases\\database-cdba74d7-d890-48b5-9b6b-80eab57308d1\\installation-3.3.5\\data\\databases\\graph.db");
    public static void main( String[] args )
    {
         graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
         //  Query 1 Query to get all user information
         try (Transaction tx = graphDb.beginTx()) {
                System.out.println("------ All user information -------");
		ResourceIterator<Node> result = graphDb.findNodes(NodeLabels.USER);
		Node node_user = null;
                while(result.hasNext()){
                    node_user = result.next();
                    System.out.printf("First name: %s \t Last name: %s \t Email ID: %s \t Phone no: %s \n",
                            node_user.getProperty("fname").toString(), node_user.getProperty("lname").toString(), node_user.getProperty("email_id").toString(), node_user.getProperty("phone_num").toString());;
                }
                System.out.println("First Query Finished");
                tx.success();
	}catch(Exception E){
            System.out.println("No user node in the database");
        }
         //  Query 2 Retrieving Information of user named William and information of workspace to which he has access
         try (Transaction tx = graphDb.beginTx()) {
		Node user = graphDb.findNode(NodeLabels.USER, "fname", "William");
		TraversalDescription userHAS = graphDb.traversalDescription()
		        .breadthFirst()
		        .relationships( RelTypes.HAS_ACCESS_TO )
		        .evaluator( Evaluators.atDepth( 1 ) );
		Traverser traverser =  userHAS.traverse(user);
		System.out.println("------ Information of user named William and information of workspace to which he has access -------");
		for(Node WORKSPACE : traverser.nodes()) {
			System.out.printf("First name:%s \t Last name%s Email ID:%s \t Status:%s \t WorkSpace:%s \t Date of creation:%s\n", user.getProperty("fname"),user.getProperty("lname"),user.getProperty("email_id"),user.getProperty("status"), WORKSPACE.getProperty("wname"), WORKSPACE.getProperty("date_of_creation"));
			System.out.println();
		}
	}catch(Exception E){
            System.out.println("User node requested doesn't exist in database or user doesn't have access to any workspace");
        }
         //  Query 3 Retrieving Information of workspace named Johnson-Harris and getting all the channels in that worksapce
         try (Transaction tx = graphDb.beginTx()) {
		Node workspace = graphDb.findNode(NodeLabels.WORKSPACE, "wname", "Johnson-Harris");
		TraversalDescription workspaceHAS = graphDb.traversalDescription()
		        .breadthFirst()
		        .relationships( RelTypes.HAS_A )
		        .evaluator( Evaluators.atDepth( 1 ) );
		Traverser traverser =  workspaceHAS.traverse(workspace);
		System.out.println("------ Information of workspace named Johnson-Harris and information of channels in that workspace -------");
		for(Node CHANNEL : traverser.nodes()) {
			System.out.printf("WorkSpace:%s \t Date of creation:%s \t Channel name:%s \t Channel creation date:%s \n", workspace.getProperty("wname"), workspace.getProperty("date_of_creation"), CHANNEL.getProperty("cname"), CHANNEL.getProperty("start_date"));
			System.out.println();
		}
	}catch(Exception E){
            System.out.println("workspace node requested doesn't exist in database or workspace doesn't have any channel");
        }
         //  Query 4 Retrieving Information of all the post posted by user whose last name is Short
         try (Transaction tx = graphDb.beginTx()) {
		Node user = graphDb.findNode(NodeLabels.USER, "lname", "Short");
		TraversalDescription userHASpost = graphDb.traversalDescription()
		        .breadthFirst()
		        .relationships( RelTypes.POSTED )
		        .evaluator( Evaluators.atDepth( 1 ) );
		Traverser traverser =  userHASpost.traverse(user);
		System.out.println("------ Information of all the post posted by user whose last name is Short -------");
		for(Node POST : traverser.nodes()) {
			System.out.printf("First name:%s \t Last name%s \t Post name:%s \t Post Date:%s\n", user.getProperty("fname"),user.getProperty("lname"), POST.getProperty("pname"),POST.getProperty("post_date"));
			System.out.println();
		}
	}catch(Exception E){
            System.out.println("User node requested doesn't exist in database or user didn;t post anything");
        }
        // Query 5 Retrieve all the channel and it's information
        try (Transaction tx = graphDb.beginTx()) {
                System.out.println("------ All channel information -------");
		ResourceIterator<Node> result = graphDb.findNodes(NodeLabels.CHANNEL);
		Node node_channel = null;
                while(result.hasNext()){
                    node_channel = result.next();
                    System.out.printf("Channel name: %s \t Creation Date: %s \n",
                            node_channel.getProperty("cname").toString(), node_channel.getProperty("start_date").toString());;
                }
                System.out.println("Fifth Query Finished");
                tx.success();
	}catch(Exception E){   
            System.out.println("there isn't any channel in the database");
        }   
    }
}
