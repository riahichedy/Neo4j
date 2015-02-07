package imfw;

import java.io.File;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class CreateGraph {
    private static final String DB_PATH = "src/test/neo4j-hello-db";

    public String greeting;

    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Node domaine;
    Node page,page2;
    Node link1,link2;
    Relationship relationship;
    // END SNIPPET: vars

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType
    {
        A_POUR_PAGE, A_POUR_LIEN
    }
    
    // END SNIPPET: createReltype

    public static void main( final String[] args )
    {
        CreateGraph hello = new CreateGraph();
        hello.createDb();
        //hello.removeData();
        //hello.shutDown();
    }

    public void createDb()
    {
        deleteFileOrDirectory( new File( DB_PATH ) );
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb

        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {
            // Database operations go here
            // END SNIPPET: transaction
            // START SNIPPET: addData
            domaine = graphDb.createNode();
            domaine.setProperty( "type", "Domaine" );
            domaine.setProperty( "domaine_name", "http://tuto.fr" );
            domaine.setProperty( "domaine_pages", "Set<Node-pages>" );
            domaine.setProperty( "created_on", "07/02/2015" );
            page = graphDb.createNode();
            page.setProperty( "type", "page" );
            page.setProperty( "page_url", "lignes-photoshop.html" );
            page.setProperty( "page_mediaimageLinks_nb", 50 );
            page.setProperty( "created_on", "07022015" );
            page2 = graphDb.createNode();
            page2.setProperty( "type", "page" );
            page2.setProperty( "page_url", "lignes.html" );
            page2.setProperty( "page_mediaimageLinks_nb", 35 );
            page2.setProperty( "created_on", "07022015" );
            link1 = graphDb.createNode();
            link1.setProperty( "type", "link" );
            link1.setProperty( "link_attribute", "id" );
            link1.setProperty( "link_position", 16 );
            link1.setProperty( "created_on", "07022015" );
            link2 = graphDb.createNode();
            link2.setProperty( "type", "link" );
            link2.setProperty( "link_attribute", "class" );
            link2.setProperty( "link_position", 5 );
            link2.setProperty( "created_on", "07022015" );
            

            relationship = domaine.createRelationshipTo( page, RelTypes.A_POUR_PAGE );
            relationship = domaine.createRelationshipTo( page2, RelTypes.A_POUR_PAGE );
            relationship = page2.createRelationshipTo( link1, RelTypes.A_POUR_LIEN );
            relationship = page2.createRelationshipTo( link2, RelTypes.A_POUR_LIEN );
            // END SNIPPET: addData

            // START SNIPPET: readData
            System.out.println( domaine.getProperty( "type" ) + " created on "+domaine.getProperty( "created_on" ));
            System.out.println( page.getProperty( "type" ) + " created on "+page.getProperty( "created_on" ) );
            System.out.println( page2.getProperty( "type" ) + " created on "+page2.getProperty( "created_on" ) );
            // END SNIPPET: readData

            /*greeting = ( (String) domaine.getProperty( "message" ) )
                       + ( (String) relationship.getProperty( "message" ) )
                       + ( (String) page.getProperty( "message" ) );*/

            // START SNIPPET: transaction
            tx.success();
        }
        // END SNIPPET: transaction
    }

    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            domaine.getSingleRelationship( RelTypes.A_POUR_PAGE, Direction.OUTGOING ).delete();
            domaine.delete();
            page.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    // END SNIPPET: shutdownHook

    private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    }
}
